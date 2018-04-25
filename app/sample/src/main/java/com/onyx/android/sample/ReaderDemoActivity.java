package com.onyx.android.sample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/25 17:23.
 */
public class ReaderDemoActivity extends Activity {
    @Bind(R.id.et_file)
    EditText etFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_demo);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_open)
    public void btn_open() {
        String filePath = etFile.getText().toString();
        if ( "".equals(filePath) ) {
            Toast.makeText(this,"请输入您要打开的文档路径",Toast.LENGTH_SHORT).show();
            return;
        }
        File f = new File(filePath);
        if ( !f.exists() ) {
            Toast.makeText(this,"该文件不存在，请输入一个存在的文件路径",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ComponentName componentName = new ComponentName("com.onyx.kreader","com.onyx.kreader.ui.ReaderTabHostActivity");
        intent.setComponent(componentName);
        intent.setData(Uri.fromFile(f));
        startActivity(intent);
    }
}
