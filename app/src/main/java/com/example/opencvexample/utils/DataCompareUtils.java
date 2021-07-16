package com.example.opencvexample.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author Administrator
 */
@SuppressWarnings("rawtypes")
public class DataCompareUtils {
    /**
     *
     * 防止被实例化
     *
     */
    private DataCompareUtils() {
        throw new AssertionError("不能实例化");
    }

    /**
     *
     * 结果返回true是相等，返回false是不相等
     *
     */
    public static boolean isCompleteSame(Object object1, Object object2) {
        if (object1 == null || object2 == null) {
            return false;
        }
        if (object1.hashCode() == object2.hashCode()) {
            return true;
        }
        if (object1.getClass() != object2.getClass()) {
            return false;
        } else {
            Class<?> aClass1 = object1.getClass();
            Field[] declaredFields1 = aClass1.getDeclaredFields();

            Class<?> aClass2 = object2.getClass();
            Field[] declaredFields2 = aClass2.getDeclaredFields();
            if (declaredFields1.length != declaredFields2.length) {
                return false;
            }
            for (int i = 0; i < declaredFields1.length; i++) {
                Field field1 = declaredFields1[i];
                Field field2 = declaredFields2[i];
                field1.setAccessible(true);
                field2.setAccessible(true);
                try {
                    String typeName = field1.getType().getName();
                    Object o1 = field1.get(object1);
                    Object o2 = field2.get(object2);
                    if (o1 == null || o2 == null) {
                        if (o1 == null && o2 == null) {
                            continue;
                        }
                        return false;
                    }
                    //如果是数组类型
                    if (typeName.startsWith("[")) {
                        int length1 = Array.getLength(o1);
                        int length2 = Array.getLength(o2);
                        if (length1 != length2) {
                            return false;
                        } else {
                            for (int j = 0; j < length1; j++) {
                                if (!Array.get(o1, j).equals(Array.get(o2, j))) {
                                    return false;
                                }
                            }
                        }
                    } else if (isCustomClass(typeName)) {
                        //可以获取值来比较（非包装类型）
                        if (typeName.startsWith("java.util")) {
                            //处理实现了collection接口的容器类
                            Collection collection1 = (Collection) o1;
                            Collection collection2 = (Collection) o2;
                            Object[] objects1 = collection1.toArray();
                            Object[] objects2 = collection2.toArray();
                            if (objects1.length != objects2.length) {
                                return false;
                            }
                            //兼容HashSet这类排序无序的情况，使用双重循环比较对象
                            for (Object obj1 : objects1) {
                                if (null != obj1) {
                                    for (int k = 0; k < objects1.length; k++) {
                                        if (isCompleteSame(obj1, objects2[k])) {
                                            break;
                                        } else {
                                            //比较到最后一位还不相等就返回false
                                            if (k == objects1.length - 1) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (o1 != null && o2 != null) {
                                if (!o1.equals(o2)) {
                                    return false;
                                }
                            } else {
                                if (o2 == null && o1 == null) {
                                    continue;
                                }
                                return false;
                            }
                        }
                    } else {
                        //包装类型 其他的类
                        if (o1 != null && o2 != null) {
                            isCompleteSame(o1, o2);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private static boolean isCustomClass(String typeName) {
        return !typeName.contains(".") || (typeName.startsWith("java") || typeName.startsWith("android")
                || typeName.startsWith("androidx") || typeName.startsWith("javax"));
    }
}