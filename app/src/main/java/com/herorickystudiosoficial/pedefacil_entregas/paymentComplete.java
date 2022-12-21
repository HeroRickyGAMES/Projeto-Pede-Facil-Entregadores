package com.herorickystudiosoficial.pedefacil_entregas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class paymentComplete extends AppCompatActivity {

    String PrecoRestante, UidEntregador, idProduto, tituloProduto, PublicKey, SecretKey, customerID, EphericalKey, ClientSecret;

    private FirebaseFirestore usersDb;
    PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_complete);

        setTitle("Pagamento");

        PrecoRestante = getIntent().getExtras().getString("PrecoRestante");
        UidEntregador = getIntent().getExtras().getString("UidEntregador");
        idProduto = getIntent().getExtras().getString("idProduto");
        tituloProduto = getIntent().getExtras().getString("tituloProduto");

        PublicKey = getIntent().getExtras().getString("PublicKeyEntregador");
        SecretKey = getIntent().getExtras().getString("SecretKeyEntregador");

        System.out.println("Preco restante: " + PrecoRestante);

        System.out.println("ID do produto" + idProduto);
        System.out.println("ID do Entregador" + UidEntregador);
        System.out.println("TituloProduto" + tituloProduto);
        System.out.println("ID do Produto" + idProduto);
        System.out.println("PublicKey final" + PublicKey);
        System.out.println("Secret final" + SecretKey);

        usersDb = FirebaseFirestore.getInstance();

        DocumentReference entregaDoc =  usersDb.collection("Entregador").document(UidEntregador);

        entregaDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();

                PaymentConfiguration.init(getApplicationContext(), PublicKey);

            }
        });
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });

        pagarBTNMetodo();
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {

        if(paymentSheetResult instanceof PaymentSheetResult.Completed){

            Toast.makeText(this, "Pagamento feito com sucesso!", Toast.LENGTH_SHORT).show();


            DocumentReference entrega =  usersDb.collection("Solicitacoes-Entregas").document(idProduto);

            Map<String, Object> data = new HashMap<>();
            data.put("statusDoProduto", "Pronto para a entrega");
            data.put("RazãodoEntregador", "");
            data.put("LocalizacaoEntregador", "");
            data.put("LatitudeDoEntregador", "");
            data.put("logitudeEntregador", "");
            entrega.update(data);

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);

        }else{
            Toast.makeText(this, "Pagamento cancelado, por favor, tente novamente!", Toast.LENGTH_SHORT).show();

            DocumentReference entrega =  usersDb.collection("Solicitacoes-Entregas").document(idProduto);

            Map<String, Object> data = new HashMap<>();
            data.put("statusDoProduto", "Pagas todas as taxas");
            data.put("PrecoRestante", PrecoRestante);

            entrega.update(data);

            Intent intent = new Intent(paymentComplete.this,MainActivity.class);
            startActivity(intent);

            finish();
        }

    }

    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    EphericalKey = object.getString("id");
                    System.out.println(EphericalKey);

                    getClientSecret(customerID, EphericalKey);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void getClientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    ClientSecret = object.getString("client_secret");

                    System.out.println(ClientSecret);

                    PaymentFlow();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                //header.put("Stripe-Version", "2022-11-15");
                return header;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("amount", PrecoRestante);
                params.put("currency", "brl");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void PaymentFlow() {

        paymentSheet.presentWithPaymentIntent(
                ClientSecret,
                new PaymentSheet.Configuration("Taxa do desenvolvedor", new PaymentSheet.CustomerConfiguration(
                        customerID,
                        EphericalKey
                ))
        );
    }

    public void pagarBTNMetodo(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    customerID = object.getString("id");

                    System.out.println(customerID);

                    getEphericalKey(customerID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                new AlertDialog.Builder(paymentComplete.this)
                        .setTitle("Ocorreu um problema ao efetuar o pagamento!")
                        .setMessage("Por favor, verifique com seu entregador, se ele preencheu as chaves Publicas e Privadas da Stripe no perfil dele! \n Caso ele tenha feito isso, peça para que ele verifique se está apto para receber pagamentos na Stripe!")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DocumentReference entrega =  usersDb.collection("Solicitacoes-Entregas").document(idProduto);

                                Map<String, Object> data = new HashMap<>();
                                data.put("statusDoProduto", "Pagas todas as taxas");
                                data.put("PrecoRestante", PrecoRestante);

                                entrega.update(data);

                                Intent intent = new Intent(paymentComplete.this,MainActivity.class);
                                startActivity(intent);

                                finish();
                            }
                        }).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);

                return super.getParams();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}