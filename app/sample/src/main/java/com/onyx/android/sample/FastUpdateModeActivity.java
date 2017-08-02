package com.onyx.android.sample;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.onyx.android.sdk.api.device.EpdDeviceManager;

import java.util.Random;

/**
 * Created by wangxu on 17-8-2.
 */

public class FastUpdateModeActivity extends AppCompatActivity {

    private final static String TAG = FastUpdateModeActivity.class.getSimpleName();
    private boolean isFastMode = false;
    private CountDownTimer timer;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_update_mode);
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    private long generateRandomTime() {
        return (new Random().nextInt(10) + 1) * 1000;
    }

    private void enterFastMode() {
        EpdDeviceManager.enterAnimationUpdate(true);
        isFastMode = true;
    }

    private void quitFastMode() {
        EpdDeviceManager.exitAnimationUpdate(true);
        isFastMode = false;
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(generateRandomTime(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(getText(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                if (isFastMode) {
                    textView.setText("quit fast update mode...");
                    quitFastMode();
                } else {
                    textView.setText("enter fast update mode...");
                    enterFastMode();
                }
                startTimer();
            }
        };
        timer.start();
    }

    private String getText(long time) {
        return "After " + time / 1000 + "s will " + (isFastMode ? "quit " : "enter ") + "fast update mode.";
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
        if (isFastMode) {
            quitFastMode();
        }
    }
}
