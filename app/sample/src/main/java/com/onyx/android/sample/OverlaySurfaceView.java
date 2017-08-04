package com.onyx.android.sample;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.onyx.android.sdk.api.device.epd.EpdController;
import com.onyx.android.sdk.api.device.epd.UpdateMode;
import com.onyx.android.sdk.utils.TestUtils;

/**
 * Created by wangxu on 17-8-4.
 */

public class OverlaySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private Thread thread;
    private Canvas canvas;
    private Paint paint;
    private boolean threadFlag;
    private int spac = 20;
    private int rectWidth;
    private int rectHeight;
    private int COLUMN_COUNT = 10;
    private int ROW_COUNT = 8;
    private int dx;
    Rect[] rects = new Rect[ROW_COUNT];
    Rect[][] allRect = new Rect[ROW_COUNT][COLUMN_COUNT];

    public OverlaySurfaceView(Activity context) {
        this(context, null);
    }

    public OverlaySurfaceView(Activity context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadFlag = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        rectWidth = width / ROW_COUNT;
        rectHeight = (height - spac * ROW_COUNT) / ROW_COUNT;
        dx = rectWidth - rectWidth / 4;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        threadFlag = false;
    }

    @Override
    public void run() {
        calculateRects();
        draw();
        while (threadFlag) {
            try {
                update();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        int row = TestUtils.randInt(0, ROW_COUNT -1);
        int column = TestUtils.randInt(0, COLUMN_COUNT -1);
        Rect rect = allRect[row][column];
        EpdController.invalidate(this, rect.left, rect.top, rect.right, rect.bottom, UpdateMode.GC);
        Rect r;
        if (column == COLUMN_COUNT -1) {
            r = allRect[row][column - 1];
        } else {
            r = allRect[row][column +1];
        }
        EpdController.invalidate(this, r.left, r.top, r.right, r.bottom, UpdateMode.GC);
    }

    private void draw() {
        canvas = holder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            for (int i = 0; i < COLUMN_COUNT; i++) {
                if (i == 0) {
                    drawRects(canvas);
                    continue;
                }
                canvas.translate(dx, 0);
                drawRects(canvas);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawRects(final Canvas canvas) {
        for (int j = 0; j < ROW_COUNT; j++) {
            canvas.drawRect(rects[j], paint);
        }
    }

    private void calculateRects() {
        for (int i = 0; i < ROW_COUNT; i++) {
            int left  = 0;
            int top = rectHeight * i + spac * i;
            int right = left + rectWidth;
            int bottom = rectHeight + top;
            Rect rect = new Rect(left, top, right, bottom);
            rects[i] = rect;
        }
        calculateAllRegion();
    }

    private void calculateAllRegion() {
        for (int i = 0; i < ROW_COUNT; i++) {
            Rect firstRect = rects[i];
            for (int j = 0; j < COLUMN_COUNT; j++) {
                int left = firstRect.left + j * dx;
                int top = firstRect.top;
                int right = firstRect.right + j * dx;
                int bottom = firstRect.bottom;
                Rect rect = new Rect(left, top, right, bottom);
                allRect[i][j] = rect;
            }
        }
    }
}
