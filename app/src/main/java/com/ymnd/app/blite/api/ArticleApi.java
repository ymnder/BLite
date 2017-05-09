package com.ymnd.app.blite.api;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ymnd.app.blite.model.Bookmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by yamazaki on 2017/05/08.
 */

public class ArticleApi {

    private final String BASE_URL = "http://b.hatena.ne.jp/api/ipad.hotentry.json";
    public void getRss(final OnRefreshListener listener) {
        getRss(0, listener);
    }
    public void getRss(final int itemCounts, final OnRefreshListener listener) {

        new AsyncTask<Void, Void, List<Bookmark>>() {

            @Override
            protected List<Bookmark> doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Uri.Builder uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("limit", "10");

                if (itemCounts != 0) {
                    uri.appendQueryParameter("of", Integer.toString(itemCounts - 1));
                }

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
                listener.onRefresh(bookmarks);
            }

        }.execute();
    }

    public interface OnRefreshListener {
        void onRefresh(List<Bookmark> bookmarks);
    }
}
