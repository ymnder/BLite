package com.ymnd.app.blite.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.URL;

/**
 * Created by yamazaki on 2017/04/25.
 */

public class Bookmark {
    /*
    * "count": "771",
    * "date": "2017-04-25T07:21:58",
    * "image_l": {
    *   "width": 500,
    *   "url": "https://cdn-ak.b.st-hatena.com/entryimage/333500596-origin-1493072583.jpg",
    *   "height": 375},
    * "eid": "333500596",
    * "description": "",
    * "entry_url": null,
    * "image": "https://cdn-ak.b.st-hatena.com/entryimage/333500596-1493072583.jpg",
    * "root_url": "http://blog.tinect.jp/",
    * "url": "http://blog.tinect.jp/?p=39105",
    * "title": "タスクをどんどん遅延させてしまう人に、何故遅延させてしまうのかヒアリングした時の話 | Books&Apps",
    * "is_pr": 0
    */
    private int count;
    private String dateTime;
    @SerializedName(value = "image_l")
    private ImageUrl imageUrl;
    private String eid;
    private String description;
    @SerializedName(value = "entry_url")
    private String entryUrl;
    @SerializedName(value = "root_url")
    private String rootUrl;
    private String url;
    private String title;
    @SerializedName(value = "is_pr")
    private int isPr;

    private boolean isFooter;

    public static class ImageUrl {
        private int width;
        private int height;
        private String url;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getHost() {
        Uri urlString = Uri.parse(url);
        return urlString.getHost();
    }

    public int getIsPr() {
        return isPr;
    }

    public int getCount() {
        return count;
    }

    public String getBookmarkCount() {
        return Integer.toString(count);
    }

    // for debug
    public void setCount(int count) {
        this.count = count;
    }

    public boolean isFooter() {
        return isFooter;
    }

    public void setFooter(boolean footer) {
        isFooter = footer;
    }
}
