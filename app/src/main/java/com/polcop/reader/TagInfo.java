package com.polcop.reader;

/**
 * Created by oleg on 08.09.14.
 */
public class TagInfo {

    private String tagTitle, tagURL, htmlTag;
    private int total;

    public TagInfo (String tagTitle,String tagURL, int total,String htmlTag){
        this.tagTitle = tagTitle;
        this.tagURL = tagURL;
        this.total = total;
        this.htmlTag = htmlTag;
    }

    public static class Builder{

        private String tagTitle, tagURL,htmlTag;
        private int total;

        public void setTagTitle(String tagTitle) {
            this.tagTitle = tagTitle;
        }

        public void setTagURL(String tagURL) {
            this.tagURL = tagURL;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setHtmlTag(String htmlTag) {
            this.htmlTag = htmlTag;
        }
        public TagInfo build(){
            return  new TagInfo(tagTitle, tagURL, total, htmlTag);
        }
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public String getTagURL() {
        return tagURL;
    }

    public int getTotal() {
        return total;
    }

    public String getHtmlTag() {
        return htmlTag;
    }


}