package com.example.opencvexample.looptest_rxjava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import io.reactivex.rxjava3.subjects.Subject;

public class Test {
    static class User{
        private String name;
        User(String name){
            this.name = name;
        }
    }
    static int count = 0;
    public static void main(String[] args){
        ArrayList<String> list1 = new ArrayList<>();
        for (int i=0;i<100;i++){
            list1.add( "add+"+Math.sqrt(i));
        }
        ArrayList<String> list2 = new ArrayList<>();
        for (int i=0;i<100;i++){
            list2.add( "add+"+i*(i+100));
        }

        Observable.range(1,100).toList().map(
                integer -> {
                    ArrayList<String> strings = new ArrayList<>();
                    for (Integer i: integer) {
                        strings.add(""+i);
                    }
                    return strings;
                }
        ).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> integer) throws Throwable {
                count ++;
//                System.out.println("count = "+count + integer);
            }
        });
        Observable.just(list1,list2).subscribe(new Consumer<ArrayList<String>>() {
            @Override
            public void accept(ArrayList<String> strings) throws Throwable {
//                count ++;
//                System.out.println("count = "+count + strings);
            }
        });
        Observable.fromIterable(Arrays.asList(list1,list2)).toList().flatMapObservable(
                arrayLists -> {
                    ArrayList<User> users = new ArrayList<>();
                    for (ArrayList<String> list: arrayLists) {
                        for (String s: list) {
                            User user = new User(s);
                            users.add(user);
                        }
                    }
                    return Observable.just(users);
                }
        ).subscribe(users -> {
            for (User s1:users){
            }
        });

        String[] strs = {"11","22","33"};

        @NonNull Disposable subscribe = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                for (String s : strs) {
                    for (char c : s.toCharArray()) {
                        emitter.onNext("" + c);
                    }
                }
                emitter.onComplete();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("result =" + s);
            }
        });

        Subject<Integer> ps = ReplaySubject.create();
        ps.onNext(1);
        ps.onNext(2);
        ps.onNext(3);
        ps.hasComplete();
        ps.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
//                System.out.println("integer="+integer);
            }
        });

        Observable.fromArray(strs).flatMap(new Function<String, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(String s) throws Throwable {
                Subject<Integer> subject = ReplaySubject.create();
                for (char c:s.toCharArray()){
                    subject.onNext(Integer.parseInt(""+c));
                }

                subject.onComplete();
                return subject;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                System.out.println("integer="+integer);

            }
        });


        Disposable disposable = Observable.just("11","22","33").toList().flatMapObservable(new Function<List<String>, ObservableSource<List<User>>>() {
            @Override
            public ObservableSource<List<User>> apply(List<String> strings) throws Throwable {
                ArrayList<User> users = new ArrayList<>();
                for (String s:strings){
                    User user = new User(s);
                    users.add(user);
                }
                return  Observable.just(users);
            }
        }).subscribe(new Consumer<List<User>>() {
            @Override
            public void accept(List<User> s) throws Throwable {

                for (User s1:s){
//                System.out.println(s1.name);
                }
            }
        });

        //响应式编程
        @NonNull Observable<String> initData = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {

            }
        }).subscribeOn(Schedulers.newThread());
        @NonNull Observable<String> initSDK = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {

            }
        }).subscribeOn(Schedulers.newThread());

        Observable.merge(initData,initSDK).subscribe(v->{
        });

//        int i = 0;
//        outer:
//        while(true){
//
//            System.out.println("Outer while loop");
//
//            while(true){
//
//                i++;
//
//                System.out.println("i="+i);
//
//                if(i==1){
//
//                    System.out.println("continue");
//
//                    continue;
//
//                }
//
//                if(i==3){
//
//                    System.out.println("continue outer");
//
//                    continue outer;
//
//                }
//
//                if(i==5){
//
//                    System.out.println("break");
//
//                    break;
//
//                }
//
//                if(i==7){
//
//                    System.out.println("break outer");
//
//                    break outer;
//
//                }
//
//            }
//
//        }
    }

}
