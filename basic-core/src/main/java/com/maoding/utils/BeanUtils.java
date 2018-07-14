package com.maoding.utils;

import com.twelvemonkeys.lang.BeanUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 类描述：类操作定义，可用于复制类属性
 * Created by Chengliang.zhang on 2017/6/26.
 */
public class BeanUtils {
    /**
     * 复制Map内属性到Bean
     */
    public static void copyProperties(final Map<String, Object> input, final Object output) {
        if (input == null || output == null)
            return;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(output.getClass(), Object.class);
            PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

            Iterator<String> it = input.keySet().iterator();
            while (it.hasNext()){
                String key = it.next();
                Object value = input.get(key);
                for (PropertyDescriptor pty : properties) {
                    if (pty.getName().equals(key) && pty.getPropertyType().equals(value.getClass())){
                        pty.getWriteMethod().invoke(output, value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制Bean属性到Map
     */
    public static void copyProperties(final Object input, final Map<String, Object> output) {
        if (input == null || output == null)
            return;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(input.getClass(), Object.class);
            PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor pty : properties) {
                Object val = pty.getReadMethod().invoke(input);
                if (val != null) {
                    output.put(pty.getName(),val);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制Bean属性
     */
    public static void copyProperties(final Object input, final Object output) {
        if (input == null || output == null)
            return;

        try {
            BeanInfo sourceBeanInfo = Introspector.getBeanInfo(input.getClass(), Object.class);
            PropertyDescriptor[] sourceProperties = sourceBeanInfo.getPropertyDescriptors();
            BeanInfo destBeanInfo = Introspector.getBeanInfo(output.getClass(), Object.class);
            PropertyDescriptor[] destProperties = destBeanInfo.getPropertyDescriptors();

            for (PropertyDescriptor sourceProperty : sourceProperties) {
                for (PropertyDescriptor destProperty : destProperties) {
                    if (sourceProperty.getName().equals(destProperty.getName()) &&
                            sourceProperty.getPropertyType().equals(destProperty.getPropertyType())){
                        Object value = sourceProperty.getReadMethod().invoke(input);
                        if (value != null) {
                            destProperty.getWriteMethod().invoke(output, value);
                        }
                        break;
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Bean属性
     */
    public static Object getProperty(final Object obj, final String ptyName) {
        if ((obj == null) || (ptyName == null)) return null;

        try {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass(), Object.class);
            PropertyDescriptor[] properties = info.getPropertyDescriptors();

            for (PropertyDescriptor pty : properties) {
                if (pty.getName().equals(ptyName)){
                    return pty.getReadMethod().invoke(obj);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据entityList复制dtoList
     */
    public static List copyFields(List sourceList, Class destClass) throws Exception {
        List destList = new ArrayList();
        for (Object source : sourceList) {
            Object dest = destClass.newInstance();
            copyProperties(source, dest);
            destList.add(dest);
        }
        return destList;
    }
}
