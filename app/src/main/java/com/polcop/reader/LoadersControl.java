package com.polcop.reader;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by oleg on 04.09.14.
 */
public class LoadersControl implements LoaderManager.LoaderCallbacks<Boolean> {

    private FeedListView feedListView;
    private Context context;
    private FeedAdapter feedAdapter;
    private String link;

    public LoadersControl(Context context, FeedListView feedListView) {
        this.context = context;
        this.feedListView=feedListView;
        feedAdapter = new FeedAdapter(context,PageInfo.getInstance().getStoryInfos());
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle bundle) {
        link = bundle.getString(Constants.LINK);
        int i = Utils.getLoaderId();
        switch (Utils.getLoaderId()){
            case Constants.IT_HAPPENS_LOADER:
                return new ItHappensAndZadolbaliLoader(context,link, Constants.IT_HAPPENS_TAG);
            case  Constants.ZADOLBALI_LOADER:
                return new ItHappensAndZadolbaliLoader(context, link, Constants.ZADDOLBALI_TAG);
        }
        return null;
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
                feedListView.updateFeedListView(feedAdapter);
            }
        });
    }

    public FeedAdapter getAdapter() {
        return feedAdapter;
    }
}
