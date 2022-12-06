package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class mandarSolicitacao extends AppCompatActivity {

    Double latitude, longitude, distance;
    private String lat, log, UID, latDb, longDB, calculoporKm, simounao, autorname, autorlocale;
    private FirebaseFirestore usersDb;
    EditText editNomeProduto, editLocalização;
    TextView textDistancia, textPreço;
    Button buttonCalcularDistancia;
    Float calculodePreco;
    int selectIDSim, selectIDNao;
    boolean sim, nao;
    RadioButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandar_solicitacao);

        editNomeProduto = findViewById(R.id.editNomeProduto);
        editLocalização = findViewById(R.id.editLocalização);
        textDistancia = findViewById(R.id.textDistancia);
        textPreço = findViewById(R.id.textPreço);
        buttonCalcularDistancia = findViewById(R.id.buttonCalcularDistancia);

        setTitle("Adicionar Item");

        updateGPS();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UID = user.getUid();

        usersDb = FirebaseFirestore.getInstance();


        final RadioGroup group = (RadioGroup) findViewById(R.id.radioSimouNao);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                button = (RadioButton) group.findViewById(checkedId);
                simounao = button.getText().toString();


                System.out.println(simounao);


                if(simounao.equals("Sim")){
                    sim = true;
                }
                if(simounao.equals("Não")){
                    nao = true;
                }
            }
        });

        DocumentReference LojaDocument =  usersDb.collection("Loja").document(user.getUid());
        DocumentReference CalculoDocument =  usersDb.collection("Server").document("ServerValues");

        LojaDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                if(document.exists()){

                    latDb = document.getString("Latitude");
                    longDB = document.getString("Longitude");
                    autorname = document.getString("nameCompleteUser");
                    autorlocale = document.getString("Localização");

                }
            }
        });

        CalculoDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if(document.exists()) {

                    calculoporKm = document.getString("valorPorKm");

                }
            }
        });

    }
    public void btn(View view){

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String result = null;

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

            latitude = Double.valueOf(latDb);
            longitude = Double.valueOf(longDB);

            Double late = Double.valueOf(lat);
            Double loge = Double.valueOf(log);


            System.out.println("LOCALIZAÇÃO EXATA EM CODIGO: " + latitude + longitude);

            LatLng inicial = new LatLng(latitude, longitude);
            LatLng endpoint = new LatLng(late, loge);

            distance = SphericalUtil.computeDistanceBetween(inicial, endpoint);


            //Calcula a distancia de um ponto a para um ponto b com o LatLng
            Toast.makeText(mandarSolicitacao.this, "A distancia é  \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();

            textDistancia.setText("Distancia do estabelecimento até o local: " + String.format("%.2f", distance / 1000) + "km");

            String kms = String.format("%.2f", distance / 10000).replaceAll("," , "");

            if(simounao == null){

            }else{

                if(simounao.equals("Sim")){

                    calculodePreco = Float.parseFloat(kms) * Float.parseFloat(calculoporKm) / 10 + 5;

                }else{

                    calculodePreco = Float.parseFloat(kms) * Float.parseFloat(calculoporKm) / 10;

                }

                DecimalFormat formatador = new DecimalFormat("0.00");

                textPreço.setText("Preço: R$ " + formatador.format(calculodePreco));

                System.out.println("Distancia: " + calculodePreco);

            }
        }

        if( editNomeProduto.getText().toString().equals("") || editLocalização.getText().toString().equals("")){

            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();

        }else{

            sandtoDB();

        }
    }

    private void updateGPS() {

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

                    latitude = Double.valueOf(latDb);
                    longitude = Double.valueOf(longDB);

                    Double late = Double.valueOf(lat);
                    Double loge = Double.valueOf(log);


                    System.out.println("LOCALIZAÇÃO EXATA EM CODIGO: " + latitude + longitude);

                    LatLng inicial = new LatLng(latitude, longitude);
                    LatLng endpoint = new LatLng(late, loge);

                    distance = SphericalUtil.computeDistanceBetween(inicial, endpoint);


                    //Calcula a distancia de um ponto a para um ponto b com o LatLng
                    Toast.makeText(mandarSolicitacao.this, "A distancia é  \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();

                    textDistancia.setText("Distancia do estabelecimento até o local: " + String.format("%.2f", distance / 1000) + "km");

                    String kms = String.format("%.2f", distance / 10000).replaceAll("," , "");

                    if(simounao == null){

                    }else{

                    if(simounao.equals("Sim")){

                        calculodePreco = Float.parseFloat(kms) * Float.parseFloat(calculoporKm) / 10 + 5;

                    }else{

                        calculodePreco = Float.parseFloat(kms) * Float.parseFloat(calculoporKm) / 10;

                    }

                    DecimalFormat formatador = new DecimalFormat("0.00");

                    textPreço.setText("Preço: R$ " + formatador.format(calculodePreco));

                    System.out.println("Distancia: " + calculodePreco);

                }
              }
            }
        });
    }
    public void sandtoDB(){

        Map<String, Object> data = new HashMap<>();
        data.put("Pertence a", autorname);
        data.put("Localização", autorlocale);
        data.put("Nome do produto", editNomeProduto.getText().toString());
        data.put("Local de Entrega", editLocalização.getText().toString());
        data.put("Distancia", String.format("%.2f", distance / 1000) + "km");
        data.put("statusDoProduto", "Ativo");
        data.put("entreguePor", "");


        if(simounao.equals("Sim")){

            data.put("É retornavel", true);

        }else{

            data.put("É retornavel", false);

        }

        data.put("Preço", calculodePreco + ",00");

        usersDb.collection("Solicitacoes-Entregas").add(data);
    }
}