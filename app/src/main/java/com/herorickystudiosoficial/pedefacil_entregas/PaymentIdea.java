package com.herorickystudiosoficial.pedefacil_entregas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class PaymentIdea extends AppCompatActivity {

    String PublicKeyIdea, pagardepois, SecretKeyIdea, idProduto, UidEntregador, tituloProduto, PrecoRestante, taxadoIdealista, customerID, EphericalKey, ClientSecret, PublicKeyEntregador, SecretKeyEntregador;
    private FirebaseFirestore usersDb;
    PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_idea);
        setTitle("Taxas");

        idProduto =  getIntent().getExtras().getString("idProduto");
        UidEntregador =  getIntent().getExtras().getString("UidEntregador");
        tituloProduto =  getIntent().getExtras().getString("tituloProduto");;
        PrecoRestante =  getIntent().getExtras().getString("PrecoRestante");
        PublicKeyEntregador = getIntent().getExtras().getString("PublicKeyEntregador");
        SecretKeyEntregador = getIntent().getExtras().getString("SecretKeyEntregador");
        pagardepois = getIntent().getExtras().getString("pagardepois");

        System.out.println("ID do produto" + idProduto);

        usersDb = FirebaseFirestore.getInstance();

        DocumentReference entregaDoc =  usersDb.collection("Server").document("ServerValues");

        entregaDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();

                PublicKeyIdea = document.getString("PublicKeyIdea");
                SecretKeyIdea = document.getString("SecretKeyIdea");
                taxadoIdealista = document.getString("taxadoDev");

                PaymentConfiguration.init(getApplicationContext(), PublicKeyIdea);
                System.out.println(PublicKeyIdea);
                System.out.println(SecretKeyIdea);

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

            Integer taxadoIdealistaa = Integer.valueOf(taxadoIdealista);
            Integer preco = Integer.valueOf(PrecoRestante) - taxadoIdealistaa;

            Intent intent = new Intent(this, paymentComplete.class);

            intent.putExtra("PrecoRestante", preco.toString());
            intent.putExtra("UidEntregador", UidEntregador);
            intent.putExtra("idProduto", idProduto);
            intent.putExtra("tituloProduto", tituloProduto);
            intent.putExtra("PublicKeyEntregador", PublicKeyEntregador);
            intent.putExtra("SecretKeyEntregador", SecretKeyEntregador);
            intent.putExtra("pagardepois", pagardepois);

            startActivity(intent);


        }else{
            Toast.makeText(this, "Pagamento cancelado, por favor, tente novamente!", Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(this, MainActivity.class);
            startActivity(intent2);
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
                header.put("Authorization", "Bearer " + SecretKeyIdea);
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
                header.put("Authorization", "Bearer " + SecretKeyIdea);
                //header.put("Stripe-Version", "2022-11-15");
                return header;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Integer taxadoIdealistaa = Integer.valueOf(taxadoIdealista);
                Integer preco = Integer.valueOf(PrecoRestante) - taxadoIdealistaa;

                Map<String, String> params = new HashMap<>();
                params.put("amount", taxadoIdealistaa.toString());
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
                new PaymentSheet.Configuration("Taxa do idealista", new PaymentSheet.CustomerConfiguration(
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
                System.out.println(error.networkResponse.toString());

                Toast.makeText(PaymentIdea.this, error.networkResponse.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKeyIdea);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }

            @Nullable
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