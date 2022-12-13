package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSION_FINE_LOCATION = 99;
    EditText editEmail, editSenha;
    LocationRequest locationRequest;
    String erro;

    //API para a localização dos usuarios
    FusedLocationProviderClient fusedLocationProviderClient;

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

        updateGPS();
    }

    public void login(View view){
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if(email.equals("")){
            Toast.makeText(this, "Preencha o campo do email", Toast.LENGTH_SHORT).show();
        }
        if(senha.equals("")){
            Toast.makeText(this, "Preencha o campo da senha", Toast.LENGTH_SHORT).show();
        }

        if(email.equals("") || senha.equals("")){

        }
        else{


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    toMainScreen();

                }else{

                    try{
                    throw task.getException();
                    }
                    catch (Exception e){
                        erro = "Erro ao logar  ";

                        if(e.getMessage().equals("The password is invalid or the user does not have a password.")){
                            Toast.makeText(LoginActivity.this, erro + "Senha Invalida!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, erro + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                }

            }
        });
        }
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
    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location == null){

                        Toast.makeText(LoginActivity.this, "O seu GPS está desativado! Por favor, ative o GPS para conseguir usar o Arbor Amorum!", Toast.LENGTH_LONG).show();
                        Toast.makeText(LoginActivity.this, "Clique no FAB que centraliza a localização, pós isso, volte ao aplicativo!", Toast.LENGTH_LONG).show();

                        Uri uri = Uri.parse("https://www.google.pt/maps");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                    Geocoder geocoder = new Geocoder(LoginActivity.this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String localização = addresses.get(0).getAddressLine(0);

                        //Codigos de registro

                        System.out.println("LOCALIZAÇÃO EXATA: " + localização);
                    } catch (Exception e) {
                        System.out.println("Não foi possivel encontrar sua localização!" + e);
                    }
                }
            });

        } else {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_FINE_LOCATION);
            }

        }
    }
}