package com.example.opencvexample.action_pattern.dutychain_pattern;

public class Test {
    @org.junit.Test
    public void main(){
        LogHandler logHandler = getLog();
        logHandler.dealWith(LogLevel.error,"test");
    }

    private LogHandler getLog() {
        Debug debug = new Debug(LogLevel.debug);
        Info info = new Info(LogLevel.info);
        Error error = new Error(LogLevel.error);
        debug.setNextLogType(info);
        info.setNextLogType(error);
        return debug;
    }
}
