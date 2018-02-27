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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import will.sscmaster.MainActivity;
import will.sscmaster.R;

import static will.sscmaster.DataParser.Faculty.*;

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
        preHandleDrawable();
    }

    private void preHandleDrawable() {
        background = new HashMap<>();
        background.put(SCIENCE_FILE, context.getDrawable(R.drawable.science));
        background.put(SAUDER_FILE, context.getDrawable(R.drawable.sauder));
        background.put(ENGENDERING_FILE, context.getDrawable(R.drawable.engineering));
        background.put(ARTS_FILE, context.getDrawable(R.drawable.arts));
        background.put(FORESTRY_FILE, context.getDrawable(R.drawable.forestry));
        background.put(DENTISTRY_FILE, context.getDrawable(R.drawable.dentistry));
        background.put(LAW_FILE, context.getDrawable(R.drawable.law));
        background.put(EDUCATION_FILE, context.getDrawable(R.drawable.edu));
        background.put(MUSIC_FILE, context.getDrawable(R.drawable.music));
        background.put(LFS_FILE, context.getDrawable(R.drawable.lfs));
        background.put(OTHERS_FILE, context.getDrawable(R.drawable.others));
    }

    private Drawable getDrawable(String faculty) {
        switch(faculty) {
            case SCIENCE:
                return background.get(SCIENCE_FILE);
            case ARTS:
                return background.get(ARTS_FILE);
            case DENTISTRY:
                return background.get(DENTISTRY_FILE);
            case ENGENDERING:
                return background.get(ENGENDERING_FILE);
            case EDUCATION:
                return background.get(EDUCATION_FILE);
            case SAUDER:
                return background.get(SAUDER_FILE);
            case FORESTRY:
                return background.get(FORESTRY_FILE);
            case LFS:
                return background.get(LFS_FILE);
            case MUSIC:
                return background.get(MUSIC_FILE);
            case LAW:
                return background.get(LAW_FILE);
            default:
                return background.get(OTHERS_FILE);
        }
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
        holder.button.getLayoutParams().width = (size.x / 2 ) - size.x / 100;
        holder.button.getLayoutParams().height = getRandomIntInRange(size.y / 5, size.y / 4);
    }

    private void setData(ViewHolder holder, int position) {
        holder.button.setText(dataset.get(position));
        // TODO: text size
        holder.button.setTextSize(20);
        holder.button.setBackground(getDrawable(dataset.get(position)));
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
        return mRandom.nextInt(max)+min;
    }
}
