package will.sscmaster.DataParser;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.Backend.ObjectManager;
import will.sscmaster.Backend.SectionObject;

import static will.sscmaster.UIController.CourseListAdapter.DEPARTMENT_SPLIT;

/**
 * Created by Will on 2018/2/24.
 */

public class RequestData {
    public static final String PREFIX = "https://courses.students.ubc.ca";
    public static final String DEPARTMENT_URL = "https://courses.students.ubc.ca/cs/main?pname=subjarea&tname=subjareas&req=1&dept=";
    public static final String SECTION_URL = "https://courses.students.ubc.ca/cs/main?pname=subjarea&tname=subjareas&req=3&dept=";
    public static final String COURSE_URL_EXTRA = "&course=";
    private static Map<SectionObject, Elements> elementsMap = new HashMap<>();
    private static ObjectManager objectManager = ObjectManager.getInstance();
    private static SectionObject lastSection;

    public static String handleCoursesList(String faculty, String departmentUnmodified) {
        String[] departmentString = departmentUnmodified.split(DEPARTMENT_SPLIT);
        try {
            String coursesListText = reading(DEPARTMENT_URL + departmentString[1]);
            Document coursesListDoc = Jsoup.parse(coursesListText);
            Elements tbodyElements = coursesListDoc.getElementsByTag("tbody");
            Element coursesListElement = tbodyElements.first();
            Elements coursesElements = coursesListElement.getElementsByTag("tr");
            for (Element courseElement : coursesElements) {
                String course = courseElement.getElementsByTag("a").first().text();
                String[] parts = course.split(" ");
                String part1 = parts[0]; // Department
                String part2 = parts[1]; // Number
                String courseTitle = courseElement.getElementsByTag("td").get(1).text();
                CourseObject temp = objectManager.addCourse(part1, part2);
                temp.setCourseName(courseTitle);
                temp.setFaculty(faculty);
                temp.setDepartmentFullName(departmentString[0]);
            }
            Log.i("tag!", "finished downloading data");
        } catch (IOException ignored) {
        }
        return departmentString[1];
    }

    public static void addCourseContent(CourseObject course) {
        try {
            String sectionListText = reading(SECTION_URL + course.getDepartmentShortName() + COURSE_URL_EXTRA + course.getCourseNumber());
            Document sectionListDoc = Jsoup.parse(sectionListText);
            Element mainContentElement = sectionListDoc.getElementsByClass("content expand").first();

            Elements creditsAndDescription = mainContentElement.getElementsByTag("p");
            Element description = creditsAndDescription.get(0);
            Element credits = creditsAndDescription.get(1);
            course.setCredits(credits.text());
            course.setDescription(description.text());

            // dealing with reqs
            StringBuilder reqs = new StringBuilder();
            String pre = "";
            for (int i = 2; i < creditsAndDescription.size(); i++) {
                String curr = creditsAndDescription.get(i).text();
                String modifiedCurr = curr.trim().toLowerCase().replaceAll("\\W", "");
                if (!pre.equals(modifiedCurr))
                    reqs.append(curr).append("\n");
                pre = modifiedCurr;
            }
            course.setReqs(reqs.toString());

            // dealing with sections
            Element sectionTableElement = mainContentElement.getElementsByTag("tbody").first();
            Elements sectionElements = sectionTableElement.getElementsByTag("tr");
            for (Element section : sectionElements)
                refreshEachSection(course, section);
        } catch (IOException ignored) {
        }
    }

    private static synchronized void refreshEachSection(CourseObject editingCourse, Element sectionElement) throws IOException {
        Elements sectionInfo = sectionElement.getElementsByTag("td");
        String status = sectionInfo.get(0).text();
        String courseFullInfo = sectionInfo.get(1).text();
        if (!courseFullInfo.equals("") && !courseFullInfo.equals(" ")) {
            String section = sectionInfo.get(1).text().split(" ")[2];
            String activity = sectionInfo.get(2).text();
            String term = sectionInfo.get(3).text();
            // ignore all the sections that are blocked
            if (activity.equals("Blocked"))
                return;
            SectionObject currentSection = new SectionObject(editingCourse, section);
            currentSection.setActivity(activity);
            currentSection.setStatus(status);
            currentSection.setTerm(term);
            setTimeForSection(sectionInfo, currentSection);
            editingCourse.addSection(currentSection);
            lastSection = currentSection;
            elementsMap.put(currentSection, sectionInfo);
            objectManager.addSection(currentSection);
//            addSectionContent(currentSection); // TODO: test the performance with this very expensive statement
        } else // if one sectionElement has separated colums storing info
            setTimeForSection(sectionInfo, lastSection);
    }

