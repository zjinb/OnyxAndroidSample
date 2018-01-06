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
import android.widget.CheckBox;

import com.onyx.android.sdk.pen.RawInputCallback;
import com.onyx.android.sdk.pen.TouchHelper;
import com.onyx.android.sdk.pen.data.TouchPoint;
import com.onyx.android.sdk.pen.data.TouchPointList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ScribbleStylusTouchHelperDemoActivity extends AppCompatActivity {

    private static final String TAG = ScribbleStylusTouchHelperDemoActivity.class.getSimpleName();

    @Bind(R.id.button_pen)
    Button buttonPen;
    @Bind(R.id.button_eraser)
    Button buttonEraser;
    @Bind(R.id.surfaceview)
    SurfaceView surfaceView;
    @Bind(R.id.cb_render)
    CheckBox cbRender;

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
        touchHelper.setRawDrawingEnabled(true);
        super.onResume();
    }

    @Override
    protected void onPause() {
        touchHelper.setRawDrawingEnabled(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        touchHelper.closeRawDrawing();
        super.onDestroy();
    }

    private void initSurfaceView() {
        touchHelper = TouchHelper.create(surfaceView, callback);

        surfaceView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int
                    oldRight, int oldBottom) {
                List<Rect> exclude = new ArrayList<>();
                exclude.add(getRelativeRect(surfaceView, buttonEraser));
                exclude.add(getRelativeRect(surfaceView, buttonPen));
                exclude.add(getRelativeRect(surfaceView, cbRender));

                Rect limit = new Rect();
                surfaceView.getLocalVisibleRect(limit);
                cleanSurfaceView();
                touchHelper.setStrokeWidth(3.0f)
                           .setLimitRect(limit, exclude)
                           .openRawDrawing();
            }
        });
    }

    @OnClick(R.id.button_pen)
    public void onPenClick() {
        touchHelper.setRawDrawingEnabled(true);
        onRenderEnableClick();
    }

    @OnClick(R.id.button_eraser)
    public void onEraserClick() {
        touchHelper.setRawDrawingEnabled(false);
        cleanSurfaceView();
    }

    @OnCheckedChanged(R.id.cb_render)
    public void onRenderEnableClick() {
        touchHelper.setRawDrawingRenderEnabled(cbRender.isChecked());
        Log.d(TAG,"onRenderEnableClick setRawDrawingRenderEnabled =  " + cbRender.isChecked());
    }

    public Rect getRelativeRect(View var1, View var2) {
        int[] var3 = new int[2];
        int[] var4 = new int[2];
        var1.getLocationOnScreen(var3);
        var2.getLocationOnScreen(var4);
        Rect var5 = new Rect();
        var2.getLocalVisibleRect(var5);
        var5.offset(var4[0] - var3[0], var4[1] - var3[1]);
        return var5;
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


    private RawInputCallback callback = new RawInputCallback() {

        @Override
        public void onBeginRawDrawing(boolean b, TouchPoint touchPoint) {
            Log.d(TAG, "onBeginRawDrawing");
        }

        @Override
        public void onEndRawDrawing(boolean b, TouchPoint touchPoint) {
            Log.d(TAG, "onEndRawDrawing");
        }

        @Override
        public void onRawDrawingTouchPointMoveReceived(TouchPoint touchPoint) {
            Log.d(TAG, "onRawDrawingTouchPointMoveReceived");
        }

        @Override
        public void onRawDrawingTouchPointListReceived(TouchPointList touchPointList) {
            Log.d(TAG, "onRawDrawingTouchPointListReceived");
        }

        @Override
        public void onBeginRawErasing(boolean b, TouchPoint touchPoint) {
            Log.d(TAG, "onBeginRawErasing");
        }

        @Override
        public void onEndRawErasing(boolean b, TouchPoint touchPoint) {
            Log.d(TAG, "onEndRawErasing");
        }

        @Override
        public void onRawErasingTouchPointMoveReceived(TouchPoint touchPoint) {
            Log.d(TAG, "onRawErasingTouchPointMoveReceived");
        }

        @Override
        public void onRawErasingTouchPointListReceived(TouchPointList touchPointList) {
            Log.d(TAG, "onRawErasingTouchPointListReceived");
        }
    };
}
