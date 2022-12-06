package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String YOUR_CLIENT_ID = "";
    private FirebaseFirestore usersDb;
    private String UID, nomeUser, lojaName, productName, lojaLocal, entregaLocal, preco, statusDoProduto, entreguePor, uidEntregador, latDb, longDB, lat, log, userLatitude, userLongitude, EndereçoEntregador;
    private double latitude, longitude, distance;
    private RecyclerView viewEntregas;
    private RecyclerView.Adapter entregasAdapter;
    entregasAdapter adapter;
    private RecyclerView.LayoutManager entregasLayoutManager;
    ArrayList<cardsEntregas> list;
    FloatingActionButton fabLojaAdd;
    cardsEntregas chaatTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewEntregas = findViewById(R.id.listEntregas);

        startService(new Intent(getBaseContext(), PushNotificationService.class));

        fabLojaAdd = findViewById(R.id.fabLojaAdd);

        fabLojaAdd.hide();
        //Adições para RecycleView
        list = new ArrayList<cardsEntregas>();
        adapter = new entregasAdapter(this, list);
        viewEntregas.setAdapter(adapter);
        viewEntregas.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UID = user.getUid();

        usersDb = FirebaseFirestore.getInstance();


        DocumentReference entregadorDocument =  usersDb.collection("Entregador").document(user.getUid());
        DocumentReference LojaDocument =  usersDb.collection("Loja").document(user.getUid());

        entregadorDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                if(document.exists()){
                    setTitle("Lista de itens para entrega proximos a você");
                    nomeUser = document.getString("nameCompleteUser");
                    userLatitude = document.getString("Latitude");
                    userLongitude = document.getString("Longitude");

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    usersDb.collection("Solicitacoes-Entregas").whereEqualTo("statusDoProduto", "Ativo").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for(DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()){

                                lojaName = dataSnapshot.get("Pertence a").toString();
                                productName = dataSnapshot.get("Nome do produto").toString();
                                lojaLocal = dataSnapshot.get("Localização").toString();
                                entregaLocal = dataSnapshot.get("Local de Entrega").toString();
                                preco = dataSnapshot.get("Preço").toString();
                                statusDoProduto = dataSnapshot.get("statusDoProduto").toString();
                                entreguePor = dataSnapshot.get("entreguePor").toString();
                                uidEntregador = dataSnapshot.get("uidEntregador").toString();

                                try {
                                    List addressList = geocoder.getFromLocationName(lojaLocal, 1);
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

                                //latitude = Double.valueOf(userLatitude);
                                latitude = Double.valueOf(userLatitude);
                                longitude = Double.valueOf(userLongitude);

                                Double late = Double.valueOf(lat);
                                Double loge = Double.valueOf(log);

                                LatLng inicial = new LatLng(latitude, longitude);
                                LatLng endpoint = new LatLng(late, loge);

                                distance = SphericalUtil.computeDistanceBetween(inicial, endpoint);

                                chaatTxt = new cardsEntregas( "Pertence á: " + lojaName, "Nome do produto: " + productName, "Local da loja: " + lojaLocal, "Local de entrega: " + entregaLocal, "Distancia de você: " +  String.format("%.2f", distance / 1000) +  " km" , "R$: " + preco, statusDoProduto, entreguePor, uidEntregador);

                                list.add(chaatTxt);

                                adapter.notifyDataSetChanged();

                            }
                        }
                    });
                }
            }
        });



        LojaDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                if(document.exists()){
                    fabLojaAdd.show();
                    setTitle("Itens adicionados por sua loja");
                    nomeUser = document.getString("nameCompleteUser");

                    usersDb.collection("Solicitacoes-Entregas").whereEqualTo("Pertence a", nomeUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for(DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()){

                                lojaName = dataSnapshot.get("Pertence a").toString();
                                productName = dataSnapshot.get("Nome do produto").toString();
                                lojaLocal = dataSnapshot.get("Localização").toString();
                                entregaLocal = dataSnapshot.get("Local de Entrega").toString();
                                preco = dataSnapshot.get("Preço").toString();
                                statusDoProduto = dataSnapshot.get("statusDoProduto").toString();
                                entreguePor = dataSnapshot.get("entreguePor").toString();
                                uidEntregador = dataSnapshot.get("uidEntregador").toString();


                                chaatTxt = new cardsEntregas( "Pertence á: " + lojaName, "Nome do produto: " + productName, "Local da loja: " + lojaLocal, "Local de entrega: " + entregaLocal, "" , "R$: " + preco, statusDoProduto, entreguePor, uidEntregador);

                                list.add(chaatTxt);

                                adapter.notifyDataSetChanged();

                            }
                        }
                    });

                }
            }
        });
    }
    public void onclickfab(View view){
        //Manda para a nova Activity
        Intent intent = new Intent(this, mandarSolicitacao.class);
        startActivity(intent);
    }
    public void gotoSettings(View view){

        Intent intent = new Intent(this, configActivity.class);
        startActivity(intent);

    }
}