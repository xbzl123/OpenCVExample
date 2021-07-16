package com.example.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project

class MyTest implements Plugin<Project>{

    @Override
    void apply(Project project) {
//        MyExtention myExtention = project.extensions.create("test111", MyExtention)
        def log = project.logger
        log.error("========MyTest========")
//        project.task("345").doLast(
//                new Action<Task>() {
//                    @Override
//                    void execute(Task task) {
//                        println("12318612333333333333aaaaaaaa")
//
//                    }
//                }
//
//        )
//        project.task("567").afterEvaluate(new Action<Task>(){
//
//            @Override
//            void execute(Task task) {
//                println("12318612333333333333aaaaaaaa")
//
//            }
//        })

    }
}