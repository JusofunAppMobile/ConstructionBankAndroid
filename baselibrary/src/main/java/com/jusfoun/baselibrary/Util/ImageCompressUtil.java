package com.jusfoun.baselibrary.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhaoyapeng
 * @version create time:15/9/9下午10:27
 * @Email zyp@jusfoun.com
 * @Description ${图片压缩}
 */
public class ImageCompressUtil {
    public static final int DEFAULT_MAX_WIDTH = 480;
    public static final int DEFAULT_MAX_HEIGHT = 800;

    /**
     * 按固定尺寸压缩
     *
     * @param context
     * @param pathName
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap getBitmapFromMedia(Context context, String pathName, int maxWidth, int maxHeight) {
        if (TextUtils.isEmpty(pathName))
            return null;
        if (pathName.startsWith("file://"))
        {
            Uri uri=Uri.parse(pathName);
            pathName=uri.getPath();
        }
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            // inJustDecodeBounds为true 不分配内存，返回一个空bitmap
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, options);
            options.inJustDecodeBounds = false;
            // 图片的宽度和高度
            int outputWidth = options.outWidth;
            int outputHeight = options.outHeight;
//			Log.e("ImageUtil", "&&&&&&&&pathName = " + pathName + " outputHeight = " + outputHeight);
            if (maxWidth <= 0) {
                maxWidth = DEFAULT_MAX_WIDTH;
            }
            if (maxHeight <= 0) {
                maxHeight = DEFAULT_MAX_HEIGHT;
            }

            if (outputWidth < maxWidth && outputHeight < maxHeight) {
                bitmap = BitmapFactory.decodeFile(pathName);
            } else {
                int inSampleSize = 0;
                int widthSmapleSize = Math.round((float) outputWidth / maxWidth);
                int heightSmapleSize = Math.round((float) outputHeight / maxHeight);
                if (widthSmapleSize >= heightSmapleSize) {
                    inSampleSize = widthSmapleSize;
                } else {
                    inSampleSize = heightSmapleSize;
                }
                options.inSampleSize = inSampleSize;
                bitmap = BitmapFactory.decodeFile(pathName, options);
            }

        } catch (OutOfMemoryError oom) {
            Log.e("ImageUtil", oom.getMessage(), oom);
            System.gc();
            return null;
        } catch (Exception e) {
            Log.e("ImageUtil", e.getMessage(), e);
            return null;
        }

        return bitmap;
    }

    /**
     * 按图片大小压缩
     *
     * @param path
     * @param fileMaxSize
     * @return
     */
    public static File scaleImageForSize(String path, long fileMaxSize) {
        File outputFile = new File(path);
        try {
            FileInputStream fis = new FileInputStream(path);
            FileChannel fc = fis.getChannel();
            long fileSize = fc.size();
            fc.close();
            fis.close();
            while (fileSize >= fileMaxSize) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                int height = options.outHeight;
                int width = options.outWidth;

                double scale = Math.sqrt((float) fileSize / fileMaxSize);
                options.outHeight = (int) (height / scale);
                options.outWidth = (int) (width / scale);
                options.inSampleSize = (int) (fileSize / fileMaxSize);
                options.inJustDecodeBounds = false;

                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//            File tempFile=new File(createImageFile().getPath());
                FileOutputStream fos = null;
                fos = new FileOutputStream(outputFile);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                FileInputStream fis1 = new FileInputStream(path);
                FileChannel fileChannel = fis1.getChannel();
                fileSize = fileChannel.size();

                fos.close();
                fis1.close();
                fileChannel.close();
//                copyFileUsingFileChannels(outputFile, tempFile);

                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {

        }
        return outputFile;

    }

    public static Uri createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,/* prefix */
                    ".jpg",         /* suffix */
                    storageDir/* directory */
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }

    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 将图片储存到sd卡
     *
     * @param context
     * @param imagePath
     * @return
     */
    public static String saveAndCompressPath(Context context, String imagePath) {
        String compressPath = ImagePathUtil.getUploadCameraPath(context);
        Bitmap bitmap = getBitmapFromMedia(context, imagePath, 0, 0);
        if (bitmap != null && !bitmap.isRecycled()) {
            saveBitmapToSD(bitmap, compressPath,imagePath);
        }
        return compressPath;
    }

    // 将Bitmap存入SD卡
    public static void saveBitmapToSD(Bitmap mBitmap, String imagePath,String originPath) {
        if (TextUtils.isEmpty(imagePath))
            return;
        if (imagePath.startsWith("file://")){
            Uri uri=Uri.parse(imagePath);
            imagePath=uri.getPath();
        }
        File f = new File(imagePath);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap=rotateBitmapByDegree(mBitmap,getBitmapDegree(originPath));
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    /**
     * 获取照片角度
     * @param path
     * @return
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            //从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            //获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree= 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree= 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("imagecompress",degree+"");
        return degree;
    }

    /**
     * 旋转图片
     * @param bm
     * @param degree 图片角度
     * @return
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree)
    {
        Bitmap returnBm = null;

        //根据旋转角度，生成旋转矩阵
            Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            //将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0,
                    bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null)
        {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}
