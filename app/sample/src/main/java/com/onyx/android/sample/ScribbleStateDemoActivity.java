package com.onyx.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceView;

import com.onyx.android.sdk.api.device.epd.EpdController;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by joy on 7/26/17.
 */

public class ScribbleStateDemoActivity extends Activity {

    @Bind(R.id.surfaceview)
    SurfaceView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scribble_state_demo);

        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        EpdController.setScreenHandWritingPenState(surfaceView, 0);

        super.onDestroy();
    }

    @OnClick(R.id.button_pen)
    void onButtonPenClicked() {
        EpdController.setScreenHandWritingPenState(surfaceView, 1);
    }

    @OnClick(R.id.button_eraser)
    void onButtonEraserClicked() {
        EpdController.setScreenHandWritingPenState(surfaceView, 0);
    }
}
