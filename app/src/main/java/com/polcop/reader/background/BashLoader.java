package com.polcop.reader.background;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.Html;

import com.polcop.reader.Constants;
import com.polcop.reader.PageInfo;
import com.polcop.reader.R;
import com.polcop.reader.StoryInfo;
import com.polcop.reader.TagInfo;
import com.polcop.reader.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oleg on 30.09.14.
 */
public class BashLoader extends AsyncTaskLoader<Boolean> {

    private String link;
    private String perviousPage;
    private String maxPage;

    public BashLoader(Context context, String link) {
        super(context);
        this.link = link;
    }

    @Override
    protected void onStartLoading() {
        if (maxPage == null && perviousPage == null) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public Boolean loadInBackground() {
        ArrayList<StoryInfo> storyInfos = new ArrayList<StoryInfo>();
        ArrayList<TagInfo> tagInfos = new ArrayList<TagInfo>();
        try {
            String[] tag = getContext().getResources().getStringArray(R.array.bash_tag);
            String[] tagLink = getContext().getResources().getStringArray(R.array.bash_link);
            ;
            TagInfo.Builder tagBuilder;
            for (int i = 0; i < tag.length; i++) {
                tagBuilder = new TagInfo.Builder();
                tagBuilder.setTagTitle(tag[i]);
                tagBuilder.setTagURL(tagLink[i]);
                tagInfos.add(tagBuilder.build());
            }
            Document document = Jsoup.connect(link).timeout(10000).get();
            Elements elements, text, rate, id, goodURL, badURL, date;
            elements = document.select(".quote");
            if (link.equals(Constants.BASH_ABYSS_TOP)) {
                id = elements.select(".abysstop");
                date = elements.select(".abysstop-date");
            } else {
                id = elements.select(".id");
                date = elements.select(".date");
            }
            text = elements.select(".text");
            rate = elements.select(".rating");
            goodURL = elements.select(".up");
            badURL = elements.select(".down");
            StoryInfo.Builder storyBuilder;
            for (int i = 0; i < text.size(); i++) {
                storyBuilder = new StoryInfo.Builder();
                storyBuilder.setTags(null);
                storyBuilder.setStory(Html.fromHtml(text.get(i).html()));
                storyBuilder.setStoryNumber(id.size() != 0 ? id.get(i).text() + " " : "");
                storyBuilder.setPublishDate(date.get(i).text());
                storyBuilder.setRate(rate.size() != 0 ? rate.get(i).text() : "");
                storyBuilder.setGoodURL(goodURL.size() != 0 ? "http://bash.im" + goodURL.get(i).attr("href") : "");
                storyBuilder.setBadURL(badURL.size() != 0 ? "http://bash.im" + badURL.get(i).attr("href") : "");
                storyInfos.add(storyBuilder.build());
            }
            if (!noPagination()) {
                setMaxPage(document);
                setPerviousPage(document);
            }

        } catch (IOException e) {
            //todo no connect frag
            e.printStackTrace();
        }
        if (PageInfo.getInstance().getStoryInfos() == null) {
            PageInfo.getInstance().setStoryInfos(storyInfos);
        } else {
            PageInfo.getInstance().addStoryInfos(storyInfos);
        }
        PageInfo.getInstance().setTagInfos(tagInfos);
        return true;
    }

    private void setPerviousPage(Document document) {
        if (link.contains(Constants.BASH_ABYSS_BEST)) {
            String currentPage = document.select(".pager").select("input").attr("data-date");
            perviousPage = getPerviousDate(currentPage);
            PageInfo.getInstance().setPreviousPage(perviousPage);
            return;
        }
        String currentPage = document.select(".pager").select("input").attr("value");
        int page = Integer.parseInt(currentPage);
        if (link.contains(Constants.BASH_LINK)) {
            if ((page - 1) == 0) {
                perviousPage = null;
                PageInfo.getInstance().setPreviousPage(perviousPage);
            } else {
                perviousPage = String.valueOf(page - 1);
                PageInfo.getInstance().setPreviousPage(perviousPage);
            }
            return;
        }
        if (link.contains(Constants.BASH_BY_RATING)) {
            if ((page + 1) > Integer.parseInt(PageInfo.getInstance().getMaxPageNumber())) {
                perviousPage = null;
                PageInfo.getInstance().setPreviousPage(perviousPage);
            } else {
                perviousPage = String.valueOf(page + 1);
                PageInfo.getInstance().setPreviousPage(perviousPage);
            }
        }

    }

    private String getPerviousDate(String currentPage) {
        currentPage = currentPage.substring(0, 4) + "/" + currentPage.substring(4, 6) + "/" + currentPage.substring(6);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = sdf.parse(currentPage);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //получить предыдущий день
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String perviousDay = simpleDateFormat.format(cal.getTime());
        return perviousDay.substring(0, 4) + perviousDay.substring(5, 7) + perviousDay.substring(8);
    }

    private void setMaxPage(Document document) {
        maxPage = document.select(".pager").select("input").attr("max");
        PageInfo.getInstance().setMaxPageNumber(maxPage);
    }

    private boolean noPagination() {
        if (link.equals(Constants.BASH_RANDOM)) return true;
        if (link.equals(Constants.BASH_BEST)) return true;
        if (link.equals(Constants.BASH_ABBYS)) return true;
        if (link.equals(Constants.BASH_ABBYS_TOP)) return true;
        return false;
    }
}