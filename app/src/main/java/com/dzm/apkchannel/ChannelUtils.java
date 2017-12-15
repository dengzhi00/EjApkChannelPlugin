package com.dzm.apkchannel;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 邓治民
 *         data 2017/12/14 下午5:41
 */

public class ChannelUtils {

    private static String channel;


    public static void initChannel(Context context){
        channel = getChannel(context);
    }

    private static String getChannel(Context context){
        try {
            InputStream in = context.getAssets().open("ej_channel");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            String txt = new String(buffer, "utf-8");
            Log.d("qudaobiaoji:",txt);
            String key1 = txt.substring(0,16);
            String key2 = txt.substring(16,32);
            String msg = txt.substring(32,txt.length());
            return OperatorUtils.decrypt(msg,key1,key2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知";
    }

    public static String getChannel() {
        return channel;
    }
}
