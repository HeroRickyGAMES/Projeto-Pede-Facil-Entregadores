package com.herorickystudiosoficial.pedefacil_entregas;

//Programado por HeroRickyGames

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class VeryfySDK extends AppCompatActivity {

    int AndroidVersionSDK = Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veryfy_sdk);

        if(AndroidVersionSDK < 33){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }else{
            Intent intent = new Intent(this, NotifyVerify.class);
            startActivity(intent);
        }
    }
}