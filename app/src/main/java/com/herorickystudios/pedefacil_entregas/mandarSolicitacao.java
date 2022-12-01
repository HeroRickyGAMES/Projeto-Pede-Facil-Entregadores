package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class mandarSolicitacao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandar_solicitacao);

        setTitle("Adicionar Item");
    }
    public void btn(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}