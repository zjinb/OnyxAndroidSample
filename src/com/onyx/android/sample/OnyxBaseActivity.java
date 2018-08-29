/**
 * 
 */
package com.onyx.android.sample;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.onyx.android.sample.adapter.GridItemBaseAdapter;
import com.onyx.android.sdk.data.util.ActivityUtil;
import com.onyx.android.sdk.device.EpdController;
import com.onyx.android.sdk.device.EpdController.UpdateMode;
import com.onyx.android.sdk.ui.OnyxGridView;
import com.onyx.android.sdk.ui.data.GridItemData;
import com.onyx.android.sdk.ui.data.GridViewPageLayout.GridViewMode;
import com.onyx.android.sdk.ui.data.SystemMenuFactory;
import com.onyx.android.sdk.ui.dialog.DialogContextMenu;
import com.onyx.android.sdk.ui.dialog.DialogScreenRotation;
import com.onyx.android.sdk.ui.menu.OnyxMenuSuite;
import com.onyx.android.sdk.ui.util.OnyxFocusFinder;

/**
 * a OnysBaseActivity must have a OnyxGridView as main GridView
 * 
 * @author joy
 *
 */
public abstract class OnyxBaseActivity extends Activity
{
    private static final String TAG = "OnyxBaseActivity";

    /**
     * get Activity's main GridView
     * 
     * @return
     */
    public abstract OnyxGridView getGridView();

    /**
     * get current selected item, null stands for non-selection
     * 
     * @return
     */
    public GridItemData getSelectedGridItem()
    {
        if (this.getGridView().getSelectedView() != null) {
            return (GridItemData)this.getGridView().getSelectedView().getTag();
        }
        return null;
    }

    public void changeViewMode(GridViewMode viewMode)
    {
        EpdController.invalidate(this.getGridView(), UpdateMode.GC);
        if (this.getGridView().getPagedAdapter().getPageLayout().getViewMode() != viewMode) {
            this.getGridView().getPagedAdapter().getPageLayout().setViewMode(viewMode);

            if (viewMode == GridViewMode.Thumbnail) {
                this.getGridView().setNumColumns(GridView.AUTO_FIT);
            } else {
                assert (viewMode == GridViewMode.Detail);
                this.getGridView().setNumColumns(1);
            }
        }
    }
    
