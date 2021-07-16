package com.example.opencvexample.action_pattern.dutychain_pattern;

public class Info extends LogHandler{
    public Info(LogLevel level){
        logLevel = level;
    }
    @Override
    public void writeContent(String msg) {
        System.out.println("---Info---"+msg);
    }
}
