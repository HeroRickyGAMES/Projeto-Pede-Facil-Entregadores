package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private final String YOUR_CLIENT_ID = "";
    private FirebaseFirestore usersDb;
    String UID;
    private String nomeUser;

    FloatingActionButton fabLojaAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(getBaseContext(), PushNotificationService.class));

        fabLojaAdd = findViewById(R.id.fabLojaAdd);

        fabLojaAdd.hide();


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

                }
            }
        });
    }
    public void onclickfab(View view){

        //Manda para a nova Activity
        Intent intent = new Intent(this, mandarSolicitacao.class);
        startActivity(intent);

    }
}