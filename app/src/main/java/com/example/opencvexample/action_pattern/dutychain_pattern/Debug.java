package com.example.opencvexample.action_pattern.dutychain_pattern;


public class Debug extends LogHandler{
    public Debug(LogLevel level){
        logLevel = level;
    }

    @Override
    public void writeContent(String msg) {
        System.out.println("---debug---"+msg);
    }
}
