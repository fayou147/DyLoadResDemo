package com.fy.administrator.dyloadresdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fy.administrator.util.DuduUtil;
import com.fy.administrator.util.DynamicLoadUtil;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/3/31.
 */
public class ConstructAsyncTask extends AsyncTask<Void, Void, Boolean> {
    WeakReference<LoadPluginsActivity> context;

    public ConstructAsyncTask( WeakReference<LoadPluginsActivity> context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        final LoadPluginsActivity activity = context.get();
        if (activity == null) {
            return false;
        }
        File tmpDir = activity.getDir("dex", Context.MODE_PRIVATE);
        DynamicLoadUtil.copy(DuduUtil.mSaveDirPath, tmpDir.getAbsolutePath(), "ResApk.apk");
        if (isCancelled()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        final LoadPluginsActivity activity = context.get();
        if (activity == null) {
            return;
        }
        if (aBoolean) {
            Toast.makeText(activity,"ddddddddddddddddd",Toast.LENGTH_SHORT).show();
            activity.gotoActivity(MainActivity.class);
        }
    }
}
