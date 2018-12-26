package com.jusfoun.baselibrary.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * @author zhaoyapeng
 * @version create time:15/9/9上午11:00
 * @Email zyp@jusfoun.com
 * @Description ${图片路径 util}
 */

@SuppressLint("UseValueOf")
public class ImagePathUtil {
    public static final String FS = File.separator;
    private static final String UPLOAD_CAMERA_FILE = "camera_file.png";
    private static final String REPORT = "report";
    private static final String UPLOAD = "upload";


    // 获取拍照图片地址
    public static String getUploadCameraPath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = context.getPackageName();
        String path = basePath + FS + REPORT + FS + pkgName + FS + UPLOAD + FS + System.currentTimeMillis()+".png";
        if (!isParentDirExist(path)) {
            Log.e("tag", "isParentDirExist1 ===no");
            makeParentDirs(path);
        }else{

        }
        return path;
    }

    public static String getBaseLocalLocation(Context context) {
        boolean isSDCanRead = getExternalStorageState();
        String baseLocation = "";
        if (isSDCanRead) {
            baseLocation = getSDCardPath();
        } else {
            baseLocation = context.getFilesDir().getAbsolutePath();
        }
        return baseLocation;
    }

    public static boolean isParentDirExist(String filePath) {
        File file = new File(filePath);
        return file.getParentFile().exists();
    }

    public static boolean getExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return false;
        } else {
            return false;
        }
    }
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().toString();
    }

    public static boolean makeParentDirs(String filePath) {
        File file = new File(filePath);
        Log.e("tag", "isParentDirExis2 ===no");
        return file.getParentFile().mkdirs();
    }

}

