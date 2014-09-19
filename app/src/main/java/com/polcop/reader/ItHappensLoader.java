package com.polcop.reader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oleg on 04.09.14.
 */
public class ItHappensLoader extends AsyncTaskLoader<Boolean> implements Comparator<TagInfo> {

    private String tagLink;
    private String link;
    private String perviousPage;

    /**
     * Stores away the application context associated with context. Since Loaders can be used
     * across multiple activities it's dangerous to store the context directly.
     *
     * @param context used to retrieve the application context.
     * @param link
     */
    public ItHappensLoader(Context context, String link, String tagLink) {
        super(context);
        this.link = link;
        this.tagLink = tagLink;
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
    public Boolean loadInBackground() {
        ArrayList<StoryInfo> storyInfos = new ArrayList<StoryInfo>();
        ArrayList<TagInfo> tagInfos = new ArrayList<TagInfo>();
        int loaderId = Utils.getLoaderId();
        try {
            Elements elements;
            Document document;
            document = Jsoup.connect(tagLink).timeout(10000).get();
            TagInfo.Builder tagBuilder = new TagInfo.Builder();
            elements = document.select(".cloud").select("li");
            for (Element element:elements){
                Element element1 = element.child(0);
                tagBuilder.setHtmlTag(element1.outerHtml());
                tagBuilder.setTagName(element.text());
                if(loaderId==Constants.IT_HAPPENS_LOADER){
                    tagBuilder.setTagURL(Constants.IT_HAPPENS_LINK + element.child(0).attr("href"));
                }else{
                    tagBuilder.setTagURL(Constants.ZADOLBALI_LINK + element.child(0).attr("href"));
                }
                tagBuilder.setTotal(Integer.parseInt(element.attr("data-count")));
                tagInfos.add(tagBuilder.build());
            }
            Collections.sort(tagInfos, this);
            document = Jsoup.connect(link).timeout(10000).get();
            elements =document.select("div.content").select(".story");
            StoryInfo.Builder storyBuilder=new StoryInfo.Builder();
            for (Element element:elements){
                storyBuilder.setBadURL("");
                storyBuilder.setGoodURL(element.select("div.button-group.like").select("a.button").attr("href"));
                storyBuilder.setPublishDate(element.select(".date-time").text());
                storyBuilder.setRate(element.select("div.rating").text());
                storyBuilder.setStoryNumber("#" + element.select(".id").text());
                storyBuilder.setTags(getHtmlLinkArray(element));
                storyBuilder.setStory(Html.fromHtml(element.select(".text").html()));
                storyBuilder.setStoryName(element.children().get(1).text());
                storyInfos.add(storyBuilder.build());
            }
            setPerviousPage(document);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (Utils.isMainLink(link, tagInfos)){
            PageInfo.getInstance().setStoryInfos(null);
            if(loaderId==Constants.IT_HAPPENS_LOADER){
                if (perviousPage!=null)
                    PageInfo.getInstance().setMaxPageNumber(String.valueOf(Integer.parseInt(perviousPage)+2));
                else
                    PageInfo.getInstance().setMaxPageNumber(String.valueOf(2));
            }else{
                if(link.equals(Constants.ZADOLBALI_LINK)){
                    //todo получаем дату из шапки
                }else{
                    //todo так же как в итхепенс
                }
            }
        }
        if (PageInfo.getInstance().getStoryInfos()==null){
            PageInfo.getInstance().setStoryInfos(storyInfos);
        }else {
            PageInfo.getInstance().addStoryInfos(storyInfos);
        }
        PageInfo.getInstance().setTagInfos(tagInfos);
        return true;
    }

    private String getPreviousPageNumber(Document document){
        try {
            return document.select("li.prev").get(1).text();
        }catch (IndexOutOfBoundsException e){
           return null;
        }
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

    @Override
    public int compare(TagInfo lhs, TagInfo rhs) {
        if (lhs.getTotal() > rhs.getTotal()){
            return -1;
        }else if (lhs.getTotal() < rhs.getTotal()){
            return 1;
        }else return 0;    }

    public void setPerviousPage(Document document) {
        int id = Utils.getLoaderId();
        if(id==Constants.IT_HAPPENS_LOADER){
            perviousPage = document.select("li.prev").get(0).text();
            PageInfo.getInstance().setPreviousPage(perviousPage);
        }else{
            Element elements = document.select("li.prev").get(0).child(0);
            perviousPage = elements.attr("href");
            PageInfo.getInstance().setPreviousPage(perviousPage);
        }
    }
}


