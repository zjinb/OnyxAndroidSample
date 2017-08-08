package com.onyx.android.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.onyx.android.sdk.api.device.epd.EpdController;
import com.onyx.android.sdk.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxu on 17-8-8.
 */

public class RectangleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private UpdateMode currentMode = UpdateMode.A;
    private Handler handler = new Handler(Looper.getMainLooper());
    private SurfaceHolder holder;
    private Paint paint;
    private List<Rect> rectList = new ArrayList<>();

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            generateRectangles();
            drawRectangles();
            screenUpdate();
            startUpdate();
        }
    };

    public enum UpdateMode {
        A, B, C, D, E
    }

    public RectangleSurfaceView(Context context) {
        this(context, null);
    }

    public RectangleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3.0f);
        setFocusable(true);
    }

    public void setUpdateMode(final UpdateMode mode) {
        currentMode = mode;
        stopUpdate();
        startUpdate();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startUpdate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopUpdate();
    }

    private void startUpdate() {
        handler.postDelayed(updateRunnable, 500);
    }

    private void stopUpdate() {
        handler.removeCallbacks(updateRunnable);
    }

    private void generateRectangles() {
        rectList.clear();
        final int rectWidth = TestUtils.randInt(100, 200);
        final int x = TestUtils.randInt(0, getWidth() - rectWidth * 2);
        final int y = TestUtils.randInt(0, getHeight() - rectWidth * 2);
        rectList.add(new Rect(x, y, x + rectWidth, y + rectWidth));
        int l = 0, t = 0, r = 0, b = 0;
        switch (currentMode) {
            case A:
                l = x + rectWidth / 2;
                t = y + rectWidth / 2;
                r = l + rectWidth;
                b = t + rectWidth;
                break;
            case B:
                l = x;
                t = y + rectWidth;
                r = l + rectWidth;
                b = t + rectWidth;
                break;
            case C:
                l = x;
                t = y + rectWidth / 2;
                r = l + rectWidth;
                b = t + rectWidth;
                break;
            case D:
                l = x - rectWidth / 2;
                t = y - rectWidth / 2;
                r = l + rectWidth * 2;
                b = t + rectWidth * 2;
                break;
            case E:
                l = x + rectWidth / 4;
                t = y + rectWidth / 4;
                r = l + rectWidth / 2;
                b = t + rectWidth / 2;
                break;
        }
        rectList.add(new Rect(l, t, r, b));
    }

    private void screenUpdate() {
        for (Rect rect : rectList) {
            EpdController.refreshScreenRegion(this, rect.left, rect.top, rect.width(), rect.height(), com.onyx.android.sdk.api.device.epd.UpdateMode.GC);
            TestUtils.sleep(100);
        }
    }

    private void drawRectangles() {
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        for (Rect rect : rectList) {
            canvas.drawRect(rect, paint);
        }
        holder.unlockCanvasAndPost(canvas);
    }
}
