package will.sscmaster.DataParser;

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

public class FacultyDepartmentPairHandler {
    public static final String SPLIT = "%";
    private static Map<String, List<String>> map = new HashMap<>();
    private static List<String> keySet = new ArrayList<>();

    public static void mapHandler(String pair) {
        try {
            JSONObject tempObj = new JSONObject(pair);
            Iterator<String> iterator = tempObj.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                JSONArray departments = tempObj.getJSONArray(key);
                List<String> tempD = new ArrayList<>();
                for (int i = 0; i < departments.length(); i++)
                    tempD.add(departments.getString(i));
                map.put(key, tempD);
                keySet.add(key);
            }
        } catch (JSONException ignored) {}
    }

    public static Map<String, List<String>> getMap() {
        return map;
    }

    public static List<String> getKeySet() {
        return keySet;
    }
}
