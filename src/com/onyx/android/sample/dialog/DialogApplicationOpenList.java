/**
 * 
 */
package com.onyx.android.sample.dialog;

import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.database.DataSetObserver;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.onyx.android.sample.R;
import com.onyx.android.sample.dialog.adapter.GridViewApplicationOpenListAdapter;
import com.onyx.android.sdk.data.sys.OnyxAppPreferenceCenter;
import com.onyx.android.sdk.data.util.FileUtil;
import com.onyx.android.sdk.device.EpdController;
import com.onyx.android.sdk.device.EpdController.UpdateMode;
import com.onyx.android.sdk.ui.OnyxGridView;
import com.onyx.android.sdk.ui.dialog.DialogBaseOnyx;

import java.io.File;
import java.util.List;

/**
 * 
 * @author joy
 *
 */
public class DialogApplicationOpenList extends DialogBaseOnyx
{
    @SuppressWarnings("unused")
    private static final String TAG = "DialogApplicationOpenList";
    
    public interface OnApplicationSelectedListener
    {
        public void onApplicationSelected(ResolveInfo info, boolean makeDefault);
    }
    
    // initialize to avoid null checking
    private OnApplicationSelectedListener mOnApplicationSelectedListener = new OnApplicationSelectedListener()
    {
        
        @Override
        public void onApplicationSelected(ResolveInfo info, boolean makeDefault)
        {
            // do nothing
        }
    };
    
    public void setOnApplicationSelectedListener(OnApplicationSelectedListener l)
    {
        mOnApplicationSelectedListener = l;
    }

    private OnyxGridView mGridView = null;
    private Button mButtonNext = null;
    private Button mButtonPrevious = null;
    private TextView mTextViewPage = null;
    private CheckBox mCheckBoxDefaultOpen = null;
    private Activity mActivity = null;
    private View mView = null;
    private GridViewApplicationOpenListAdapter mAdapter = null;
    private File mFile = null;

    private boolean mIsBackOpenWith = false;

    public DialogApplicationOpenList(Activity activity, List<ResolveInfo> resolveInfoList, File file, boolean backOpenWith)
    {
        super(activity);

        this.setContentView(R.layout.dialog_application_openlist);
        mView = findViewById(R.id.layout_dialog);

        mActivity = activity;
        mFile = file;
        mIsBackOpenWith = backOpenWith;

        mGridView = (OnyxGridView)this.findViewById(R.id.gridview_openlist);
        mButtonNext = (Button)this.findViewById(R.id.button_next);
        mButtonPrevious = (Button)this.findViewById(R.id.button_prev);
        mTextViewPage = (TextView)this.findViewById(R.id.textview_dialog_application_page);
        mCheckBoxDefaultOpen = (CheckBox)this.findViewById(R.id.checkbox_default_open);
        
        mGridView.setOnItemClickListener(new OnItemClickListener()
        { 
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                
                ResolveInfo resolve_info = (ResolveInfo) view.getTag();
                
                if (mCheckBoxDefaultOpen.isChecked()) {
                    String appName = resolve_info.activityInfo.applicationInfo.loadLabel(mActivity.getPackageManager()).toString();
                    String pkg = resolve_info.activityInfo.packageName;
                    String cls = resolve_info.activityInfo.name;

                    if (OnyxAppPreferenceCenter.setAppPreference(mActivity, FileUtil.getFileExtension(mFile), appName, pkg, cls)) {
                        Toast.makeText(mActivity, R.string.Succeed_setting, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mActivity, R.string.Fail_setting, Toast.LENGTH_SHORT).show();
                    }
                }
                
                ResolveInfo info = GridViewApplicationOpenListAdapter.getViewTag(view);
                mOnApplicationSelectedListener.onApplicationSelected(info, false);
                DialogApplicationOpenList.this.dismiss();
            }
        });
        
        mButtonNext.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                int height = mGridView.getHeight();
                
                if (mAdapter.getPaginator().canNextPage()) {
                    mAdapter.getPaginator().nextPage();
                }
                
                if (height != mGridView.getLayoutParams().height) {
                    mGridView.getLayoutParams().height = height;
                }
            }
        });
        
        mButtonPrevious.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (mAdapter.getPaginator().canPrevPage()) {
                    mAdapter.getPaginator().prevPage();
                }
            }
        });
        
        mAdapter = new GridViewApplicationOpenListAdapter(activity, mGridView, resolveInfoList);
        mAdapter.registerDataSetObserver(new DataSetObserver()
        {
            @Override
            public void onChanged()
            {
                DialogApplicationOpenList.this.updateTextViewPage();
            }
            
            @Override
            public void onInvalidated()
            {
                DialogApplicationOpenList.this.updateTextViewPage();
            }
        });
        
        mGridView.setAdapter(mAdapter);
        mAdapter.getPaginator().setPageSize(resolveInfoList.size());
    }
    
    private void updateTextViewPage()
    {
        final int current_page = mAdapter.getPaginator().getPageIndex() + 1;
        final int page_count = (mAdapter.getPaginator().getPageCount() != 0) ?
                mAdapter.getPaginator().getPageCount() : 1;

        mTextViewPage.setText(String.valueOf(current_page) + "/" + String.valueOf(page_count));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        EpdController.invalidate(mView, UpdateMode.GU);

        if (mIsBackOpenWith && keyCode == KeyEvent.KEYCODE_BACK) {
            DialogApplicationOpenList.this.dismiss();
            DialogOpenWith dialogOpenWith = new DialogOpenWith(mActivity, mFile);
            dialogOpenWith.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
