package com.ymnd.app.blite.model;

import com.facebook.litho.Column;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.widget.Progress;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaJustify;
import com.ymnd.app.blite.R;

/**
 * Created by yamazaki on 2017/04/27.
 */

@LayoutSpec
public class FooterListItemSpec {
    @OnCreateLayout
    static ComponentLayout onCreateLayout(ComponentContext componentContext) {
        ComponentLayout.ContainerBuilder builder =
                Column.create(componentContext)
                .alignItems(YogaAlign.CENTER)
                .child(
                        Progress.create(componentContext)
                                .colorRes(R.color.colorPrimary)
                                .withLayout()
                                .heightDip(40)
                                .widthDip(40)
                                .build()
                );

        return builder.build();
    }
}
