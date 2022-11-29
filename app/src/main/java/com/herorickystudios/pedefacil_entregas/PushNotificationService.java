package com.herorickystudios.pedefacil_entregas;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PushNotificationService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        System.out.println("Serviço rodando de fundo!!!");
        return START_STICKY;
    }

}
