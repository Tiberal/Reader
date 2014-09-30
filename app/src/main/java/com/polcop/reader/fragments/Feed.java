package com.polcop.reader.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.polcop.reader.Constants;
import com.polcop.reader.background.LoadersControl;
import com.polcop.reader.MainActivity;
import com.polcop.reader.PageInfo;
import com.polcop.reader.R;
import com.polcop.reader.UI.FeedListView;
import com.polcop.reader.UI.LoadingFooterView;
import com.polcop.reader.Utils;

/**
 * Created by oleg on 03.09.14.
 */
public class Feed extends Fragment {

    private FeedListView listView;
    private LoadersControl loadersControl;
    private LoadingDialog loadingDialod;
    private String loadLink;
    private boolean firstLoad = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialod = LoadingDialog.getDialod();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(PageInfo.getInstance().getStoryInfos()!=null){
//            getListView().updateFeedListView(loadersControl.getAdapter());
//        }
        ((MainActivity)getActivity()).getSupportActionBar().getCustomView().setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).getSupportActionBar().getCustomView().setClickable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_layout, null);
        listView = (FeedListView) view.findViewById(R.id.feed_list);
        listView.setDivider(new ColorDrawable(Color.parseColor("#a4a4a4")));
        listView.setDividerHeight(10);
        listView.getLoadingFooterView().setOnReloadListener(new LoadingFooterView.OnReloadListener() {
            @Override
            public void onReload() {
                if(Utils.isOnline()){
                    listView.getLoadingFooterView().setInvisibleReloadButton();
                    listView.getLoadingFooterView().setVisibleLoadingLoadingViews();
                    loadData(loadLink, Constants.IT_HAPPENS_LOADER);
                }else{
                    Toast.makeText(getActivity(), "Отсутствует подключение к сети", Toast.LENGTH_LONG).show();
                }
            }
        });
        loadersControl = new LoadersControl(getActivity(),getListView());
        //todo лодер контрол сделать в единственно экземпляре
        listView.setPagination(new FeedListView.Pagination() {
            @Override
            public void onLoadContent() {
                int id = Utils.getLoaderId();
                switch (id){
                    case Constants.IT_HAPPENS_LOADER:
                        if (PageInfo.getInstance().getCurrentPage().equals(Constants.IT_HAPPENS_LINK)){
                        loadLink = Constants.IT_HAPPENS_PAGE+PageInfo.getInstance().getPreviousPage();
                    }else {
                        loadLink = PageInfo.getInstance().getCurrentPage()+"/"+PageInfo.getInstance().getPreviousPage();
                    }
                        break;
                    case Constants.ZADOLBALI_LOADER:
                        loadLink = Constants.ZADOLBALI_LINK+PageInfo.getInstance().getPreviousPage();
                        break;
                    case Constants.BASH_LOADER:
                        if(PageInfo.getInstance().getCurrentPage().equals(Constants.BASH_ABYSS_BEST))
                            loadLink = Constants.BASH_ABYSS_BEST+"/"+PageInfo.getInstance().getPreviousPage();
                        else if(PageInfo.getInstance().getCurrentPage().equals(Constants.BASH_BY_RATING))
                            loadLink = Constants.BASH_BY_RATING+"/"+PageInfo.getInstance().getPreviousPage();
                        else if(PageInfo.getInstance().getCurrentPage().equals(Constants.BASH_LINK))
                            loadLink = Constants.BASH_LINK+"/"+PageInfo.getInstance().getPreviousPage();
                        else {
                            listView.removeLoadingFooterView();
                            return;
                        }
                }

                if(!Utils.isOnline()){
                    Toast.makeText(getActivity(), "Отсутствует подключение к сети", Toast.LENGTH_LONG).show();
                    listView.getLoadingFooterView().setInvisibleLoadingLoadingViews();
                    listView.getLoadingFooterView().setVisibleReloadButton();
                    return;
                }
                if(PageInfo.getInstance().getPreviousPage()==null) return;
                Toast.makeText(getActivity(),loadLink,Toast.LENGTH_LONG).show();
                loadData(loadLink,Utils.getLoaderId());
                }
        });
        if(firstLoad){
            //выполняется при создании фрагмента и первой загрузке
            Bundle arg = getArguments();
            loadingDialod.show(getActivity().getSupportFragmentManager(), Constants.LOADING_DIALOG_TAG);
            loadData(arg.getString(Constants.LINK),arg.getInt(Constants.LOADER_ID));
            firstLoad=false;
        }else{
            //todo после возвращения из дома если есть истории показить их
            //отобразить ленту если был возрат с одиночной истории
            listView.updateFeedListView(loadersControl.getAdapter());
        }
        return view;
    }

    public FeedListView getListView() {
        return listView;
    }

    public void loadData (String link, int loaderId){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LINK,link);
        getActivity().getSupportLoaderManager().restartLoader(loaderId,bundle,loadersControl);
    }

}
