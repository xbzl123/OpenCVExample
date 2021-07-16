package com.example.opencvexample.action_pattern.null_pattern;

abstract class AbstractEmployee{
    private String name;
    private boolean isNull;
    abstract String getName();
    abstract boolean isNull();
}

class Programmer extends AbstractEmployee{
    private String name;

    public Programmer(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    String getName() {
        return name;
    }

    @Override
    boolean isNull() {
        return false;
    }
}

class FireEmployee extends AbstractEmployee{

    @Override
    String getName() {
        return "name is no exit";
    }

    @Override
    boolean isNull() {
        return true;
    }
}

class AllMember{
    final static String[] EMPLOYEES = new String[]{"1","2","3"};
    static AbstractEmployee getMember(String name){
        for (String employee : EMPLOYEES) {
            if (name.equalsIgnoreCase(employee)) {
                return new Programmer(employee);
            }
        }
        return new FireEmployee();
    }
}
class Demo {
    public static void main(String[] args) {
        AbstractEmployee member = AllMember.getMember("1");
        System.out.println(member.getName());
        AbstractEmployee member1 = AllMember.getMember("4");
        System.out.println(member1.getName());

    }

}
