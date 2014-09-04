package com.polcop.reader;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oleg on 04.09.14.
 */
public class StoryInfo implements Parcelable{

    private String storyNumber, publishDate, tags, rate, goodURL, badURL, story;
    //private Spanned story;

    private StoryInfo(String storyNumber, String publishDate, String tags,
                      String story, String rate, String goodURL ,String badURL) {
        this.storyNumber = storyNumber;
        this.publishDate = publishDate;
        this.tags = tags;
        this.story = story;
        this.rate = rate;
        this.goodURL = goodURL;
        this.badURL = badURL;
    }

    public StoryInfo(Parcel parcel) {
        this.storyNumber = parcel.readString();
        this.publishDate = parcel.readString();
        this.tags = parcel.readString();
        this.story = parcel.readString();
        this.rate = parcel.readString();
        this.goodURL = parcel.readString();
        this.badURL = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(storyNumber);
        parcel.writeString(publishDate);
        parcel.writeString(tags);
        parcel.writeString(story);
        parcel.writeString(rate);
        parcel.writeString(goodURL);
        parcel.writeString(badURL);

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

        private String storyNumber, publishDate, tags, rate, goodURL, badURL;
        private String story;

        public void setStoryNumber(String storyNumber) {
            this.storyNumber = storyNumber;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public void setStory(String story) {
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

        public StoryInfo build() {
            return new StoryInfo(storyNumber, publishDate, tags, story, rate, goodURL, badURL);
        }
    }

    public String getStoryNumber() {
        return storyNumber;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getTags() {
        return tags;
    }

    public String getStory() {
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

}