package com.onyx.android.sample.utils;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuzeng on 08/08/2017.
 */

public class RectUtils {

    public static class RectResult {
        public Rect parent;
        public Rect child;
        public Rect[] inParent = new Rect[4];
        public Rect[] inChild = new Rect[4];
    }

    public static List<Rect> subtract(Rect rect, Rect subtracted) {
        if (rect.isEmpty())  {
            return null;
        }
        if (rect.equals(subtracted)) {
            return null;
        }
        if (!rect.intersect(subtracted)) {
            return null;
        }
        List<Rect> results = new ArrayList<Rect>();
        Rect topRect = getTopRect(rect, subtracted);
        if (!topRect.isEmpty()) {
            results.add(topRect);
        }
        Rect leftRect = getLeftRect(rect, subtracted);
        if (!leftRect.isEmpty()) {
            results.add(leftRect);
        }
        Rect rightRect = getRightRect(rect, subtracted);
        if (!rightRect.isEmpty()) {
            results.add(rightRect);
        }
        Rect bottomRect = getBottomRect(rect, subtracted);
        if (!bottomRect.isEmpty()) {
            results.add(bottomRect);
        }
        return results;
    }

    private static Rect getTopRect(Rect rect, Rect subtracted)
    {
        return new Rect(rect.left, rect.top, rect.width(), Math.max(subtracted.top, 0));
    }

    private static Rect getRightRect(Rect rect, Rect subtracted)
    {
        return new Rect(subtracted.right, subtracted.top, Math.max(rect.right - subtracted.right, 0), subtracted.height());
    }

    private static Rect getLeftRect(Rect rect, Rect subtracted)
    {
        return new Rect(rect.left, subtracted.top, Math.max(subtracted.left - rect.left, 0), subtracted.height());
    }

    private static Rect getBottomRect(Rect rect, Rect subtracted)
    {
        return new Rect(rect.left, subtracted.bottom, rect.width(), Math.max(rect.height() - subtracted.bottom, 0));
    }


    public static boolean getTopLeftInterset(final RectResult result) {
        result.inChild[0] = new Rect(result.child.left,
                result.child.top,
                result.child.right,
                result.parent.top - result.child.top);
        result.inChild[1] = new Rect(result.child.left,
                result.parent.top,
                result.parent.left - result.child.left,
                result.child.bottom - result.parent.top);
        result.inParent[0] = new Rect(result.parent.left,
                result.parent.top,
                result.child.right - result.parent.left,
                result.child.bottom - result.parent.top);
        return true;
    }

    public static boolean getTopRightInterset(final RectResult result) {
        result.inChild[0] = new Rect(result.child.left,
                result.child.top,
                result.child.right,
                result.parent.top - result.child.top);
        result.inChild[1] = new Rect(result.parent.left,
                result.parent.top,
                result.child.right - result.parent.left,
                result.child.bottom - result.parent.top);
        result.inParent[0] = new Rect(result.child.left,
                result.parent.top,
                result.parent.left - result.child.left,
                result.child.bottom - result.parent.top);
        return true;
    }

}
