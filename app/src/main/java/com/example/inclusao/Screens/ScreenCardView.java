package com.example.inclusao.Screens;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.inclusao.R;

public class ScreenCardView extends AppCompatActivity {

    private ScreenCardView screenCardView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screencardview);

        Button button = findViewById(R.id.voltar);

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        //NOTIFICAÇÃO
        Button but=findViewById(R.id.notify);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.BASE){
            NotificationChannel channel=new NotificationChannel("notification","notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crie um intent para a Activity que você deseja abrir
                Intent intent = new Intent(ScreenCardView.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(ScreenCardView.this, 0, intent, 0);

                // Construa a notificação com o PendingIntent
                NotificationCompat.Builder builder = new NotificationCompat.Builder(ScreenCardView.this, "notification")
                        .setSmallIcon(R.drawable.chat)
                        .setContentTitle("MyFirstNotification")
                        .setContentText("HeHEBoy")
                        .setContentIntent(pendingIntent)  // Define o PendingIntent
                        .setAutoCancel(true);  // Remove a notificação ao ser clicada

                // Notifique usando o NotificationManagerCompat
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ScreenCardView.this);
                notificationManager.notify(1, builder.build());


            }

        });
    }



}

