package com.herorickystudios.pedefacil_entregas;

//Programado por HeroRickyGames

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editNome, editIdade, editCPF, editEmail, editPIX, editSenha;
    String nome, idade, CPF, email, PIX, senha, typeACC, getUID;
    RadioGroup radioAccType, radioPixType;
    FirebaseFirestore referencia = FirebaseFirestore.getInstance();


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
    }

    public void CadastroBtn(View view){
        //Strings
        nome = editNome.getText().toString();
        idade = editIdade.getText().toString();
        CPF = editCPF.getText().toString();
        email = editEmail.getText().toString();
        senha = editSenha.getText().toString();
        PIX = editPIX.getText().toString();


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

                getUID = FirebaseAuth.getInstance().getUid();

                int selectIDType = radioAccType.getCheckedRadioButtonId();
                int selectIDPIXType = radioPixType.getCheckedRadioButtonId();

                final RadioButton radioTypeAcc = (RadioButton) findViewById(selectIDType);
                final RadioButton radioIDPIXType = (RadioButton) findViewById(selectIDPIXType);

                if(radioTypeAcc.getText().equals("null")){
                    return;
                }

                if(radioIDPIXType.getText().equals("null")){
                    return;
                }

                String AccType = radioTypeAcc.getText().toString();
                String idPIXType = radioIDPIXType.getText().toString();


                if(AccType.equals("Sou um Entregador")){

                    typeACC = "Entregador";

                }else if(AccType.equals("Sou uma Loja")){

                    typeACC = "Loja";

                }

                Map<String, Object> user = new HashMap<>();
                user.put("nameCompleteUser", nome);
                user.put("idadeUser", idade);
                user.put("CPF", CPF);
                user.put("Email", email);
                user.put("Localização", email);
                user.put("PIX", PIX);
                user.put("Tipo de conta", typeACC);
                user.put("Metodo de PIX", idPIXType);

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

}