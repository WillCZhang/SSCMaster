package will.sscmaster.SupportingUI;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.R;

/**
 * Created by Will on 2017/10/20.
 */

public class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.ViewHolder> {
    private Context context;
    private List<CourseObject> dataset;
    private int color;

    public SubListAdapter(Object[] tempData, Context context) {
        dataset = (List<CourseObject>) tempData[0];
        color = (int) tempData[1];
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = dataset.get(position).getDepartmentShortName() + dataset.get(position).getCourseNumber();
        holder.textView.setText(name);
        holder.textView.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.courseName);
        }
    }
}
