/**
 * 
 */
package com.onyx.android.sample.adapter;

import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onyx.android.sample.R;
import com.onyx.android.sample.StorageActivity;
import com.onyx.android.sdk.data.OnyxItemURI;
import com.onyx.android.sdk.ui.OnyxGridView;
import com.onyx.android.sdk.ui.data.BookItemData;
import com.onyx.android.sdk.ui.data.FileItemData;
import com.onyx.android.sdk.ui.data.GridItemData;
import com.onyx.android.sdk.ui.data.GridViewPageLayout.GridViewMode;

/**
 * @author Administrator
 *
 */
public class StorageAdapter extends GridItemBaseAdapter
{
    private LayoutInflater mInflater = null;

    private static final int sHorizontalSpacing = 0;

    public StorageAdapter(Context context, OnyxGridView gridView)
    {
        super(gridView);

        mInflater = LayoutInflater.from(context);

        this.getPageLayout().setItemMinWidth((int)context.getResources().getDimension(R.dimen.storage_item_width));
        this.getPageLayout().setItemMinHeight((int)context.getResources().getDimension(R.dimen.storage_item_height));
        this.getPageLayout().setItemThumbnailMinHeight((int)context.getResources().getDimension(R.dimen.storage_item_height));
        this.getPageLayout().setItemDetailMinHeight((int)context.getResources().getDimension(R.dimen.storage_listitem_height));
        this.getPageLayout().setVerticalSpacing((int)context.getResources().getDimension(R.dimen.storage_vertical_spacing));
        this.getPageLayout().setHorizontalSpacing(sHorizontalSpacing);
    }
    
    @Override
    public void fillItems(OnyxItemURI hostURI, GridItemData[] items)
    {
        super.fillItems(hostURI, items);
        
        this.sortItems(StorageActivity.SortPolicy, StorageActivity.AscOrder);
    }
    
    @Override
    public void fillItems(OnyxItemURI hostURI, Collection<? extends GridItemData> items)
    {
        super.fillItems(hostURI, items);
        
        this.sortItems(StorageActivity.SortPolicy, StorageActivity.AscOrder);
    }
    
    @Override
    public void appendItem(GridItemData data)
    {
        super.appendItem(data);
        
        this.sortItems(StorageActivity.SortPolicy, StorageActivity.AscOrder);
    }
    
    @Override
    public void appendItems(GridItemData[] data)
    {
        super.appendItems(data);
        
        this.sortItems(StorageActivity.SortPolicy, StorageActivity.AscOrder);
    }
    
    @Override
    public void appendItems(Collection<? extends GridItemData> data)
    {
        super.appendItems(data);
        
        this.sortItems(StorageActivity.SortPolicy, StorageActivity.AscOrder);
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View ret_view = null;

        int idx = this.getPaginator().getItemIndex(position, this.getPaginator().getPageIndex());
        GridItemData item_data = this.getItems().get(idx);

        if (this.getPageLayout().getViewMode() == GridViewMode.Thumbnail) {
            ret_view = mInflater.inflate(R.layout.gridview_item_thumbnailview, null);
            TextView textview_thumbnail_item_name = (TextView)ret_view.findViewById(R.id.textview_thumbnail_gridview_item_name);

            @SuppressWarnings("unused")
            TextView textview_thumbnail_item_author = (TextView)ret_view.findViewById(R.id.textview_thumbnail_gridview_item_author);
            ImageView imageview_thumbnail_item = (ImageView)ret_view.findViewById(R.id.imageview_thumbnail_gridview_item_cover);

            ret_view.setTag(item_data);

            if ((item_data instanceof BookItemData) &&
                    (((BookItemData)item_data).getThumbnail() != null)) {
                BookItemData book = (BookItemData)item_data;
                imageview_thumbnail_item.setImageBitmap(book.getThumbnail());
            }
            else if (item_data.getBitmap() != null) {
                imageview_thumbnail_item.setImageBitmap(item_data.getBitmap());
            }
            else {
                imageview_thumbnail_item.setImageResource(item_data.getImageResourceId());
            }

            if (item_data.getTextId() != -1) {
            	textview_thumbnail_item_name.setText(item_data.getTextId());
            }
            else {
                assert(textview_thumbnail_item_name != null);
            	textview_thumbnail_item_name.setText(item_data.getText());
            }
        }
        else {
            assert(this.getPageLayout().getViewMode() == GridViewMode.Detail);
            ret_view = mInflater.inflate(R.layout.gridview_item_detailview, null);
            TextView textview_detail_item_name = (TextView)ret_view.findViewById(R.id.textview_detail_gridview_item_name);
            
//            do nothing 
//            TextView textview_detail_item_author = (TextView)ret_view.findViewById(R.id.textview_detail_gridview_item_author);
//            TextView textview_detail_item_time = (TextView)ret_view.findViewById(R.id.textview_detail_gridview_item_time);
            ImageView imageview_detail_item = (ImageView)ret_view.findViewById(R.id.imageview_detail_gridview_item_cover);

            RelativeLayout.LayoutParams imageview_params = new RelativeLayout.LayoutParams(this.getPageLayout().getItemCurrentHeight(),
                    this.getPageLayout().getItemCurrentHeight());
            imageview_detail_item.setLayoutParams(imageview_params);

            ret_view.setTag(item_data);

            if ((item_data instanceof BookItemData) &&
                    (((BookItemData)item_data).getThumbnail() != null)) {
                BookItemData book = (BookItemData)item_data;
                imageview_detail_item.setImageBitmap(book.getThumbnail());
            }
            else if (item_data.getBitmap() != null) {
                imageview_detail_item.setImageBitmap(item_data.getBitmap());
            }
            else {
                imageview_detail_item.setImageResource(item_data.getImageResourceId());
            }

            if (textview_detail_item_name != null) {
                textview_detail_item_name.setText(item_data.getText());
            }
        }

        CheckBox cb = (CheckBox) ret_view.findViewById(R.id.checkbox_multi);
        if (item_data instanceof FileItemData) {
            if (this.isInMultipleSelectionMode()) {
                cb.setVisibility(View.VISIBLE);
                if (this.getSelectedItems().contains(item_data)) {
                    cb.setChecked(true);
                }
            }
        }
        else if (this.isInMultipleSelectionMode()) {
            cb.setVisibility(View.GONE);
        }

        // warning!
        // repeatedly calling setLayoutParams() will cause a strange bug that makes TextView's content disappearing, 
        // having no clue about it
        if (ret_view.getLayoutParams() == null) {
            OnyxGridView.LayoutParams params = new OnyxGridView.LayoutParams(this.getPageLayout().getItemCurrentWidth(),
                    this.getPageLayout().getItemCurrentHeight());
            ret_view.setLayoutParams(params);  
        }
        else {
            ret_view.getLayoutParams().width = this.getPageLayout().getItemCurrentWidth();
            ret_view.getLayoutParams().height = this.getPageLayout().getItemCurrentHeight();
        }

        return ret_view;
    }
    
}
