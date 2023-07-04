package com.example.inclusao.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.inclusao.R;

public class WelcomeScreen1 extends AppCompatActivity {

    private TextView proximo;
    private TextView pular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen1);
        iniciarcomponentes();

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen1.this, WelcomeScreen2.class);
                startActivity(intent);
            }
        });
        pular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen1.this, Home.class);
                startActivity(intent);
            }
        });
    }

    private void iniciarcomponentes(){
        proximo = findViewById(R.id.Próximo);
        pular = findViewById(R.id.Pular);
    }
}