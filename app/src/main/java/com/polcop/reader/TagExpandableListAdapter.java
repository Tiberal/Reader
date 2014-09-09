package com.polcop.reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Oleg on 09.09.2014.
 */
public class TagExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<TagInfo> tagInfos;
    private String[] group = new String[] { "" ,"Тэги"};
    private String[] zadolbaliMainLinks = new String[]{"Свежие", "Лучшие", "Случайные"};
    private String[] itHappensMainLinks = new String[]{"Свежие", "Лучшие", "Случайные"};
    private String[] killMePlzMainLinks = new String[]{"Новые", "Самые страшные", "Случайная"};
    private HashMap<String, ArrayList<String>> data;
    private LayoutInflater layoutInflater;


    public TagExpandableListAdapter(ArrayList<TagInfo> tagInfos, Context context){
        this.tagInfos = tagInfos;
        this.context = context;
        data = prepareData(tagInfos);
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(group[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(group[groupPosition]).get(childPosition);
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
        return true;
    }

    private HashMap<String, ArrayList<String>> prepareData(ArrayList<TagInfo> tagInfos){
        HashMap<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();
        ArrayList<String> child = new ArrayList<String>();

        for (int i = 0; i < itHappensMainLinks.length; i++) {
            child.add(itHappensMainLinks[i]);
            }
            data.put(group[0], child);
        child = new ArrayList<String>();
        for (int i = 0; i < tagInfos.size(); i++) {
            child.add(tagInfos.get(i).getTagName());
            }
        data.put(group[1],child);
        return data;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(groupPosition==0) return new View(context);
        String headerTitle = (String) getGroup(groupPosition);
        convertView = layoutInflater
                .inflate(R.layout.drawer_section, null);
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.drawer_section_title);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = layoutInflater
                    .inflate(R.layout.drawer_child_item, null);
        }
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.drawer_child_item);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
