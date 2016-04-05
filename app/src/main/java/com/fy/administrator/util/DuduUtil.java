package com.fy.administrator.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/3/30.
 */
public class DuduUtil {
    public static class SoFile{
        public static final String IJKSDL = "ijksdl";
        public static final String IJKFFMPEG = "ijkffmpeg";
    }

    public static class ApkFile {
        public static final String RESAPK = "ResApk";
    }

    public static final String mSaveDirPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "youan" ;
    public static final String ZIP_NAME = "jniLibs.zip";
    public static String resApkPath = mSaveDirPath + File.separator + "ResApk.apk";
    public static String BASE_URL = "http://wifiup.ggsafe.com/jniLibs";


    public static String getCPU() {
        Process process = null;
        String abi = "armeabi";
        InputStreamReader ir = null;
        BufferedReader input = null;

        try {
            process = Runtime.getRuntime().exec("getprop ro.product.cpu.abi");
            ir = new InputStreamReader(process.getInputStream());
            input = new BufferedReader(ir);
            abi = input.readLine();
            return abi;
        } catch (Exception var24) {
            ;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception var23) {
                    ;
                }
            }

            if (ir != null) {
                try {
                    ir.close();
                } catch (Exception var22) {
                    ;
                }
            }

            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception var21) {
                    ;
                }
            }

        }

        return abi;
    }
}
