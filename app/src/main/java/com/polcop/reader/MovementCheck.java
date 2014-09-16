package com.polcop.reader;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oleg on 12.09.14.
 */
public class MovementCheck  extends LinkMovementMethod {

    private Context context;

    public MovementCheck(Context context) {
        this.context=context;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();
            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            URLSpan[] links = buffer.getSpans(off, off, URLSpan.class);
            if (links.length==0) return true;
            String link = links[0].getURL();
            Pattern pattern = Pattern.compile("^/story/[0-9]+$");
            Matcher matcher = pattern.matcher(link);
            if(matcher.matches()){
                Toast.makeText(context, "link " + link, Toast.LENGTH_LONG).show();
                showLoadingDialog();
                SingleStoryFragment singleStoryFragment = new SingleStoryFragment();
                Bundle arg = new Bundle();
                arg.putString(Constants.LINK, link);
                singleStoryFragment.setArguments(arg);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container,singleStoryFragment,null).addToBackStack(null).commit();
                return true;
            }
            if(Utils.isMainLink(Constants.IT_HAPPENS_LINK+link,PageInfo.getInstance().getTagInfos())){
                Feed feed = new Feed();
                Bundle arg = new Bundle();
                arg.putString(Constants.LINK,Constants.IT_HAPPENS_LINK+link);
                arg.putInt(Constants.ID_KEY,Utils.getLoaderId(context));
                feed.setArguments(arg);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container,feed,null).commit();
                Toast.makeText(context, "link " + link, Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    private void showLoadingDialog(){
        LoadingDialog loadingDialod = LoadingDialog.getDialod();
        loadingDialod.show(((MainActivity)context).getSupportFragmentManager(),Constants.LOADING_DIALOG_TAG);
    }

}
