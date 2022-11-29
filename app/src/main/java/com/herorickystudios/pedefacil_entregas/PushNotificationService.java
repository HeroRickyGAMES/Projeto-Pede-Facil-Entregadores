package com.herorickystudios.pedefacil_entregas;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class PushNotificationService extends Service {

    private static final int PERMISSION_FINE_LOCATION = 99;

    //API para a localização dos usuarios
    FusedLocationProviderClient fusedLocationProviderClient;

    String UID;

    private String currentLocation;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        updateGPS();
        System.out.println("Serviço rodando de fundo!!!");
        return START_STICKY;
    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location == null){

                        Toast.makeText(PushNotificationService.this, "O seu GPS está desativado! Por favor, ative o GPS para conseguir usar o Arbor Amorum!", Toast.LENGTH_LONG).show();
                        Toast.makeText(PushNotificationService.this, "Clique no FAB que centraliza a localização, pós isso, volte ao aplicativo!", Toast.LENGTH_LONG).show();

                        Uri uri = Uri.parse("https://www.google.pt/maps");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                    Geocoder geocoder = new Geocoder(PushNotificationService.this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        currentLocation = addresses.get(0).getAddressLine(0);
                        System.out.println("LOCALIZAÇÃO EXATA: " + currentLocation);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();


                        FirebaseFirestore usersDb = FirebaseFirestore.getInstance();
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        UID = user.getUid();

                        DocumentReference entregadorDocument =  usersDb.collection("Entregador").document(user.getUid());

                        Map<String, Object> userhs = new HashMap<>();
                        userhs.put("Localização", currentLocation);

                        entregadorDocument.update(userhs);

                    } catch (Exception e) {
                        System.out.println("Não foi possivel encontrar sua localização!" + e);
                    }
                }
            });
        }
    }

}
