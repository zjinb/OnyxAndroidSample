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
            drawRectangle();
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

    private void screenUpdate() {
        for (Rect rect : rectList) {
            EpdController.refreshScreenRegion(this, rect.left, rect.top, rect.width(), rect.height(), com.onyx.android.sdk.api.device.epd.UpdateMode.GC);
            TestUtils.sleep(100);
        }
    }

    private void drawRectangle() {
        rectList.clear();
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        final int rectWidth = TestUtils.randInt(100, 200);
        final int x = TestUtils.randInt(0, getWidth() - rectWidth * 2);
        final int y = TestUtils.randInt(0, getHeight() - rectWidth * 2);

        Rect rect1 = new Rect(x, y, x + rectWidth, y + rectWidth);
        rectList.add(rect1);
        canvas.drawRect(rect1, paint);

        switch (currentMode) {
            case A:
                int l1 = x + rectWidth / 2;
                int t1 = y + rectWidth / 2;
                int r1 = l1 + rectWidth;
                int b1 = t1 + rectWidth;
                Rect rect2 = new Rect(l1, t1 , r1, b1);
                rectList.add(rect2);
                canvas.drawRect(rect2, paint);
                break;
            case B:
                int l2 = x;
                int t2 = y + rectWidth;
                int r2 = l2 + rectWidth;
                int b2 = t2 + rectWidth;
                Rect rect3 = new Rect(l2, t2 , r2, b2);
                rectList.add(rect3);
                canvas.drawRect(rect3, paint);
                break;
            case C:
                int l3 = x;
                int t3 = y + rectWidth / 2;
                int r3 = l3 + rectWidth;
                int b3 = t3 + rectWidth;
                Rect rect4 = new Rect(l3, t3 , r3, b3);
                rectList.add(rect4);
                canvas.drawRect(rect4, paint);
                break;
            case D:
                int l4 = x - rectWidth / 2;
                int t4 = y - rectWidth / 2;
                int r4 = l4 + rectWidth * 2;
                int b4 = t4 + rectWidth * 2;
                Rect rect5 = new Rect(l4, t4 , r4, b4);
                rectList.add(rect5);
                canvas.drawRect(rect5, paint);
                break;
            case E:
                int l5 = x + rectWidth / 4;
                int t5 = y + rectWidth / 4;
                int r5 = l5 + rectWidth / 2;
                int b5 = t5 + rectWidth / 2;
                Rect rect6 = new Rect(l5, t5 , r5, b5);
                rectList.add(rect6);
                canvas.drawRect(rect6, paint);
                break;
        }
        holder.unlockCanvasAndPost(canvas);
    }
}
