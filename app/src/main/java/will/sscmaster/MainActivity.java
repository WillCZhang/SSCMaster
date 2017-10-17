package will.sscmaster;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import will.sscmaster.SupportingUI.MainListAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String FACULTY_LIST = "faculty_list";

    private String[] facultyData;
    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open(FACULTY_LIST), "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = br.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = br.readLine();
            }
            br.close();
            inputStreamReader.close();
            String temp = stringBuilder.toString();
            return temp.split(";");
        } catch(IOException e) {
            return new String[1];
        }
    }

    private void readAllData() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open("allData.json"), "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = br.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = br.readLine();
            }
            br.close();
            inputStreamReader.close();
            String temp = stringBuilder.toString();
        } catch(IOException e) {

        }
    }
}
