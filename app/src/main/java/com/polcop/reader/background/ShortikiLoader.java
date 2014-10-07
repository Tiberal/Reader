package com.polcop.reader.background;

import android.content.Context;
import android.text.Html;

import com.polcop.reader.R;
import com.polcop.reader.StoryInfo;
import com.polcop.reader.TagInfo;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by oleg on 07.10.14.
 */
public class ShortikiLoader extends BaseQuoteLoader {

    private String link;

    public ShortikiLoader(Context context, String link) {
        super(context, link);
    this.link = link;
    }

    @Override
    protected ArrayList<StoryInfo> getStoryInfos(Document document) {
        ArrayList<StoryInfo> storyInfos = new ArrayList<StoryInfo>();
        Elements story = document.select(".shell");
        StoryInfo.Builder builder = new StoryInfo.Builder();
        for (Element element:story){
            builder.setStoryNumber(element.select("div.id").text());
            builder.setRate(element.select("div.rating").text());
            builder.setPublishDate(element.select("div.date").text());
            builder.setStory(Html.fromHtml(element.select("div.shortik").text()));
            builder.setGoodURL(link);
            storyInfos.add(builder.build());
        }
        story.clear();
        return storyInfos;
    }

    @Override
    protected ArrayList<TagInfo> getTagInfo(Document document) {
        ArrayList<TagInfo> tagInfos = new ArrayList<TagInfo>();
        String [] tagTitle = getContext().getResources().getStringArray(R.array.shortiki_tag);
        String [] tagLink = getContext().getResources().getStringArray(R.array.shortiki_link);
        TagInfo.Builder builder=new TagInfo.Builder();
        for (int i=0;i<tagTitle.length;i++){
            builder.setTagTitle(tagTitle[i]);
            builder.setTagURL(tagLink[i]);
            tagInfos.add(builder.build());
        }
        tagInfos.size();
        return tagInfos;
    }

    @Override
    protected String getMaxPage(Document document) {
        Element max = document.select("div.pagination").select("a.active").get(0);
        return max.text();
    }

    @Override
    protected String getPerviousPage(Document document) {
        Elements pages = document.select("div.pagination").get(0).children();
        ListIterator<Element> iterator = pages.listIterator();
        while (iterator.hasNext()){
            if (iterator.next().select("a.active")!=null){
                return iterator.next().text();
            }
        }
    return null;
    }
}
