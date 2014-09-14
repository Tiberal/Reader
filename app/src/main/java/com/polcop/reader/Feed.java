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
    private LoadersControl loadersControl;
    private boolean aBoolean = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_layout, null);
        listView = (FeedListView) view.findViewById(R.id.feed_list);
        listView.setDivider(new ColorDrawable(Color.parseColor("#a4a4a4")));
        listView.setDividerHeight(10);
        loadersControl = new LoadersControl(getActivity(),getListView());
        //todo лодер контрол сделать в единственно экземпляре
        listView.setPagination(new FeedListView.Pagination() {
            @Override
            public void onLoadContent() {
                if(PageInfo.getInstance().getPreviousPage()==null) return;
                if(PageInfo.getInstance().getCurrentPage().equals(Constants.IT_HAPPENS_LINK)){
                    loadData("http://ithappens.me/page/"+PageInfo.getInstance().getPreviousPage(),Constants.IT_HAPPENS_LOADER);
                }else{
                    loadData(PageInfo.getInstance().getCurrentPage()+"/"+PageInfo.getInstance().getPreviousPage(),Constants.IT_HAPPENS_LOADER);
                }

            }
        });
        if(aBoolean){
            Bundle arg = getArguments();
            loadData(arg.getString(Constants.CONTENT_KEY),arg.getInt(Constants.ID_KEY));
            aBoolean=false;
        }else{
            //отобразить ленту там, где был переход по ссылке
            FeedAdapter  adapter = new FeedAdapter(getActivity(),null);
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            adapter.updateData(PageInfo.getInstance().getStoryInfos());
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    public FeedListView getListView() {
        return listView;
    }

    public void loadData (String link, int loaderId){
        Bundle bundle = new Bundle();
        bundle.putString("URL",link);
        getActivity().getSupportLoaderManager().restartLoader(loaderId,bundle,loadersControl);
    }

}
