package com.herorickystudios.pedefacil_entregas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){

    }

    public void criarcontaAc(View view){

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }
}