package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;

public class mandarSolicitacao extends AppCompatActivity {

    Double latitude, longitude, distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandar_solicitacao);

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

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        System.out.println("LOCALIZAÇÃO EXATA EM CODIGO: " + latitude + longitude);

                        LatLng sydney = new LatLng(latitude, longitude);
                        LatLng Brisbane = new LatLng(-23.4744447, -46.5302229);

                        distance = SphericalUtil.computeDistanceBetween(sydney, Brisbane);

                        Toast.makeText(mandarSolicitacao.this, "A distancia é  \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();

                        System.out.println("Distancia: " + distance);
                    }
                }
            });
        }
    }
}