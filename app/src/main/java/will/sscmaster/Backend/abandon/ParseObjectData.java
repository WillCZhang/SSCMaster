package will.sscmaster.Backend.abandon;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.Backend.SectionObject;

public class ParseObjectData {
    private static JSONArray jsonArray;

    public static Set<CourseObject> parseJson(String jsonString) {
        Set<CourseObject> result = new HashSet<>();
        try {
            jsonArray = new JSONArray(jsonString);
            createCourseObject(result);
        } catch (Exception e) {
            Log.e("ERROR", "Got an error when parsing JSON");
        }
        return result;
    }

    private static void createCourseObject(Set<CourseObject> result) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                CourseObject temp = new CourseObject(jsonObject.getString("departmentShortName"), jsonObject.getString("courseNumber"));
                temp.setDepartmentFullName(jsonObject.getString("departmentFullName"));
                temp.setFaculty(jsonObject.getString("faculty"));
                temp.setCourseName(jsonObject.getString("courseName"));
                temp.setCredits(jsonObject.getString("credits"));
                temp.setDescription(jsonObject.getString("description"));
                temp.setReqs(jsonObject.getString("reqs"));

                // TODO: find a better algorithm to load section data
//                creatSectionObject(temp, jsonObject.getJSONArray("sections"));

                result.add(temp);
            } catch (JSONException e) {
                Log.e("ERROR", "Got an error when storing data");
            }
        }
    }

    private static void creatSectionObject(CourseObject courseObject, JSONArray sections) throws JSONException {
        Set<SectionObject> sectionlist = new HashSet<>();
        for (int i = 0; i < sections.length(); i++) {
            JSONObject sectionJson = sections.getJSONObject(i);
            SectionObject sectionObject = new SectionObject(sectionJson.getString("section"), sectionJson.getString("status"),
                    sectionJson.getString("activity"), sectionJson.getString("term"));

            sectionObject.setInstructor(sectionJson.getString("instructor"));
            sectionObject.setInstructorWebsite(sectionJson.getString("instructorWebsite"));
            sectionObject.setClassroom(sectionJson.getString("classroom"));
            sectionObject.setRestrictTo(sectionJson.getString("restrictTo"));
            sectionObject.setLastWithdraw(sectionJson.getString("lastWithdraw"));
            sectionObject.setSeatsInfo(sectionJson.getInt("totalSeats"), sectionJson.getInt("currentRegistered"),
                    sectionJson.getInt("restrictSeats"), sectionJson.getInt("generalSeats"));

            JSONArray timeMap = sectionJson.getJSONArray("timeMap");
            if (timeMap != null) {
                Map<String, String> temp = new HashMap<>();
                for (int n = 0; n < timeMap.length(); n++) {
                    JSONObject jsonObject = timeMap.getJSONObject(n);
                    while (jsonObject.keys().hasNext()) {
                        String key = jsonObject.keys().next();
                        temp.put(key, jsonObject.getString(key));
                    }
                    sectionObject.setTimeMap(temp);
                }
            }
            sectionlist.add(sectionObject);
        }
        courseObject.setSections(sectionlist);
    }
}
