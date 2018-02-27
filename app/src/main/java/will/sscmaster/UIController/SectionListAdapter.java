package will.sscmaster.UIController;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import will.sscmaster.Backend.CourseObject;
import will.sscmaster.Backend.ObjectManager;
import will.sscmaster.Backend.SectionObject;
import will.sscmaster.MainActivity;
import will.sscmaster.R;

/**
 * Created by Will on 2018/2/26.
 */

public class SectionListAdapter extends BaseExpandableListAdapter {
    private ObjectManager instance = ObjectManager.getInstance();
    private Context context;
    private List<String> activities;
    private List<List<String>> sections;

    static class GroupViewHolder {
        TextView tvTitle;
    }
    static class ChildViewHolder {
        TextView tvTitle;
    }

    public SectionListAdapter(Context context, CourseObject courseObject) {
        this.context = context;
        initData(instance.getPair(courseObject));
    }

    private void initData(Map<String, List<SectionObject>> pair) {
        activities = new ArrayList<>(pair.size());
        sections = new ArrayList<>();
        for (Map.Entry<String, List<SectionObject>> entry : pair.entrySet()) {
            activities.add(entry.getKey());
            List<SectionObject> objects = entry.getValue();
            List<String> tempList = new ArrayList<>(objects.size());
            for (SectionObject object : objects)
                tempList.add(object.toString());
            sections.add(tempList);
        }
    }

    @Override
    public int getGroupCount() {
        return activities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return sections.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return activities.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return sections.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.section_list_view, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.sectionList);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(activities.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder groupViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.section_sublist_view, parent, false);
            groupViewHolder = new ChildViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.sectionSublist);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (ChildViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(sections.get(groupPosition).get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
