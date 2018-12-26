package com.jusfoun.baselibrary.Util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lee
 * @version create time:2015年7月23日_上午11:28:38
 * @Description 正则判断
 */

public class RegularUtils {


    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        if (TextUtils.isEmpty(email))
            return false;
//		String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        String regex = "^(\\w-*\\.*)+@(\\w-?)+(\\.\\w{2,})+";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证字母和数字
     * @param value
     * @return
     */
    public static boolean checkLetterAndNum(String value){
        if (TextUtils.isEmpty(value))
            return false;
        String regex = "[A-Za-z1-9]+$";
        return Pattern.matches(regex, value);

    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[345789]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }


    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 五位或五位以上0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\+?[1-9]\\d{4,}";
        return Pattern.matches(regex, digit);
    }


    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }


    public static boolean checkFax(String fax) {
        String regex = "^[+]?(\\d){1,3}[ ]?([-]?((\\d)|[ ]){1,12})+$";
        return Pattern.matches(regex, fax);
    }

    /**
     * 去除电话号码中的非数字字符
     *
     * @param string 要去除的字符串
     * @return 纯数字电话号码
     */
    public static String getNumber(String string) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        return m.replaceAll("").trim();
    }

    /**
     * 检查是否包含表情
     *
     * @param string
     * @return
     */
    public static boolean isEmoji(String string) {

        if (TextUtils.isEmpty(string))
            return false;
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
