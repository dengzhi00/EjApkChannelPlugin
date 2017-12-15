package com.ej.ejapk;


import java.io.File;

/**
 * @author 邓治民
 *         data 2017/12/13 下午5:23
 */

public class InputParam {

    public String inputFolder;
    public String channel;
    public String apkPath;
    public File   signFile;
    public String keypass;
    public String storealias;
    public String storepass;

    private InputParam(String inputFolder,
                       String channel,
                       String apkPath,
                       File   signFile,
                       String keypass,
                       String storealias,
                       String storepass){
        this.inputFolder = inputFolder;
        this.channel = channel;
        this.apkPath = apkPath;
        this.signFile = signFile;
        this.keypass = keypass;
        this.storealias = storealias;
        this.storepass = storepass;
    }

    public static class Builder{
        private String inputFolder;
        private String channel;
        private String apkPath;
        private File signFile;
        private String            keypass;
        private String            storealias;
        private String            storepass;

        public Builder setApkPath(String apkPath) {
            this.apkPath = apkPath;
            return this;
        }

        public Builder setChannel(String channel) {
            this.channel = channel;
            return this;
        }

        public Builder setInputFolder(String inputFolder) {
            this.inputFolder = inputFolder;
            return this;
        }

        public Builder setKeypass(String keypass) {
            this.keypass = keypass;
            return this;
        }

        public Builder setSignFile(File signFile) {
            this.signFile = signFile;
            return this;
        }

        public Builder setStorealias(String storealias) {
            this.storealias = storealias;
            return this;
        }

        public Builder setStorepass(String storepass) {
            this.storepass = storepass;
            return this;
        }

        public InputParam build(){
            return new InputParam(inputFolder,
                    channel,
                    apkPath,
                    signFile,
                    keypass,
                    storealias,
                    storepass);
        }
    }

}
