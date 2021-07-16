package com.example.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppPlugin

class JavassistPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("test111",MyExtention)
        AppExtension android  = project.extensions.getByType AppExtension
        def log = project.logger
        log.error "================android=>"+android.getCompileSdkVersion()
        log.error("************JavassistTransform*************")
        android.registerTransform(new JavassistTransform(project))

        //生成一个类
        if (project.plugins.hasPlugin(AppPlugin)) {
            //获取到Extension，Extension就是 build.gradle中的{}闭包
            android.applicationVariants.all { variant ->
                //获取到scope,作用域
                def variantData = variant.variantData
                def scope = variantData.scope

                //拿到build.gradle中创建的Extension的值
                def config = project.extensions.getByName("test111")

                //创建一个task
                def createTaskName = scope.getTaskName("testTask", "myTestPlugin")
                def createTask = project.task(createTaskName)
                //设置task要执行的任务
                createTask.doLast {
                    //生成java类
                    createJavaTest(variant, config)
                }
                //设置task依赖于生成BuildConfig的task，然后在生成BuildConfig后生成我们的类
                String generateBuildConfigTaskName = variant.getVariantData().getScope().getGenerateBuildConfigTask().name
                def generateBuildConfigTask = project.tasks.getByName(generateBuildConfigTaskName)
                if (generateBuildConfigTask) {
                    createTask.dependsOn generateBuildConfigTask
                    generateBuildConfigTask.finalizedBy createTask
                }
            }

        }
    }

    static void createJavaTest(variant, config) {
        //要生成的内容
        def content = "package com.aoaoyi.hotfix.plugin";


        //获取到BuildConfig类的路径
        File outputDir = variant.getVariantData().getScope().getBuildConfigSourceOutputDir()

        def javaFile = new File(outputDir, "MyPluginTestClass.java")

        javaFile.write(content, 'UTF-8')
    }
}

//public class MyPluginTestClass {
//    public static final String str = "${config.str}";
//}
