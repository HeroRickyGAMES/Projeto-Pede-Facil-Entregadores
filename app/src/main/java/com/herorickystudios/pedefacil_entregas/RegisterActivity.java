package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;

public class RegisterActivity extends AppCompatActivity {

    EditText editNome, editIdade, editCPF, editEmail, editPIX, editSenha;
    String nome, idade, CPF, email, PIX, senha;
    RadioButton radioEntregador, radioLoja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ids
        editNome = findViewById(R.id.editNome);
        editIdade = findViewById(R.id.editIdade);
        editCPF = findViewById(R.id.editCPF);
        editEmail = findViewById(R.id.editEmailr);
        editSenha = findViewById(R.id.editSenhar);
        editPIX = findViewById(R.id.editPIXcadastro);
        radioEntregador = findViewById(R.id.radioEntregador);
        radioLoja = findViewById(R.id.radioLoja);

        //Strings
        nome = editNome.getText().toString();
        idade = editIdade.getText().toString();
        CPF = editCPF.getText().toString();
        senha = editSenha.getText().toString();
    }
}