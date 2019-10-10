package com.return0.Kucsa;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class SmsService extends Service {
    public SmsReceiver receiver;
    public SmsService() {
        this.receiver=new SmsReceiver();
    }

    @Override
    public IBinder onBind(Intent intent) {

     return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(receiver,new IntentFilter("android.intent.action.BOOT_COMPLETED"));
        return START_STICKY;
    }
}
