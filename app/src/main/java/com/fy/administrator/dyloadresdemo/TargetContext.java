package com.fy.administrator.dyloadresdemo;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.view.LayoutInflater;

import com.fy.administrator.util.DuduUtil;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/3/29.
 */
public class TargetContext extends ContextWrapper {
    private static final String TAG = "TargetContext";
    AssetManager mAssetManager = null;
    Resources mResources = null;
    LayoutInflater mLayoutInflater = null;
    Theme mTheme = null;
    ClassLoader mClassLoader = null;

    String packageName="com.example.resapk";

    protected void loadResources(File tmpDir) {
        try {
            File[] currentFiles=tmpDir.listFiles();
            String path;
            if (currentFiles == null || currentFiles.length == 0) {
                path = DuduUtil.resApkPath;
            } else {
                path = currentFiles[0].getAbsolutePath();
            }
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod(
                    "addAssetPath", String.class);//通过methodName和参数的数据类型得到要执行的Method。
            addAssetPath.invoke(assetManager, path);//assetManager对象中带有参数path的addAssetPath方法。返回值是Object，也既是该方法的返回值
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
                superRes.getConfiguration());
    }

    public TargetContext(Context base, File tmpDir) {
        super(base);
        //耗时
//        PackageInfo pkgInfo = DynamicLoadUtil.getPackageInfo(base, libPath);
//        if (pkgInfo != null) {
//            // Workaround for http://code.google.com/p/android/issues/detail?id=9151
//            ApplicationInfo appInfo = pkgInfo.applicationInfo;
//            if (Build.VERSION.SDK_INT >= 8) {
//                appInfo.sourceDir = libPath;
//                appInfo.publicSourceDir = libPath;
//            }
//            packageName = appInfo.packageName;
//        }
        loadResources(tmpDir);
    }

    public void setContext(ClassLoader loader) {
        mClassLoader = loader;
    }

    @Override
    public Resources getResources() {
        return mResources;
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager;
    }

//    @Override
//    public ClassLoader getClassLoader() {
//        if(mClassLoader == null) {
//            File tmpDir = getDir("dex", 0);
//            mClassLoader = new DexClassLoader(DuduUtil.resApkPath, tmpDir.getAbsolutePath(),
//                    null, super.getClassLoader());
//        }
//
//        return mClassLoader;
//    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mLayoutInflater == null) {
                try {
                    Class<?> cls = Class
                            .forName("com.android.internal.policy.PolicyManager");
                    Method m = cls.getMethod("makeNewLayoutInflater",
                            Context.class);
                    mLayoutInflater = (LayoutInflater) m.invoke(null, this);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            if (mLayoutInflater != null) {
                return mLayoutInflater;
            }
        }
        return super.getSystemService(name);
    }

    @Override
    public Theme getTheme() {
        if (mTheme != null) {
            return mTheme;
        }

        mTheme = mResources.newTheme();
        return mTheme;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }
}
