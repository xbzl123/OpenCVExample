package com.example.opencvexample.action_pattern.strategy;

import java.util.Arrays;
import java.util.Comparator;

public class Sortor<T> {
    public T[] sort(T[] datas, Comparator<T> comparator){
        T[] tmp = Arrays.copyOf(datas,datas.length);
        T data;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp.length -1 - i; j++) {
                if(comparator.compare(tmp[j], tmp[j+1]) > 0){
//                    System.out.println(comparator.compare(tmp[j], tmp[j+1]));
                    data = tmp[j+1];
                    tmp[j+1] = tmp[j];
                    tmp[j] = data;
                }
            }
        }
        return tmp;
    }
}
