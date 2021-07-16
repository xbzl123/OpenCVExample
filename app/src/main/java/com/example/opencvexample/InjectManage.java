package com.example.opencvexample;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.opencvexample.annotation.ContentView;
import com.example.opencvexample.annotation.EventBase;
import com.example.opencvexample.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManage {
    public static void inject(Activity activity){
        injectLayout(activity);
        injectView(activity);
        injectMethod(activity);
    }

    private static void injectLayout(Activity activity) {
        Class<? extends Activity> type = activity.getClass();
        ContentView contentView = type.getAnnotation(ContentView.class);
        Log.e("nnnnn","====================contentView="+contentView);
        if(contentView != null){
            int layoutId = contentView.value();
//            activity.setContentView(layoutId);
            try {
                Method method = type.getMethod("setContentView", int.class);
                method.invoke(activity,layoutId);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectView(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (Field field:fields){
            InjectView injectView = field.getAnnotation(InjectView.class);
            if(injectView != null){
                int id = injectView.value();
                try {
                    Method method = cls.getMethod("findViewById", int.class);
                    Object view = method.invoke(activity,id);
                    field.setAccessible(true);
                    field.set(activity,view);//属性=执行findViewById方法的结果
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectMethod(Activity activity) {
        Class<?> cls = activity.getClass();
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            //遍历每个方法上的注解
            Annotation[] annotations = method.getAnnotations();
            //遍历方法里面的每一个注解
            for (Annotation annotation : annotations) {
                //获取注解的类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType != null) {
                    //获取EventBase注解对象
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (eventBase != null) {
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> clazz = eventBase.listenerType();
                        String callBackListener = eventBase.callBackListener();
                        try {
                            Method valueMethod = annotationType.getDeclaredMethod("value");
                            int[] viewIds = (int[])valueMethod.invoke(annotation);
                            //拦截+执行
                            ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                            handler.add(callBackListener,method);

                            Object listenr = Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},handler);
                            for (int viewId:viewIds) {
                                //获取View
                                View view = activity.findViewById(viewId);
                                //控件类的父类View.class里面找事件方法
                                Method setXXX = view.getClass().getMethod(listenerSetter,clazz);
                                //执行方法
                                setXXX.invoke(view,listenr);
                            }
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }
}
