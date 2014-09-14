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
            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length==0) return true;
            String s = link[0].getURL();
            Pattern pattern = Pattern.compile("^/story/[0-9]+$");
            Matcher matcher = pattern.matcher(s);
            if(matcher.matches()){
                Toast.makeText(context, "link " + s, Toast.LENGTH_LONG).show();
                SingleStoryFragment fragment = new SingleStoryFragment();
                Bundle arg = new Bundle();
                arg.putString("URL",s);
                fragment.setArguments(arg);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment,null).addToBackStack(null).commit();
                return true;
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }

}
