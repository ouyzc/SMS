package com.example.sms;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity {

    IntentFilter filter;
    SmsReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filter=new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        receiver= new SmsReceiver();
        registerReceiver(receiver, filter);
    }

    public void writeFile(View v){
        int REQUEST_CODE_CONTACT = 101;
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String str:permissions) {
            if (MainActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                MainActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                return;
            } else {
                String path = Environment.getExternalStorageDirectory()+"/MyText";
                File files = new File(path);
                if(!files.exists()) {
                    files.mkdirs();
                }
                try {
                    FileWriter fileWriter = new FileWriter(path+File.separator+"code.txt");
                    fileWriter.write("123456");
                    fileWriter.close();
                    Toast.makeText(MainActivity.this, "文件写入成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
    }
}
