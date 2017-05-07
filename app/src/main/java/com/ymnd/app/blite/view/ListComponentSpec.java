package com.ymnd.app.blite.view;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentInfo;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;

/**
 * Created by yamazaki on 2017/05/07.
 */
@LayoutSpec
public class ListComponentSpec {

    @OnCreateLayout
    static ComponentLayout onCreateLayout(ComponentContext c) {
        final RecyclerBinder recyclerBinder = new RecyclerBinder(c);

        //binder is here.
        return Recycler.create(c)
                .binder(recyclerBinder)
                .itemDecoration(new DividerItemDecoration(c,
                        new LinearLayoutManager(c).getOrientation()))
                .withLayout()
                .flexShrink(0)
                .build();
    }

    public static void addAllToBinder(RecyclerBinder recyclerBinder, ComponentContext c) {
        ComponentInfo.Builder componentInfoBuilder = ComponentInfo.create();
        componentInfoBuilder.component(
                ListItem.create(c)
                        .build());
        recyclerBinder.insertItemAt(recyclerBinder.getItemCount(), componentInfoBuilder.build());
    }
}
