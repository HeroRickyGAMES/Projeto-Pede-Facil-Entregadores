package com.herorickystudios.pedefacil_entregas;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class configActivity extends AppCompatActivity {

    EditText editNameSt, editCPFSt, editIdadeSt, editChavePublicast, editChavePrivadaSt, edtiTextEndereco;
    private FirebaseFirestore usersDb;
    TextView textNome, textCPF, textChavePublica, textChaveSecreta, textEndereco;
    String UID, latitude, longitude;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        setTitle("Configurações");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UID = user.getUid();

        usersDb = FirebaseFirestore.getInstance();

        editNameSt = findViewById(R.id.editNameSt);
        editCPFSt = findViewById(R.id.editCPFSt);
        editIdadeSt = findViewById(R.id.editIdadeSt);
        editChavePublicast = findViewById(R.id.editChavePublicaSt);
        editChavePrivadaSt = findViewById(R.id.editChavePrivadaSt);
        textNome = findViewById(R.id.textNome);
        textCPF = findViewById(R.id.textCPF);
        textChavePublica = findViewById(R.id.textChavePublica);
        textChaveSecreta = findViewById(R.id.textChaveSecreta);
        edtiTextEndereco = findViewById(R.id.edtiTextEndereco);
        textEndereco = findViewById(R.id.textEndereco);

        Task<DocumentSnapshot> documentReference = usersDb.collection("Entregador").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                if(document.exists()){

                    edtiTextEndereco.setVisibility(View.INVISIBLE);
                    textEndereco.setVisibility(View.INVISIBLE);

                    editNameSt.setText(document.getString("nameCompleteUser"));
                    editCPFSt.setText(document.getString("CPF"));
                    editIdadeSt.setText(document.getString("idadeUser"));
                    editChavePublicast.setText(document.getString("publicKey"));
                    editChavePrivadaSt.setText(document.getString("secretKey"));
                }
            }
        });

        Task<DocumentSnapshot> documentReference2 = usersDb.collection("Loja").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();

                if(document.exists()){

                    textChavePublica.setVisibility(View.INVISIBLE);
                    textChaveSecreta.setVisibility(View.INVISIBLE);

                    edtiTextEndereco.setVisibility(View.VISIBLE);
                    textEndereco.setVisibility(View.VISIBLE);

                    editNameSt.setHint("Razão Social");
                    editCPFSt.setHint("CNPJ");
                    textNome.setText("Razão Social");
                    textCPF.setText("CNPJ");
                    editChavePublicast.setVisibility(View.INVISIBLE);
                    editChavePrivadaSt.setVisibility(View.INVISIBLE);

                    editNameSt.setText(document.getString("nameCompleteUser"));
                    editCPFSt.setText(document.getString("CPF"));
                    editIdadeSt.setText(document.getString("idadeUser"));
                    edtiTextEndereco.setText(document.getString("Localização"));

                }

            }
        });
    }
    public void confirmarbtn(View view){

        DocumentReference documentReference3 = usersDb.collection("Entregador").document(UID);

        documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                if(document.exists()){
                    DocumentReference doc = usersDb.collection("Entregador").document(UID);

                    Map<String, Object> data = new HashMap<>();
                    data.put("nameCompleteUser", editNameSt.getText().toString());
                    data.put("CPF", editCPFSt.getText().toString());
                    data.put("publicKey", editChavePublicast.getText().toString());
                    data.put("secretKey", editChavePrivadaSt.getText().toString());


                    Toast.makeText(configActivity.this, "Clicado!", Toast.LENGTH_SHORT).show();
                    doc.update(data);

                }
            }
        });

        DocumentReference documentReference4 = usersDb.collection("Loja").document(UID);

        documentReference4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                if(document.exists()){
                    DocumentReference doc = usersDb.collection("Loja").document(UID);

                    try {
                        List addressList = geocoder.getFromLocationName(edtiTextEndereco.getText().toString(), 1);
                        if (addressList != null && addressList.size() > 0) {

                            if(edtiTextEndereco.getText().toString().equals("")){
                                Toast.makeText(configActivity.this, "Por favor, preencha o campo de endereço.", Toast.LENGTH_SHORT).show();
                            }else{

                                Address address = (Address) addressList.get(0);
                                StringBuilder sb = new StringBuilder();

                                sb.append(address.getLatitude()).append("\n");
                                sb.append(address.getLongitude()).append("\n");

                                //Reconverte para Lat e long

                                latitude = String.valueOf(address.getLatitude());
                                longitude = String.valueOf(address.getLongitude());

                                System.out.println("Em Latitude e longitude " + latitude + " " +  longitude);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Map<String, Object> data = new HashMap<>();
                    data.put("nameCompleteUser", editNameSt.getText().toString());
                    data.put("CPF", editCPFSt.getText().toString());
                    data.put("Localização", edtiTextEndereco.getText().toString());
                    data.put("Latitude", latitude);
                    data.put("Longitude", longitude);

                    doc.update(data);

                }

            }
        });

    }

    public void onclickback(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}