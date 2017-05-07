package com.ymnd.app.blite.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.facebook.litho.ClickEvent;
import com.facebook.litho.Column;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.Event;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Image;
import com.facebook.litho.widget.PTRRefreshEvent;
import com.facebook.litho.widget.Text;
import com.facebook.litho.widget.VerticalGravity;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaJustify;
import com.ymnd.app.blite.R;
import com.ymnd.app.blite.model.Bookmark;

import timber.log.Timber;

import static java.security.AccessController.getContext;

/**
 * Created by yamazaki on 2017/04/23.
 */
@LayoutSpec
public class ListItemSpec {
    @OnCreateLayout
    static ComponentLayout onCreateLayout(ComponentContext componentContext,
                                          @Prop Bookmark bookmark
    ) {
        ComponentLayout.ContainerBuilder builder =
            Row.create(componentContext)
                .paddingDip(YogaEdge.ALL, 16)
                .backgroundColor(Color.WHITE)
                .child(
                        Column.create(componentContext)
                        .child(
                                Text.create(componentContext)
                                        .text(bookmark.getTitle())
                                        .textColorRes(R.color.primaryText)
                                        .textSizeRes(R.dimen.sub_heading)
                                        .spacingMultiplier(1.25f)
                                        .ellipsize(TextUtils.TruncateAt.END)
                                        .maxLines(2)
                                        .typeface(Typeface.createFromAsset(componentContext.getAssets(), "font/YakuHanJP-Regular.ttf"))

                        ).child(
                                Row.create(componentContext)
                                    .child(
                                            Text.create(componentContext)
                                                    .text(bookmark.getBookmarkCount())
                                                    .textColorRes(R.color.colorAccent)
                                                    .textSizeRes(R.dimen.body)
                                                    .withLayout()
                                                    .marginRes(YogaEdge.RIGHT, R.dimen.small_margin)
                                    )
                                    .child(
                                            Text.create(componentContext)
                                                    .text(bookmark.getHost())
                                                    .textColorRes(R.color.secondaryText)
                                                    .textSizeRes(R.dimen.body)
                        ))
                        .clickHandler(ListItem.onClick(componentContext, bookmark.getUrl()))
                )
                .child(
                        Image.create(componentContext)
                        .drawableRes(R.drawable.ic_share_color)
                        .withLayout()
                        .marginRes(YogaEdge.LEFT, R.dimen.small_margin)
                )
                .justifyContent(YogaJustify.SPACE_BETWEEN)
                .clickHandler(ListItem.onClickIcon(componentContext, bookmark.getUrl()));

        return builder.build();
    }

    @OnEvent(ClickEvent.class)
    static void onClick(ComponentContext componentContext,
                        @Param String url){
        if (url == null) return;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder
                .addDefaultShareMenuItem()
                .setToolbarColor(ContextCompat.getColor(componentContext, R.color.colorPrimary))
                .build();
        customTabsIntent.launchUrl(componentContext, Uri.parse(url));
    }

    @OnEvent(ClickEvent.class)
    static void onClickIcon(ComponentContext componentContext,
                            @Param String url) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        componentContext.startActivity(intent);

    }

    @OnEvent(PTRRefreshEvent.class)
    static void onPTRRefresh(ComponentContext componentContext) {
        Timber.d("イベントが発行されたよ！！");
    }
}
