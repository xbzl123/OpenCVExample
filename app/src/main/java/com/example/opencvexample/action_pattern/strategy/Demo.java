package com.example.opencvexample.action_pattern.strategy;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class Demo {
    public static void main(String[] args) {
//        System.out.println((int)(1.0-(0.1*9)));
        Dog[] dogs = new Dog[]{new Dog(2),new Dog(4),
                new Dog(6),new Dog(1),new Dog(7),new Dog(3)};
        Sortor<Dog> dogSortor = new Sortor<>();
        Dog[] sort = dogSortor.sort(dogs, ((o1, o2) -> {
            if (o1.getWeight() > o2.getWeight()) {
                return 1;
            } else if (o1.getWeight() < o2.getWeight()) {
                return -1;
            }
            return 0;
        }));
        System.out.println(Arrays.toString(sort));

        List<Dog> dogList = Arrays.asList(dogs);
//        Collections.sort(dogList,((o1, o2) -> {
//            if (o1.getWeight() > o2.getWeight()) {
//                return 1;
//            } else if (o1.getWeight() < o2.getWeight()) {
//                return -1;
//            }
//            return 0;
//        }));
        System.out.println(dogList);
        Set<Dog> dogSet = new TreeSet<>((o1, o2) -> {
            if (o1.getWeight() > o2.getWeight()) {
                return 1;
            } else if (o1.getWeight() < o2.getWeight()) {
                return -1;
            }
            return 0;
        });
        dogSet.addAll(Arrays.asList(dogs));
        System.out.println(dogSet);
    }
}
