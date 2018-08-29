/**
 * 
 */
package com.onyx.android.sample.data;

import java.util.ArrayList;

import com.onyx.android.sdk.ui.data.FileItemData;

/**
 * @author joy
 *
 */
public class CopyService
{
    private static ArrayList<FileItemData> sSourceItems = null;
    private static boolean sIsCut = false;
    
    public static ArrayList<FileItemData> getSourceItems()
    {
        return sSourceItems;
    }
    public static boolean isCut()
    {
        return sIsCut;
    }
    
    public static void copy(ArrayList<FileItemData> items)
    {
        setSourceItems(items);
        sIsCut = false;
    }
    
    public static void cut(ArrayList<FileItemData> items)
    {
        setSourceItems(items);
        sIsCut = true;
    }
    
    public static void clean()
    {
        sSourceItems = null;
    }

    private static void setSourceItems(ArrayList<FileItemData> items)
    {
        if (sSourceItems == null) {
            sSourceItems = new ArrayList<FileItemData>();
        }
        else {
            sSourceItems.clear();
        }
        sSourceItems.addAll(items);
    }
}
