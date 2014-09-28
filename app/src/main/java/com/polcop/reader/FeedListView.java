package com.polcop.reader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by oleg on 03.09.14.
 */
public class FeedListView extends ListView {

    private boolean loading;
    private OnScrollListener onScrollListener;
    private Pagination pagination;
    private LoadingFooterView loadingFooterView;

    public interface Pagination{
        public void onLoadContent();
    }


    public FeedListView(Context context) {
        super(context);
        init();
    }

    public FeedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLoading(false);
        loadingFooterView = new LoadingFooterView(getContext());
        addFooterView(loadingFooterView);
        this.setVisibility(INVISIBLE);
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (totalItemCount > 0 && !isLoading() && (lastVisibleItem == totalItemCount)) {
                    if(PageInfo.getInstance().getPreviousPage()==null){
                        removeFooterView(loadingFooterView);
                    }
                    setLoading(true);
                    pagination.onLoadContent();
                }
            }
        });
    }

    public void updateFeedListView(FeedAdapter feedAdapter){
        ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction().remove(LoadingDialog.getDialod()).commitAllowingStateLoss();
        //загрузка окончена. разрешить новую
        setLoading(false);
        setVisibility(View.VISIBLE);
        if (getAdapter()==null){
            //устанавливает адаптер один раз, после первой загрузки контента лодером
            setAdapter(feedAdapter);
        }
        feedAdapter.updateData(PageInfo.getInstance().getStoryInfos());
        feedAdapter.notifyDataSetChanged();
    }

    private boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean isLoading) {
        this.loading = isLoading;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public LoadingFooterView getLoadingFooterView() {
        return loadingFooterView;
    }

}
