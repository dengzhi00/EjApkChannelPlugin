package com.ej.ejapk.entry;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;


/**
 * @author 邓治民
 *         data 2017/12/14 下午3:36
 */

public class ChannelEncode {

    public static String encode(String s) throws Exception {
        String key1 = getKey();
        String key2 = getKey();
        String msg = OperatorUtils.Encrypt(s,key1,key2);
        System.out.println("channel msg:"+msg);
        return key1+key2+msg;
    }


    public static final byte PROTOCOL_0x7E = 0x7e;
    public static final byte PROTOCOL_0x7D = 0x7d;
    public static final byte PROTOCOL_0x01 = 0x01;
    public static final byte PROTOCOL_0x02 = 0x02;
    public static final Charset GBK = Charset.forName("GBK");


    public static String getKey(){
        String u1 = UUID.randomUUID().toString();
        String u2 = UUID.randomUUID().toString();
        String key = "";
        Random random = new Random();
        for(int i = 0;i<8;i++){
            key+=String.valueOf(u1.charAt(random.nextInt(32)));
        }
        for(int i = 0;i<8;i++){
            key+=String.valueOf(u2.charAt(random.nextInt(32)));
        }
        return key;
    }

}
