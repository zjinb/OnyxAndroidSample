package com.onyx.android.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.onyx.android.sdk.api.device.epd.EpdController;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        final View view = findViewById(android.R.id.content);
        EpdController.enablePost(view, 1);
    }


    @OnClick(R.id.button_epd)
    void button_epd() {
        startActivity(new Intent(this, EpdDemoActivity.class));
    }

    @OnClick(R.id.button_front_light)
    void button_front_light() {
        startActivity(new Intent(this, FrontLightDemoActivity.class));
    }

    @OnClick(R.id.button_full_screen)
    void button_full_screen() {
        startActivity(new Intent(this, FullScreenDemoActivity.class));
    }

    @OnClick(R.id.button_environment)
    void button_environment() {
        startActivity(new Intent(this, EnvironmentDemoActivity.class));
    }

    @OnClick(R.id.button_scribble_touch_helper)
    void button_scribble_touch_helper() {
        startActivity(new Intent(this, ScribbleTouchScreenDemoActivity.class));
    }

    @OnClick(R.id.button_surfaceview_stylus_scribble)
    void button_surfaceview_stylus_scribble() {
        startActivity(new Intent(this, ScribbleStylusSurfaceViewDemoActivity.class));
    }

    @OnClick(R.id.button_webview_stylus_scribble)
    void button_webview_stylus_scribble() {
        startActivity(new Intent(this, ScribbleStylusWebViewDemoActivity.class));
    }

    @OnClick(R.id.button_touch_screen_scribble)
    void button_touch_screen_scribble() {
        startActivity(new Intent(this, ScribbleTouchScreenDemoActivity.class));
    }

    @OnClick(R.id.btn_onyx_test)
    void btn_onyx_test() {
        startActivity(new Intent(this, OnyxTestActivity.class));
    }
}
