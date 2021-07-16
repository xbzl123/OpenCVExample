package com.example.opencvexample.structrue_pattern.composite;

import java.util.ArrayList;
import java.util.List;

class Person{
    private String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}

class SelectPersons{
    private String address;
    private String nation;
    private List<Person> personList = new ArrayList<>();

    public void add(Person person){
        if(!personList.contains(person)){
        personList.add(person);
        }
    }


    public void remove(Person person){
        if(personList.contains(person)){
            personList.remove(person);
        }
    }
    public List<Person> getPersonList() {
        return personList;
    }
}
class Demo {
    public static void main(String[] args) {
        SelectPersons selectPersons = new SelectPersons();
        selectPersons.add(new Person("jack"));
        System.out.println(selectPersons.getPersonList());
    }
}
