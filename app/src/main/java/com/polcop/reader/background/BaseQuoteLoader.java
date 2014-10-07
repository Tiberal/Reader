package com.polcop.reader.background;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.polcop.reader.PageInfo;
import com.polcop.reader.StoryInfo;
import com.polcop.reader.TagInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by oleg on 07.10.14.
 */
public abstract class BaseQuoteLoader extends AsyncTaskLoader<Boolean> {

    private String link;
    private String maxPage;
    private String perviousPage;

    public BaseQuoteLoader(Context context, String link) {
        super(context);
        this.link = link;
    }

    @Override
    protected void onStartLoading() {
        if(maxPage==null&&perviousPage==null){
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public Boolean loadInBackground() {
        ArrayList<StoryInfo> storyInfos;
        ArrayList<TagInfo> tagInfos;
        try {
            Document document = Jsoup.connect(link).timeout(10000).get();
            tagInfos = getTagInfo(document);
            storyInfos = getStoryInfos(document);
            maxPage = getMaxPage(document);
            perviousPage = getPerviousPage(document);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (PageInfo.getInstance().getStoryInfos() == null) {
            PageInfo.getInstance().setStoryInfos(storyInfos);
        } else {
            PageInfo.getInstance().addStoryInfos(storyInfos);
        }
        PageInfo.getInstance().setMaxPageNumber(maxPage);
        PageInfo.getInstance().setPreviousPage(perviousPage);
        PageInfo.getInstance().setTagInfos(tagInfos);
        return true;
    }

    abstract protected ArrayList<StoryInfo> getStoryInfos(Document document);

    abstract protected ArrayList<TagInfo> getTagInfo(Document document);

    abstract protected String getMaxPage (Document document);

    abstract protected String getPerviousPage (Document document);
}
