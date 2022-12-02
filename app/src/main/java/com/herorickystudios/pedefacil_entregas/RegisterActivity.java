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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editNome, editIdade, editCPF, editEmail, editPIX, editSenha, editEnderecoLoja;
    String nome, idade, CPF, email, PIX, senha, typeACC, getUID, AccType, idPIXType, localização, latitude, longitude;
    RadioGroup radioAccType, radioPixType;
    FirebaseFirestore referencia = FirebaseFirestore.getInstance();
    int selectIDType, selectIDPIXType;
    RadioButton radioTypeAcc, radioIDPIXType;

    private static final int PERMISSION_FINE_LOCATION = 99;
    LocationRequest locationRequest;

    //API para a localização dos usuarios
    FusedLocationProviderClient fusedLocationProviderClient;

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
        radioAccType = findViewById(R.id.radioAccType);
        radioPixType = findViewById(R.id.radioPixType);
        editEnderecoLoja = findViewById(R.id.editEnderecoLoja);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 30);
        locationRequest.setFastestInterval(1000 * 5);

        editEnderecoLoja.setVisibility(View.INVISIBLE);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        updateGPS();
    }

    public void CadastroBtn(View view){
        //Strings
        nome = editNome.getText().toString();
        idade = editIdade.getText().toString();
        CPF = editCPF.getText().toString();
        email = editEmail.getText().toString();
        senha = editSenha.getText().toString();
        PIX = editPIX.getText().toString();


        selectIDType = radioAccType.getCheckedRadioButtonId();
        selectIDPIXType = radioPixType.getCheckedRadioButtonId();

        radioTypeAcc = (RadioButton) findViewById(selectIDType);
        radioIDPIXType = (RadioButton) findViewById(selectIDPIXType);

        AccType = radioTypeAcc.getText().toString();
        idPIXType = radioIDPIXType.getText().toString();

        if(AccType.equals("Sou um Entregador")){

            typeACC = "Entregador";
        }else if(AccType.equals("Sou uma Loja")){

            typeACC = "Loja";
        }

        if(editNome.getText().toString().equals("")
                && editIdade.getText().toString().equals("")
                && editCPF.getText().toString().equals("")
                && editEmail.getText().toString().equals("")
                && editSenha.getText().toString().equals("")
                && editPIX.getText().toString().equals(""))
        {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();

        }else{
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if(typeACC.equals("Loja")){
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                        localização = editEnderecoLoja.getText().toString();

                        try {
                            List addressList = geocoder.getFromLocationName(editEnderecoLoja.getText().toString(), 1);
                            if (addressList != null && addressList.size() > 0) {
                                Address address = (Address) addressList.get(0);
                                StringBuilder sb = new StringBuilder();

                                sb.append(address.getLatitude()).append("\n");
                                sb.append(address.getLongitude()).append("\n");

                                //Reconverte para Lat e long

                                latitude = String.valueOf(address.getLatitude());
                                longitude = String.valueOf(address.getLongitude());

                                System.out.println("Em Latitude e longitude " + latitude + " " +  longitude);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    getUID = FirebaseAuth.getInstance().getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("nameCompleteUser", nome);
                    user.put("idadeUser", idade);
                    user.put("CPF", CPF);
                    user.put("Email", email);
                    user.put("Localização", localização);
                    user.put("PIX", PIX);
                    user.put("Tipo de conta", typeACC);
                    user.put("Metodo de PIX", idPIXType);
                    user.put("Latitude", latitude);
                    user.put("Longitude", longitude);

                    System.out.println("String" + getUID);

                    DocumentReference setDB = referencia.collection(typeACC).document(getUID);

                    setDB.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Map<String, Object> errorDb = new HashMap<>();
                            errorDb.put("CadastroError", "Erro no Cadastro: "+ e);
                            DocumentReference setDB = referencia.collection("ErrorDB").document(getUID);

                            System.out.println("Ocorreu um erro: "+ e);
                        }
                    });

                    //Starta a Activity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Map<String, Object> errorC = new HashMap<>();
                    errorC.put("CadastroError", "Erro no Cadastro: "+ e);
                    DocumentReference setDB = referencia.collection("ErrorDB").document(getUID);

                    System.out.println("Ocorreu um erro: "+ e);
                }
            });
        }
    }
    public void hitnome(View view){

        typeACC = "Entregador";

        editNome.setHint("Seu nome completo");
        editCPF.setHint("CPF");
    }
    public void hintloja(View view){

        typeACC = "Loja";

        editNome.setHint("Razão Social / Nome da Loja");
        editCPF.setHint("CNPJ");
        editEnderecoLoja.setVisibility(View.VISIBLE);


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

                        Toast.makeText(RegisterActivity.this, "O seu GPS está desativado! Por favor, ative o GPS para conseguir usar o Arbor Amorum!", Toast.LENGTH_LONG).show();
                        Toast.makeText(RegisterActivity.this, "Clique no FAB que centraliza a localização, pós isso, volte ao aplicativo!", Toast.LENGTH_LONG).show();

                        Uri uri = Uri.parse("https://www.google.pt/maps");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());

                    Geocoder geocoder = new Geocoder(RegisterActivity.this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        localização = addresses.get(0).getAddressLine(0);

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