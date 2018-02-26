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
    private Map<String, SectionObject> sections;

    public static ObjectManager getInstance() {
        if (instance == null)
            instance = new ObjectManager();
        return instance;
    }

    private ObjectManager() {
        courses = new HashMap<>();
        sections = new HashMap<>();
        courseList = new HashMap<>();
    }

    public CourseObject addCourse(String departmentShortName, String courseNumber) {
        String courseString = departmentShortName+courseNumber;
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

    public SectionObject addSection(CourseObject courseObject, String sectionNumber) {
        String sectionString = courseObject.toString() + sectionNumber;
        if (sections.get(sectionString) != null)
            return sections.get(sectionString);
        SectionObject sectionObject = new SectionObject(courseObject, sectionNumber);
        sections.put(sectionString, sectionObject);
        return sectionObject;
    }

    public List<CourseObject> getCourseObjects(String department) {
        return courseList.get(department);
    }
}
