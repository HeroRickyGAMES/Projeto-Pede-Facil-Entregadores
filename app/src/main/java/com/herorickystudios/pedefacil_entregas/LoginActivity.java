package com.herorickystudios.pedefacil_entregas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSION_FINE_LOCATION = 99;
    EditText editEmail, editSenha;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Ele seta os arquivos da interface para o codigo
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 30);
        locationRequest.setFastestInterval(1000 * 5);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Não fazer nada
                }else{
                    Toast.makeText(this, "Esse aplicativo precisa das permissões para funcionar, caso você negou sem querer, acesse as configurações!", Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }

}