    public static void addSectionContent(SectionObject currentSection) throws IOException {
        Elements sectionInfo = elementsMap.get(currentSection);
        String urlForSectionInfo = sectionInfo.get(1).getElementsByTag("a").attr("href");
        String sectionInfoText = reading(PREFIX + urlForSectionInfo);
        Document sectionInfoDoc = Jsoup.parse(sectionInfoText);
        Element sectionPage = sectionInfoDoc.getElementsByClass("content expand").first();

        // dealing with last day to withdraw
        Element withdrawDay = sectionPage.getElementsByClass("table table-nonfluid").first();
        Element withdrawInfoElement = withdrawDay.getElementsByTag("tbody").first();
        String withdrawInfo = withdrawInfoElement.text();

        // dealing with seats
        Element seatsSummaryElement = sectionPage.getElementsByAttribute("table-nonfluid&#39;").first();
        int total = 0;
        int current = 0;
        int general = 0;
        int restricted = 0;
        String restrictedTo = "";
        if (seatsSummaryElement != null) {
            Element seatsInfoElement = seatsSummaryElement.getElementsByTag("tbody").first();
            String seatsInfoCombine = getSeatsInfo(seatsInfoElement);
            try {
                String[] seatsInfoArray = seatsInfoCombine.split("@");
                total = Integer.parseInt(seatsInfoArray[0]);
                current = Integer.parseInt(seatsInfoArray[1]);
                general = Integer.parseInt(seatsInfoArray[2]);
                restricted = Integer.parseInt(seatsInfoArray[3]);
                if (seatsInfoArray.length > 4)
                    restrictedTo = seatsInfoArray[4];
            } catch (IndexOutOfBoundsException | NumberFormatException ignored) {

            }
        }

        // dealing with classroom info
        Element classroomInfo = sectionPage.getElementsByClass("table  table-striped").first();
        String building = "";
        String classroomTemp = "";
        if (classroomInfo != null) {
            Elements buildingInfoTable = classroomInfo.getElementsByTag("td");
            building = buildingInfoTable.get(4).text();
            classroomTemp = buildingInfoTable.get(5).text();
            if (building.equals("No Scheduled Meeting"))
                building = "No Scheduled Meeting";
        }
        String classroom = building + "," + classroomTemp;

        // dealing with instructor info
        Elements info = sectionPage.getElementsByTag("table");
        Elements instructorInfoElements = findingInstructorInfo(info);
        String instructor = "";
        String instructorWeb = "";
        if (instructorInfoElements != null) {
            instructor = instructorInfoElements.text().split(": ")[1];
            Element tempWeb = instructorInfoElements.tagName("a").first().getElementsByTag("a").get(1);
            instructorWeb = tempWeb.attr("href");
        }

        // Set the sectionElement
        currentSection.setClassroom(classroom);
        currentSection.setInstructor(instructor);
        currentSection.setInstructorWebsite(instructorWeb);
        currentSection.setLastWithdraw(withdrawInfo);
        currentSection.setSeatsInfo(total, current, restricted, general);
        currentSection.setRestrictTo(restrictedTo);
    }

    private static void setTimeForSection(Elements sectionInfo, SectionObject currentSection) {
        String days = sectionInfo.get(5).text();
        try {
            if (!days.equals("") && !days.equals(" ")) { // in some cases, days are available and yet time is not
                String[] time1 = sectionInfo.get(6).text().split(":");
                Time start = new Time(Integer.parseInt(time1[0]), Integer.parseInt(time1[1]), 0);
                String[] time2 = sectionInfo.get(7).text().split(":");
                Time end = new Time(Integer.parseInt(time2[0]), Integer.parseInt(time2[1]), 0);
                currentSection.addTime(days, start.toString(), end.toString());
            }
        } catch (NumberFormatException ignored) {
        }
    }

    private static String getSeatsInfo(Element seatsInfoElement) {
        String temp = "";
        String split = "@";
        String total = "";
        String current = "";
        String general = "";
        String restricted = "";
        String restrictedTo = "";
        Elements tempInfo = seatsInfoElement.getElementsByTag("tr");
        for (Element element : tempInfo) {
            if (element.text().contains("Total"))
                total = element.getElementsByTag("strong").first().text();
            else if (element.text().contains("Current"))
                current = element.getElementsByTag("strong").first().text();
            else if (element.text().contains("General"))
                general = element.getElementsByTag("strong").first().text();
            else if (element.text().contains("Restricted"))
                restricted = element.getElementsByTag("strong").first().text();
            else
                restrictedTo = element.text();
        }
        temp = total + split +
                current + split +
                general + split +
                restricted + split + restrictedTo;

        return temp;
    }

    private static Elements findingInstructorInfo(Elements info) {
        for (Element element : info) {
            Elements childNodes = element.children().tagName("td").tagName("td");
            if (childNodes.text().contains("Instructor"))
                return childNodes;
        }
        return null;
    }

    @NonNull
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @NonNull
    private static String reading(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        return readAll(reader);
    }
}