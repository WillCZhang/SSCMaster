package will.sscmaster.HandleData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Will on 2017/11/17.
 */

public class FacultyDepartmentDataPairHandler {
    public static Map<String, List<String>> mapHandler(String pair) {
        Map<String, List<String>> result = new HashMap<>();
        try {
            JSONObject tempObj = new JSONObject(pair);
            Iterator<String> iterator = tempObj.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                JSONArray departments = tempObj.getJSONArray(key);
                List<String> tempD = new ArrayList<>();
                for (int i = 0; i < departments.length(); i++)
                    tempD.add(departments.getString(i));
                result.put(key, tempD);
            }
        } catch (JSONException ignored) {}
        return result;
    }

    public static List<String> keySetHandler(String pair) {
        List<String> result = new ArrayList<>();
        try {
            JSONObject temp = new JSONObject(pair);
            Iterator<String> iterator = temp.keys();
            while (iterator.hasNext())
                result.add(iterator.next());
        } catch (Exception ignored) {}
        return result;
    }
}