    public void registerLongPressListener()
    {
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
                ArrayList<OnyxMenuSuite> suites = OnyxBaseActivity.this.getContextMenuSuites();
                OnyxMenuSuite system_menu = OnyxBaseActivity.this.getContextSystemMenuSuite();
                new DialogContextMenu(OnyxBaseActivity.this, suites, system_menu).show();
            }
        });
    }
    
    /**
     * should be light weight method
     * 
     * never return null, when return empty collection, means there is no context menu in this activity
     * 
     * @return
     */
    public ArrayList<OnyxMenuSuite> getContextMenuSuites()
    {
        ArrayList<OnyxMenuSuite> suites = new ArrayList<OnyxMenuSuite>();
        return suites;
    }

    public OnyxMenuSuite getContextSystemMenuSuite()
    {
        return SystemMenuFactory.getAllSystemMenuSuite(this);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	Log.d(TAG, "onCreate");
    	
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onStart()
    {
    	Log.d(TAG, "onStart");
    	
        super.onStart();
    }
    
    @Override
    protected void onResume() {
    	Log.d(TAG, "onResume");

    	super.onResume();
    }
    
    @Override
    protected void onStop()
    {
    	Log.d(TAG, "onStop");
    	
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Log.d(TAG, "onDestory called on thread: " + Thread.currentThread().getName());

        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        Log.d(TAG, "onSaveInstanceState");
        
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (this.getGridView() != null) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                if (this.getGridView().getPagedAdapter().getPaginator().canPrevPage()) {
                    EpdController.invalidate(this.getGridView(), UpdateMode.GU);
                    this.getGridView().getPagedAdapter().getPaginator().prevPage();
                }
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                if (this.getGridView().getPagedAdapter().getPaginator().canNextPage()) {
                    EpdController.invalidate(this.getGridView(), UpdateMode.GU);
                    this.getGridView().getPagedAdapter().getPaginator().nextPage();
                }
                return true;
            }
        }
        
        if ((keyCode == KeyEvent.KEYCODE_DPAD_UP) || 
                (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) ||
                (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) ||
                (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
            EpdController.invalidate(this.getWindow().getDecorView(), UpdateMode.GU_FAST);
            
            if (this.getCurrentFocus() != null) {
                int direction = 0;
                switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    direction = View.FOCUS_UP;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    direction = View.FOCUS_DOWN;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    direction = View.FOCUS_LEFT;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    direction = View.FOCUS_RIGHT;
                    break;
                default:
                    assert(false);
                    throw new IndexOutOfBoundsException();
                }

                View dst_view = this.getCurrentFocus().focusSearch(direction);
                if (dst_view == null) { 
                    int reverse_direction = OnyxFocusFinder.getReverseDirection(direction);
                    dst_view = OnyxFocusFinder.findFartherestViewInDirection(this.getCurrentFocus(), reverse_direction);

                    Rect rect = OnyxFocusFinder.getAbsoluteFocusedRect(this.getCurrentFocus());

                    if (dst_view instanceof OnyxGridView) {
                        // simply requestFocus() wont work here, use the method provided by OnyxGridView instead
                        ((OnyxGridView)dst_view).searchAndSelectNextFocusableChildItem(direction, rect);
                    }
                    else {
                        dst_view.requestFocus(direction, rect);
                    }

                    return true;
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.home_option_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        ArrayList<OnyxMenuSuite> suites = this.getContextMenuSuites();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.home_option_menu_other) {
                if (suites == null || suites.size() == 0) {
                    menu.getItem(i).setEnabled(false);
                }
                else {
                    menu.getItem(i).setEnabled(true);
                }
            }
        }

        if (OnyxBaseActivity.this.getWindow().getDecorView().findFocus() != null) {
            OnyxBaseActivity.this.getWindow().getDecorView().findFocus().requestFocusFromTouch();
        }

        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public void openOptionsMenu()
    {
        // TODO Auto-generated method stub
        super.openOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
        case R.id.home_option_menu_screen_rotation:
        	DialogScreenRotation rotation = new DialogScreenRotation(this);
        	rotation.show();
            return true;
        case R.id.home_option_menu_storage_settings:
            this.mountSdCard();
            return true;
        case R.id.home_option_menu_other:
            ArrayList<OnyxMenuSuite> suites = this.getContextMenuSuites();
            OnyxMenuSuite system_menu = this.getContextSystemMenuSuite();
            Log.d(TAG, "menu suites: " + suites.size());
            new DialogContextMenu(this, suites, system_menu).show();
            return true;
        case R.id.home_option_menu_search:
            this.onSearchRequested();
            return true;
        case R.id.home_option_menu_exit:
            this.finish();
            return true;
        case R.id.home_option_menu_select_mutiple:
            GridItemBaseAdapter adapter = (GridItemBaseAdapter)this.getGridView().getPagedAdapter();
            if (!adapter.isInMultipleSelectionMode()) {
                adapter.setMultipleSelectionMode(true);
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSearchRequested()
    {
        if (this.getGridView().getPagedAdapter() instanceof GridItemBaseAdapter) {
//            GridItemBaseAdapter adapter = (GridItemBaseAdapter)this.getGridView().getPagedAdapter();            
//            Bundle app_data = new Bundle();
//            app_data.putString(SearchResultActivity.HOST_URI, adapter.getHostURI().toString());
//            this.startSearch(null, false, app_data, false);
            return true;
        }
        else {
            assert(false);
            return false;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id) {
        case 0:
        default:
            return super.onCreateDialog(id);
        }
    }

    protected void disabledMenuMultiple(Menu menu)
    {
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.home_option_menu_select_mutiple) {
                menu.getItem(i).setEnabled(false);
            }
        }
    }

    protected void disabledMenuScreenRotation(Menu menu)
    {
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.home_option_menu_screen_rotation) {
                menu.getItem(i).setEnabled(false);
            }
        }
    }

    protected void initGridViewItemNavigation()
    {
        this.getGridView().setCrossVertical(true);
        this.getGridView().setCrossHorizon(false);
    }

    private void mountSdCard()
    {
        Intent i = new Intent(android.provider.Settings.ACTION_MEMORY_CARD_SETTINGS);
        ActivityUtil.startActivitySafely(this, i);
    }
}
