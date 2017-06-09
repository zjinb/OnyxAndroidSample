/**
 * 
 */
package com.onyx.android.sample;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.onyx.android.sample.adapter.GridItemBaseAdapter;
import com.onyx.android.sample.adapter.GridItemBaseAdapter.OnHostURIChangedListener;
import com.onyx.android.sample.adapter.StorageAdapter;
import com.onyx.android.sample.data.CopyService;
import com.onyx.android.sample.data.FileOperationHandler;
import com.onyx.android.sample.data.GridItemManager;
import com.onyx.android.sample.dialog.DialogPathIndicator;
import com.onyx.android.sample.dialog.DialogSortBy;
import com.onyx.android.sample.view.OnyxFileGridView;
import com.onyx.android.sdk.data.AscDescOrder;
import com.onyx.android.sdk.data.OnyxItemURI;
import com.onyx.android.sdk.data.SortOrder;
import com.onyx.android.sdk.data.util.ActivityUtil;
import com.onyx.android.sdk.data.util.FileUtil;
import com.onyx.android.sdk.data.util.IntentFilterFactory;
import com.onyx.android.sdk.device.EpdController;
import com.onyx.android.sdk.device.EpdController.UpdateMode;
import com.onyx.android.sdk.ui.OnyxGridView;
import com.onyx.android.sdk.ui.data.FileItemData;
import com.onyx.android.sdk.ui.data.GridItemData;
import com.onyx.android.sdk.ui.data.GridViewPageLayout.GridViewMode;
import com.onyx.android.sdk.ui.data.GridViewPaginator.OnPageIndexChangedListener;
import com.onyx.android.sdk.ui.data.GridViewPaginator.OnStateChangedListener;

/**
 * @author joy
 *
 */
public class StorageActivity extends OnyxBaseActivity
{
    private static final String TAG = "StorageActivity";
    
    public static class GoUpLevelItem extends GridItemData
    {
        public GoUpLevelItem(OnyxItemURI uri, int textId, int imageResourceId)
        {
            super(uri, textId, imageResourceId);
        }

    }

    public static final String STARTING_URI = "com.onyx.android.launcher.StorageActivity.STARTING_URI";
    
    private static final String SAVE_INSTANCE_URI = "com.onyx.android.launcher.StorageActivity.SAVE_INSTANCE_URI";
    private static final String SAVE_INSTANCE_PAGE_INDEX = "com.onyx.android.launcher.StorageActivity.SAVE_INSTANCE_PAGE_INDEX";

    public static SortOrder SortPolicy = SortOrder.Name;
    public static GridViewMode ViewMode = GridViewMode.Thumbnail;
    public static AscDescOrder AscOrder = AscDescOrder.Asc;

    // root URI when activity startup
    private OnyxItemURI mStartingURI = null;

    private OnyxFileGridView mFileGridView = null;
    private TextView mTextViewPathIndicator = null;
    private EditText mSearchEditText;
    private ImageButton mButtonSortBy = null;
    private ImageButton mButtonChangeView = null;
    private ImageView mImageViewPathIndicator = null;
    private StorageAdapter mAdapter = null;

    private BroadcastReceiver mSDCardUnmountedReceiver = null;

    private FileOperationHandler mFileOperationHandler = null;
    
    private GridItemData mGoUpItem = null;
    private boolean mIsLongClickItem = false;

    /**
     * StorageActivity starting up helper
     * 
     * @param from
     * @param dstStorageURI
     */
    public static boolean startStorageActivity(Activity from, OnyxItemURI dstStorageURI)
    {
        Intent intent = new Intent(from, StorageActivity.class);
        intent.putExtra(StorageActivity.STARTING_URI, dstStorageURI.toString()); 

        return ActivityUtil.startActivitySafely(from, intent);
    }

    @Override
    public OnyxGridView getGridView()
    {
        return mFileGridView.getGridView();
    }

    @Override
    public void changeViewMode(GridViewMode viewMode)
    {
        super.changeViewMode(viewMode);

        StorageActivity.ViewMode = viewMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_storage);

        mFileGridView = (OnyxFileGridView)this.findViewById(R.id.gridview_storage);
        mTextViewPathIndicator = (TextView)this.findViewById(R.id.textview_path_indicator);
        mButtonSortBy = (ImageButton)findViewById(R.id.button_sort_by);
        mButtonChangeView = (ImageButton)findViewById(R.id.button_change_view);
        mImageViewPathIndicator = (ImageView)findViewById(R.id.imageview_path_indicator);

        mFileGridView.setCanPaste(true);
        
