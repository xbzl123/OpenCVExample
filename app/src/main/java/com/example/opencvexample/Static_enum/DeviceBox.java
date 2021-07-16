package com.example.opencvexample.Static_enum;

import java.util.ArrayList;
import java.util.List;

public enum DeviceBox implements Data<Device>{
    INSTANCE;
    DeviceBox(){
        lists.add(new Device());
        System.out.println("Initing success !"+ hashCode());
//        Log.e("test","Initing success !"+ hashCode());
    }
    private List<Device> lists = new ArrayList<>();
    @Override
    public List<Device> getList() {
        return lists;
    }
    @Override
    public void setList(List<Device> list) {
        lists.addAll(list);
    }
}
