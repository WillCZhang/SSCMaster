package will.sscmaster.UIController;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import will.sscmaster.DataParser.FacultyDepartmentPairHandler;
import will.sscmaster.R;

/**
 * Created by Will on 2018/2/25.
 */

public class CourseListAdapter extends ArrayAdapter<String> {
    public static final String DEPARTMENT_SPLIT = "@";
    private Context context;
    private List<String> values;
    private List<String> firstList;
    private List<String> secondList;
    private List<Drawable> images;
    private Random random = new Random();

    private static class ViewHolder {
        TextView firstLine;
        TextView secondLine;
        ImageView imageView;
    }

    public CourseListAdapter(Context context, List<String> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        prepareData(values);
        prepareImage();
    }

    public String getDepartment(int position) {
        return firstList.get(position) + DEPARTMENT_SPLIT + secondList.get(position);
    }

    public String getCourse(int position) {
        return secondList.get(position);
    }

    private void prepareData(List<String> values) {
        Collections.sort(values);
        firstList = new ArrayList<>(values.size());
        secondList = new ArrayList<>(values.size());
        for (String value : values) {
            String[] temp = value.split(FacultyDepartmentPairHandler.SPLIT);
            firstList.add(temp[1]);
            secondList.add(temp[0]);
        }
    }

    private void prepareImage() {
        images = new ArrayList<>();
        images.add(context.getDrawable(R.mipmap.science));
        images.add(context.getDrawable(R.mipmap.sauder));
        images.add(context.getDrawable(R.mipmap.engineering));
        images.add(context.getDrawable(R.mipmap.arts));
        images.add(context.getDrawable(R.mipmap.forestry));
        images.add(context.getDrawable(R.mipmap.dentistry));
        images.add(context.getDrawable(R.mipmap.law));
        images.add(context.getDrawable(R.mipmap.edu));
        images.add(context.getDrawable(R.mipmap.music));
        images.add(context.getDrawable(R.mipmap.lfs));
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        prepareData(values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.course_list_view, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.firstLine = (TextView) convertView.findViewById(R.id.first);
            viewHolder.secondLine = (TextView) convertView.findViewById(R.id.second);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        setDisplayData(viewHolder, position);
        return convertView;
    }

    private void setDisplayData(ViewHolder viewHolder, int position) {
        viewHolder.firstLine.setText(firstList.get(position));
        viewHolder.secondLine.setText(secondList.get(position));
        viewHolder.imageView.setImageDrawable(images.get(getRandom()));
    }

    private int getRandom() {
        return random.nextInt(images.size());
    }
}
