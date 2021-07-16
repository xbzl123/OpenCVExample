package com.example.opencvexample.utils.test;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.opencvexample.observable.Student;
import com.example.opencvexample.utils.DataCompareUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
class MyList extends ArrayList{
}

public class Demo {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @org.junit.Test
    public void test() {
//        Class<? extends Set<String>> aClass = null;
//        try {
//            aClass = (Class<? extends Set<String>>) Class.forName("java.util.TreeSet");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Constructor<? extends Set<String>> conn = null;
//        try {
//            conn = aClass.getDeclaredConstructor();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//
//        Set<String> set = null;
//        try {
//            set = conn.newInstance();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        set.add("BBB");
//        set.add("CCC");
//        set.add("VVV");
//        set.add("AAA");
//        set.add("DDD");
//        System.out.println("size : "+set);
//        long count = LongStream.rangeClosed(2, 15)
//                .mapToObj(BigInteger::valueOf)
//                .filter(i->i.isProbablePrime(50)).count();
//
//        assert count != -1;
//        System.out.println(count);

        String tmp1 = "test";
        String tmp2 = new String("test");
        String tmp3 = "te" + "st";
        char[] chars = tmp1.toCharArray();
        System.out.println(tmp2.intern() == tmp1);

        ArrayList<Student> strings = new ArrayList<>(16);
        strings.add(new Student(9,"mike"));
        strings.add(new Student(3,"june"));
        strings.add(new Student(1,"kuku"));

        Person person = new Person(007, "zhou", new Student(001, "zhou"));
        person.setList(strings);
        person.setContacts(new String[]{"A","B","C"});
        person.setPhoneNum(new Integer(5302256));

        Person person1 = new Person(007, "zhou", new Student(001, "zhou"));
        ArrayList<Student> strings1 = new ArrayList<>(16);
        strings1.add(new Student(3,"june"));
        strings1.add(new Student(1,"kuku"));
        strings1.add(new Student(9,"mike"));

        person1.setList(strings1);
        person1.setContacts(new String[]{"A","B","C"});
        person1.setPhoneNum(new Integer(5302256));

        Set<Student> students = new HashSet<>(16);
        students.add(new Student(9,"mike"));
        students.add(new Student(8,"hiky"));
        students.add(new Student(7,"jace"));
        person.setStudentSet(students);

        Set<Student> students1 = new HashSet<>(16);
        students1.add(new Student(7,"jace"));
        students1.add(new Student(8,"hiky"));
        students1.add(new Student(9,"mike"));
        person1.setStudentSet(students1);

        Vector<Student> vector1 = new Vector<>();
        vector1.add(new Student(7,"jack"));
        vector1.add(new Student(8,"hiky"));
        vector1.add(new Student(9,"mike1"));
        person.setStudentVector(vector1);

        Vector<Student> vector2 = new Vector<>();
        vector2.add(new Student(7,"jack"));
        vector2.add(new Student(8,"hiky"));
        vector2.add(new Student(9,"mike1"));
        person1.setStudentVector(vector2);
//        String[] tmp = new String[]{"A","B","C"};
//        String[] temp = new String[]{"A","B"};
//        Object o = tmp;
//        Object o1 = temp;
//        String[] tmp1 = (String[])o;
//        String[] tmp2 = (String[])o1;
//        System.out.println(tmp.equals(temp));
//        System.out.println(Arrays.equals(tmp1,tmp2));

        System.out.println("the value of two object is same ? "+DataCompareUtils.isCompleteSame(person,person1));
        System.out.println(union(students1,students));
    }

    public <E> Set<E> union(Set<E> set1, Set<E> set2){
        Set<E> result = new HashSet<>(set1);
        result.addAll(set2);
        return result;
    }

}
