package com.polcop.reader.background;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.Html;

import com.polcop.reader.Constants;
import com.polcop.reader.PageInfo;
import com.polcop.reader.StoryInfo;
import com.polcop.reader.TagInfo;
import com.polcop.reader.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by oleg on 01.10.14.
 */
public class KillMePlzLoader extends AsyncTaskLoader<Boolean> {

    private String link;
    private String maxPage;
    private String perviousPage;

    public KillMePlzLoader(Context context, String link) {
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
        ArrayList<StoryInfo> storyInfos = new ArrayList<StoryInfo>();
        ArrayList<TagInfo> tagInfos = new ArrayList<TagInfo>();
        int loaderId = Utils.getLoaderId();
        try {
            Document document = Jsoup.connect(link).timeout(10000).get();
            if (link.equals(Constants.KILL_ME_PLZ_LINK)){
                //парсим теги только во время загрузки главной страницы
                Elements allTags = document.select(".tag");
                TagInfo.Builder tagBuilder = new TagInfo.Builder();
                for(Element element:allTags){
                    String total = element.select("span").text();
                    total = total.substring(1,total.length()-1);
                    tagBuilder.setTotal(Integer.parseInt(total));
                    Element s = element.children().get(0);
                    tagBuilder.setTagURL(element.child(0).child(0).attr("href"));
                    String title = element.child(0).child(0).text();
                    tagBuilder.setTagTitle(title);
                    tagBuilder.setHtmlTag("<a href=\"" + link +"\">"+ title +"</a>");
                    tagInfos.add(tagBuilder.build());
                    PageInfo.getInstance().setTagInfos(tagInfos);
                }
            }
            Elements tags = document.select(".fi_tags");
            Elements pubid = document.select(".fi_pubid");
            Elements text = document.select(".fi_text");
            Elements vote = document.select(".fi_vote");
            StoryInfo.Builder storyBuilder = new StoryInfo.Builder();
            for (int i = 0; i < tags.size(); i++) {
                storyBuilder.setGoodURL(Constants.KILL_ME_PLZ_LINK + vote.get(i).child(0).select(".votelink").attr("href"));
                storyBuilder.setBadURL(Constants.KILL_ME_PLZ_LINK + vote.get(i).child(2).select(".votelink").attr("href"));
                storyBuilder.setTags(getHtmlLinkArray(tags.get(i).children()));
                storyBuilder.setStoryNumber(getStoryNumber(pubid.get(i).text()));
                storyBuilder.setPublishDate(getPublishTime(pubid.get(i).text()));
                storyBuilder.setRate(getRate(vote.get(i).text()));
                if (text.get(i).text().equals("18+")){
                    String link = text.get(i).select("a").attr("href");
                    try {
                        //загрузка 18+ историй в общую ленту
                        Document document1 = Jsoup.connect(Constants.KILL_ME_PLZ_LINK+link)
                                .get();
                        storyBuilder.setStory(Html.fromHtml(document1.select("div.fi_text").text()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    storyBuilder.setStory(Html.fromHtml(text.get(i).html()));
                }
                storyBuilder.setStoryTitle("");
                storyInfos.add(storyBuilder.build());
            }
            if(!(link.contains("top")||link.contains("random")))
                setPerviousPageAndMaxPage(document);
        } catch (IOException e) {
            //((MainActivity)getContext()).showNoConnectionFragment(link, Constants.KILL_ME_PLZ_LOADER);
            e.printStackTrace();
            return false;
        }
        if (PageInfo.getInstance().getStoryInfos() == null) {
            PageInfo.getInstance().setStoryInfos(storyInfos);
        } else {
            PageInfo.getInstance().addStoryInfos(storyInfos);
        }
        return true;
       }

    private void setPerviousPageAndMaxPage(Document document){
        Elements elements = document.select("span.pagina");
        PageInfo.getInstance().setMaxPageNumber(elements.get(0).text());
        for(int i = 0; i < elements.size(); i++){
            int s1 = elements.get(i).children().size();
            if(s1==0){
                    String s = elements.get(i).text();
                    if(s.equals("1")){
                        PageInfo.getInstance().setPreviousPage(null);
                    }else{
                        PageInfo.getInstance().setPreviousPage(elements.get(i+1).children().get(0).text());
                        return;
                    }
            }
        }
    }

    private String getRate(String string) {
        String [] strings = string.split(" ");
        return strings[2].substring(0,strings[2].length()-1);
    }

    private String getStoryNumber(String string) {
        return string.split(" ")[0];
    }

    private String getPublishTime(String string) {
        return string.substring(string.indexOf(" ") + 1);
    }

    private String[] getHtmlLinkArray(Elements elements) {
        if(elements.size()==0) return null;
        ArrayList<String> list = new ArrayList<String>();
        for (Element e : elements) {
            list.add(e.outerHtml());
        }
        return list.toArray(new String[]{});
    }

}
