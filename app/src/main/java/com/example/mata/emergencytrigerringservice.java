package com.example.mata;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

public class emergencytrigerringservice extends Service {
    final BroadcastReceiver mReceiver = new emergency_trigger();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createnotification();
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mReceiver, filter);
        return super.onStartCommand(intent, flags, startId);
    }

    private void createnotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel= new NotificationChannel(
                    "ChannelId1","foreground notification", NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
    }

    public class LocalBinder extends Binder {
        emergencytrigerringservice getService() {
            return emergencytrigerringservice.this;
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}