package com.onyx.android.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.button_epd)
    Button buttonEpd;
    @Bind(R.id.button_scribble)
    Button buttonScribble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        buttonEpd.setOnClickListener(this);
        buttonScribble.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonEpd)) {
            startActivity(new Intent(this, EpdDemoActivity.class));
            return;
        } else if (v.equals(buttonScribble)) {
            startActivity(new Intent(this, ScribbleDemoActivity.class));
            return;
        }
    }
}
