package com.onyx.android.sample;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.onyx.android.sdk.scribble.api.TouchHelper;
import com.onyx.android.sdk.scribble.api.event.BeginRawDataEvent;
import com.onyx.android.sdk.scribble.api.event.BeginRawErasingEvent;
import com.onyx.android.sdk.scribble.api.event.DrawingTouchEvent;
import com.onyx.android.sdk.scribble.api.event.EndRawDataEvent;
import com.onyx.android.sdk.scribble.api.event.ErasingTouchEvent;
import com.onyx.android.sdk.scribble.api.event.RawErasePointListReceivedEvent;
import com.onyx.android.sdk.scribble.api.event.RawErasePointMoveReceivedEvent;
import com.onyx.android.sdk.scribble.api.event.RawTouchPointListReceivedEvent;
import com.onyx.android.sdk.scribble.api.event.RawTouchPointMoveReceivedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScribbleStylusTouchHelperDemoActivity extends AppCompatActivity {

    private static final String TAG = ScribbleStylusTouchHelperDemoActivity.class.getSimpleName();

    @Bind(R.id.button_pen)
    Button buttonPen;
    @Bind(R.id.button_eraser)
    Button buttonEraser;
    @Bind(R.id.surfaceview)
    SurfaceView surfaceView;

    private EventBus eventBus = new EventBus();
    private TouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scribble_touch_helper_stylus_demo);

        ButterKnife.bind(this);

        initSurfaceView();
    }

    @Override
    protected void onResume() {
        touchHelper.setRawDrawingState(true);
        super.onResume();
    }

    @Override
    protected void onPause() {
        touchHelper.setRawDrawingState(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        touchHelper.closeRawDrawing();
        super.onDestroy();
    }

    private void initSurfaceView() {
        eventBus.register(this);
        touchHelper = new TouchHelper(eventBus);
        surfaceView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                List<Rect> exclude = new ArrayList<>();
                exclude.add(touchHelper.getRelativeRect(surfaceView, buttonEraser));
                exclude.add(touchHelper.getRelativeRect(surfaceView, buttonPen));

                Rect limit = new Rect();
                surfaceView.getLocalVisibleRect(limit);
                cleanSurfaceView();
                touchHelper.setup(surfaceView)
                        .setStrokeWidth(3.0f)
                        .setUseRawInput(true)
                        .setLimitRect(limit, exclude)
                        .openRawDrawing();
            }
        });
    }

    @OnClick(R.id.button_pen)
    public void onPenClick(){
        touchHelper.setRawDrawingState(true);
    }

    @OnClick(R.id.button_eraser)
    public void onEraserClick(){
        touchHelper.setRawDrawingState(false);
        cleanSurfaceView();
    }

    private void cleanSurfaceView() {
        if (surfaceView.getHolder() == null) {
            return;
        }
        Canvas canvas = surfaceView.getHolder().lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.WHITE);
        surfaceView.getHolder().unlockCanvasAndPost(canvas);
    }

    // below are callback events sent from TouchHelper

    @Subscribe
    public void onErasingTouchEvent(ErasingTouchEvent e) {
        Log.d(TAG, "onErasingTouchEvent");
    }

    @Subscribe
    public void onDrawingTouchEvent(DrawingTouchEvent e) {
        Log.d(TAG, "onDrawingTouchEvent");
    }

    @Subscribe
    public void onBeginRawDataEvent(BeginRawDataEvent e) {
        Log.d(TAG, "onBeginRawDataEvent");
    }

    @Subscribe
    public void onEndRawDataEvent(EndRawDataEvent e) {
        Log.d(TAG, "onEndRawDataEvent");
    }

    @Subscribe
    public void onRawTouchPointMoveReceivedEvent(RawTouchPointMoveReceivedEvent e) {
        Log.d(TAG, "onRawTouchPointMoveReceivedEvent");
    }

    @Subscribe
    public void onRawTouchPointListReceivedEvent(RawTouchPointListReceivedEvent e) {
        Log.d(TAG, "onRawTouchPointListReceivedEvent");
    }

    @Subscribe
    public void onRawErasingStartEvent(BeginRawErasingEvent e) {
        Log.d(TAG, "onRawErasingStartEvent");
    }

    @Subscribe
    public void onRawErasingFinishEvent(RawErasePointListReceivedEvent e) {
        Log.d(TAG, "onRawErasingFinishEvent");
    }

    @Subscribe
    public void onRawErasePointMoveReceivedEvent(RawErasePointMoveReceivedEvent e) {
        Log.d(TAG, "onRawErasePointMoveReceivedEvent");

    }

    @Subscribe
    public void onRawErasePointListReceivedEvent(RawErasePointListReceivedEvent e) {
        Log.d(TAG, "onRawErasePointListReceivedEvent");
    }

}
