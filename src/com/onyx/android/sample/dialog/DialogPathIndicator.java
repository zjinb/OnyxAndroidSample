/**
 * 
 */
package com.onyx.android.sample.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.onyx.android.sample.R;
import com.onyx.android.sample.StorageActivity;
import com.onyx.android.sample.adapter.GridItemBaseAdapter;
import com.onyx.android.sample.dialog.adapter.ListViewPathIndicatorAdapter;
import com.onyx.android.sdk.data.OnyxItemURI;
import com.onyx.android.sdk.ui.OnyxGridView;
import com.onyx.android.sdk.ui.data.GridItemData;
import com.onyx.android.sdk.ui.dialog.DialogBaseOnyx;

import java.util.ArrayList;

/**
 * @author joy
 *
 */
public class DialogPathIndicator extends DialogBaseOnyx
{
    private StorageActivity mActivity = null;
    private OnyxItemURI mRoot = null;
    private OnyxItemURI mCurrentURI = null;
    private OnyxGridView mGridView = null;
    private Button mButtonCancel = null;
    
    public DialogPathIndicator(StorageActivity activity, OnyxItemURI root, OnyxItemURI currentURI)
    {
        super(activity);
        
        mActivity = activity;
        mRoot = root;
        mCurrentURI = currentURI;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.dialog_path_indicator);
        
        mGridView = (OnyxGridView)this.findViewById(R.id.gridview_path_indicator);
        mButtonCancel = (Button)this.findViewById(R.id.button_cancel);
        
        GridItemBaseAdapter adapter = new ListViewPathIndicatorAdapter(this.getContext(), mGridView);
        mGridView.setAdapter(adapter);
        
        mGridView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                GridItemData item = ListViewPathIndicatorAdapter.getViewTag(view);
                
                DialogPathIndicator.this.dismiss();
                if (item != null) {
                    DialogPathIndicator.this.mActivity.startURI(item.getURI()); 
                }
            }
        });
        
        mButtonCancel.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                DialogPathIndicator.this.dismiss();
            }
        });
        
        ArrayList<GridItemData> items = new ArrayList<GridItemData>();
        
        OnyxItemURI u = mCurrentURI;
        while (!u.equals(mRoot) && !u.equals(OnyxItemURI.ROOT)) {
            items.add(0, new GridItemData(u, u.getName(), R.drawable.dictionary));
            u = u.getParent();
        }
        
        items.add(0, new GridItemData(mRoot, mRoot.getName(), R.drawable.dictionary));
        
        adapter.fillItems(mCurrentURI, items);
        
        adapter.getPaginator().setPageSize(items.size());
    }

}
