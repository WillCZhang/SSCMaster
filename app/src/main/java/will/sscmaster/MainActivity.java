package will.sscmaster;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.Backend.ParseObjectData;
import will.sscmaster.SupportingUI.MainListAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String FACULTY_LIST = "faculty_list";
    public static final String ALL_DATA = "allData.json";

    private String[] facultyData;
    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Set<CourseObject> courseDataset;

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
    }

    private void initView() {
        mainList = (RecyclerView) findViewById(R.id.mainlist);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mainList.setLayoutManager(mLayoutManager);
        mAdapter = new MainListAdapter(facultyData, this);
        mainList.setAdapter(mAdapter);
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
}
