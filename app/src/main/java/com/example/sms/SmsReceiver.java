package com.example.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName SmsReceiver
 * @Description 短信监听
 * @Author ouyangzicheng
 * @Date 2020/8/2 下午2:49
 * @Version 1.0
 */
public class SmsReceiver extends BroadcastReceiver {
    protected static final String TAG = "log";

    public static String getCode(String body) {
        Pattern p = Pattern.compile("[0-9]{6,6}(?![0-9])");
        Matcher m = p.matcher(body);
        if (m.find()) {
            System.out.println(m.group());
            return m.group(0);
        }
        return null;
    }

    public static void written(String file, String content) {
        String path = Environment.getExternalStorageDirectory() + "/MyText";
        try {
            FileWriter fileWriter = new FileWriter(path + File.separator + file);
            fileWriter.write(content);
            fileWriter.close();
            Log.i(TAG, "写入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String content = null;
        String sender = null;
        Bundle bundle = intent.getExtras();
        String format = intent.getStringExtra("format");
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (Object object : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object, format);
                sender = message.getOriginatingAddress();
                content = message.getMessageBody();
                if (sender.contains("18770463303")) {
                    System.out.println("手机号：" + sender);
                    if (content.contains("验证码")) {
                        String code = getCode(content);
                        System.out.println("验证码：" + code);
                        written("code.txt", code);
                    }
                }
            }
        }
    }
}
