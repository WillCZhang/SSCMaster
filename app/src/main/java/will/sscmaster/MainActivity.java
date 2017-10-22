package will.sscmaster;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.Backend.ParseObjectData;
import will.sscmaster.SupportingUI.MainListAdapter;
import will.sscmaster.SupportingUI.SubListAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String FACULTY_LIST = "faculty_list";
    public static final String ALL_DATA = "allData.json";

    private String[] facultyData;
    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean isSubList;
    private RecyclerView.Adapter subListAdapter;
    private RecyclerView.LayoutManager subListLayoutManager;

    private Set<CourseObject> courseDataset;
    private Map<String, List<CourseObject>> facultyCoursePair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        facultyData = readFacultyList();
        readAllData();

        facultyCoursePair = new HashMap<>();
        new PairFacultyCourse().execute(courseDataset);
    }

    private void initView() {
        mainList = (RecyclerView) findViewById(R.id.mainlist);
        mAdapter = new MainListAdapter(facultyData, this);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        setToMainList();
    }

    private void setToMainList() {
        mainList.setLayoutManager(mLayoutManager);
        mainList.setAdapter(mAdapter);
        isSubList = false;
    }

    public void switchToSubList(String faculty, int color) {
        if (!isSubList) {
            isSubList = true;
            Object[] tempData = new Object[2];
            tempData[0] = facultyCoursePair.get(faculty);
            tempData[1] = color;
            subListAdapter = new SubListAdapter(tempData, this);
            subListLayoutManager = new GridLayoutManager(this, 4);
            mainList.setLayoutManager(subListLayoutManager);
            mainList.setAdapter(subListAdapter);
        } else {

        }
    }

    @Override
    public void onBackPressed() {
        if (isSubList) {
            isSubList = false;
            setToMainList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 1. search course/instructor/buildings 2.tools(registration, calendar, etc.) 3. setting
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // return an array of string in a form of "faculty_name,color"
    private String[] readFacultyList() {
        String temp = readLocalData(FACULTY_LIST);
        return temp.split(";");
    }

    private void readAllData() {
        String temp = readLocalData(ALL_DATA);
        courseDataset = ParseObjectData.parseJson(temp);
    }

    @NonNull
    private String readLocalData(String require) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open(require), "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = br.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = br.readLine();
            }
            br.close();
            inputStreamReader.close();
        } catch (IOException e) {
            Log.e("ERROR", "Can't read local data");
        }
        return stringBuilder.toString();
    }

    private class PairFacultyCourse extends AsyncTask<Set<CourseObject>, String, String> {
        private Toast toast;
        private final static String SCIENCE = "Faculty of Science";
        private final static String ARTS = "Faculty of Arts";
        private final static String SAUDER = "Sauder School of Business";
        private final static String ENGEERING = "Faculty of Applied Science";
        private final static String EDUCATION = "Faculty of Education";
        private final static String FORESTRY = "Faculty of Forestry";
        private final static String LFS = "Faculty of Land and Food System";
        private final static String DENTISTRY = "Faculty of Dentistry";
        private final static String MUSIC = "School of Music";
        private final static String OTHERS = "Others";

        @Override
        protected String doInBackground(Set<CourseObject>... params) {
            Set<CourseObject> allData = params[0];
            for (CourseObject course : allData) {
                switch(course.getFaculty()) {
                    case SCIENCE:
                        addToPair(course, SCIENCE);
                        break;
                    case ARTS:
                        addToPair(course, ARTS);
                        break;
                    case DENTISTRY:
                        addToPair(course, DENTISTRY);
                        break;
                    case ENGEERING:
                        addToPair(course, ENGEERING);
                        break;
                    case EDUCATION:
                        addToPair(course, EDUCATION);
                        break;
                    case "Faculty of Comm and Bus Admin":
                        addToPair(course, SAUDER);
                        break;
                    case FORESTRY:
                        addToPair(course, FORESTRY);
                        break;
                    case LFS:
                        addToPair(course, LFS);
                        break;
                    case MUSIC:
                        addToPair(course, MUSIC);
                        break;
                    default:
                        addToPair(course, OTHERS);
                        break;
                }
            }
            return "Finished";
        }

        private void addToPair(CourseObject course, String faculty) {
            List<CourseObject> temp = facultyCoursePair.get(faculty);
            if (temp != null)
                temp.add(course);
            else {
                temp = new ArrayList<>();
                temp.add(course);
                facultyCoursePair.put(faculty, temp);
            }
        }

        @Override
        protected void onPreExecute() {
            toast = Toast.makeText(getApplicationContext(), "Loading data... Please wait", Toast.LENGTH_LONG);
            toast.show();
        }

        @Override
        protected void onPostExecute(String response) {
            toast.cancel();
        }
    }
}
