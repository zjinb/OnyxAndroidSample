/**
 * 
 */
package com.onyx.android.sample.dialog;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.onyx.android.sample.R;
import com.onyx.android.sample.dialog.DialogApplicationOpenList.OnApplicationSelectedListener;
import com.onyx.android.sdk.data.util.ActivityUtil;
import com.onyx.android.sdk.ui.dialog.OnyxDialogBase;

/**
 * @author qingyue
 *
 */
public class DialogOpenWith extends OnyxDialogBase
{
    private Button mButtonText = null;
    private Button mButtonAudio = null;
    private Button mButtonVideo = null;
    private Button mButtonImage = null;
    private Activity mActivity = null;
    private File mFile = null;

    public DialogOpenWith(Activity activity, File file)
    {
        super(activity);

        DialogOpenWith.this.setContentView(R.layout.dialog_open_with);

        mActivity = activity;
        mFile = file;

        mButtonText = (Button) findViewById(R.id.button_text);
        mButtonAudio = (Button) findViewById(R.id.button_audio);
        mButtonVideo = (Button) findViewById(R.id.button_video);
        mButtonImage = (Button) findViewById(R.id.button_image);

        mButtonText.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                openWith(mButtonText.getText().toString().toLowerCase()+"/*");
            }
        });

        mButtonAudio.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                openWith(mButtonAudio.getText().toString().toLowerCase()+"/*");
            }
        });

        mButtonVideo.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                openWith(mButtonVideo.getText().toString().toLowerCase()+"/*");
            }
        });

        mButtonImage.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                openWith(mButtonImage.getText().toString().toLowerCase()+"/*");
            }
        });

        Button button_cancel = (Button) findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogOpenWith.this.dismiss();
            }
        });
    }

    private void openWith(String type)
    {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mFile), type);

        List<ResolveInfo> info_list = mActivity.getPackageManager()
                .queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        intent.setData(Uri.fromFile(mFile));

        if (info_list.size() <= 0) {
            Toast.makeText(mActivity,
                    R.string.unable_to_open_this_type_of_file,
                    Toast.LENGTH_SHORT).show();
        }
        else {
            DialogApplicationOpenList dlg = new DialogApplicationOpenList(
                    mActivity, info_list, mFile, true);
            dlg.setOnApplicationSelectedListener(new OnApplicationSelectedListener()
            {

                @Override
                public void onApplicationSelected(ResolveInfo info,
                        boolean makeDefault)
                {
                    ActivityUtil.startActivitySafely(mActivity,
                            intent, info.activityInfo);
                }

            });
            dlg.show();
        }

        DialogOpenWith.this.dismiss();
    }
}
