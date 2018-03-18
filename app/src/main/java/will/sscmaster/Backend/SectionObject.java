package will.sscmaster.Backend;

import java.util.*;

public class SectionObject {
    private CourseObject courseObject;
    private String sectionNumber, status, activity;
    private String instructor;
    private String instructorWebsite;
    private String classroom;
    private String term;
    private String restrictTo;
    private String lastWithdraw;

    private Set<String> days;
    private Map<String, String> timeMap;
    private int totalSeats, currentRegistered, restrictSeats, generalSeats;

    public SectionObject(CourseObject courseObject, String sectionNumber) {
        this.activity = "";
        this.courseObject = courseObject;
        this.sectionNumber = sectionNumber;
        this.status = "";
        this.instructor = "TBA";
        this.instructorWebsite = "";
        this.classroom = "";
        this.term = "";
        this.restrictTo = "";
        timeMap = new HashMap<String, String>();
        days = new HashSet<>();
    }

    public SectionObject(String section, String status, String activity, String term) {
        this.activity = activity;
        this.sectionNumber = section;
        this.status = status;
        this.instructor = "TBA";
        this.instructorWebsite = "";
        this.classroom = "";
        this.term = term;
        this.restrictTo = "";
        timeMap = new HashMap<String, String>();
        days = new HashSet<>();
    }

    public void setSeatsInfo(int totalSeats, int currentRegistered, int restrictSeats, int generalSeats) {
        this.totalSeats = totalSeats;
        this.currentRegistered = currentRegistered;
        this.restrictSeats = restrictSeats;
        this.generalSeats = generalSeats;
    }

    public void addTime(String days, String start, String end) {
        this.days.add(days);

        String times = timeMap.get(days);
        if (times != null) {
            times += ";" + start + " - " + end;
        } else {
            String temp = start + " - " + end;
            timeMap.put(days,temp);
        }
    }

    @Override
    public String toString() {
        return sectionNumber + " Term: " + term + " " + status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setActivity(String activity) {
        this.activity = activity.equals("")? "No Specified Activity" : activity;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setRestrictTo(String restrictTo) {
        this.restrictTo = restrictTo;
    }

    public void setLastWithdraw(String lastWithdraw) {
        this.lastWithdraw = lastWithdraw;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setInstructorWebsite(String instructorWebsite) {
        this.instructorWebsite = instructorWebsite;
    }

    public void setTimeMap(Map<String, String> timeMap) {
        this.timeMap = timeMap;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getActivity() {
        return activity;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getTerm() {
        return term;
    }

    public Set<String> getDays() {
        return days;
    }

    public Map<String, String> getTimeMap() {
        return timeMap;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getCurrentRegistered() {
        return currentRegistered;
    }

    public int getRestrictSeats() {
        return restrictSeats;
    }

    public int getGeneralSeats() {
        return generalSeats;
    }

    public String getRestrictTo() {
        return restrictTo;
    }

    public String getLastWithdraw() {
        return lastWithdraw;
    }

    public String getInstructorWebsite() {
        return instructorWebsite;
    }

    public CourseObject getCourseObject() {
        return courseObject;
    }
}
