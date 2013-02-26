/**
 * 
 */
package com.onyx.android.sample.data;

import java.io.File;

import android.app.Activity;

import com.onyx.android.sdk.data.OnyxItemURI;
import com.onyx.android.sdk.ui.OnyxGridView;

/**
 * @author joy
 *
 */
public class GridItemManager
{
    public static OnyxItemURI getStorageURI()
    {
        return OnyxItemURI.ROOT.append("Storage");
    }
    
    public static File getFileFromURI(OnyxItemURI uri)
    {
        return null;
    }
    
    public static OnyxItemURI getURIFromFilePath(String path)
    {
        return null;
    }
    
    public static boolean isItemContainer(OnyxItemURI uri)
    {
        return false;
    }
    
    public static boolean processURI(OnyxGridView gridview, OnyxItemURI uri, Activity activity)
    {
        return false;
    }
}
