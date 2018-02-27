package will.sscmaster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.Backend.ObjectManager;
import will.sscmaster.DataParser.RequestData;
import will.sscmaster.UIController.SectionListAdapter;

public class CourseView extends AppCompatActivity {
    public static final String COURSE = "putCourse";
    private CourseObject courseObject;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        initData();
        initView();
    }

    private void initData() {
        Intent temp = this.getIntent();
        String course = temp.getStringExtra(COURSE);
        courseObject = ObjectManager.getInstance().getCourseObject(course);
        RequestCourseDetail requestCourseDetail = new RequestCourseDetail();
        requestCourseDetail.execute(courseObject);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void updateList() {
        expandableListAdapter = new SectionListAdapter(this, courseObject);
        expandableListView = (ExpandableListView) findViewById(R.id.sectionExpandable);
        expandableListView.setAdapter(expandableListAdapter);
        assignClickListener();
    }

    private class RequestCourseDetail extends AsyncTask<CourseObject, String, String> {
        private Toast toast;
        @Override
        protected String doInBackground(CourseObject... params) {
            CourseObject course = params[0];
            RequestData.addCourseContent(course);
            return course.toString();
        }

        @Override
        protected void onPreExecute() {
            toast = Toast.makeText(getApplicationContext(), "Loading data... Please wait", Toast.LENGTH_LONG);
            toast.show();
        }

        @Override
        protected void onPostExecute(String response) {
            updateList();
            toast.cancel();
        }
    }

    private void assignClickListener() {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });
    }
}
