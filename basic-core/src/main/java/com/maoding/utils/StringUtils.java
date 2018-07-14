package com.maoding.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by Wuwq on 2016/11/14.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 生成UUID
     */
    public static final String buildUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    /**
     * 转换为字节数组
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 转换为字符串
     */
    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    /**
     * 转换容量大小
     */
    public static String getSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }
    /**
     * 方法描述：非空判断
     * 作        者：MaoSF
     * 日        期：2016年7月7日-下午5:33:52
     * @param o
     * @return
     */
    public static boolean isNullOrEmpty(Object o){
        if(o==null){
            return true;
        }else{
            try{
                if("".equals(o.toString()) ||  "".equals((o.toString()).trim())){
                    return true;
                }
            }catch(Exception e){
                return false;
            }
        }
        return false;
    }
    public static Boolean isEmpty(String s){
        return ((s == null) || s.trim().isEmpty());
    }

    /**
     * 判断两个字符串是否相同，视null和""为相同字符串
     */
    public static Boolean isSame(String a,String b){
        return (isEmpty(a) && isEmpty(b)) ||
                (!isEmpty(a) && !isEmpty(b) && (a.equals(b)));
    }

    /**
     * 方法描述：字符串格式化
     * 作者：Chenxj
     * 日期：2015年6月18日 - 下午4:50:07
     * @param f
     * @return StringBuilder
     */
    public static StringBuilder format(StringBuilder f,Object...objs){
        StringBuilder sb=new StringBuilder();
        int count=0;
        int flag=0;
        char a="?".charAt(0);
        String b="";
        for(int i=0;i<f.length();i++){
            if(a==f.charAt(i)){
                String s= "";
                if(count<objs.length) {//2017-04-28添加此段代码，为了防止模板与参数不对应的情况，引起错误
                    s = objs[count].toString();
                }
                f.replace(i,i+1,b);
                sb.append(f.substring(flag, i)).append(s);
                flag=i;
                count++;
            }
        }
        sb.append(f.substring(flag, f.length()));
        return sb;
    }
    /**
     * 方法描述：字符串格式化
     * 作者：Chenxj
     * 日期：2015年6月18日 - 下午4:58:56
     * @param string
     * @param objs
     * @return String
     */
    public static String format(String string,Object...objs){
        return format(new StringBuilder(string), objs).toString();
    }
}
