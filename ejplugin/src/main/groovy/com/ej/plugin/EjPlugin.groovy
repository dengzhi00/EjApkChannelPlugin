package com.ej.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project;

/**
 * @author 邓治民
 *         data 2017/12/5 下午4:07
 */

public class EjPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
//        String module = project.path.replace(":", "")
//        System.out.println("current module is " + module)
//
//        String mainmodulename = project.rootProject.property("mainmodulename")
//        System.out.println("current mainmodulename is " + mainmodulename)
//        if(mainmodulename.equals(module)){
//            String compileComponent = project.properties.get("compileComponent")
//            String[] compiles = compileComponent.trim().split(",")
//            for(String str : compiles){
//                System.out.println("current compile is " + str)
//                project.dependencies.add("implementation",project.project(':' + str))
//            }
//        }

        project.afterEvaluate {
            System.out.println("ejApkRelease is start")
            def taskName = "resguardRelease"
            def ejTask = "ejApkRelease"
            def task = project.task(ejTask, type: EjApkTask)
            //判断是否存在resguardRelease task
            if(project.tasks.findByPath(taskName) != null){
                System.out.println("ejApkRelease is exit")
                task.dependsOn "resguardRelease"
            } else {
                task.dependsOn "assembleRelease"
            }
        }
    }
}
