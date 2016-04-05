package com.fy.administrator.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/3/29.
 */
public class DynamicLoadUtil {
    private static final String TAG = "DynamicLoadUtil";
    private static String formPath = Environment.getExternalStorageDirectory() + File.separator + "youan";

    public static int copy(String fromFile, String toFile,String fileType) {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists()) {
            return -1;
        }
        Log.e(TAG,"fromFile:"+fromFile);
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();
        Log.e(TAG,"currentFiles:"+currentFiles);
        if (currentFiles==null){
            return -1;
        }
        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory()) {
                //如果当前项为子目录 进行递归
               // copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            } else {
                //如果当前项为文件则进行文件拷贝
                Log.e(TAG, "path:" + currentFiles[i].getPath());
                Log.e(TAG, "name:" + currentFiles[i].getName());
                if (currentFiles[i].getName().contains(fileType)) {
                    int id = copySdcardFile(currentFiles[i].getPath(), toFile + File.separator + currentFiles[i].getName());
                    Log.e(TAG, "id:" + id);
                }
            }
        }
        return 0;
    }


    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public static int copySdcardFile(String fromFile, String toFile) {

        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fosfrom.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            // 从内存到写入到具体文件
            fosto.write(baos.toByteArray());
            // 关闭文件流
            baos.close();
            fosto.close();
            fosfrom.close();
            return 0;

        } catch (Exception ex) {
            return -1;
        }
    }

    public static boolean isLoadResFile(Context context) {
        File tmpDir = context.getDir("dex", Context.MODE_PRIVATE);
        File[] currentFiles = tmpDir.listFiles();
        if (currentFiles == null || currentFiles.length == 0) {
            return false;
        }
        boolean hasResApk = false;
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].getName().contains(DuduUtil.ApkFile.RESAPK)) {
                hasResApk = true;
                break;
            }
        }
        return hasResApk;
    }
    public static boolean isLoadSoFile(Context context) {
        File dir = context.getDir("libs", Context.MODE_PRIVATE);
        return isLoadSoFile(dir);
    }

    public static boolean isLoadSoFile(File dir) {
        File[] currentFiles;
        currentFiles = dir.listFiles();
        boolean hasJksdl = false;
        boolean hasJkffmpeg = false;
        if (currentFiles == null) {
            return false;
        }
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].getName().contains(DuduUtil.SoFile.IJKSDL)) {
                hasJksdl = true;
            } else if (currentFiles[i].getName().contains(DuduUtil.SoFile.IJKFFMPEG)) {
                hasJkffmpeg = true;
            }
        }
        return hasJksdl && hasJkffmpeg;
    }

    public static void loadSoFile(Context context) {
        File dir = context.getDir("libs", Context.MODE_PRIVATE);
        if (!isLoadSoFile(dir)) {
            copy(formPath, dir.getAbsolutePath(), ".so");
        }
    }

    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param zipFileString 压缩包的名字
     * @param outPathString 指定的路径
     * @throws Exception
     */
    public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
        File rootFile = new File(outPathString);
        File[] childFiles = rootFile.listFiles();
        boolean hasJksdl = false;
        boolean hasJkffmpeg = false;
        boolean hasResApk = false;
        for (File file : childFiles) {
            if (file.getName().contains(DuduUtil.SoFile.IJKSDL)) {
                hasJksdl = true;
            } else if (file.getName().contains(DuduUtil.SoFile.IJKFFMPEG)) {
                hasJkffmpeg = true;
            } else if (file.getName().contains(DuduUtil.ApkFile.RESAPK)) {
                hasResApk = true;
            }
        }
        if (hasJksdl && hasJkffmpeg && hasResApk) {
            return;
        }
        java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(new FileInputStream(zipFileString));
        java.util.zip.ZipEntry zipEntry;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {

                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();

            } else {

                File file = new File(outPathString + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }//end of while

        inZip.close();

    }//end of func

    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        } catch (Exception e) {
            // should be something wrong with parse
            e.printStackTrace();
        }

        return pkgInfo;
    }
}
