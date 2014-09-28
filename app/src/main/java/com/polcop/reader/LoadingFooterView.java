package com.polcop.reader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by oleg on 04.09.14.
 */
public class LoadingFooterView extends LinearLayout implements View.OnClickListener{

    private Button reload;
    private LinearLayout loadingLinearLayout;
    private OnReloadListener onReloadListener;

    public  interface OnReloadListener{
        public void onReload();
    }

    public LoadingFooterView(Context context) {
        super(context);
        init();
    }

    public LoadingFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = inflate(getContext(),R.layout.loading_footer_view,this);
        reload = (Button)view.findViewById(R.id.reload);
        reload.setOnClickListener(this);
        setInvisibleReloadButton();
        loadingLinearLayout = (LinearLayout)view.findViewById(R.id.loading_linear_layout);
    }

    public void setVisibleReloadButton(){
        reload.setVisibility(VISIBLE);
        reload.setClickable(true);
    }

    public void setInvisibleReloadButton(){
        reload.setVisibility(INVISIBLE);
        reload.setClickable(false);
    }

    public void setVisibleLoadingLoadingViews(){
        loadingLinearLayout.setVisibility(VISIBLE);
    }

    public void setInvisibleLoadingLoadingViews(){
        loadingLinearLayout.setVisibility(INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        onReloadListener.onReload();
    }

    public void setOnReloadListener(OnReloadListener onReloadListener){
        this.onReloadListener = onReloadListener;
    }
}
