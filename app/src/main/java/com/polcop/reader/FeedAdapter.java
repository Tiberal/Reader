package com.polcop.reader;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by oleg on 04.09.14.
 */
public class FeedAdapter extends BaseAdapter {

    private ArrayList<StoryInfo> storyInfos;
    private LayoutInflater inflater;

    public FeedAdapter(Context context, ArrayList<StoryInfo>storyInfos) {
        this.storyInfos=storyInfos;
        inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(ArrayList<StoryInfo> storyInfos) {
        this.storyInfos=storyInfos;
    }

    static class ViewHolder {
        public TextView tvStoryNumberAndDate;
        public TextView tvStory;
        public ImageButton ibGood;
        public ImageButton ibBad;
        public TextView tvTags;
        public TextView tvRate;
    }

    @Override
    public int getCount() {
        if(storyInfos==null){
            return 0;
        }
        return storyInfos.size();
    }

    @Override
    public Object getItem(int position) {
        if(storyInfos==null){
            return null;
        }
        return storyInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.feed_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvStoryNumberAndDate = (TextView) view
                    .findViewById(R.id.tvStoryNumberAndDate);
            viewHolder.tvStory = (TextView) view.findViewById(R.id.tvStory);
            viewHolder.ibGood = (ImageButton) view.findViewById(R.id.ibGood);
            viewHolder.ibGood.setFocusable(false);
            viewHolder.ibBad = (ImageButton) view.findViewById(R.id.ibBad);
            viewHolder.ibBad.setFocusable(false);
            viewHolder.tvTags = (TextView) view.findViewById(R.id.tvTags);
            viewHolder.tvRate = (TextView) view.findViewById(R.id.tvRate);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvStoryNumberAndDate.setText(storyInfos.get(position).getStoryNumber()+storyInfos.get(position).getPublishDate());
        viewHolder.tvTags.setText(storyInfos.get(position).getSpannedTags());
        viewHolder.tvRate.setText(storyInfos.get(position).getRate());
        viewHolder.tvStory.setText(storyInfos.get(position).getStory());
        return view;
    }

}
