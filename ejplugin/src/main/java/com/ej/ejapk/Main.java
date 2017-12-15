package com.ej.ejapk;

import java.io.IOException;

/**
 * @author 邓治民
 *         data 2017/12/13 下午2:28
 */

public class Main {

    public static void gradleRun(InputParam inputParam){
        System.out.println("gradleRun start ok channel:"+inputParam.channel+"  file:"+inputParam.inputFolder);
        Main main = new Main();
        main.run(inputParam);
    }

    private void run(InputParam inputParam){
        synchronized (Main.class){
            ApkDecoder decoder = new ApkDecoder(inputParam);
            try {
                decoder.decode();
                decoder.buildApk();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
