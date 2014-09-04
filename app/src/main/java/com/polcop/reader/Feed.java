package com.polcop.reader;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oleg on 03.09.14.
 */
public class Feed extends Fragment {

    private FeedListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_layout, null);
        listView = (FeedListView) view.findViewById(R.id.feed_list);
        listView.setDivider(new ColorDrawable(Color.parseColor("#a4a4a4")));
        listView.setDividerHeight(10);
        final LoadersControl loadersControl = new LoadersControl(getActivity(),getListView());
        listView.setPagination(new FeedListView.Pagination() {
            @Override
            public void onLoadContent() {
                Bundle bundle = new Bundle();
                bundle.putString("URL","http://ithappens.me/page/"+PageInfo.getInstance().getPreviousPage());
                getActivity().getSupportLoaderManager().restartLoader(1,bundle,loadersControl);
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("URL","http://ithappens.me/");
        getActivity().getSupportLoaderManager().restartLoader(1,bundle,loadersControl);
        return view;
    }

    public FeedListView getListView() {
        return listView;
    }

}
