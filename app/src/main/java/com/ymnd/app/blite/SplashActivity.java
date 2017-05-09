package com.ymnd.app.blite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.ymnd.app.blite.model.Bookmark;
import com.ymnd.app.blite.view.SplashComponent;

import java.util.List;

/**
 * Created by yamazaki on 2017/05/07.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                LithoView.create(
                        this,
                        SplashComponent.create(new ComponentContext(this))
                                .build()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(timer, 1000);
    }

    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            new ArticleApi().getRss(new ArticleApi.OnRefreshListener() {
                @Override
                public void onRefresh(List<Bookmark> bookmarks) {
                    MyApplication ap = (MyApplication)getApplication();
                    Bookmark footer = Bookmark.createFooter();
                    bookmarks.add(footer);
                    ap.setBookmarks(bookmarks);
                    startActivity(new Intent(getApplicationContext(), SampleActivity.class));
                    finish();
                }
            });
        }
    };

}
