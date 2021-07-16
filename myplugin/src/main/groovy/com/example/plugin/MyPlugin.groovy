package com.example.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class MyPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        MyExtention myExtention = project.extensions.create("test111", MyExtention)
        def log = project.logger
        log.error("========MyPlugin========")
        project.afterEvaluate(new Action<Project>() {
            @Override
            void execute(Project pr) {

                println("------------------myExtention-"+myExtention.getStr())
            }
        })

        def android = project.extensions.getByType(AppExtension)
        def javaTransform = new JavassistTransform(project)

    }
}