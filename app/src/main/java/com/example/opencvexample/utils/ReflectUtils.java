package com.example.opencvexample.utils;

import java.lang.reflect.Field;

public class ReflectUtils {

    //使用Hook去获取系统的私有的属性
    private static Object getField(Object obj, Class<?> cls, String field) throws NoSuchFieldException, IllegalAccessException {
        Field localField = cls.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    public static Object getPathList(Object pathClassLoader) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        return getField(pathClassLoader,Class.forName("dalvik.system.BaseDexClassLoader"),"pathList");
    }

    public static Object getDexElements(Object paramObject) throws NoSuchFieldException, IllegalAccessException {
        return getField(paramObject,paramObject.getClass(),"dexElements");
    }
    //使用Hook去修改系统的私有的属性
    public static void setField(Object obj,Class<?> classz,Object value) throws NoSuchFieldException, IllegalAccessException {
        Field localField = classz.getDeclaredField("dexElements");
        localField.setAccessible(true);
        localField.set(obj, value);
    }
}
