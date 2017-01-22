package com.onyx.android.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onyx.android.sample.device.ReaderDeviceManager;
import com.onyx.android.sdk.api.device.FrontLightController;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EpdDemoActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.button_screen_refresh)
    Button buttonRefresh;
    @Bind(R.id.button_light_toggle)
    Button buttonLightToggle;
    @Bind(R.id.button_light_darker)
    Button buttonLightDarker;
    @Bind(R.id.button_light_lighter)
    Button buttonLightLighter;
    @Bind(R.id.textview)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epd_demo);

        ButterKnife.bind(this);
        buttonRefresh.setOnClickListener(this);
        buttonLightToggle.setOnClickListener(this);
        buttonLightDarker.setOnClickListener(this);
        buttonLightLighter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonRefresh)) {
            ReaderDeviceManager.applyWithGCInterval(textView, true);
        } else if (v.equals(buttonLightToggle)) {
            if (FrontLightController.isLightOn(this)) {
                FrontLightController.turnOff(this);
            } else {
                FrontLightController.turnOn(this);
            }
        } else if (v.equals(buttonLightDarker)) {
            int now = FrontLightController.getBrightness(this);
            List<Integer> list = FrontLightController.getFrontLightValueList(this);
            Collections.reverse(list);
            for (Integer target : list) {
                if (target < now) {
                    FrontLightController.setBrightness(this, target);
                    return;
                }
            }
        } else if (v.equals(buttonLightLighter)) {
            int now = FrontLightController.getBrightness(this);
            List<Integer> list = FrontLightController.getFrontLightValueList(this);
            for (Integer target : list) {
                if (target > now) {
                    FrontLightController.setBrightness(this, target);
                    return;
                }
            }
        }
    }
}
