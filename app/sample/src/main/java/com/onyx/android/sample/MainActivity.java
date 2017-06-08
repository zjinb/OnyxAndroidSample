package com.onyx.android.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.button_environment)
    Button buttonEnvironment;
    @Bind(R.id.button_epd)
    Button buttonEpd;
    @Bind(R.id.button_front_light)
    Button buttonFrontLight;
    @Bind(R.id.button_touch_screen_scribble)
    Button buttonTouchScreenScribble;
    @Bind(R.id.button_stylus_scribble)
    Button buttonStylusScribble;
    @Bind(R.id.button_full_screen)
    Button buttonFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        buttonEnvironment.setOnClickListener(this);
        buttonEpd.setOnClickListener(this);
        buttonFrontLight.setOnClickListener(this);
        buttonTouchScreenScribble.setOnClickListener(this);
        buttonStylusScribble.setOnClickListener(this);
        buttonFullScreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonEnvironment)) {
            startActivity(new Intent(this, EnvironmentDemoActivity.class));
            return;
        } else if (v.equals(buttonEpd)) {
            startActivity(new Intent(this, EpdDemoActivity.class));
            return;
        } else if (v.equals(buttonFrontLight)) {
            startActivity(new Intent(this, FrontLightDemoActivity.class));
            return;
        } else if (v.equals(buttonTouchScreenScribble)) {
            startActivity(new Intent(this, ScribbleTouchScreenDemoActivity.class));
            return;
        } else if (v.equals(buttonStylusScribble)) {
            startActivity(new Intent(this, ScribbleStylusDemoActivity.class));
            return;
        } else if (v.equals(buttonFullScreen)) {
            startActivity(new Intent(this, FullScreenDemoActivity.class));
        }
    }

    @OnClick(R.id.button_sdk_data_ota_test)
    void onSdkDataTestClick() {
        startActivity(new Intent(this, SdkDataOTATestActivity.class));
    }

    @OnClick(R.id.button_settings)
    void settingsClick() {
        startActivity(new Intent(this, SettingsDemoActivity.class));
    }
}
