package com.jusfoun.baselibrary.Util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/4.
 * Email wcc@jusfoun.com
 * Description
 */
public class StringUtil {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    // 得到一个url的md5值
    public static String getMD5Str(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(url.getBytes());
            return getFormattedText(digest.digest());
        } catch (NoSuchAlgorithmException ex) {
            return url;
        }
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static boolean isAZ(String str) {
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }

    }

    private static byte[] iv = { 0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

    public static String toSemiangle(String src) {
         /*全角空格为12288，半角空格为32
         * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
         * 将字符串中的全角字符转为半角
         * @param src 要转换的包含全角的任意字符串
         * @return  转换之后的字符串
         */
        char[]c=src.toCharArray();
        for(int index=0;index<c.length;index++){
            if (c[index]==12288){   //全角空格
                c[index]=(char)32;
            }
            else if (c[index]>65280 && c[index]<65375){   //其他全角字符
                c[index]=(char)(c[index]-65248);
            }
        }
        return String.valueOf(c);
    }

    /*
     * 字符串是否为空。
     */
    public static boolean isEmpty(String str) {
        String string = str;
        if (string == null) {
            return true;
        }
        string = string.trim();
        return TextUtils.isEmpty(string) || str.equalsIgnoreCase("null");
    }

//	/*
//	 * 字符串是否为空。
//	 */
//	public static boolean isEmptyWithUnknown(String str) {
//		String string = str;
//		if (string == null) {
//			return true;
//		}
//		string = string.trim();
//		return TextUtils.isEmpty(string) || string.equalsIgnoreCase("null") || isUnknownString(string);
//	}

    /*
     * 字符串是否相等。
     */
    public static boolean equals(String str1, String str2) {
        if (TextUtils.isEmpty(str1))
        {
            return false;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str2.equals(str1);
    }


    public static String getDate(String dateStr){
        try{
            Long l=Long.parseLong(dateStr);
            Date date=new Date(l);
            SimpleDateFormat format=new SimpleDateFormat("MM-dd hh:mm");
            return format.format(date);
        }catch (Exception e){

        }
        return "";
    }

    /**
     * 获取渠道号
     *
     */
    public static String getChannelName(Context context){
        String msg = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            msg =appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return msg;
    }
}
