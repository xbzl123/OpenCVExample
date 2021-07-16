package com.example.opencvexample.aspect;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.library.Utils.NetworkUtils;
import com.example.opencvexample.annotation.NoNetwork;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

@Aspect
public class NetworkCheckAspect {
    //切点（绑定注解）
    @Pointcut("execution(@ com.example.opencvexample.annotation.NetworkCheck * *(..))")
    public void pointToAspectJ(){ }

    //切面（绑定切点）
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Around("pointToAspectJ()")
    public void pointToPanel(ProceedingJoinPoint proceedingJoinPoint)throws Throwable{
        //get the environment
        Context context = null;
        Object obj = null;
        final Object object = proceedingJoinPoint.getThis();
        if(object instanceof Context){
            context = (Context)object;
        }else if(object instanceof Fragment){
            context = ((Fragment)object).getActivity();
        }

        if(context == null){
            throw new IllegalAccessException("context == null");
        }
        if(NetworkUtils.isAvailableNetwork(context)){
            obj = proceedingJoinPoint.proceed();
        }else{
            Log.e("test11","mm==="+ context.toString());

            Toast.makeText(context, "not network", Toast.LENGTH_SHORT).show();


            Class<?> aClass = object.getClass();
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                declaredMethod.setAccessible(true);
                boolean isExit =  declaredMethod.isAnnotationPresent(NoNetwork.class);
                if(isExit){
                    declaredMethod.invoke(object);
                }
            }

            NetworkUtils.gotoNetworkSettingsPage(context);
        }
    }
}
