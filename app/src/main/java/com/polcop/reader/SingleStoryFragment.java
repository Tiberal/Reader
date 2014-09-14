package com.polcop.reader;


import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.text.Html;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Oleg on 14.09.2014.
 */
public class SingleStoryFragment extends ListFragment {

    private String link;
    private ExecutorService executorService;
    private Future<StoryInfo> storyInfoFuture;
    private FeedAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        while (!storyInfoFuture.isDone()){
            //w8
        }
        try {
            ArrayList<StoryInfo> storyInfos = new ArrayList<StoryInfo>();
            storyInfos.add(storyInfoFuture.get());
            adapter.updateData(storyInfos);
            adapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FeedAdapter(getActivity(),null);
        setListAdapter(adapter);
        executorService = Executors.newFixedThreadPool(5);
        link = getArguments().getString("URL");
        storyInfoFuture = executorService.submit(new Callable<StoryInfo>() {
            @Override
            public StoryInfo call() throws Exception {
                Document document = Jsoup.connect(Constants.IT_HAPPENS_LINK+link).timeout(10000).get();
                Elements elements =document.select("div.content").select(".story");
                StoryInfo.Builder storyBuilder=new StoryInfo.Builder();
                storyBuilder.setBadURL("");
                storyBuilder.setGoodURL(elements.get(0).select("div.button-group.like").select("a.button").attr("href"));
                storyBuilder.setPublishDate(elements.get(0).select(".date-time").text());
                storyBuilder.setRate(elements.get(0).select("div.rating").text());
                storyBuilder.setStoryNumber("#" + elements.get(0).select(".id").text());
                storyBuilder.setTags(getHtmlLinkArray(elements.get(0)));
                storyBuilder.setStory(Html.fromHtml(elements.get(0).select(".text").html()));
                storyBuilder.setStoryName(elements.get(0).children().get(1).text());
                return storyBuilder.build();
            }
        });
    }

    private String[] getHtmlLinkArray (Element element){
        Elements elements;
        try {
            elements = element.select(".tags").get(0).child(1).children();
        }catch (IndexOutOfBoundsException e){
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        for (Element e:elements){
            list.add(e.html());
        }
        return list.toArray(new String[]{});
    }

}
