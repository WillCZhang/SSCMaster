package will.sscmaster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.Backend.ObjectManager;
import will.sscmaster.DataParser.FacultyDepartmentPairHandler;
import will.sscmaster.DataParser.RequestData;
import will.sscmaster.UIController.CourseListAdapter;

public class ListActivity extends AppCompatActivity {
    public static final String FACULTY = "selectedFaculty";
    private ListView list;
    private CourseListAdapter listAdapter;
    private static String faculty;
    private List<String> dataList;

    private List<CourseObject> courseObjects;
    private boolean shouldClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        initData();
        initView();
    }

    private void initData() {
        Intent temp = this.getIntent();
        faculty = temp.getStringExtra(FACULTY);
        dataList = new ArrayList<String>(FacultyDepartmentPairHandler.getMap().get(faculty));
    }

    private void initView() {
        list = (ListView) findViewById(R.id.courseList);
        listAdapter = new CourseListAdapter(this, dataList);
        list.setAdapter(listAdapter);
        shouldClose = true;
        assignOnItemClick();
    }

    private void assignOnItemClick() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (shouldClose) {
                    RequestCourseData temp = new RequestCourseData();
                    temp.execute(listAdapter.getDepartment(position));
                    shouldClose = false;
                } else {
                    String courseObject = listAdapter.getCourse(position);
                    Intent course = new Intent(getApplicationContext(), CourseView.class);
                    course.putExtra(CourseView.COURSE, courseObject);
                    startActivity(course);
                }
            }
        });
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

    @Override
    public void onBackPressed() {
        if (shouldClose)
            finish();
        updateToDepartment();
        shouldClose = true;
    }

    private class RequestCourseData extends AsyncTask<String, String, String> {
        private Toast toast;
        @Override
        protected String doInBackground(String... params) {
            String department = params[0];
            RequestData.handleCoursesList(department);
            return department;
        }

        @Override
        protected void onPreExecute() {
            toast = Toast.makeText(getApplicationContext(), "Loading data... Please wait", Toast.LENGTH_LONG);
            toast.show();
        }

        @Override
        protected void onPostExecute(String response) {
            toast.cancel();
            courseObjects = ObjectManager.getInstance().getCourseObjects(response);
            updateToCourse(courseObjects);
        }
    }

    public void updateToCourse(List<CourseObject> courseObjects) {
        dataList.clear();
        for (CourseObject courseObject: courseObjects)
            dataList.add(courseObject.toString() + FacultyDepartmentPairHandler.SPLIT + courseObject.getCourseName());
        listAdapter.notifyDataSetChanged();
    }

    public void updateToDepartment() {
        dataList.clear();
        List<String> temp = FacultyDepartmentPairHandler.getMap().get(faculty);
        for (String department : temp)
            dataList.add(department);
        listAdapter.notifyDataSetChanged();
    }
}
