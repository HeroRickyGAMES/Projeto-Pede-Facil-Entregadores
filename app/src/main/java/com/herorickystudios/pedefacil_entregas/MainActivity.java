package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String YOUR_CLIENT_ID = "";
    private FirebaseFirestore usersDb;
    String UID;
    private String nomeUser;
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

                    chaatTxt = new cardsEntregas(nomeUser);

                    list.add(chaatTxt);

                    adapter.notifyDataSetChanged();
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