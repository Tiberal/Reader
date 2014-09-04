package com.polcop.reader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by oleg on 04.09.14.
 */
public class ItHappensLoader extends AsyncTaskLoader<ArrayList<StoryInfo>> {

    private String link;

    /**
     * Stores away the application context associated with context. Since Loaders can be used
     * across multiple activities it's dangerous to store the context directly.
     *
     * @param context used to retrieve the application context.
     * @param link
     */
    public ItHappensLoader(Context context, String link) {
        super(context);
        this.link = link;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public ArrayList<StoryInfo> loadInBackground() {
        ArrayList<StoryInfo> storyInfos = new ArrayList<StoryInfo>();
        try {
            Document document = Jsoup.connect(link).timeout(10000).get();
            Elements elements =document.select("div.content").select(".story");
            PageInfo.getInstance().setPreviousPage(getPreviousPageNumber(document));
            for (Element element:elements){
                StoryInfo.Builder builder=new StoryInfo.Builder();
                builder.setStory(element.text());
                storyInfos.add(builder.build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storyInfos;
    }

    private String getPreviousPageNumber(Document document){
        String s = document.select("li.prev").get(1).text();
        return s;//.substring(s.indexOf("–")+1,s.indexOf("–")+5);
    }

}


