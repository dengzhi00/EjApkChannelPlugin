package com.ej.plugin

import com.ej.ejapk.InputParam
import com.ej.ejapk.Main
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author 邓治民
 *         data 2017/12/13 上午11:06
 */

public class EjApkTask extends DefaultTask{

    def android
    def buildConfigs = []

    EjApkTask(){
        description = 'Assemble ej APK'
        group = 'andChannelApk'
        outputs.upToDateWhen { false }
        android = project.extensions.android

        android.applicationVariants.all { variant ->
            variant.outputs.each { output ->
                // remove "ejApk"
                System.out.println("ejApkRelease EjApkTask name:"+this.name)
                String variantName = this.name["ejApk".length()..-1]
                if (variantName.equalsIgnoreCase(variant.buildType.name as String)
                        || isTargetFlavor(variantName, variant.productFlavors, variant.buildType.name)
                ) {
                    System.out.println("ejApkRelease EjApkTask init buildConfigs")
                    buildConfigs << new BuildInfo(
                            output.outputFile,
                            variant.variantData.variantConfiguration.signingConfig,
                            variant.variantData.variantConfiguration.applicationId,
                            variant.buildType.name,
                            variant.productFlavors,
                            variantName
                    )
                }
            }
        }
    }

    static isTargetFlavor(variantName, flavors, buildType) {
        if (flavors.size() > 0) {
            String flavor = flavors.get(0).name
            return variantName.equalsIgnoreCase(flavor) || variantName.equalsIgnoreCase([flavor, buildType].join(""))
        }
        return false
    }

    static useFolder(file) {
        //remove .apk from filename
        def fileName = file.name[0..-5]
        return "${file.parent}/AndResGuard_${fileName}/"
    }

    @TaskAction
    run(){
        System.out.println("ejApkRelease EjApkTask start")
        //渠道文件配置位置
        String channel = project.properties.get("channelFile")
        buildConfigs.each{ config ->
            if (config.file == null || !config.file.exists()) {
                System.out.println("ejApkRelease EjApkTask apk file not exit 1")
                return
            }
            //签名文件
            def signConfig = config.signConfig
            //app-release.apk位置
            String path = config.file.getAbsolutePath()
            System.out.println("path:"+path)
            InputParam.Builder builder = new InputParam.Builder()
                    .setChannel(channel)
                    .setInputFolder(useFolder(config.file))
                    .setApkPath(path)
                    .setSignFile(signConfig.storeFile)
                    .setKeypass(signConfig.keyPassword)
                    .setStorealias(signConfig.keyAlias)
                    .setStorepass(signConfig.storePassword)

            InputParam inputParam = builder.build()
            Main.gradleRun(inputParam)

        }

    }

}
