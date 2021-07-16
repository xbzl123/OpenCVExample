package com.example.opencvexample.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public  @interface EventBase {
    //1 监听方法
    String listenerSetter();
    //2 监听的接口
    Class<?> listenerType();
    //3 观察到用户行为后，告知的回调方法
    String callBackListener();
}
