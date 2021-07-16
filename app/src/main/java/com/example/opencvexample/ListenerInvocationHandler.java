package com.example.opencvexample;

import android.app.Activity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class ListenerInvocationHandler implements InvocationHandler {
    private Object target;
    private Map<String,Method> map = new HashMap<>();
    public ListenerInvocationHandler(Activity activity) {
        this.target = activity;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //将本应该回调的onClick/onLongClick方法拦截，而去执行开发者定义的abc/xyz方法
        if(target != null){
            //假如是onClick方法
            String methodName = method.getName();
            method = map.get(methodName);//有需要拦截的方法，进行重新赋值
            if(method != null){
                if(method.getGenericParameterTypes().length == 0){
                    return method.invoke(target);
                }
                return method.invoke(target,args);
            }
        }
        return null;
    }

    //添加事件方法到本地
    public void add(String callBackListener, Method method) {
        map.put(callBackListener,method);
    }
}
