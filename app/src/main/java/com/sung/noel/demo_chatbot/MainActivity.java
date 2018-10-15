package com.sung.noel.demo_chatbot;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sung.noel.demo_chatbot.bubble.BubbleService;
import com.sung.noel.demo_chatbot.util.notification.BubbleNotification;
import com.sung.noel.demo_chatbot.util.notification.NormalNotification;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private NormalNotification normalNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //service未啓動
        if (!isServiceRunning(BubbleService.class)) {
            //目標版本號23以上 且 //不具備顯示於其他應用上層之權限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                normalNotification = new NormalNotification(this, intent);
                normalNotification.displayNotification();

            } else {
                intent = new Intent(this, BubbleService.class);
                intent.putExtra(BubbleNotification.KEY_ACTION, BubbleNotification.VALUE_START);
                startService(intent);
            }
        } else {
            intent = new Intent(this, BubbleService.class);
            intent.putExtra(BubbleNotification.KEY_ACTION, BubbleNotification.VALUE_CLOSE);
            startService(intent);
        }
        finish();


    }



    //------------

    /***
     * 檢查背景服務是否正在執行
     * @param serviceClass
     * @return
     */
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
