package will.sscmaster.UIController;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import will.sscmaster.MainActivity;
import will.sscmaster.R;

import static will.sscmaster.DataParser.FacultyDrawableHandler.*;

/**
 * Created by Will on 2017/8/5.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {
    private List<String> dataset;
    private Map<String, Drawable> background;
    private Context context;
    private Random mRandom = new Random();

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button button;
        ViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.faculty);
        }
    }

    public MainListAdapter (List<String> dataset, Context context) {
        this.context = context;
        this.dataset = dataset;
    }

    @Override
    public MainListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainListAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setWidthHeight(holder);
        setOnClickListener(holder);
    }

    private void setWidthHeight(ViewHolder holder) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // TODO: find a nicer way to render button
//        holder.button.getLayoutParams().height = getRandomIntInRange(200,150);
//        holder.button.getLayoutParams().height = (size.y / 5) + mRandom.nextInt(size.y / 5);
        holder.button.getLayoutParams().width = (size.x / 2 ) - size.x / 95;
        holder.button.getLayoutParams().height = getRandomIntInRange(2 * size.y / 5, size.y / 4);
    }

    private void setData(ViewHolder holder, int position) {
        holder.button.setText(dataset.get(position));
        // TODO: text size
        holder.button.setTextSize(20);
        holder.button.setBackground(getDrawable(context, dataset.get(position)));
    }

    private void setOnClickListener(final ViewHolder holder) {
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity temp = (MainActivity) context;
                temp.switchToCourseView(holder.button.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    private int getRandomIntInRange(int max, int min){
        return mRandom.nextInt(max-min)+min;
    }
}
