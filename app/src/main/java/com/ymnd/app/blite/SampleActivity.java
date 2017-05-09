package com.ymnd.app.blite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.ymnd.app.blite.view.ListComponent;

/**
 * Created by yamazaki on 2017/05/07.
 */

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ComponentContext c = new ComponentContext(this);
        setContentView(
                LithoView.create(
                        this,
                        ListComponent.create(new ComponentContext(this))
                                .build()));
    }
}
