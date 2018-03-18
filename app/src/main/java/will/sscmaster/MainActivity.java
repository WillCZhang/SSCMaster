package will.sscmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.DataParser.FacultyDepartmentPairHandler;
import will.sscmaster.UIController.MainListAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String FACULTY_DEPARTMENT_PAIR = "FacultyDepartmentPair.json";
    public static final String DEPARTMENT_COURSE_PAIR = "DepartmentCoursePair.json";
    public static final String FACULTY_LIST = "faculty_list";
    public static final String ALL_DATA = "allData.json";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Set<CourseObject> courseDataset;
    private Map<String, List<CourseObject>> facultyCoursePair;

    private List<String> facultyData;
    private Map<String, List<String>> facultyDepartmentPair;
    private Map<String, List<String>> departmentCoursePair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        readFacultyDepartmentPair();
    }

    private void initView() {
        initMainList();
    }

    private void initMainList() {
        mainList = (RecyclerView) findViewById(R.id.mainlist);
        mAdapter = new MainListAdapter(facultyData, this);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mainList.setLayoutManager(mLayoutManager);
        mainList.setAdapter(mAdapter);
    }

    public void switchToCourseView(String faculty) {
        Intent courseView = new Intent(this, ListActivity.class);
        courseView.putExtra(ListActivity.FACULTY, faculty);
        startActivity(courseView);
    }

    @Override
    public void onBackPressed() {

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

    private void readFacultyDepartmentPair() {
        String temp = readAssertData(FACULTY_DEPARTMENT_PAIR);
        FacultyDepartmentPairHandler.mapHandler(temp);
        facultyData = FacultyDepartmentPairHandler.getKeySet();
        facultyDepartmentPair = FacultyDepartmentPairHandler.getMap();
    }

    @NonNull
    private String readAssertData(String require) {
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
}
