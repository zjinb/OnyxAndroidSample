/**
 * 
 */
package com.onyx.latinime.keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.graphics.Point;
import android.util.SparseArray;

/**
 * help to navigate soft keyboard by DPAD key
 * 
 * @author joy@onyx
 *
 */
public class KeyboardNavigator {
    
    private static class Row {
        /**
         * top line of the row
         */
        @SuppressWarnings("unused")
        private final int mY;
        /**
         * from left to right in order
         */
        private final ArrayList<Key> mKeys = new ArrayList<Key>();
        
        public Row(int y) 
        {
            mY = y;
        }
        
        public ArrayList<Key> getKeys()
        {
            return mKeys;
        }
        /**
         * insert next right key
         */
        public void insertKey(Key key)
        {
            mKeys.add(key);
        }
    }
    
    /**
     * from top to bottom in order
     */
    ArrayList<Row> mRows = new ArrayList<Row>();
    private Key mCurrentKey = null;
    private HashMap<Key, Point> mKeyPositionMap = new HashMap<Key, Point>();
    
    public KeyboardNavigator(Keyboard keyboard)
    {
        List<Key> keys = new ArrayList<>();
        Key[] keyboardKeys = keyboard.getKeys();
        for (int i = 0; i < keyboardKeys.length; i++) {
            keys.add(keyboardKeys[i]);
        }

        ArrayList<Integer> sorted_y_rows = new ArrayList<Integer>();
        SparseArray<Row> row_map = new SparseArray<Row>();
        for (Key key : keys) {
            Row r = row_map.get(key.mY);
            if (r == null) {
                r = new Row(key.mY);
                row_map.put(key.mY, r);
                sorted_y_rows.add(Integer.valueOf(key.mY));
            }
            r.insertKey(key);
        }

        Collections.sort(sorted_y_rows);
        
        int pos_x = 0;
        int pos_y = 0;
        for (Integer i : sorted_y_rows) {
            Row r = row_map.get(i);
            assert(r != null);
            
            Collections.sort(r.getKeys(), new Comparator<Key>()
            {

                @Override
                public int compare(Key object1, Key object2)
                {
                    return object1.mX - object2.mX;
                }
            });
            
            mRows.add(r);
            
            pos_x = 0;
            for (Key k : r.getKeys()) {
                Point pos = new Point(pos_x, pos_y);
                mKeyPositionMap.put(k, pos);
                pos_x++;
            }
            
            pos_y++;
        }
        
        mCurrentKey = mRows.get(0).getKeys().get(0);
    }
    
    public Key getCurrentKey()
    {
        return mCurrentKey;
    }
    public void setCurrentKey(Key key)
    {
        mCurrentKey = key;
    }
    
    public Key searchLeft(Key key)
    {
        Point pos = mKeyPositionMap.get(key);
        if (pos == null) {
            assert(false);
            return null;
        }
        
        if (pos.x <= 0) {
            return null;
        }
        else {
            return mRows.get(pos.y).getKeys().get(pos.x - 1);
        }
    }
    public Key searchLeftFarMost(Key key)
    {
        Point pos = mKeyPositionMap.get(key);
        if (pos == null) {
            assert(false);
            return null;
        }
        
        return mRows.get(pos.y).getKeys().get(0);
    }
    public Key searchRight(Key key)
    {
        Point pos = mKeyPositionMap.get(key);
        if (pos == null) {
            assert(false);
            return null;
        }
        
        ArrayList<Key> keys = mRows.get(pos.y).getKeys();  
        if (pos.x >= (keys.size() - 1)) {
            return null;
        }
        else {
            return mRows.get(pos.y).getKeys().get(pos.x + 1);
        }
    }
    public Key searchRightFarMost(Key key)
    {
        Point pos = mKeyPositionMap.get(key);
        if (pos == null) {
            assert(false);
            return null;
        }
        
        ArrayList<Key> keys = mRows.get(pos.y).getKeys();  
        return keys.get(keys.size() - 1);
    }
    public Key searchUp(Key key)
    {
        Point pos = mKeyPositionMap.get(key);
        if (pos == null) {
            assert(false);
            return null;
        }
        
        if (pos.y <= 0) {
            return null;
        }
        else {
            Row r = mRows.get(pos.y - 1);
            return this.locateNearestKeyInRow(r, key);
        }
    }
    public Key searchUpFarMost(Key key)
    {
        Point pos = mKeyPositionMap.get(key);
        if (pos == null) {
            assert(false);
            return null;
        }
        
        Row r = mRows.get(0);
        return this.locateNearestKeyInRow(r, key);
    }
    public Key searchDown(Key key)
    {
        Point pos = mKeyPositionMap.get(key);
        if (pos == null) {
            assert(false);
            return null;
        }
        
        if (pos.y >= (mRows.size() - 1)) {
            return null;
        }
        else {
            Row r = mRows.get(pos.y + 1);
            return this.locateNearestKeyInRow(r, key);
        }
    }
    public Key searchDownFarMost(Key key)
    {
        Point pos = mKeyPositionMap.get(key);
        if (pos == null) {
            assert(false);
            return null;
        }
        
        Row r = mRows.get(mRows.size() - 1);
        return this.locateNearestKeyInRow(r, key);
    }
    
    /**
     * never return null
     * 
     * @param row
     * @param key
     * @return
     */
    private Key locateNearestKeyInRow(Row row, Key key)
    {
        int size = row.getKeys().size();
        assert(size > 0);
        if (size <= 1) {
            return row.getKeys().get(0);
        }
        
        if (key.mX <= row.getKeys().get(0).mX) {
            return row.getKeys().get(0);
        }
        
        if (key.mX >= row.getKeys().get(size - 1).mX) {
            return row.getKeys().get(size - 1);
        }
        
        int key_right = key.mX + key.mWidth;
        
        int i = 0;
        for (; i < size - 1; i++) {
            Key current = row.getKeys().get(i);
            Key next = row.getKeys().get(i + 1);
            
            int current_right = current.mX + current.mWidth;
            if (current_right < key.mX) {
                continue;
            }

            if (key_right < next.mX) {
                // which can hardly happen
                return current;
            }
            
            int overlap_left = current_right - key.mX;
            int overlap_right =  key_right - next.mX;
            if (overlap_left >= overlap_right) {
                return current;
            }
            else {
                return next;
            }
        }
        
        return row.getKeys().get(size - 1);
    }
}
