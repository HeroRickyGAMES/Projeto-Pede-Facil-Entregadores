package com.herorickystudios.pedefacil_entregas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class configActivity extends AppCompatActivity {

    EditText editNameSt, editCPFSt, editIdadeSt, editChavePublicast, editChavePrivadaSt;
    private FirebaseFirestore usersDb;
    TextView textNome, textCPF, textChavePublica, textChaveSecreta;
    String UID;

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

        Task<DocumentSnapshot> documentReference = usersDb.collection("Entregador").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                if(document.exists()){

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
                    editNameSt.setHint("Razão Social");
                    editCPFSt.setHint("CNPJ");
                    editChavePublicast.setVisibility(View.INVISIBLE);
                    editChavePrivadaSt.setVisibility(View.INVISIBLE);

                    editNameSt.setText(document.getString("nameCompleteUser"));
                    editCPFSt.setText(document.getString("CPF"));
                    editIdadeSt.setText(document.getString("idadeUser"));

                }

            }
        });

    }
    public void onclickback(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}