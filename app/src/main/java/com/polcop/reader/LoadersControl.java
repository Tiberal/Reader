package com.polcop.reader;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Context;
import android.os.Bundle;
import java.util.ArrayList;

/**
 * Created by oleg on 04.09.14.
 */
public class LoadersControl implements LoaderManager.LoaderCallbacks<ArrayList<StoryInfo>> {

    private Context context;
    private FeedAdapter feedAdapter;
    private String link;

    public LoadersControl(Context context, FeedListView feedListView) {
        this.context = context;
        feedAdapter = new FeedAdapter(context,PageInfo.getInstance().getStoryInfos());
        feedListView.setAdapter(feedAdapter);
    }

    @Override
    public Loader<ArrayList<StoryInfo>> onCreateLoader(int id, Bundle bundle) {
        link = bundle.getString("URL");
        return new ItHappensLoader(context,link);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<StoryInfo>> loader, final ArrayList<StoryInfo> data) {
        if (PageInfo.getInstance().getStoryInfos()==null){
            PageInfo.getInstance().setStoryInfos(data);
        }else {
            PageInfo.getInstance().addStoryInfos(data);
        }
        setFeedListViewData();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<StoryInfo>> loader) {

    }

    private void setFeedListViewData(){
        ((MainActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                feedAdapter.updateData(PageInfo.getInstance().getStoryInfos());
                feedAdapter.notifyDataSetChanged();
                }
        });
    }

}
