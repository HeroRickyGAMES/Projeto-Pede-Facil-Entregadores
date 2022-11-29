package com.herorickystudios.pedefacil_entregas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Ele seta os arquivos da interface para o codigo
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
    }

    public void login(View view){
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    toMainScreen();

                }else{

                    String erro;

                    try{
                    throw task.getException();
                    }
                    catch (Exception e){
                        erro = "erro ao logar ";
                        Snackbar snackbar = Snackbar.make(view, erro + e,Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }

            }
        });

    }

    public void criarcontaAc(View view){

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        FirebaseUser usuariologado = FirebaseAuth.getInstance().getCurrentUser();

        if(usuariologado != null){
            toMainScreen();
        }
        super.onStart();
    }
    public void toMainScreen(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}