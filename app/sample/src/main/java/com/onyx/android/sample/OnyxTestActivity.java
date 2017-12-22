package com.onyx.android.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnyxTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_sdk_data_ota_test)
    void onSdkDataTestClick() {
        startActivity(new Intent(this, SdkDataOTATestActivity.class));
    }

    @OnClick(R.id.button_settings)
    void settingsClick() {
        startActivity(new Intent(this, SettingsDemoActivity.class));
    }

    @OnClick(R.id.button_parallel_update)
    void button_parallel_update() {
        startActivity(new Intent(this, ParallelUpdateActivity.class));
    }

    @OnClick(R.id.button_fast_update)
    void button_fast_update() {
        startActivity(new Intent(this, FastUpdateModeActivity.class));
    }

    @OnClick(R.id.button_overlay_update)
    void button_overlay_update() {
        startActivity(new Intent(this, OverlayUpdateActivity.class));
    }

    @OnClick(R.id.button_text_select)
    void button_text_select() {
        startActivity(new Intent(this, TextSelectionActivity.class));
    }

    @OnClick(R.id.button_rect_update)
    void button_rect_update() {
        startActivity(new Intent(this, RectangleUpdateTest.class));
    }

}
