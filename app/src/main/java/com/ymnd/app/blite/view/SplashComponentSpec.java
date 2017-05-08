package com.ymnd.app.blite.view;

import com.facebook.litho.Column;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaJustify;

/**
 * Created by yamazaki on 2017/05/07.
 */
@LayoutSpec
public class SplashComponentSpec {
    @OnCreateLayout
    static ComponentLayout onCreateLayout(ComponentContext c) {
        return Column.create(c)
                .child(
                        Text.create(c)
                            .text("ζ*'ヮ')ζ  < 準備中ですよ〜")
                        .textSizeSp(30f)
                        .withLayout()
                        .alignSelf(YogaAlign.CENTER)

                ).justifyContent(YogaJustify.CENTER)
                .build();
    }
}
