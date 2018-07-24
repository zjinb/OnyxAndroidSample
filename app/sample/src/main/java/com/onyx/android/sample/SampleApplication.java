package com.onyx.android.sample;

import android.app.Application;

import com.onyx.android.sample.data.ScribbleDatabase;
import com.onyx.android.sdk.data.OnyxDownloadManager;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.ScribbleGeneratedDatabaseHolder;

/**
 * Created by suicheng on 2017/3/23.
 */

public class SampleApplication extends Application {
    private static SampleApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        initConfig();
        initDataProvider();
    }

    private void initConfig() {
        try {
            sInstance = this;
            initDownloadManager();
        } catch (Exception e) {
        }
    }

    private void initDataProvider() {
        FlowManager.init(FlowConfig.builder(this)
                .addDatabaseHolder(ScribbleGeneratedDatabaseHolder.class)
                .addDatabaseConfig(DatabaseConfig.builder(ScribbleDatabase.class)
                        .databaseName(ScribbleDatabase.NAME)
                        .build())
                .build());
    }

    private void initDownloadManager() {
        OnyxDownloadManager.getInstance(sInstance);
    }
}
