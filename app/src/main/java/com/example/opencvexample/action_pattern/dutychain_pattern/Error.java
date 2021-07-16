package com.example.opencvexample.action_pattern.dutychain_pattern;

public class Error extends LogHandler{

    public Error(LogLevel level){
        logLevel = level;
    }

    @Override
    public void writeContent(String msg) {
        System.out.println("---error---"+msg);

    }
}
