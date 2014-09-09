package com.polcop.reader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        ArrayList<TagInfo> tagInfos = new ArrayList<TagInfo>();
        try {
            Elements elements;
            Document document;
            document = Jsoup.connect(Constants.IT_HAPPENS_TAG).timeout(10000).get();
            TagInfo.Builder tagBuilder = new TagInfo.Builder();
            elements = document.select(".cloud").select("li");
            for (Element element:elements){
                Element element1 = element.child(0);
                tagBuilder.setHtmlTag(element1.outerHtml());// Html.fromHtml(element1.html());
                tagBuilder.setTagName(element.text());
                tagBuilder.setTagURL(element.child(0).attr("href"));
                tagBuilder.setTotal(Integer.parseInt(element.attr("data-count")));
                tagInfos.add(tagBuilder.build());
            }
            document = Jsoup.connect(link).timeout(10000).get();
            elements =document.select("div.content").select(".story");
            PageInfo.getInstance().setPreviousPage(getPreviousPageNumber(document));
            StoryInfo.Builder storyBuilder=new StoryInfo.Builder();
            for (Element element:elements){
                storyBuilder.setBadURL("");
                storyBuilder.setGoodURL(element.select("div.button-group.like").select("a.button").attr("href"));
                storyBuilder.setPublishDate(element.select(".date-time").text());
                storyBuilder.setRate(element.select("div.rating").text());
                storyBuilder.setStoryNumber(element.select(".id").text() + " ");
                storyBuilder.setTags(getHtmlLinkArray(element));
                //storyBuilder.setTags(element.select(".tags").text());
                storyBuilder.setStory(Html.fromHtml(element.select(".text").html()));
                storyInfos.add(storyBuilder.build());
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

    private String[] getHtmlLinkArray (Element element){
        Elements elements;
        try {
            elements = element.select(".tags").get(0).child(1).children();
        }catch (IndexOutOfBoundsException e){
            return null;
        }
        ArrayList<String>list = new ArrayList<String>();
        for (Element e:elements){
            list.add(e.html());
        }
        return list.toArray(new String[]{});
    }

}


