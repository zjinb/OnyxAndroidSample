/**
 * 
 */
package com.onyx.android.sample.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.onyx.android.sample.R;
import com.onyx.android.sample.adapter.GridItemBaseAdapter;
import com.onyx.android.sample.dialog.DialogApplicationOpenList;
import com.onyx.android.sample.dialog.DialogApplicationOpenList.OnApplicationSelectedListener;
import com.onyx.android.sample.dialog.DialogOpenWith;
import com.onyx.android.sdk.data.OnyxItemURI;
import com.onyx.android.sdk.data.sys.OnyxAppPreference;
import com.onyx.android.sdk.data.sys.OnyxAppPreferenceCenter;
import com.onyx.android.sdk.data.util.ActivityUtil;
import com.onyx.android.sdk.data.util.FileUtil;
import com.onyx.android.sdk.device.EnvironmentUtil;
import com.onyx.android.sdk.ui.OnyxGridView;
import com.onyx.android.sdk.ui.data.BookItemData;
import com.onyx.android.sdk.ui.data.FileItemData;
import com.onyx.android.sdk.ui.data.FileItemData.FileType;
import com.onyx.android.sdk.ui.data.GridItemData;

/**
 * @author joy
 *
 */
public class GridItemManager
{
    private static final String TAG = "GridItemManager";
    
    public static class GoUpLevelItem extends GridItemData
    {
        public GoUpLevelItem(OnyxItemURI uri, int textId, int imageResourceId)
        {
            super(uri, textId, imageResourceId);
        }

    }
    
    private static final GoUpLevelItem sGoUpItem = new GoUpLevelItem(null,
            R.string.Go_up, R.drawable.go_up);
    
    private static final OnyxItemURI sStorageRootURI = OnyxItemURI.ROOT.append("Storage");
    
    public static OnyxItemURI getStorageURI()
    {
        return sStorageRootURI;
    }
    
    /**
     * never return null;
     * 
     * @param uri
     * @return
     */
    public static File getFileFromURI(OnyxItemURI uri)
    {
        File file_root = EnvironmentUtil.getExternalStorageDirectory();

        OnyxItemURI storage_root_uri = getStorageURI();

        if (!uri.equals(storage_root_uri) && !uri.isChildOf(storage_root_uri)) {
            throw new IllegalArgumentException();
        }

        StringBuilder sb = new StringBuilder(file_root.getAbsolutePath());
        int uri_level_size = uri.getPathLevels().size();
        final char seperator = '/';
        for (int i = storage_root_uri.getPathLevels().size(); i < uri_level_size; i++) {
            sb.append(seperator).append(uri.getPathLevels().get(i));
        }

        return new File(sb.toString());
    }
    
    /**
     * return null when failed
     * 
     * @param filePath
     * @return
     */
    public static OnyxItemURI getURIFromFilePath(String filePath)
    {
        String root_path = getFileFromURI(getStorageURI()).getAbsolutePath();
        
        if (!filePath.startsWith(root_path)) {
            Log.w(TAG, "file not in root directory: (file)" + filePath + ", (root)" + root_path);
            return null;
        }

        final char seperator = '/';
        
        if ((filePath.length() > 0) && (filePath.charAt(filePath.length() - 1) == seperator)) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        
        String postfix = filePath.substring(root_path.length());
        if ((postfix.length() > 0) && (postfix.charAt(0) == seperator)) {
            postfix = postfix.substring(1);
        }
        if (postfix.length() == 0) {
            return getStorageURI();
        }
        
        OnyxItemURI uri = (OnyxItemURI)getStorageURI().clone();
        
        String[] array = postfix.split(String.valueOf(seperator)); 
        for (String s : array) {
            uri.append(s);
        }
        
        return uri;
    }
    
    public static boolean isItemContainer(OnyxItemURI uri)
    {
        File f = getFileFromURI(uri);
        return f.isDirectory();
    }
    
