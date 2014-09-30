package com.polcop.reader.adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.polcop.reader.Constants;
import com.polcop.reader.PageInfo;
import com.polcop.reader.UI.MovementCheck;
import com.polcop.reader.R;
import com.polcop.reader.StoryInfo;

import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by oleg on 04.09.14.
 */
public class FeedAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StoryInfo> storyInfos;
    private ArrayList<ClickInfo> clickInfos;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    public FeedAdapter(Context context, ArrayList<StoryInfo>storyInfos) {
        this.context=context;
        this.storyInfos=storyInfos;
        clickInfos = new ArrayList<ClickInfo>();
        inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(ArrayList<StoryInfo> storyInfos) {
        this.storyInfos=storyInfos;
        updateClickInfos();
    }

    private void updateClickInfos() {
        int size = storyInfos.size() - clickInfos.size();
        for (int i = 0; i < size; i++) {
            clickInfos.add(new ClickInfo());
        }
    }

    static class ViewHolder {
        public TextView tvStoryNumber;
        public TextView tvStoryDate;
        public TextView tvStory;
        public ImageButton ibGood;
        public ImageButton ibBad;
        public TextView tvTags;
        public TextView tvRate;
    }

    static  class ClickInfo{
        public boolean isGoodClicked = false;
        public boolean isBadClicked = false;
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
        MovementCheck movementCheck = new MovementCheck();
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.feed_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvStoryNumber = (TextView) view
                    .findViewById(R.id.tvStoryNumber);
            viewHolder.tvStoryDate = (TextView) view
                    .findViewById(R.id.tvStoryDate);
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
        viewHolder.tvStoryNumber.setText(storyInfos.get(position).getStoryNumber());
        viewHolder.tvStoryDate.setText(storyInfos.get(position).getPublishDate());
        viewHolder.tvTags.setText(storyInfos.get(position).getSpannedTags());
        viewHolder.tvTags.setMovementMethod(movementCheck);
        viewHolder.tvRate.setText(storyInfos.get(position).getRate());
        viewHolder.tvStory.setText(createSpannableStory(position));
        viewHolder.tvStory.setMovementMethod(movementCheck);
        createClickListener(viewHolder, position);
        setRate(viewHolder,position);
        viewHolder.ibBad.setOnClickListener(listener);
        viewHolder.ibGood.setOnClickListener(listener);
        return view;
    }


    //совмещает низвание истории и историю
    private SpannableStringBuilder createSpannableStory(int position){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Spanned storyTitle = storyInfos.get(position).getStoryTitle();
        if(storyTitle!=null)
            builder.append(storyInfos.get(position).getStoryTitle());
        builder.append(storyInfos.get(position).getStory());
        return builder;
    }

    private void setRate(ViewHolder viewHolder, int position) {
        //todo!!!!
//        if(clickInfos.size()!=storyInfos.size()){
//            updateClickInfos();
//        }
        viewHolder.ibBad.setVisibility(View.INVISIBLE);
        viewHolder.ibGood.setVisibility(View.VISIBLE);
        if(storyInfos.get(position).getGoodURL().equals("")){
            viewHolder.ibGood.setVisibility(View.INVISIBLE);
            return;
        }
        if(clickInfos.get(position).isGoodClicked)
            goodClick(viewHolder,position);
    }

    private void createClickListener(final ViewHolder viewHolder, final int position){
        this.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ibGood:
                        goodClick(viewHolder,position);
                        if(!(storyInfos.get(position).getGoodURL().equals(""))){
                            postData(Constants.IT_HAPPENS_LINK+storyInfos.get(position).getGoodURL());
                    }
                }
            }
        };
    }

    private void goodClick(ViewHolder viewHolder, int position) {
        clickInfos.get(position).isGoodClicked=true;
        int rate = Integer.parseInt(storyInfos.get(position).getRate());
        rate++;
        viewHolder.tvRate.setText(""+rate);
        viewHolder.ibGood.setVisibility(View.INVISIBLE);
    }

    private void badClick(ViewHolder viewHolder, int position) {
        clickInfos.get(position).isBadClicked=true;
        int rate = Integer.parseInt(storyInfos.get(position).getRate());
        rate--;
        viewHolder.tvRate.setText(""+rate);
        viewHolder.ibBad.setVisibility(View.INVISIBLE);
    }

    public void postData(final String link){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Jsoup.connect(link).get();
                } catch (IOException e) {
                    Toast.makeText(PageInfo.getInstance().getContext(),"косяк",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
