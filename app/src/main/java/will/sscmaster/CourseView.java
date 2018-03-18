package will.sscmaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.List;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.Backend.ObjectManager;
import will.sscmaster.DataParser.FacultyDrawableHandler;
import will.sscmaster.DataParser.RequestData;
import will.sscmaster.Search.Search;
import will.sscmaster.UIController.SectionListAdapter;

public class CourseView extends AppCompatActivity {
    public static final String COURSE = "putCourse";
    private CourseObject courseObject;
    private SectionListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    private Button details;
    private Button cancel;
    private EditText search;

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
        toolbar.setTitle(courseObject.getCourseName());
        toolbar.setSubtitle(courseObject.toString());
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setBackground(FacultyDrawableHandler.getDrawable(this, courseObject.getFaculty()));

        details = (Button) findViewById(R.id.details);
        cancel = (Button) findViewById(R.id.cancel);

        search = (EditText) findViewById(R.id.searchSection);
    }

    private void setSectionExpandableList() {
        expandableListAdapter = new SectionListAdapter(this, ObjectManager.getInstance().getPair(courseObject));
        expandableListView = (ExpandableListView) findViewById(R.id.sectionExpandable);
        expandableListView.setAdapter(expandableListAdapter);
        assignClickListener();
        assignTextChangeListener();
    }

    private void assignTextChangeListener() {
        search.addTextChangedListener(new TextWatcher() {
            private List<String> result = expandableListAdapter.getAllSection();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals("")) {
                    expandableListAdapter.stopSearching();
                    expandableListAdapter.notifyDataSetChanged();
                    return;
                }
                expandableListAdapter.startSearching();
                result = expandableListAdapter.getAllSection();
                Search.searchGivenString(s.toString(), result);
                expandableListAdapter.addSearchResult(result);
                result.clear();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
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
            setSectionExpandableList();
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Context that = this;
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String req = courseObject.getReqs() == null? "" : courseObject.getReqs() + "\n";
                String msg = courseObject.getDepartmentShortName() + " " + courseObject.getCourseNumber() + "\n\n" +
                        req +
                        courseObject.getDescription();
                if (courseObject.getDescription().length() > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(that);
                    builder.setMessage(msg)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(that, "No description for this course", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
