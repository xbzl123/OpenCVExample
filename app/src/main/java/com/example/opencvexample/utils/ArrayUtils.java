package com.example.opencvexample.utils;

import android.util.Log;

import java.lang.reflect.Array;

public class ArrayUtils {

    public static Object combineArray(Object arrayLhs,Object arrayRhs){
        //获取对象的类型
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        Log.e("www","====补丁长度="+i);
        //计算新数组的总长度
        int j = i + Array.getLength(arrayRhs);
        Log.e("www","====总长度="+Array.getLength(arrayRhs));
        //生成新的数组对象
        Object result = Array.newInstance(localClass,j);
        for (int k = 0; k < j; k++) {
            if(k<i){
                Log.e("www","=1===k="+k);
                Array.set(result,k,Array.get(arrayLhs,k));
            }else{
                Log.e("www","=2===k="+k);
                Array.set(result,k,Array.get(arrayRhs,k-i));
            }
        }
        Log.e("www","====总长度result="+Array.getLength(result));

        return result;
    }
}
