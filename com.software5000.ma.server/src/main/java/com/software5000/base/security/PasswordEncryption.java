package com.software5000.base.security;

import com.zscp.master.util.ValidUtil;

import java.security.MessageDigest;

/**
 * 对帐号口令加密
 *
 * @author shanl
 */
public class PasswordEncryption {
    private static final MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("MD5", "SUN");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查密码
     *
     * @param inputPasswd 用户输入的密码
     * @param storePasswd 已存储的密码
     * @return true:通过检查,false:未通过
     */
//    synchronized
    public static boolean checkPasswd(String inputPasswd, String salt, String storePasswd) {
        boolean ok ;

        try {
            String inPwd = ValidUtil.isEmpty(salt)?toPasswd(inputPasswd):toPasswd(inputPasswd, salt.getBytes("UTF-8"));
            ok = inPwd.equals(storePasswd);
        } catch (Exception ex) {
            ok = false;
        }

        return ok;
    }

    /**
     * 去盐验证密码密码
     *
     * @param inputPasswd 用户输入的密码
     * @param storePasswd 已存储的密码
     * @return true:通过检查,false:未通过
     */
//    synchronized
    public static boolean checkPasswd(String inputPasswd, String storePasswd) {
        return checkPasswd(inputPasswd,null,storePasswd);
    }

    /**
     * 将客户输入的密码加密
     *
     * @param inputPasswd 客户输入的密码
     * @param salt        盐
     * @return 加密后的字符串
     */
//    synchronized
    public static String toPasswd(String inputPasswd, byte[] salt) {
        String pwd = "";

        try {
            md.reset();
            md.update(salt);
            md.update(inputPasswd.getBytes("UTF-8"));
            byte[] bys = md.digest();

            pwd += (char) salt[0];
            pwd += (char) salt[1];
            pwd += java.util.Base64.getEncoder().encodeToString(bys);
        } catch (Exception ex) {
            pwd = "";
        }

        return pwd;
    }

    /**
     * 将客户输入的密码加密
     *
     * @param inputPasswd 客户输入的密码
     * @return 加密后的字符串
     */
//    synchronized
    public static String toPasswd(String inputPasswd) {
        String pwd = "";

        try {
            md.reset();
            md.update(inputPasswd.getBytes("UTF-8"));
            byte[] bys = md.digest();
            pwd += java.util.Base64.getEncoder().encodeToString(bys);
        } catch (Exception ex) {
            pwd = "";
        }

        return pwd;
    }

    public static void main(String[] args) {
        System.out.println(PasswordEncryption.toPasswd("123456"));
//        System.out.println(new Md5Hash("123456","D8EDF",2).toString());
    }
}