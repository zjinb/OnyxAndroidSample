package com.onyx.android.sample.widget;

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

    }

    private void startUpdate() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateRectangles();
                drawRectangle();
                screenUpdate();
                startUpdate();
            }
        }, 5000);
    }

    private void screenUpdate() {
        TestUtils.sleep(2000);
        for (Rect rect : rectList) {
            EpdController.refreshScreenRegion(this, rect.left, rect.top, rect.width(), rect.height(), com.onyx.android.sdk.api.device.epd.UpdateMode.GC);
        }
    }

    private void generateRectangles() {
        rectList.clear();
        switch (currentMode) {
            case A:
                generateRectanglesTypeA();
                break;
            case B:
                generateRectanglesTypeB();
                break;
            case C:
                generateRectanglesTypeC();
                break;
            case D:
                generateRectanglesTypeD();
                break;
            case E:
                generateRectanglesTypeE();
                break;
            default:
                break;
        }
    }

    private void generateRectanglesTypeA() {
        int div = 3;
        int left = TestUtils.randInt(0, getWidth() / div);
        int top = TestUtils.randInt(0, getHeight() / div);
        int width = TestUtils.randInt(getWidth() /  (div * 2), getWidth() /  div);
        int height = TestUtils.randInt(getWidth() /  (div * 2), getHeight() / div);
        Rect r1 = new Rect(left, top, left + width, top + height);

        int left2 = left + width / 2;
        int top2 = top + height / 2;
        Rect r2 = new Rect(left2, top2, left2 + width, top2 + height);

        rectList.add(r1);
        rectList.add(r2);
    }

    private void generateRectanglesTypeB() {
        int div = 4;
        int left = TestUtils.randInt(0, getWidth() / div);
        int top = TestUtils.randInt(0, getHeight() / div);
        int width = TestUtils.randInt(0, getWidth() /  div);
        int height = TestUtils.randInt(0, getHeight() / div);
        Rect r1 = new Rect(left, top, left + width, top + height);

        int left2 = left;
        int top2 = top + height - 1;
        Rect r2 = new Rect(left2, top2, left2 + width, top2 + height);

        rectList.add(r1);
        rectList.add(r2);
    }

    private void generateRectanglesTypeC() {
        int div = 4;
        int left = TestUtils.randInt(0, getWidth() / div);
        int top = TestUtils.randInt(0, getHeight() / div);
        int width = TestUtils.randInt(0, getWidth() /  div);
        int height = TestUtils.randInt(0, getHeight() / div);
        Rect r1 = new Rect(left, top, left + width, top + height);

        int left2 = left;
        int top2 = top + height - 1;
        Rect r2 = new Rect(left2, top2, left2 + width, top2 + height);

        rectList.add(r1);
        rectList.add(r2);
    }

    private void generateRectanglesTypeD() {
        int div = 4;
        int left = TestUtils.randInt(0, getWidth() / div);
        int top = TestUtils.randInt(0, getHeight() / div);
        int width = TestUtils.randInt(0, getWidth() /  div);
        int height = TestUtils.randInt(0, getHeight() / div);
        Rect r1 = new Rect(left, top, left + width, top + height);

        int lowLimit = 20;
        int upLimit = 30;
        int left2 = left + TestUtils.randInt(lowLimit, upLimit);
        int top2 = top + TestUtils.randInt(lowLimit, upLimit);
        int width2 = width - TestUtils.randInt(lowLimit, upLimit);
        int height2 = height - TestUtils.randInt(lowLimit, upLimit);
        Rect r2 = new Rect(left2, top2, left2 + width2, top2 + height2);

        rectList.add(r1);
        rectList.add(r2);
    }

    private void generateRectanglesTypeE() {
        int div = 4;
        int left = TestUtils.randInt(0, getWidth() / div);
        int top = TestUtils.randInt(0, getHeight() / div);
        int width = TestUtils.randInt(0, getWidth() /  div);
        int height = TestUtils.randInt(0, getHeight() / div);
        Rect r1 = new Rect(left, top, left + width, top + height);

        int lowLimit = 20;
        int upLimit = 30;
        int left2 = left - TestUtils.randInt(lowLimit, upLimit);
        int top2 = top - TestUtils.randInt(lowLimit, upLimit);
        int width2 = width + TestUtils.randInt(lowLimit, upLimit);
        int height2 = height + TestUtils.randInt(lowLimit, upLimit);
        Rect r2 = new Rect(left2, top2, left2 + width2, top2 + height2);

        rectList.add(r1);
        rectList.add(r2);
    }

    private void drawRectangle() {
        Canvas canvas = holder.lockCanvas();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        int index = 0;
        for(Rect rect: rectList) {
            int value = index * 128;
            paint.setColor(Color.rgb(value, value, value));
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(rect, paint);
            ++index;
        }
        holder.unlockCanvasAndPost(canvas);
    }
}