        assert(this.getGridView() == mFileGridView.getGridView());
        this.getGridView().registerOnAdapterChangedListener(new OnyxGridView.OnAdapterChangedListener()
        {

            @Override
            public void onAdapterChanged()
            {
                final GridItemBaseAdapter adapter = (GridItemBaseAdapter)StorageActivity.this.getGridView().getPagedAdapter();

                adapter.setOnHostURIChangedListener(new OnHostURIChangedListener()
                {

                    @Override
                    public void onHostURIChanged()
                    {
                        StorageActivity.this.mTextViewPathIndicator.setText(adapter.getHostURI().getName());
                    }
                }); 
                adapter.getPaginator().registerOnPageIndexChangedListener(new OnPageIndexChangedListener()
                {

                    @Override
                    public void onPageIndexChanged(int oldIndex, int newIndex)
                    {
                        EpdController.invalidate(StorageActivity.this.getGridView(), UpdateMode.GU);
                    }
                });
                adapter.getPaginator().registerOnStateChangedListener(new OnStateChangedListener() {

					@Override
					public void onStateChanged() {
						EpdController.invalidate(StorageActivity.this.getGridView(), UpdateMode.GU);
					}
				});
            }
        });
        
        mSearchEditText = (EditText)findViewById(R.id.edittext_search_box);
        mSearchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            search();
                            return true;
                        }
                        return false;
                    }
                });
        
        
        ImageButton button_search = (ImageButton)findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                search();
            }
        });
        

        mButtonSortBy.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogSortBy dlg = new DialogSortBy(StorageActivity.this, 
                        new SortOrder[] { SortOrder.Name,
                        SortOrder.FileType, SortOrder.Size, 
                        SortOrder.CreationTime, },
                        StorageActivity.SortPolicy, AscOrder);

                dlg.setOnSortByListener(new DialogSortBy.OnSortByListener()
                {

                    @Override
                    public void onSortBy(SortOrder order, AscDescOrder ascOrder)
                    {
                        mAdapter.sortItems(order, ascOrder);
                        StorageActivity.SortPolicy = order;
                        for(GridItemData data : mAdapter.getItems()){
                        	if(data instanceof GoUpLevelItem){
                        		mGoUpItem = data;
                        		break;
                        	}
                        }
                        mAdapter.getItems().remove(mGoUpItem);
                        mAdapter.getItems().add(0,mGoUpItem);
                        
                        StorageActivity.AscOrder = ascOrder;
                        EpdController.invalidate(mFileGridView, EpdController.UpdateMode.GU);
                    }
                });

                WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
                lp.x = (int)StorageActivity.this.getResources().getDimension(R.dimen.storage_sort_dialog_position_x);
                lp.y = -(int)StorageActivity.this.getResources().getDimension(R.dimen.storage_sort_dialog_position_y);
                dlg.getWindow().setAttributes(lp);
                dlg.show();
            }
        });

        if (StorageActivity.ViewMode == GridViewMode.Detail) {
        	mButtonChangeView.setImageResource(R.drawable.button_grid);
        }
        else {
            mButtonChangeView.setImageResource(R.drawable.button_list);
        }

        mButtonChangeView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (StorageActivity.this.getGridView().getPagedAdapter().getPageLayout().getViewMode() == GridViewMode.Thumbnail) {
                    StorageActivity.this.changeViewMode(GridViewMode.Detail);
                    mButtonChangeView.setImageResource(R.drawable.button_grid);
                }
                else {
                    StorageActivity.this.changeViewMode(GridViewMode.Thumbnail);
                    mButtonChangeView.setImageResource(R.drawable.button_list);
                }
            }
        });

        mImageViewPathIndicator.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                GridItemBaseAdapter adapter = (GridItemBaseAdapter)StorageActivity.this.getGridView().getPagedAdapter();
                OnyxItemURI u = adapter.getHostURI();
                new DialogPathIndicator(StorageActivity.this, GridItemManager.getStorageURI(), u).show();
            }
        });

        mTextViewPathIndicator.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                GridItemBaseAdapter adapter = (GridItemBaseAdapter)StorageActivity.this.getGridView().getPagedAdapter();
                OnyxItemURI u = adapter.getHostURI();
                new DialogPathIndicator(StorageActivity.this, GridItemManager.getStorageURI(), u).show();
            }
        });

        this.getGridView().setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                Log.d(TAG, "onItemStarting");
                if (mAdapter.isInMultipleSelectionMode()) {
                    if (view.getTag() instanceof GoUpLevelItem) {
                        return;
                    }

                    mAdapter.addSelectedItems((GridItemData)view.getTag());
                }
                else {
                    if (mAdapter.getSelectedItems().contains((GridItemData)view.getTag())) {
                        return;
                    }

                    StorageActivity.this.startGridViewItem(view);
                }
            }
        });

        mSDCardUnmountedReceiver = new BroadcastReceiver()
        {

            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.d(TAG, "receive broadcast: " + intent.getAction() + ", " + intent.getDataString());

                String sd_path = FileUtil.getFilePathFromUri(intent.getDataString());

                GridItemBaseAdapter adapter = (GridItemBaseAdapter)StorageActivity.this.getGridView().getPagedAdapter();

                File current_host_folder = GridItemManager.getFileFromURI(adapter.getHostURI());
                if (current_host_folder.getAbsolutePath().startsWith(sd_path)) {
                    StorageActivity.this.finish();
                }
            }
        };

        registerReceiver(this.mSDCardUnmountedReceiver, IntentFilterFactory.getSDCardUnmountedFilter());

        mAdapter = new StorageAdapter(this, this.getGridView());
        mAdapter.getPageLayout().setViewMode(StorageActivity.ViewMode);
        this.getGridView().setAdapter(mAdapter);

        mFileOperationHandler = new FileOperationHandler(this, mAdapter) {
            @Override
            public void onCopy()
            {
                super.onCopy();
                mFileGridView.onPreparePaste();
            }
            
            @Override
            public void onCut()
            {
                super.onCut();
                mFileGridView.onPreparePaste();
            }
        };

        if (CopyService.getSourceItems() != null) {
            mFileGridView.onPreparePaste();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVE_INSTANCE_URI)) {
			mStartingURI = GridItemManager.getURIFromFilePath((new File(savedInstanceState.getString(SAVE_INSTANCE_URI)).getPath()));
		}

        if (this.handleNewIntent()) {
            if (savedInstanceState != null && savedInstanceState.containsKey(SAVE_INSTANCE_PAGE_INDEX)) {
                int page_index = savedInstanceState.getInt(SAVE_INSTANCE_PAGE_INDEX);
                if (page_index < mAdapter.getPaginator().getPageCount()) {
                    mAdapter.getPaginator().setPageIndex(page_index);
                }
            }
        }
        this.initGridViewItemNavigation();
        this.registerLongPressListener();
    }
    
    private void search()
    {
//        String query = mSearchEditText.getText().toString();
//        
//        OnyxItemURI uri = StorageActivity.this.mAdapter.getHostURI();
//        SearchResultActivity.startSearch(uri, query, StorageActivity.this);        
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        Log.d(TAG, "onNewIntent");
        
        mStartingURI = null;
        
        this.setIntent(intent);
        this.handleNewIntent();
    }
    
    @Override
    protected void onResume()
    {
        Log.d(TAG, "onResume");
        
        super.onResume();
        
    }

    @Override
    protected void onDestroy()
    {
        Log.d(TAG, "onDestroy");

        if (mSDCardUnmountedReceiver != null) {
            unregisterReceiver(mSDCardUnmountedReceiver);
        }
        
        super.onDestroy();

        Log.d(TAG, "onDestroy finished");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if (((OnyxFileGridView)findViewById(R.id.gridview_storage)).getButtonCancel().isFocused() == true
    			|| ((OnyxFileGridView)findViewById(R.id.gridview_storage)).getButtonCopy().isFocused() == true
    			|| ((OnyxFileGridView)findViewById(R.id.gridview_storage)).getButtonCut().isFocused() == true
    			|| ((OnyxFileGridView)findViewById(R.id.gridview_storage)).getButtonDelete().isFocused() == true
    			|| ((OnyxFileGridView)findViewById(R.id.gridview_storage)).getButtonPaste().isFocused() == true) {
    		EpdController.invalidate(((OnyxFileGridView)findViewById(R.id.gridview_storage)), UpdateMode.GU);
    	}

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mIsLongClickItem = false;
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mAdapter.isInMultipleSelectionMode()) {
                mFileGridView.onCancelMultipleSelection();
                return true;
            }

            GridItemBaseAdapter adapter = (GridItemBaseAdapter)this.getGridView().getPagedAdapter();
            if (adapter.getHostURI().isChildOf(GridItemManager.getStorageURI())) {
                this.startURI(adapter.getHostURI().getParent());
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
    
    private boolean handleNewIntent()
    {
        String root_uri_string = this.getIntent().getStringExtra(STARTING_URI);
        OnyxItemURI uri = OnyxItemURI.createFromString(root_uri_string);
        if (uri == null) {
            uri = GridItemManager.getStorageURI();
        }
        
        if (!uri.equals(GridItemManager.getStorageURI()) &&
                !uri.isChildOf(GridItemManager.getStorageURI())) {
            assert(false);
            throw new RuntimeException();
        }

        if (mStartingURI == null) {
        	mStartingURI = uri;
		}
        return GridItemManager.processURI(this.getGridView(), mStartingURI, this);
    }

    private boolean startGridViewItem(View itemView)
    {
        GridItemData item = (GridItemData)itemView.getTag();

        // db operating may fail, but should not interfere with item opening function
        if (this.startURI(item.getURI())) {
            return true;
        }
        
        return false;
    }

    public boolean startURI(OnyxItemURI uri)
    {
        GridItemBaseAdapter adapter = (GridItemBaseAdapter)this.getGridView().getPagedAdapter();

        if (!uri.equals(adapter.getHostURI())) {
            if (GridItemManager.getStorageURI().isChildOf(uri)) {
                this.finish();
                return true;
            }
            
            OnyxItemURI old_uri = mAdapter.getHostURI();
            boolean res = GridItemManager.processURI(this.getGridView(), uri, this);
            if (!res) {
                return false;
            }
            
            if ((uri.equals(GridItemManager.getStorageURI()) || uri.isChildOf(GridItemManager.getStorageURI())) && 
                    GridItemManager.isItemContainer(uri)) {
                if (uri.isChildOf(old_uri)) {
                    if (this.getGridView().getCount() > 0) {
                        this.getGridView().setSelection(0);
                    }
                }
                else if (old_uri.isChildOf(uri)) {
                    OnyxItemURI old_uri_parent_in_current_host_uri = new OnyxItemURI(old_uri.getPathLevels().subList(0, 
                            uri.getPathLevels().size() + 1));
                    mAdapter.locateItemInGridView(old_uri_parent_in_current_host_uri);
                }
            }
            
            return true;
        }

        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	OnyxItemURI item_uri = mAdapter.getHostURI();
    	String uri = GridItemManager.getFileFromURI(item_uri).getPath();
    	outState.putString(SAVE_INSTANCE_URI, uri);
    	outState.putInt(SAVE_INSTANCE_PAGE_INDEX, mAdapter.getPaginator().getPageIndex());
    	super.onSaveInstanceState(outState);
    }

    public void registerLongPressListener() {
        if (this.getGridView() == null) {
            assert(false);
            return;
        }

        this.getGridView().setOnItemLongClickListener(new OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                return true;
            }
        });
        this.getGridView().registerOnLongPressListener(new OnyxGridView.OnLongPressListener()
        {

            @Override
            public void onLongPress()
            {
                mIsLongClickItem = false;

                if (mAdapter.isInMultipleSelectionMode() && (mAdapter.getSelectedItems().size() > 0)
                        && getSelectedGridItem() != null) {
                    ArrayList<FileItemData> items = new ArrayList<FileItemData>(mAdapter.getSelectedItems().size());
                    for (GridItemData i : mAdapter.getSelectedItems()) {
                        items.add((FileItemData) i);
                    }
                    mFileOperationHandler.setSourceItems(items);
                }
                if (getSelectedGridItem() != null && (getSelectedGridItem() instanceof FileItemData)) {
                    ArrayList<FileItemData> items = new ArrayList<FileItemData>();
                    items.add((FileItemData) getSelectedGridItem());
                    mFileOperationHandler.setSourceItems(items);

                    mIsLongClickItem = true;
                }

                StorageActivity.this.openOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.storage_option_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if (mAdapter.isInMultipleSelectionMode()) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setEnabled(false);
            }
            return true;
        }

        if (mIsLongClickItem) {
            boolean isDirectory = false;
            if (mFileOperationHandler.getSourceItems().get(0).isDirectory()) {
                isDirectory = true;
            }

            for (int i = 0; i < menu.size(); i++) {
                if (isDirectory && (menu.getItem(i).getItemId() == R.id.menu_open_with)) {
                    menu.getItem(i).setEnabled(false);
                    continue;
                }
                if (isDirectory && (menu.getItem(i).getItemId() == R.id.menu_property)) {
                    menu.getItem(i).setEnabled(false);
                    continue;
                }
                menu.getItem(i).setEnabled(true);
            }
        }
        else {
            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i).getItemId() == R.id.menu_new_file) {
                    continue;
                }
                if (menu.getItem(i).getItemId() == R.id.menu_new_folder) {
                    continue;
                }
                if (menu.getItem(i).getItemId() == R.id.menu_select_mutiple) {
                    continue;
                }
                menu.getItem(i).setEnabled(false);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
        case R.id.menu_new_file:
            mFileOperationHandler.onNewFile();
            return true;
        case R.id.menu_new_folder:
            mFileOperationHandler.onNewFolder();
            return true;
        case R.id.menu_rename:
            mFileOperationHandler.onRename();
            return true;
        case R.id.menu_copy:
            mFileOperationHandler.onCopy();
            return true;
        case R.id.menu_cut:
            mFileOperationHandler.onCut();
            return true;
        case R.id.menu_delete:
            mFileOperationHandler.onRemove();
            return true;
        case R.id.menu_property:
            mFileOperationHandler.onProperty();
            return true;
        case R.id.menu_open_with:
            mFileOperationHandler.onShowOpenWith();
            return true;
        case R.id.menu_select_mutiple:
            if (!mAdapter.isInMultipleSelectionMode()) {
                mAdapter.setMultipleSelectionMode(true);
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}