    public static boolean processURI(OnyxGridView gridView, OnyxItemURI uri, Activity hostActivity)
    {
        GridItemBaseAdapter adapter = (GridItemBaseAdapter) gridView
                .getPagedAdapter();

        File file = getFileFromURI(uri);
        if (file == null) {
            return false;
        }
        Log.d(TAG, "file not null");

        if (!file.exists()) {
            if (EnvironmentUtil.isFileOnRemovableSDCard(file)
                    && !Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                Toast.makeText(hostActivity, R.string.SD_card_has_been_removed,
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(hostActivity,
                        hostActivity.getResources().getString(R.string.file_not_exist) + " " + file.getAbsolutePath(),
                        Toast.LENGTH_SHORT).show();
            }

            return false;
        }
        Log.d(TAG, "file exist");

        if (file.isDirectory()) {
            Log.d(TAG, "file is directory");

            ArrayList<GridItemData> dir_list = new ArrayList<GridItemData>();
            ArrayList<GridItemData> file_list = new ArrayList<GridItemData>();

            File[] files = file.listFiles();
            if (files != null) {
                Log.d(TAG, "child not empty");

                for (File f : files) {
                    if (f.isHidden()) {
                        continue;
                    }

                    OnyxItemURI copy_uri = ((OnyxItemURI) uri.clone()).append(f
                            .getName());

                    if (f.isDirectory()) {
                        dir_list.add(new FileItemData(copy_uri,
                                FileType.Directory, f.getName(),
                                R.drawable.directory));
                    }
                    else {
                        Bitmap icon = FileIconFactory.getIconByFileName(hostActivity, f.getName());
                        file_list.add(new BookItemData(copy_uri,
                                FileType.NormalFile, f.getName(), icon));
                    }
                }
            }

            Log.d(TAG, "fill items");
            sGoUpItem.setURI(uri.getParent());
            adapter.fillItems(uri, new GridItemData[] { sGoUpItem });
            adapter.appendItems(dir_list);
            adapter.appendItems(file_list);
            adapter.getItems().remove(sGoUpItem);
            adapter.getItems().add(0, sGoUpItem);
            return true;
        }
        else {
            Log.d(TAG, "try openning file: " + file.getAbsolutePath());

            final Intent intent = new Intent();
            intent.setData(Uri.fromFile(file));
            intent.setAction(Intent.ACTION_VIEW);

            ActivityInfo app_info = null;
            OnyxAppPreference app_preference = OnyxAppPreferenceCenter
                    .getApplicationPreference(hostActivity, file);
            if (app_preference != null) {
                try {
                    app_info = hostActivity.getPackageManager()
                            .getActivityInfo(
                                    new ComponentName(
                                            app_preference.getAppPackageName(),
                                            app_preference.getAppClassName()),
                                    0);
                }
                catch (NameNotFoundException e) {
                    Log.i(TAG, app_preference.getAppName() + " not found");
                    app_info = null;
                }
            }

            if (app_info != null) {
                return ActivityUtil.startActivitySafely(hostActivity, intent,
                        app_info);
            }
            else {
                String type = MimeTypeMap.getSingleton()
                        .getMimeTypeFromExtension(
                                FileUtil.getFileExtension(file));

                if (type != null) {
                    intent.setDataAndType(Uri.fromFile(file), type);
                }
                else {
                    intent.setData(Uri.fromFile(new File(file.getParent() + File.separator +
                            "file_name." + FileUtil.getFileExtension(file))));
                }

                List<ResolveInfo> info_list = hostActivity.getPackageManager()
                        .queryIntentActivities(intent,
                                PackageManager.MATCH_DEFAULT_ONLY);

                intent.setData(Uri.fromFile(file));

                if (info_list.size() <= 0) {
                    DialogOpenWith dialogOpenWith = new DialogOpenWith(hostActivity, file);
                    dialogOpenWith.show();
                }
                // TODO temporarily use first app as default
//                else if (info_list.size() == 1) { 
                else if (info_list.size() >= 1) {
                    ResolveInfo info = info_list.get(0);

                    String ext = FileUtil.getFileExtension(file);
                    if (ext.equalsIgnoreCase("epub") || ext.equalsIgnoreCase("pdf")) {
                        final String onyx_reader = "com.onyx.android.reader";
                        int size = info_list.size();
                        for (int i = 0; i < size; i++) {
                            if (info_list.get(i).activityInfo.packageName.equals(onyx_reader)) {
                                info = info_list.get(i);
                                break;
                            }
                        }
                    }
                    else if (ext.equalsIgnoreCase("txt") || ext.equalsIgnoreCase("fb2") || ext.equalsIgnoreCase("mobi")) {
                        final String fb_reader = "org.geometerplus.zlibrary.ui.android";
                        int size = info_list.size();
                        for (int i = 0; i < size; i++) {
                            if (info_list.get(i).activityInfo.packageName.equals(fb_reader)) {
                                info = info_list.get(i);
                                break;
                            }
                        }
                    }

                    return ActivityUtil.startActivitySafely(hostActivity,
                            intent, info.activityInfo);
                }
                else {
                    assert (info_list.size() > 1);

                    final Activity host_activity = hostActivity;

                    DialogApplicationOpenList dlg = new DialogApplicationOpenList(
                            hostActivity, info_list, file, false);
                    dlg.setOnApplicationSelectedListener(new OnApplicationSelectedListener()
                    {

                        @Override
                        public void onApplicationSelected(ResolveInfo info,
                                boolean makeDefault)
                        {
                            ActivityUtil.startActivitySafely(host_activity,
                                    intent, info.activityInfo);
                        }

                    });

                    dlg.show();

                    return true;
                }

            }
        }

        return false;
    }
}
