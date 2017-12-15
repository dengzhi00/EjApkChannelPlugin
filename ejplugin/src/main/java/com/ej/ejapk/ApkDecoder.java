package com.ej.ejapk;

import com.ej.ejapk.entry.ChannelEncode;
import com.ej.ejapk.entry.DesUtils;

import java.io.*;
import java.security.Key;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author 邓治民
 *         data 2017/12/14 上午10:21
 */

public class ApkDecoder {
    public static final String  DEFAULT_DIGEST_ALG      = "SHA1";

    private HashMap<String, Integer> mCompressData;
    private InputParam inputParam;
    private File fileApk;
    private File tempFile;

    public ApkDecoder(InputParam inputParam){
        this.inputParam = inputParam;
    }

    public void decode() throws IOException {
        File mOutApkDir = new File(inputParam.apkPath);
        fileApk = new File(mOutApkDir.getParentFile().getAbsolutePath(), "ejapk");
        FileZipUtils.clean(fileApk);
//        String unZipDest = new File(mOutApkDir.getParentFile().getAbsolutePath(), "ejapk/temp").getAbsolutePath();
        tempFile = new File(fileApk,"temp");
        FileZipUtils.clean(tempFile);
        System.out.println("ejApkRelease decode unZipDest:"+tempFile.getAbsolutePath());
        mCompressData = FileZipUtils.unZipAPk(inputParam.apkPath,tempFile.getAbsolutePath());

    }

    public void buildApk() throws Exception {
        //删除签名文件
        File sinFile = new File(tempFile,"META-INF");
        if(sinFile.exists()){
            FileZipUtils.deleteDir(sinFile);
            System.out.println("删除原签名文件");
        }
        //存放签名包位置
        File fileassets = new File(tempFile,"assets");
        if(!fileassets.exists()){
            fileassets.mkdirs();
        }
        File fileEjChannel = new File(fileassets,"ej_channel");
        if(!fileEjChannel.exists()){
            fileEjChannel.createNewFile();
        }

        //打包apk位置
        File fileChannel = new File(fileApk,"channel");
        if(fileChannel.exists()){
            fileChannel.delete();
        }
        File unSinedFiles = new File(fileChannel,"unsign");
        File sinedFiles = new File(fileChannel,"sign");
        unSinedFiles.mkdirs();
        sinedFiles.mkdirs();
        File fileChannelTxt = new File(inputParam.channel);
        if(!fileChannelTxt.exists()){
            System.out.println("channel.txt not exit");
            return;
        }
        //获取所有渠道
        InputStream in = new FileInputStream(fileChannelTxt);
        int size = in.available();
        byte[] buffer = new byte[size];
        in.read(buffer);
        in.close();
        String allChannel = new String(buffer, "utf-8");
        //获取所有渠道数组
        String[] channels = allChannel.split(",");
        //循环渠道打包
        for(String content : channels){
            {
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileEjChannel),"UTF-8");
                System.out.println("channel content:"+content);
                //渠道名称加密
                String channel = ChannelEncode.encode(content);
                //不加密
//                String channel = content;
                System.out.println("channel:"+channel);
                osw.write(channel, 0, channel.length());
                osw.flush();
                osw.close();
                //压缩文件
                File outApkUnsin = new File(unSinedFiles,"release-"+content+"-unsin.apk");

                FileZipUtils.compress(tempFile,outApkUnsin);
                //签名
                File outApkSign = new File(sinedFiles,"release-"+content+"-sin.apk");
                signWithV1sign(outApkUnsin,outApkSign);
            }
        }

    }

    private void signWithV1sign(File unSignedApk, File signedApk) throws IOException, InterruptedException {
        String signatureAlgorithm = "MD5withRSA";
        try {
            signatureAlgorithm = getSignatureAlgorithm(DEFAULT_DIGEST_ALG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] argv = {
                "jarsigner",
                "-sigalg", signatureAlgorithm,
                "-digestalg", DEFAULT_DIGEST_ALG,
                "-keystore", inputParam.signFile.getAbsolutePath(),
                "-storepass", inputParam.storepass,
                "-keypass", inputParam.keypass,
                "-signedjar", signedApk.getAbsolutePath(),
                unSignedApk.getAbsolutePath(),
                inputParam.storealias
        };
        dumpParams(argv);
        Process pro = null;
        try {
            pro = Runtime.getRuntime().exec(argv);
            //destroy the stream
            pro.waitFor();
            System.out.print(pro.exitValue());
            if (pro.exitValue() != 0) {
                System.err.println("Jarsigner Failed! Please check your signature file.\n");

            }
        } finally {
            if (pro != null) {
                pro.destroy();
            }
        }
    }

    private void dumpParams(String[] params) {
        StringBuilder sb = new StringBuilder();
        for (String param : params) {
            sb.append(param).append(" ");
        }
        System.out.println(sb.toString());
    }

    private String getSignatureAlgorithm(String hash) throws Exception {
        String signatureAlgorithm;
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fileIn = new FileInputStream(inputParam.signFile);
        keyStore.load(fileIn, inputParam.storepass.toCharArray());
        Key key = keyStore.getKey(inputParam.storealias, inputParam.keypass.toCharArray());
        if (key == null) {
            throw new RuntimeException(
                    "Can't get private key, please check if storepass storealias and keypass are correct"
            );
        }
        String keyAlgorithm = key.getAlgorithm();
        hash = formatHashAlgorithName(hash);
        if (keyAlgorithm.equalsIgnoreCase("DSA")) {
            keyAlgorithm = "DSA";
        } else if (keyAlgorithm.equalsIgnoreCase("RSA")) {
            keyAlgorithm = "RSA";
        } else if (keyAlgorithm.equalsIgnoreCase("EC")) {
            keyAlgorithm = "ECDSA";
        } else {
            throw new RuntimeException("private key is not a DSA or RSA key");
        }
        signatureAlgorithm = String.format("%swith%s", hash, keyAlgorithm);
        return signatureAlgorithm;
    }

    private String formatHashAlgorithName(String hash) {
        return hash.replace("-", "");
    }
}
