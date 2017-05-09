package com.ymnd.app.blite;

import android.app.Application;

import com.facebook.litho.LithoWebKitInspector;
import com.facebook.litho.config.ComponentsConfiguration;
import com.facebook.soloader.SoLoader;
import com.facebook.stetho.Stetho;
import com.ymnd.app.blite.model.Bookmark;

import java.util.List;

import timber.log.Timber;

/**
 * Created by yamazaki on 2017/04/23.
 */

public class MyApplication extends Application {

    private List<Bookmark> bookmarks;

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, false);
        Timber.plant(new Timber.DebugTree());
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableWebKitInspector(new LithoWebKitInspector(this))
                        .build()
        );
        ComponentsConfiguration.debugHighlightMountBounds = true;
        ComponentsConfiguration.debugHighlightInteractiveBounds = true;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }
}
