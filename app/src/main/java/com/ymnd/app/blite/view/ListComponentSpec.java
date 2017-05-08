package com.ymnd.app.blite.view;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentInfo;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.widget.PTRRefreshEvent;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;
import com.facebook.litho.widget.RecyclerBinderUpdateCallback;
import com.facebook.litho.widget.RecyclerEventsController;
import com.google.gson.Gson;
import com.ymnd.app.blite.ArticleApi;
import com.ymnd.app.blite.DiffCallback;
import com.ymnd.app.blite.MyApplication;
import com.ymnd.app.blite.SampleActivity;
import com.ymnd.app.blite.model.Bookmark;
import com.ymnd.app.blite.model.FooterListItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by yamazaki on 2017/05/07.
 */
@LayoutSpec
public class ListComponentSpec {

    @OnCreateLayout
    static ComponentLayout onCreateLayout(final ComponentContext c) {
        final RecyclerBinder recyclerBinder = new RecyclerBinder(c);
        final RecyclerEventsController controller = new RecyclerEventsController();

        //binder is here.
        List<Bookmark> bookmarks = ((MyApplication) c.getApplicationContext()).getBookmarks();
        for (Bookmark bookmark: bookmarks) {
            ComponentInfo.Builder componentInfoBuilder = ComponentInfo.create();
            componentInfoBuilder.component(
                    bookmark.isFooter() ?
                    FooterListItem.create(c).build() :
                    ListItem.create(c).bookmark(bookmark).build()
            );
            recyclerBinder.insertItemAt(recyclerBinder.getItemCount(), componentInfoBuilder.build());
        }

        return Recycler.create(c)
                .binder(recyclerBinder)
                .recyclerEventsController(controller)
                .itemDecoration(new DividerItemDecoration(c, new LinearLayoutManager(c).getOrientation()))
                .refreshHandler(ListComponent.onPTRrefresh(c, recyclerBinder, controller))
                .onScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        int current = recyclerBinder.findLastVisibleItemPosition();
                        int lastIndex = recyclerBinder.getItemCount() - 1;
                        Timber.d("current %s, last %s", current, lastIndex);
                        if (current == lastIndex) {
                            if (recyclerBinder.getItemCount() >= 49) return;
                            getRss(c, recyclerBinder, recyclerBinder.getItemCount());
                        }
                    }
                })
                .withLayout()
                .flexShrink(0)
                .build();
    }

    private static final String BASE_URL = "http://b.hatena.ne.jp/api/ipad.hotentry.json";
    private static void getRss(final ComponentContext c, final RecyclerBinder recyclerBinder, final int count) {
        new AsyncTask<Void, Void, List<Bookmark>>() {
            @Override
            protected List<Bookmark> doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Uri.Builder uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("limit", "10");
                int itemCount = count - 1;
                uri.appendQueryParameter("of", Integer.toString(itemCount));
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

                List<Bookmark> oldData = ((MyApplication) c.getApplicationContext()).getBookmarks();
                List<Bookmark> newData = new ArrayList<Bookmark>();

                newData.addAll(oldData);
                newData.remove(oldData.size() - 1);

                //fix this
                MyApplication ap = (MyApplication) c.getApplicationContext();
                Bookmark footer = new Bookmark();
                footer.setFooter(true);
                bookmarks.add(footer);
                newData.addAll(bookmarks);

                // saved
                ap.setBookmarks(newData);

                final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(DiffCallback.newInstance(oldData, newData));
                Timber.d("old %s, new %s", oldData.size(), newData.size());
                final RecyclerBinderUpdateCallback.ComponentRenderer<Bookmark> mComponentRenderer = new RecyclerBinderUpdateCallback.ComponentRenderer<Bookmark>() {
                    @Override
                    public ComponentInfo render(Bookmark bookmark, int idx) {
                        return ComponentInfo.create().component(
                                bookmark.isFooter() ?
                                        FooterListItem.create(c).build() :
                                        ListItem.create(c).bookmark(bookmark).build()
                        ).build();
                    }
                };

                final RecyclerBinderUpdateCallback callback = RecyclerBinderUpdateCallback.acquire(
                        oldData.size(),
                        newData,
                        mComponentRenderer,
                        recyclerBinder
                );

                diffResult.dispatchUpdatesTo(callback);
                callback.applyChangeset();
                RecyclerBinderUpdateCallback.release(callback);

//                MyApplication ap = (MyApplication) c.getApplicationContext();
//                List<Bookmark> currentData = ap.getBookmarks();
//
//                //footer部分を削除
//                currentData.remove(recyclerBinder.getItemCount() - 1);
//                currentData.addAll(bookmarks);
//                Bookmark footer = new Bookmark();
//                footer.setFooter(true);
//                currentData.add(footer);
//                ap.setBookmarks(currentData);
//                recyclerBinder.removeItemAt(recyclerBinder.getItemCount() - 1);
//
//                for (Bookmark bookmark: bookmarks) {
//                    ComponentInfo.Builder componentInfoBuilder = ComponentInfo.create();
//                    componentInfoBuilder.component(
//                            bookmark.isFooter() ?
//                                    FooterListItem.create(c).build() :
//                                    ListItem.create(c).bookmark(bookmark).build()
//                    );
//                    recyclerBinder.insertItemAt(recyclerBinder.getItemCount(), componentInfoBuilder.build());
//                }
            }

        }.execute();
    }

    @OnEvent(PTRRefreshEvent.class)
    static void onPTRrefresh(final ComponentContext c,
                             @Param final RecyclerBinder recyclerBinder,
                             @Param final RecyclerEventsController controller){

        new ArticleApi().getRss(false, new ArticleApi.OnRefreshListener() {
            @Override
            public void onRefresh(List<Bookmark> bookmarks) {
                //this dataset is for debug so must fixed
                List<Bookmark> oldData = ((MyApplication) c.getApplicationContext()).getBookmarks();

                //fix this
                MyApplication ap = (MyApplication) c.getApplicationContext();
                Bookmark footer = new Bookmark();
                footer.setFooter(true);
                bookmarks.add(footer);
                ap.setBookmarks(bookmarks);

                final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(DiffCallback.newInstance(oldData, bookmarks));
                Timber.d("old %s, new %s", oldData.size(), bookmarks.size());
                final RecyclerBinderUpdateCallback.ComponentRenderer<Bookmark> mComponentRenderer = new RecyclerBinderUpdateCallback.ComponentRenderer<Bookmark>() {
                    @Override
                    public ComponentInfo render(Bookmark bookmark, int idx) {
                        return ComponentInfo.create().component(
                                bookmark.isFooter() ?
                                        FooterListItem.create(c).build() :
                                        ListItem.create(c).bookmark(bookmark).build()
                        ).build();
                    }
                };

                final RecyclerBinderUpdateCallback callback = RecyclerBinderUpdateCallback.acquire(
                        oldData.size(),
                        bookmarks,
                        mComponentRenderer,
                        recyclerBinder
                );

                diffResult.dispatchUpdatesTo(callback);
                callback.applyChangeset();
                RecyclerBinderUpdateCallback.release(callback);
                controller.clearRefreshing();
            }
        });
    }

}
