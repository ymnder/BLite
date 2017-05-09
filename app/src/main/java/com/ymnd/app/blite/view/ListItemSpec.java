package com.ymnd.app.blite.view;

import android.content.Intent;
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
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Image;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaJustify;
import com.ymnd.app.blite.R;
import com.ymnd.app.blite.model.Bookmark;

/**
 * Created by yamazaki on 2017/04/23.
 */
@LayoutSpec
public class ListItemSpec {
    @OnCreateLayout
    static ComponentLayout onCreateLayout(ComponentContext c,
                                          @Prop Bookmark bookmark
    ) {
        ComponentLayout.ContainerBuilder builder =
            Row.create(c)
                .paddingDip(YogaEdge.ALL, 16)
                .backgroundColor(Color.WHITE)
                .child(
                        Column.create(c)
                        .child(
                                Text.create(c)
                                        .text(bookmark.getTitle())
                                        .textColorRes(R.color.primaryText)
                                        .textSizeRes(R.dimen.sub_heading)
                                        .spacingMultiplier(1.25f)
                                        .ellipsize(TextUtils.TruncateAt.END)
                                        .maxLines(2)
                                        .typeface(Typeface.createFromAsset(c.getAssets(), "font/YakuHanJP-Regular.ttf"))

                        ).child(
                                Row.create(c)
                                    .child(
                                            Text.create(c)
                                                    .text(bookmark.getBookmarkCount())
                                                    .textColorRes(R.color.colorAccent)
                                                    .textSizeRes(R.dimen.body)
                                                    .withLayout()
                                                    .marginRes(YogaEdge.RIGHT, R.dimen.small_margin)
                                    )
                                    .child(
                                            Text.create(c)
                                                    .text(bookmark.getHost())
                                                    .textColorRes(R.color.secondaryText)
                                                    .textSizeRes(R.dimen.body)
                        ))
                        .clickHandler(ListItem.onClick(c, bookmark.getUrl()))
                )
                .child(
                        Image.create(c)
                        .drawableRes(R.drawable.ic_share_color)
                        .withLayout()
                        .alignSelf(YogaAlign.CENTER)
                        .marginRes(YogaEdge.LEFT, R.dimen.small_margin)
                )
                .justifyContent(YogaJustify.SPACE_BETWEEN)
                .clickHandler(ListItem.onClickIcon(c, bookmark.getUrl()));

        return builder.build();
    }

    @OnEvent(ClickEvent.class)
    static void onClick(ComponentContext c,
                        @Param String url){
        if (url == null) return;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder
                .addDefaultShareMenuItem()
                .setToolbarColor(ContextCompat.getColor(c, R.color.colorPrimary))
                .build();
        customTabsIntent.launchUrl(c, Uri.parse(url));
    }

    @OnEvent(ClickEvent.class)
    static void onClickIcon(ComponentContext c,
                            @Param String url) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        c.startActivity(intent);

    }
}
