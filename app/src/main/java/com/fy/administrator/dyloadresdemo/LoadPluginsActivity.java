package com.fy.administrator.dyloadresdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fy.administrator.util.DuduUtil;
import com.fy.administrator.util.DynamicLoadUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;

public class LoadPluginsActivity extends AppCompatActivity {
    private ConstructAsyncTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_plugins);
        if (DynamicLoadUtil.isLoadResFile(this)) {
            gotoActivity(MainActivity.class);
        } else {
            downloadPlugins();
        }
    }


    private void downloadPlugins() {
        task = new ConstructAsyncTask(new WeakReference<LoadPluginsActivity>(this));
        task.executeOnExecutor(Executors.newSingleThreadExecutor());
        //下载

    }

    public void gotoActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

}
