package com.polcop.reader;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by oleg on 04.09.14.
 */
public class LoadersControl implements LoaderManager.LoaderCallbacks<Boolean> {

    private FeedListView feedListView;
    private Context context;
    private FeedAdapter feedAdapter;
    private String link;
    private LoadingFooterView footerView;

    public LoadersControl(Context context, FeedListView feedListView) {
        this.context = context;
        this.feedListView=feedListView;
        this.footerView = new LoadingFooterView(context);
        feedAdapter = new FeedAdapter(context,PageInfo.getInstance().getStoryInfos());
        feedListView.setAdapter(feedAdapter);
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle bundle) {
        link = bundle.getString("URL");
        return new ItHappensLoader(context,link);
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, final Boolean data) {
        if(data){
            setFeedListViewData();
            ((MainActivity)context).anotherContent(PageInfo.getInstance().getTagInfos());
        }

    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {

    }

    private void setFeedListViewData(){
        ((MainActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                feedListView.setLoadContent(false);
                feedListView.setVisibility(View.VISIBLE);
                feedAdapter.updateData(PageInfo.getInstance().getStoryInfos());
                feedAdapter.notifyDataSetChanged();
                //todo танцы с бубном футер
                if(PageInfo.getInstance().getPreviousPage()==null){
                    feedListView.removeFooterView(footerView);
                }else{
                    feedListView.addFooterView(footerView);
                }
                }
        });
    }

}
