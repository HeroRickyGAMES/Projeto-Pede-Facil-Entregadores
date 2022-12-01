package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class mandarSolicitacao extends AppCompatActivity {

    Double latitude, longitude, distance;
    private String lat, log;

    EditText editNomeProduto, editLocalização;
    TextView textDistancia, textPreço;
    RadioGroup radioSimouNao;
    RadioButton radioSim, radioNao;
    Button buttonCalcularDistancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandar_solicitacao);

        editNomeProduto = findViewById(R.id.editNomeProduto);
        editLocalização = findViewById(R.id.editLocalização);
        textDistancia = findViewById(R.id.textDistancia);
        textPreço = findViewById(R.id.textPreço);
        radioSimouNao = findViewById(R.id.radioSimouNao);
        radioSim = findViewById(R.id.radioSim);
        radioNao = findViewById(R.id.radioNao);
        buttonCalcularDistancia = findViewById(R.id.buttonCalcularDistancia);

        setTitle("Adicionar Item");

        updateGPS();
    }
    public void btn(View view){

    }


    private void updateGPS() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location == null){

                    }else{
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        String result = null;

                        buttonCalcularDistancia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(editLocalização.getText().toString().equals("")) {

                                    Toast.makeText(mandarSolicitacao.this, "Preencha o campo de Localização!", Toast.LENGTH_SHORT).show();

                                }else{
                               try {
                                    List addressList = geocoder.getFromLocationName(editLocalização.getText().toString(), 1);
                                    if (addressList != null && addressList.size() > 0) {
                                        Address address = (Address) addressList.get(0);
                                        StringBuilder sb = new StringBuilder();

                                        sb.append(address.getLatitude()).append("\n");
                                        sb.append(address.getLongitude()).append("\n");

                                        //Reconverte para Lat e long

                                        lat = String.valueOf(address.getLatitude());
                                        log = String.valueOf(address.getLongitude());
                                        System.out.println("Em Latitude e longitude " + lat + " " +  log);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                Double late = Double.valueOf(lat);
                                Double loge = Double.valueOf(log);


                                System.out.println("LOCALIZAÇÃO EXATA EM CODIGO: " + latitude + longitude);

                                LatLng inicial = new LatLng(latitude, longitude);
                                LatLng endpoint = new LatLng(late, loge);

                                distance = SphericalUtil.computeDistanceBetween(inicial, endpoint);


                                //Calcula a distancia de um ponto a para um ponto b com o LatLng
                                Toast.makeText(mandarSolicitacao.this, "A distancia é  \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();

                                textDistancia.setText("Distancia do estabelecimento até o local: " + String.format("%.2f", distance / 1000) + "km");

                                System.out.println("Distancia: " + distance);
                            }
                           }
                        });
                    }
                }
            });
        }
    }
}