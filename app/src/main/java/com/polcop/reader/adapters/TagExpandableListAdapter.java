package com.polcop.reader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.polcop.reader.Constants;
import com.polcop.reader.R;
import com.polcop.reader.TagInfo;
import com.polcop.reader.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Oleg on 09.09.2014.
 */
public class TagExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<TagInfo> tagInfos;
    private String[] group;
    private String[] ZadolbaliAndItHappensItems;
    private String[] killMePlzMainItems;
    private HashMap<String, ArrayList<String>> data;
    private LayoutInflater layoutInflater;


    public TagExpandableListAdapter(ArrayList<TagInfo> tagInfos, Context context){
        group = context.getResources().getStringArray(R.array.expandable_list_items);
        ZadolbaliAndItHappensItems = context.getResources().getStringArray(R.array.zadolbali_it_happens_items);
        killMePlzMainItems = context.getResources().getStringArray(R.array.kill_ma_plz_items);
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
        switch (Utils.getLoaderId()){
            case Constants.IT_HAPPENS_LOADER:
            case Constants.ZADOLBALI_LOADER:
                for (int i = 0; i < ZadolbaliAndItHappensItems.length; i++) {
                    child.add(ZadolbaliAndItHappensItems[i]);
                }
                data.put(group[0], child);
                break;
            case Constants.KILL_ME_PLZ_LOADER:
                for (int i = 0; i < killMePlzMainItems.length; i++) {
                    child.add(killMePlzMainItems[i]);
                }
                data.put(group[0], child);
                break;
            case Constants.BASH_LOADER:
                for (int i = 0; i < tagInfos.size(); i++) {
                    child.add(tagInfos.get(i).getTagTitle());
                }
                data.put(group[0], child);
                return data;

        }

        child = new ArrayList<String>();
        for (int i = 0; i < tagInfos.size(); i++) {
            child.add(tagInfos.get(i).getTagTitle());
        }
        data.put(group[1],child);
        return data;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(groupPosition==0||Utils.getLoaderId() == Constants.BASH_LOADER) return new View(context);
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
