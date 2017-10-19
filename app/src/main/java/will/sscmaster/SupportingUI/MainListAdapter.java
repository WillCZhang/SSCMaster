package will.sscmaster.SupportingUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import will.sscmaster.R;

/**
 * Created by Will on 2017/8/5.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {
    private String[] dataset;
    private String[] colorset;
    private Context context;
    private Random mRandom = new Random();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.faculty);
        }
    }

    public MainListAdapter (String[] dataset, Context context) {
        preHandleData(dataset);
        this.context = context;
    }

    private void preHandleData(String[] dataset) {
        this.dataset = new String[dataset.length];
        this.colorset = new String[dataset.length];

        for (int i = 0; i < dataset.length; i++) {
            this.dataset[i] = dataset[i].split(",")[0];
            this.colorset[i] = dataset[i].split(",")[1];
        }
    }

    @Override
    public MainListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainListAdapter.ViewHolder holder, int position) {
        setWidthHeight(holder);
        setData(holder, position);
        setOnClickListener(holder, position);
    }

    private void setWidthHeight(ViewHolder holder) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // TODO: find a nicer way to render button
        holder.button.getLayoutParams().height = getRandomIntInRange(250,200);
//        holder.button.getLayoutParams().height = (size.y / 5) + mRandom.nextInt(size.y / 5);
        holder.button.getLayoutParams().width = (size.x / 2 ) - size.x / 100;
    }

    private void setData(ViewHolder holder, int position) {
        holder.button.setText(dataset[position]);
        holder.button.setBackgroundColor(Color.parseColor(colorset[position]));
    }

    private void setOnClickListener(final ViewHolder holder, int position) {
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, holder.button.getText(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    private int getRandomIntInRange(int max, int min){
        return mRandom.nextInt((max-min)+min)+min;
    }
}
