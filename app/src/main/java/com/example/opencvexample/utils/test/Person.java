package com.example.opencvexample.utils.test;

import com.example.opencvexample.observable.Student;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;

class Person {
    private long id;
    private String name;
    private Student student;
    private List<Student> list;
    private String[] contacts;
    private Integer phoneNum;
    private Set<Student> studentSet;
    private Vector<Student> studentVector;

    public Vector<Student> getStudentVector() {
        return studentVector;
    }

    public void setStudentVector(Vector<Student> studentVector) {
        this.studentVector = studentVector;
    }

    Person(long id, String name, Student student) {
        this.id = id;
        this.name = name;
        this.student = student;
    }

    public List<Student> getList() {
        return list;
    }

    public void setList(List<Student> list) {
        this.list = list;
    }

    public String[] getContacts() {
        return contacts;
    }

    public void setContacts(String[] contacts) {
        this.contacts = contacts;
    }

    public Integer getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Integer phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Set<Student> getStudentSet() {
        return studentSet;
    }

    public void setStudentSet(Set<Student> studentSet) {
        this.studentSet = studentSet;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", student=" + student +
                ", list=" + list +
                ", contacts=" + Arrays.toString(contacts) +
                ", phoneNum=" + phoneNum +
                ", studentSet=" + studentSet +
                '}';
    }
}
