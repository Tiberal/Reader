package com.polcop.reader;

import java.util.ArrayList;

/**
 * Created by oleg on 04.09.14.
 */
public class PageInfo {

    private static PageInfo instance;
    private ArrayList<StoryInfo> storyInfos=null;
    private String currentPage;
    private String maxPageNumber;
    private String currentPageNumber;

    private String previousPage;

    public PageInfo() {
    }

    public static  void initInstance (){
        if (instance == null) {
            instance = new PageInfo();
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
        this.maxPageNumber = maxPageNumber;
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

}
