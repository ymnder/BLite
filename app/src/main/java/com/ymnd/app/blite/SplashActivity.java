package com.ymnd.app.blite;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.google.gson.Gson;
import com.ymnd.app.blite.model.Bookmark;
import com.ymnd.app.blite.view.SplashComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

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
            getRss(false);
        }
    };

    private final String BASE_URL = "http://b.hatena.ne.jp/api/ipad.hotentry.json";

    private void getRss(final boolean loadMore) {
        getRss(loadMore, false);
    }
    private void getRss(final boolean loadMore, final boolean isRefresh) {

        new AsyncTask<Void, Void, List<Bookmark>>() {

            @Override
            protected List<Bookmark> doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Uri.Builder uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("limit", "10");

                Request request = builder
                        .url(uri.toString())
                        .build();
                List<Bookmark> bookmarkList = new ArrayList<>();
                try {
                    Response response = client.newCall(request).execute();
                    Collections.addAll(
                            bookmarkList,
                            new Gson().fromJson(response.body().charStream(), Bookmark[].class)
                    );
                } catch (IOException e) {
                    Timber.e(e, e.getMessage());
                }
                return bookmarkList;
            }

            @Override
            protected void onPostExecute(List<Bookmark> bookmarks) {
                super.onPostExecute(bookmarks);
                MyApplication ap = (MyApplication)getApplication();
                //fix this
                Bookmark footer = new Bookmark();
                footer.setFooter(true);
                bookmarks.add(footer);
                ap.setBookmarks(bookmarks);
                startActivity(new Intent(getApplicationContext(), SampleActivity.class));
                finish();
            }

        }.execute();
    }

}
