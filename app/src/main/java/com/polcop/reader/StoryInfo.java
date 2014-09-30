package com.polcop.reader;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by oleg on 04.09.14.
 */
public class StoryInfo implements Parcelable{

    private String storyNumber, publishDate, rate, goodURL, badURL, storyTitle;
    private String[] tags;
    private Spanned story;

    private StoryInfo(String storyNumber, String publishDate, String[] tags,
                      Spanned story, String rate, String goodURL ,String badURL, String storyTitle) {
        this.storyNumber = storyNumber;
        this.publishDate = publishDate;
        this.tags = tags;
        this.story = story;
        this.rate = rate;
        this.goodURL = goodURL;
        this.badURL = badURL;
        this.storyTitle = storyTitle;
    }

    public StoryInfo(Parcel parcel) {
        this.storyNumber = parcel.readString();
        this.publishDate = parcel.readString();
        this.tags = parcel.createStringArray();
        this.story = Html.fromHtml(parcel.readString());
        this.rate = parcel.readString();
        this.goodURL = parcel.readString();
        this.badURL = parcel.readString();
        this.storyTitle = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(storyNumber);
        parcel.writeString(publishDate);
        parcel.writeStringArray(tags);
        parcel.writeString(story.toString());
        parcel.writeString(rate);
        parcel.writeString(goodURL);
        parcel.writeString(badURL);
        parcel.writeString(storyTitle);

    }

    public static final Parcelable.Creator<StoryInfo> CREATOR = new Creator<StoryInfo>() {

        @Override
        public StoryInfo[] newArray(int size) {
            return new StoryInfo[size];
        }

        @Override
        public StoryInfo createFromParcel(Parcel parcel) {
            return new StoryInfo(parcel);
        }
    };

    public static class Builder {

        private String storyNumber;
        private String publishDate;
        private String rate;
        private String goodURL;
        private String badURL;

        private String storyTitle;
        String[] tags;
        private Spanned story;

        public void setStoryNumber(String storyNumber) {
            this.storyNumber = storyNumber;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        public void setStory(Spanned story) {
            this.story = story;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public void setGoodURL(String goodURL) {
            this.goodURL = goodURL;
        }

        public void setBadURL(String badURL) {
            this.badURL = badURL;
        }

        public void setStoryTitle(String storyTitle) {
            this.storyTitle = "<h1>"+ storyTitle +"</h1>";
        }

        public StoryInfo build() {
            return new StoryInfo(storyNumber, publishDate, tags, story, rate, goodURL, badURL, storyTitle);
        }
    }

    public String getStoryNumber() {
        return storyNumber;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String[] getTags() {
        return tags;
    }

    public Spanned getStory() {
        return story;
    }

    public String getRate() {
        return rate;
    }

    public String getGoodURL() {
        return goodURL;
    }

    public String getBadURL() {
        return badURL;
    }

    public Spanned getStoryTitle() {
        if (storyTitle==null) return null;
        return Html.fromHtml(storyTitle);
    }

    public Spanned getSpannedTags(){
        if(getTags()==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<getTags().length;i++){
            sb.append(getTags()[i]).append(" ");
        }
        return Html.fromHtml(sb.toString());
    }

}
