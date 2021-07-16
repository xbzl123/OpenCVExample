package com.example.opencvexample.observable;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public enum MyObervable {
    INSTACE;
    private static MyObervable myObervabel = null;
    private Map<String, Map<Class<?>,CustomObservable>> observableMap = new HashMap<>();

    private MyObervable(){
//        throw new AssertionError("ERROR");
    }

//    public static MyObervable getInstance(){
//        if(myObervabel == null){
//            myObervabel = new MyObervable();
//        }
//        return myObervabel;
//    }
//
    public void addObserver(String tag, CustomObserver observer){
        Map<Class<?>,CustomObservable> customObservableMap = observableMap.get(tag);
        //获取传入对象所使用的泛型的类
        Class<?> aClass = (Class<?>) ((ParameterizedType)observer.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
//        for (Type type:((ParameterizedType) observer.getClass().getGenericSuperclass()).getActualTypeArguments()) {
//            System.out.println("type = "+observer.getClass().getGenericSuperclass());
//        }
        CustomObservable customObservable;
        if(customObservableMap == null){
                customObservableMap = new HashMap<>();
                customObservable = new CustomObservable();
                customObservableMap.put(aClass,customObservable);
                observableMap.put(tag,customObservableMap);
        }else {
            customObservable = customObservableMap.get(aClass);
            if (customObservable == null) {
                customObservable = new CustomObservable();
                customObservableMap.put(aClass,customObservable);
            }
        }
        customObservable.addObserver(observer);
    }

    public void deleteObserver(Class<?> cls){
        Iterator<String> iterator = observableMap.keySet().iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            Map<Class<?>, CustomObservable> classCustomObservableMap = observableMap.get(next);
            if (classCustomObservableMap.containsKey(cls)){
                classCustomObservableMap.remove(cls);
            }
        }
    }
    public void deleteObservers(String tag){
        observableMap.remove(tag);
    }
    public void clearObservers(){
        observableMap.clear();
    }
    public void postMessage(String tag,Object obj){
        Map<Class<?>,CustomObservable> customObservableMap = observableMap.get(tag);
        Class<?> aClass = obj.getClass();
        if(customObservableMap != null){
            CustomObservable customObservable = customObservableMap.get(aClass);
            if (customObservable != null) {
                customObservable.notifyCostomObservers(obj);
            }
        }
    }

    public class CustomObservable extends Observable {
        public void notifyCostomObservers(Object obj) {
            setChanged();
            notifyObservers(obj);
        }
    }

    public abstract static class CustomObserver<T> implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            T arg1 = (T) arg;
            updateMessage(o,arg1);
        }
        protected abstract void updateMessage(Observable o, T arg);

    }
}
