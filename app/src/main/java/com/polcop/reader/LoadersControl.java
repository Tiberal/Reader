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

    public LoadersControl(Context context, FeedListView feedListView) {
        this.context = context;
        this.feedListView=feedListView;
        feedAdapter = new FeedAdapter(context,PageInfo.getInstance().getStoryInfos());
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle bundle) {
        link = bundle.getString(Constants.LINK);
        switch (Utils.getLoaderId()){
            case Constants.IT_HAPPENS_LOADER:
                return new ItHappensLoader(context,link, Constants.IT_HAPPENS_TAG);
            case  Constants.ZADOLBALI_LOADER:
                return new ItHappensLoader(context, link, Constants.ZADDOLBALI_TAG);
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
                Utils.dismissLoadingDialog(context);
                feedListView.setLoadContent(false);
                feedListView.setVisibility(View.VISIBLE);
                if (feedListView.getAdapter()==null){
                    //устанавливает адаптер один раз, после первой загрузки контента лодером
                    feedListView.setAdapter(feedAdapter);
                }
                feedAdapter.updateData(PageInfo.getInstance().getStoryInfos());
                feedAdapter.notifyDataSetChanged();
            }
        });
    }

}
