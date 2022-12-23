package com.herorickystudiosoficial.pedefacil_entregas;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationService extends FirebaseMessagingService{

    Context context;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<cardsEntregas> list;
    cardsEntregas chaatTxt;

    @Override
    public void onCreate() {

        servicenotficiation();

        super.onCreate();
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String titulo = remoteMessage.getNotification().getTitle();
        String corpo = remoteMessage.getNotification().getBody();

        final String CHANNEL_ID = "HANDS_UP_NOTIFICATION";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Minha Notificação",
                    NotificationManager.IMPORTANCE_HIGH);

        getSystemService(NotificationManager.class).createNotificationChannel(channel);

            Notification.Builder notification  = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(titulo)
                    .setContentText(corpo)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(true);

            NotificationManagerCompat.from(this).notify(1, notification.build());

        }
    }
    private void servicenotficiation() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            System.out.println("Reconhecendo que está rodando de fundo!!");
            FirebaseFirestore usersDb;
            usersDb = FirebaseFirestore.getInstance();
            DocumentReference entregadorDocument = usersDb.collection("Entregador").document(user.getUid());


            Calendar c = Calendar.getInstance();
            String str = c.getTime().toString();

            int PMAM = c.get(Calendar.AM_PM);

            String calendarioData = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);

            String hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);

            String juntos = calendarioData + " " + hora;

            entregadorDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(value.exists()){

                        usersDb.collection("Solicitacoes-Entregas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()){

                                    if(dataSnapshot.exists()){


                                    //Adições para RecycleView
                                    list = new ArrayList<cardsEntregas>();

                                    chaatTxt = new cardsEntregas(dataSnapshot.getString("Nome do produto"), dataSnapshot.getString("Data de publicação"), "", "", "", "", "", "","", "");

                                    int sizemenos1 = list.size();

                                    System.out.println(sizemenos1);

                                    list.add(chaatTxt);

                                    if(juntos.equals(list.get(sizemenos1).getNome())){
                                        final String CHANNEL_ID = "HANDS_UP_NOTIFICATION";
                                        NotificationChannel channel;
                                        Notification.Builder builder;

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                            channel = new NotificationChannel(
                                                    CHANNEL_ID,
                                                    "Hands Up Notification",
                                                    NotificationManager.IMPORTANCE_HIGH
                                            );
                                            getSystemService(NotificationManager.class).createNotificationChannel(channel);

                                            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                                                    .setContentTitle("Novo produto adicionado!")
                                                    .setContentText(list.get(sizemenos1).getTitulo())
                                                    .setSmallIcon(R.drawable.ic_launcher)
                                                    .setAutoCancel(true);

                                            NotificationManagerCompat.from(getApplicationContext()).notify(1,builder.build());

                                            servicenotficiation();
                                        }
                                    }
                                }
                            }
                            }
                        });
                    }
                }
            });
        }
    }
}
