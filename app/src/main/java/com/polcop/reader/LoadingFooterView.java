package com.polcop.reader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by oleg on 04.09.14.
 */
public class LoadingFooterView extends LinearLayout {
    public LoadingFooterView(Context context) {
        super(context);
        init();
    }

    public LoadingFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(),R.layout.loading_footer_view,this);
    }
}
