package com.ymnd.app.blite;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentInfo;
import com.facebook.litho.EventDispatcher;
import com.facebook.litho.EventHandler;
import com.facebook.litho.HasEventDispatcher;
import com.facebook.litho.LithoView;
import com.facebook.litho.annotations.Event;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.widget.LinearLayoutInfo;
import com.facebook.litho.widget.PTRRefreshEvent;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;
import com.facebook.litho.widget.RecyclerEventsController;
import com.google.gson.Gson;
import com.ymnd.app.blite.model.Bookmark;
import com.ymnd.app.blite.model.FooterListItem;
import com.ymnd.app.blite.view.ListItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://b.hatena.ne.jp/api/ipad.hotentry.json";

    OkHttpClient client;
    ComponentContext componentContext;
    RecyclerEventsController controller;
    RecyclerBinder recyclerBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
        componentContext = new ComponentContext(this);
        controller = new RecyclerEventsController();

        recyclerBinder =
                new RecyclerBinder(
                        componentContext,
                        new LinearLayoutInfo(this, OrientationHelper.VERTICAL, false)
                );

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                new LinearLayoutManager(this).getOrientation());

        final Component component =
                Recycler.create(componentContext)
                        .itemDecoration(dividerItemDecoration)
                        .binder(recyclerBinder)
                        .recyclerEventsController(controller)
                    .refreshHandler(new EventHandler(new HasEventDispatcher() {
                        @Override
                        public EventDispatcher getEventDispatcher() {
                            return new EventDispatcher() {
                                @Override
                                public Object dispatchOnEvent(EventHandler eventHandler, Object eventState) {
                                    //refresh
                                    getRss(false, true);
                                    return null;
                                }
                            };
                        }
                    }, 100 ,new Object[] {""}))
                        .onScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                            }

                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                int current = recyclerBinder.findLastVisibleItemPosition();
                                int lastIndex = recyclerBinder.getItemCount() - 1;
                                Timber.d("current %s, last %s", current, lastIndex);
                                if (current == lastIndex) {
                                    getRss(true);
                                }
                            }
                        })
                        .build();

        setContentView(LithoView.create(componentContext, component));

        getRss(false);
    }


    private void getRss(final boolean loadMore) {
        getRss(loadMore, false);
    }
    private void getRss(final boolean loadMore, final boolean isRefresh) {

        new AsyncTask<Void, Void, List<Bookmark>>() {

            @Override
            protected List<Bookmark> doInBackground(Void... params) {

                Request.Builder builder = new Request.Builder();
                Uri.Builder uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("limit", "10");
                if (loadMore){
                    int itemCount = recyclerBinder.getItemCount() - 1;
                    uri.appendQueryParameter("of", Integer.toString(itemCount));
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

                if (isRefresh) {
                    int itemCount = recyclerBinder.getItemCount()-1;
                    for (int l = itemCount; l >= 0; l--) {
                        recyclerBinder.removeItemAt(l);
                    }
                }

                //ここで処理
                int i = 0;
                if (loadMore) {
                    i = recyclerBinder.getItemCount() - 1;
                    recyclerBinder.removeItemAt(i);
                }


                for(Bookmark bookmark: bookmarks) {
                    recyclerBinder.insertItemAt(i,
                            ComponentInfo.create()
                                    .component(
                                            ListItem.create(componentContext)
                                                    .bookmark(bookmark)
                                                    .build()
                                    )
                                    .build()
                    );
                    i++;
                }
                recyclerBinder.insertItemAt(i,
                        ComponentInfo.create()
                                .component(
                                        FooterListItem.create(componentContext)
                                                .build()
                                ).build()
                );
                controller.clearRefreshing();

            }

        }.execute();
    }

}
