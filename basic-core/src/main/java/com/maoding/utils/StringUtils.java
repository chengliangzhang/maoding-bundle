package com.maoding.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/31
 * @package: StringUtils
 * @description : 字符串类通用方法
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

    private static final String CHARSET_NAME = "UTF-8";

    /** 时间戳转换为字符串的各种格式 */
    public static final String STAMP_FORMAT_COMPACT_DATE = "yyyyMMdd";
    public static final String STAMP_FORMAT_COMPACT_DATE_TIME = "yyyyMMddHHmmss";
    public static final String STAMP_FORMAT_DATE = "yyyy-MM-dd";
    public static final String STAMP_FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String STAMP_FORMAT_DATE_TIME_MS = "yyyy-MM-dd HH:mm:ss.sss";
    public static final String DEFAULT_STAMP_FORMAT = STAMP_FORMAT_DATE;

    /** json转换器 */
    private static ObjectMapper objectMapper = null;
    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null){
            objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_STAMP_FORMAT));
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        }
        return objectMapper;
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     判断两个字符串是否相同，视null和""为相同的字符串，区分大小写
     * @param   s1 第一个字符串
     * @param   s2 第二个字符串
     * @return 相同返回true，不相同返回false
     **/
    public static boolean isSame(String s1, String s2){
        return (isEmpty(s1) && isEmpty(s2)) || equals(s1,s2);
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     比较两个字符串是否不相同
     * @param   s1 第一个字符串
     * @param   s2 第二个字符串
     * @return 相同返回false，不相同返回true
     **/
    public static boolean isNotSame(String s1,String s2){
        return !equals(s1,s2);
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     获取输入字符串，如果输入字符串为空，返回默认字符串
     * @param   s 输入字符串
     * @param   defaultStr 默认字符串
     * @return  输入字符串或默认字符串
     **/
    public static String getStringOrDefault(String s, String defaultStr){
        return (isEmpty(s)) ? defaultStr : s;
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     获取字符串中第n个字符，如果超出范围，返回默认字符
     * @param   s 输入字符串
     * @param   n 要返回的字符所在位置
     * @param   defaultCh 默认字符
     * @return  字符串中第n个字符或默认字符
     **/
    public static Character getChar(String s, int n, Character defaultCh){
        return (isNotEmpty(s) && (s.length() >= n)) ? getCharOrDefault(s.charAt(n-1),defaultCh) : defaultCh;
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     获取字符串中第n个字符，如果超出范围，返回null
     * @param   s 输入字符串
     * @param   n 要返回的字符所在位置
     * @return  字符串中第n个字符或null
     **/
    public static Character getChar(String s, int n){
        return getChar(s,n,null);
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     获取输入字符或默认字符
     * @param   ch 输入字符
     * @param   defaultCh 默认字符
     * @return  如果输入属于unicode字符集，返回输入字符，否则返回默认字符
     **/
    public static Character  getCharOrDefault(Character ch, Character defaultCh){
        return (Character.isDefined(ch)) ? ch : defaultCh;
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     把对象转为json字符串
     * @param   obs 对象序列
     * @return  json字符串
     **/
    public static String toJsonString(Object... obs){
        if (obs == null) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        for (Object o : obs) {
            try {
                s.append(getObjectMapper().writeValueAsString(o));
            } catch (JsonProcessingException e) {
                log.warn("Json转换时出现错误：", e);
            }
        }
        return s.toString();
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
     * 描述     获取唯一编号
     * 日期     2018/8/1
     * @author  张成亮
     * @return  唯一编号
     **/
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * 描述     获取第一个分隔符左边的字符串
     * 日期     2018/8/2
     * @author  张成亮
     * @return  符合条件的字符串或空字符串
     * @param   str 源字符串
     * @param   split 分隔符
     **/
    public static String left(String str,String split){
        if ((str == null) || (split == null) || (!str.contains(split))) {
            return (str == null) ? EMPTY : str;
        }

        return (str.substring(0,str.indexOf(split)));
    }

    /**
     * 描述     获取左边第一个分隔符右边的字符串
     * 日期     2018/8/2
     * @author  张成亮
     * @return  符合条件的字符串或空字符串
     * @param   str 源字符串
     * @param   split 分隔符
     **/
    public static String right(String str,String split){
        if ((str == null) || (split == null) || (!str.contains(split))) return EMPTY;
        return (str.substring(str.indexOf(split) + split.length()));
    }

    /**
     * 描述     获取最右边分隔符左边的字符串
     * 日期     2018/8/2
     * @author  张成亮
     * @return  符合条件的字符串或空字符串
     * @param   str 源字符串
     * @param   split 分隔符
     **/
    public static String lastLeft(String str, String split){
        if ((str == null) || (split == null) || (!str.contains(split))) {
            return (str == null) ? EMPTY : str;
        }
        return (str.substring(0,str.lastIndexOf(split)));
    }

    /**
     * 描述     获取最右边分隔符右边的字符串
     * 日期     2018/8/2
     * @author  张成亮
     * @return  符合条件的字符串或空字符串
     * @param   str 源字符串
     * @param   split 分隔符
     **/
    public static String lastRight(String str, String split){
        if ((str == null) || (split == null) || (!str.contains(split))) return EMPTY;
        return (str.substring(str.lastIndexOf(split) + split.length()));
    }

    /**
     * 描述     取出字符串中以split分隔的第n段字符串，如果n小于0从右边数
     * 日期     2018/8/2
     * @author  张成亮
     * @return  符合条件的字符串或空字符串
     * @param   str 源字符串
     * @param   n 第几段
     * @param   split 分隔符
     **/
    public static String getContent(String str,int n,String split){
        if ((str == null) || (isEmpty(split))) {
            return (str == null) ? EMPTY : str;
        }

        String[] arr = str.split(split);
        //如果n<0从右边数
        if (n < 0){
            n = arr.length + n + 1;
        }
        return (0 < n && n <= arr.length) ? arr[n-1] : EMPTY;
    }

    /**
     * 生成UUID
     */
    public static final String buildUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
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

    /**
     * 描述     获取调用者信息，即堆栈中第一个不包含指定类名的类及方法
     * 日期     2018/8/14
     * @author  张成亮
     * @return  调用者信息
     * @param   utilsName 查找调用者信息的类的类名
     **/
    public static String getCaller(String utilsName){
        String caller = "";
        StackTraceElement[] stackArray = Thread.currentThread().getStackTrace();
        for (int i=0; i<stackArray.length; i++){
            if (!contains(stackArray[i].getClassName(),StringUtils.class.getName())
                    && !contains(stackArray[i].getClassName(),utilsName)){
                caller = lastRight(stackArray[i].getClassName(),".") + "." + stackArray[i].getMethodName();
            }
        }
        return caller;
    }
}
