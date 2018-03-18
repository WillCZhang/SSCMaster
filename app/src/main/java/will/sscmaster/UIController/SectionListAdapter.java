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

import will.sscmaster.Backend.ObjectManager;
import will.sscmaster.Backend.SectionObject;
import will.sscmaster.R;

/**
 * Created by Will on 2018/2/26.
 */

public class SectionListAdapter extends BaseExpandableListAdapter {
    private ObjectManager instance = ObjectManager.getInstance();
    private Context context;
    private List<String> activities;
    private List<List<String>> sections;
    private boolean isSearching;
    private List<String> searchResult;
    private List<List<String>> result;

    public List<String> getActivities() {
        return activities;
    }

    public List<List<String>> getSections() {
        return sections;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }
    static class ChildViewHolder {
        TextView tvTitle;
    }

    public SectionListAdapter(Context context, Map<String, List<SectionObject>> pair) {
        this.context = context;
        isSearching = false;
        initData(pair);
    }

    private void initData(Map<String, List<SectionObject>> pair) {
        activities = new ArrayList<>(pair.size());
        sections = new ArrayList<>();
        String lecture = null;
        List<String> lectureList = null;
        for (Map.Entry<String, List<SectionObject>> entry : pair.entrySet()) {
            List<SectionObject> objects = entry.getValue();
            List<String> tempList = new ArrayList<>(objects.size());
            handleEachActivity(objects, tempList);
            if (entry.getKey().toLowerCase().contains("lecture")) {
                lecture = entry.getKey();
                lectureList = tempList;
                continue;
            }
            activities.add(entry.getKey());
            sections.add(tempList);
        }
        if (lecture != null) {
            activities.add(0, lecture);
            sections.add(0, lectureList);
        }

        searchResult = new ArrayList<>(1);
        searchResult.add("Search Result");
        result = new ArrayList<>(1);
        result.add(new ArrayList<String>());
    }

    private void handleEachActivity(List<SectionObject> objects, List<String> tempList) {
        for (SectionObject object : objects) {
            String objName = object.toString();
            if (objName.toLowerCase().contains("block") || objName.toLowerCase().contains("full"))
                tempList.add(tempList.size() == 0 ? 0 : tempList.size() - 1, objName);
            else if (objName.length() <= 12)
                tempList.add(0, objName);
            else
                tempList.add(objName);
        }
    }

    @Override
    public int getGroupCount() {
        if (isSearching)
            return searchResult.size();
        return activities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (isSearching)
            return result.get(groupPosition).size();
        return sections.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (isSearching)
            return searchResult.get(groupPosition);
        return activities.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (isSearching)
            return result.get(groupPosition).get(childPosition);
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
        String title;
        if (isSearching)
            title = searchResult.get(groupPosition);
        else
            title = activities.get(groupPosition);
        groupViewHolder.tvTitle.setText(title);
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
        String title;
        if (isSearching)
            title = result.get(groupPosition).get(childPosition);
        else
            title = sections.get(groupPosition).get(childPosition);
        groupViewHolder.tvTitle.setText(title);
        return convertView;
    }

    public void startSearching() {
        isSearching = true;
    }

    public void stopSearching() {
        isSearching = false;
    }

    public void addSearchResult(List<String> list) {
        result.get(0).clear();
        result.get(0).addAll(list);
        notifyDataSetChanged();
    }

    public List<String> getAllSection() {
        List<String> result = new ArrayList<>();
        for (List<String> list : sections)
            result.addAll(list);
        return result;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
