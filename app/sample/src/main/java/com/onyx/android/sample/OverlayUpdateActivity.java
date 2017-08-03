package com.onyx.android.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wangxu on 17-8-3.
 */

public class OverlayUpdateActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean tag = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_update);

        textView1 = (TextView) findViewById(R.id.textview1);
        textView2 = (TextView) findViewById(R.id.textview2);
        textView3 = (TextView) findViewById(R.id.textview3);

        startUpdate();
    }

    private void startUpdate() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateView();
                startUpdate();
            }
        }, 10);
    }

    private void updateView() {
        if (tag) {
            if (!isVisible(textView1) && !isVisible(textView2) && !isVisible(textView3)) {
                setVisible(textView1, true);
            } else if (isVisible(textView1) && !isVisible(textView2) && !isVisible(textView3)) {
                setVisible(textView1, false);
                setVisible(textView2, true);
            } else if (!isVisible(textView1) && isVisible(textView2) && !isVisible(textView3)) {
                setVisible(textView2, false);
                setVisible(textView3, true);
                tag = !tag;
            }
        } else {
            if (!isVisible(textView1) && !isVisible(textView2) && isVisible(textView3)) {
                setVisible(textView3, false);
                setVisible(textView2, true);
            } else if (!isVisible(textView1) && isVisible(textView2) && !isVisible(textView3)) {
                setVisible(textView2, false);
                setVisible(textView1, true);
                tag = !tag;
            }
        }
    }

    private boolean isVisible(final View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    private void setVisible(final View view, final boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

}
