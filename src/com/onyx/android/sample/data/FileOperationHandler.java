/**
 * 
 */
package com.onyx.android.sample.data;

import java.util.ArrayList;

import android.content.Context;

import com.onyx.android.sample.adapter.GridItemBaseAdapter;
import com.onyx.android.sdk.ui.data.FileItemData;

/**
 * @author joy
 *
 */
public class FileOperationHandler implements IFileOperationHandler
{
    private Context mContext = null;
    private GridItemBaseAdapter mAdapter = null;
    private ArrayList<FileItemData> mSourceItems = new ArrayList<FileItemData>();
    
    public FileOperationHandler(Context context, GridItemBaseAdapter adapter)
    {
        mContext = context;
        mAdapter = adapter;
    }
    
    public Context getContext()
    {
        return mContext;
    }
    public GridItemBaseAdapter getAdapter()
    {
        return mAdapter;
    }
    
    public ArrayList<FileItemData> getSourceItems()
    {
        return mSourceItems;
    }
    
    public void setSourceItems(ArrayList<FileItemData> items)
    {
        mSourceItems.clear();
        mSourceItems.addAll(items);
    }
    
    @Override
    public void onNewFile()
    {
    }

    @Override
    public void onNewFolder()
    {
    }

    @Override
    public void onRename()
    {
    }

    @Override
    public void onCopy()
    {
    }

    @Override
    public void onCut()
    {
    }

    @Override
    public void onRemove()
    {
    }
    
    @Override
    public void onProperty()
    {
    }
    
    @Override
    public void onGotoFolder()
    {
    }

    @Override
    public void onFavorite()
    {
    }

    @Override
    public void onShowOpenWith()
    {
    }
}
