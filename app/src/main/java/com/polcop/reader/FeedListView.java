package com.polcop.reader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by oleg on 03.09.14.
 */
public class FeedListView extends ListView {

    private boolean isLoadContent;
    private OnScrollListener onScrollListener;
    private Pagination pagination;

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
        isLoadContent = true;
        addFooterView(new LoadingFooterView(getContext()));
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
                if (totalItemCount > 0 && !isLoadContent() && (lastVisibleItem == totalItemCount)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                    pagination.onLoadContent();
                }
            }
        });
    }

    public boolean isLoadContent() {
        return isLoadContent;
    }

    public void setLoadContent(boolean isLoadContent) {
        this.isLoadContent = isLoadContent;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

}
