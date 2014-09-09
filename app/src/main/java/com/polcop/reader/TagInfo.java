package com.polcop.reader;

import android.text.Spanned;

/**
 * Created by oleg on 08.09.14.
 */
public class TagInfo {

    private String tagName, tagURL, htmlTag;
    private int total;

    public TagInfo (String tagName,String tagURL, int total,String htmlTag){
        this.tagName = tagName;
        this.tagURL = tagURL;
        this.total = total;
        this.htmlTag = htmlTag;
    }

    public static class Builder{

        private String tagName, tagURL,htmlTag;
        private int total;

        public void setTagName(String tagName) {
            this.tagName = tagName;
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
            return  new TagInfo(tagName, tagURL, total, htmlTag);
        }
    }

    public String getTagName() {
        return tagName;
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
