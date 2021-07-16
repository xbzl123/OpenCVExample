package com.example.opencvexample.aspect;

import android.app.Activity;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

@Aspect
public class ActivityAopTest {
    private long timeSpend;
    private long timeStart;
    private long lastClick;

    //方法1
//    @Before("execution(* android.app.Activity.on**(..))")
//    public void ActivityLogRecord(){
//        Log.e("onActivity","this a test ! ");
//    }
//    @After("execution(* android.app.Activity.on**(..))")
//    public void ActivityLogRecord1(){
//        Log.e("onActivity","this a test over ! ");
//    }
    //方法2
    @Pointcut("execution(* android.app.Activity.on**(..))")
    public void setPointCut(){}

    @Before("setPointCut()")
    public void setPanelCut(JoinPoint proceedingJoinPoint){
        timeStart = System.currentTimeMillis();
        Object object = proceedingJoinPoint.getThis();
        Activity activity = (Activity)object;
        Log.e("onActivity111","this a test ! "+activity.getLocalClassName()+", mothod is :"+proceedingJoinPoint);
    }

    //方法2
    @Pointcut("execution(* android.app.Activity.on**(..))")
    public void setPointCutAfetr(){}

    @After("setPointCutAfetr()")
    public void setPanelCutAfter(JoinPoint proceedingJoinPoint){
        timeSpend = System.currentTimeMillis() - timeStart;
        Log.e("onActivity111","this a test setPointCutAfetr ! timeSpend = "+timeSpend);
    }


    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void setPointCutOnClick(){}

    @Around("setPointCutOnClick()")
    public void setPanelCutOnClick(ProceedingJoinPoint proceedingJoinPoint)throws Throwable {
        if(System.currentTimeMillis() - lastClick > 500){
            proceedingJoinPoint.proceed();
            Log.e("onActivity111","this a test setPointCutOnClick ! timeSpend = "+timeSpend);

        }
        lastClick = System.currentTimeMillis();
    }
}
