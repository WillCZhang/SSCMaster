package will.sscmaster.Backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Will on 2018/2/24.
 */

public class ObjectManager {
    private static ObjectManager instance;
    private Map<String, List<CourseObject>> courseList;
    private Map<String, CourseObject> courses;
    private Map<String, Map<String, List<SectionObject>>> sectionsActivityPair;

    public static ObjectManager getInstance() {
        if (instance == null)
            instance = new ObjectManager();
        return instance;
    }

    private ObjectManager() {
        courses = new HashMap<>();
        sectionsActivityPair = new HashMap<>();
        courseList = new HashMap<>();
    }

    public CourseObject addCourse(String departmentShortName, String courseNumber) {
        String courseString = departmentShortName + " " + courseNumber;
        if (courses.get(courseString) != null)
            return courses.get(courseString);
        if (courseList.get(departmentShortName) == null)
            courseList.put(departmentShortName, new ArrayList<CourseObject>());
        CourseObject courseObj = new CourseObject(departmentShortName, courseNumber);
        courses.put(courseString, courseObj);
        List<CourseObject> courseNumberList = courseList.get(departmentShortName);
        courseNumberList.add(courseObj);
        return courseObj;
    }

    public void addSection(SectionObject sectionObject) {
        CourseObject courseObject = sectionObject.getCourseObject();
        Map<String,List<SectionObject>> pair = sectionsActivityPair.get(courseObject.toString());
        if (pair == null)
            pair = new HashMap<>();
        List<SectionObject> objects = pair.get(sectionObject.getActivity());
        if (objects == null)
            objects = new ArrayList<>();
        objects.add(sectionObject);
        pair.put(sectionObject.getActivity(), objects);
        sectionsActivityPair.put(courseObject.toString(), pair);
    }

    public List<CourseObject> getCourseObjects(String department) {
        return courseList.get(department);
    }

    public CourseObject getCourseObject(String course) {
        return courses.get(course);
    }

    public Map<String, List<SectionObject>> getPair(CourseObject course) {
        return sectionsActivityPair.get(course.toString());
    }
}
