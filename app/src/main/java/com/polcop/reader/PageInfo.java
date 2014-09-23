package com.polcop.reader;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by oleg on 04.09.14.
 */
public class PageInfo {

    private Context context;
    private static PageInfo instance;
    private ArrayList<StoryInfo> storyInfos=null;
    private ArrayList<TagInfo> tagInfos = null;
    private String currentPage;
    private String maxPageNumber;
    private String currentPageNumber;
    private String previousPage;
    private boolean maxPageMark = true;

    public PageInfo() {
    }

    public static  void initInstance (Context context){
        if (instance == null) {
            instance = new PageInfo();
            instance.setContext(context);
        }
    }

    public static synchronized  PageInfo getInstance(){
        return instance;
    }

    public ArrayList<StoryInfo> getStoryInfos() {
        return storyInfos;
    }

    public void setStoryInfos(ArrayList<StoryInfo> storyInfos) {
        this.storyInfos = storyInfos;
    }

    public void addStoryInfos(ArrayList<StoryInfo> data) {
        for(StoryInfo storyInfo:data){
            storyInfos.add(storyInfo);
        }
    }


    public ArrayList<TagInfo> getTagInfos() {
        return tagInfos;
    }

    public void setTagInfos(ArrayList<TagInfo> tagInfos) {
        this.tagInfos = tagInfos;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getMaxPageNumber() {
        return maxPageNumber;
    }

    public void setMaxPageNumber(String maxPageNumber) {
        if(maxPageMark){
            this.maxPageNumber = maxPageNumber;
            maxPageMark=false;
        }
    }

    public String getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(String currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public  void clearStoryInfo(){
        setStoryInfos(null);
        maxPageMark = true;
    }
}
