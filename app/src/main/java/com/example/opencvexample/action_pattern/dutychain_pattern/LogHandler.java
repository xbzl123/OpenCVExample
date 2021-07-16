package com.example.opencvexample.action_pattern.dutychain_pattern;

public abstract class LogHandler {
    public LogHandler nextLogHandler;
    public LogLevel logLevel;

    public void setNextLogType(LogHandler handler){
        nextLogHandler = handler;
    }

    public void dealWith(LogLevel level,String msg){
        if(logLevel == level){
            writeContent(msg);
        }else {
//            System.out.println("---nextLogHandler-"+nextLogHandler.logLevel);
            nextLogHandler.dealWith(level,msg);
        }
    }

    public abstract void writeContent(String msg);
}
