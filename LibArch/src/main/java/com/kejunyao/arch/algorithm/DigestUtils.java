package com.kejunyao.arch.algorithm;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * 日期工具类
 *
 * @author kejunyao
 * @since 2020年09月06日
 */
public class DigestUtils {

    private DigestUtils() {
    }
    
    /**
     * 获取给定内容的MD5信息
     * @param content 内容
     * @return MD5字符串
     */
    public static String getMD5(String content) {
        try {
            return getMD5(content.getBytes("UTF-8"), false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 把二进制byte数组生成 md5 32位 十六进制字符串，单个字节小于0xf，高位补0。
     * @param bytes 输入
     * @param upperCase true：大写， false 小写字符串
     * @return 把二进制byte数组生成 md5 32位 十六进制字符串，单个字节小于0xf，高位补0。
     */
    public static String getMD5(byte[] bytes, boolean upperCase) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(bytes);
            return toHexString(digest.digest(), "", upperCase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取给定内容的SHA-256信息
     * @param content 内容
     * @return SHA-256字符串
     */
    public static String getSHA256(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(content.getBytes("UTF-8"));
            return toHexString(digest.digest(), "", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 把二进制byte数组生成十六进制字符串，单个字节小于0xf，高位补0。
     * @param bytes 输入
     * @param separator 分割线
     * @param upperCase true：大写， false 小写字符串
     * @return 把二进制byte数组生成十六进制字符串，单个字节小于0xf，高位补0。
     */
    public static String toHexString(byte[] bytes, String separator, boolean upperCase) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String str = Integer.toHexString(0xFF & b); // SUPPRESS CHECKSTYLE
            if (upperCase) {
                str = str.toUpperCase();
            }
            if (str.length() == 1) {
                hexString.append("0");
            }
            hexString.append(str).append(separator);
        }
        return hexString.toString();
    }
    
}
