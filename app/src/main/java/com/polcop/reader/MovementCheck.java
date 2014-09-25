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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oleg on 12.09.14.
 */
public class MovementCheck  extends LinkMovementMethod {

    private Context context;

    public MovementCheck() {
        this.context=PageInfo.getInstance().getContext();
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {

        int action = event.getAction();
        int loaderId = Utils.getLoaderId();

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
            switch (loaderId){
                case Constants.IT_HAPPENS_LOADER:
                    if(Utils.isMainLink(Constants.IT_HAPPENS_LINK+link,PageInfo.getInstance().getTagInfos())){
                        loadTagData(Constants.IT_HAPPENS_LINK,link);
                        Toast.makeText(context, "link " + link, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    if(openStoryLink(Constants.IT_HAPPENS_LINK,link)){
                        Toast.makeText(context, "link " + link, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    break;
                case Constants.ZADOLBALI_LOADER:
                    if(Utils.isMainLink(Constants.ZADOLBALI_LINK+link,PageInfo.getInstance().getTagInfos())){
                        loadTagData(Constants.ZADOLBALI_LINK,link);
                        Toast.makeText(context, "link " + link, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    if(openStoryLink(Constants.ZADOLBALI_LINK,link)){
                        Toast.makeText(context, "link " + link, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    break;
            }

        }
        return super.onTouchEvent(widget, buffer, event);
    }

    //открывает ссылку на историю
    private boolean openStoryLink(String linkPart1, String linkPart2){
        Pattern pattern = Pattern.compile("^/story/[0-9]+$");
        Matcher matcher = pattern.matcher(linkPart2);
        if(matcher.matches()) {
            showLoadingDialog();
            SingleStoryFragment singleStoryFragment = new SingleStoryFragment();
            Bundle arg = new Bundle();
            arg.putString(Constants.LINK, linkPart1+linkPart2);
            singleStoryFragment.setArguments(arg);
            ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, singleStoryFragment, null).addToBackStack(null).commit();
            return true;
        }
        return false;
    }

    private void loadTagData(String linkPart1, String linkPart2){
        Utils.clearBackStack();
        PageInfo.getInstance().setCurrentPage(linkPart1+linkPart2);
        Feed feed = new Feed();
        Bundle arg = new Bundle();
        arg.putString(Constants.LINK, linkPart1 + linkPart2);
        arg.putInt(Constants.ID_KEY, Utils.getLoaderId());
        feed.setArguments(arg);
        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container,feed,null).commit();
        ((MainActivity) context).showCurrentPageInActionBar(getTagNameByLink(linkPart1 + linkPart2));
    }

    private void showLoadingDialog(){
        LoadingDialog loadingDialod = LoadingDialog.getDialod();
        loadingDialod.show(((MainActivity)context).getSupportFragmentManager(),Constants.LOADING_DIALOG_TAG);
    }

    private String getTagNameByLink(String link){
        ArrayList<TagInfo> tagInfos = PageInfo.getInstance().getTagInfos();
        for (TagInfo tagInfo: tagInfos){
            if(tagInfo.getTagURL().equals(link))
                return tagInfo.getTagName();
        }
        return null;
    }

}
