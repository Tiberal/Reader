package com.polcop.reader.background;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.polcop.reader.Constants;
import com.polcop.reader.MainActivity;
import com.polcop.reader.PageInfo;
import com.polcop.reader.UI.FeedListView;
import com.polcop.reader.Utils;
import com.polcop.reader.adapters.FeedAdapter;
import com.polcop.reader.background.ItHappensAndZadolbaliLoader;

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
        feedAdapter = new FeedAdapter(context, PageInfo.getInstance().getStoryInfos());
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle bundle) {
        link = bundle.getString(Constants.LINK);
        //.makeText(context,"start loader",Toast.LENGTH_SHORT).show();
        switch (Utils.getLoaderId()){
            case Constants.IT_HAPPENS_LOADER:
                return new ItHappensAndZadolbaliLoader(context,link, Constants.IT_HAPPENS_TAG);
            case  Constants.ZADOLBALI_LOADER:
                return new ItHappensAndZadolbaliLoader(context, link, Constants.ZADDOLBALI_TAG);
            case  Constants.BASH_LOADER:
                return new BashLoader(context, link);
            case Constants.KILL_ME_PLZ_LOADER:
                return new KillMePlzLoader(context,link);
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
