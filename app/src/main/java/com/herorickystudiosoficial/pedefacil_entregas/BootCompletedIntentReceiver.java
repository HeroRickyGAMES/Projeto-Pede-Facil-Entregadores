package com.herorickystudiosoficial.pedefacil_entregas;

//Programado por HeroRickyGames

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, PushNotificationService.class);
            context.startService(pushIntent);
        }
    }
}