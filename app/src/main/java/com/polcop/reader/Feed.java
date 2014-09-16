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
    private boolean isFirstLoad = true;
    private LoadingDialog loadingDialod;


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
                String loadLink;
                if (PageInfo.getInstance().getCurrentPage().equals(Constants.IT_HAPPENS_LINK)){
                    loadLink = "http://ithappens.me/page/"+PageInfo.getInstance().getPreviousPage();
                }else {
                    loadLink = PageInfo.getInstance().getCurrentPage()+"/"+PageInfo.getInstance().getPreviousPage();
                }
                if(!Utils.isOnline(getActivity())){
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.LINK,loadLink);
                    bundle.putInt(Constants.LOADER_ID,Constants.IT_HAPPENS_LOADER);
                    Utils.showNoConnectionFragment(bundle,getActivity());
                    return;
                }
                if(PageInfo.getInstance().getPreviousPage()==null) return;
                loadData(loadLink,Utils.getLoaderId(getActivity()));
                }
        });
        if(isFirstLoad){
            //выполняется при создании фрагмента и первой загрузке
            Bundle arg = getArguments();
            loadingDialod = LoadingDialog.getDialod();
            loadingDialod.show(getActivity().getSupportFragmentManager(), Constants.LOADING_DIALOG_TAG);
            loadData(arg.getString(Constants.LINK),arg.getInt(Constants.LOADER_ID));
            isFirstLoad = false;
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
