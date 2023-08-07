package com.example.inclusao.Screens;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inclusao.R;
import com.google.android.material.navigation.NavigationView;

public class Event extends AppCompatActivity {
    TextView titulo;
    TextView descricao;
    ImageView imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        NavigationView nav_view = findViewById(R.id.nav_view);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();




        titulo=findViewById(R.id.titulo);
        descricao=findViewById(R.id.descricao);
        imagem=findViewById(R.id.imagem);
        Intent intent=getIntent();

        String tituloEvento = intent.getStringExtra("tituloEvento");
        String descricaoEvento = intent.getStringExtra("descricaoEvento");
        String imagemEvento = intent.getStringExtra("imagemEvento");
        String diaEvento = intent.getStringExtra("diaEvento");
        String mesEvento = intent.getStringExtra("mesEvento");
        String anoEvento = intent.getStringExtra("anoEvento");

        titulo.setText(tituloEvento);
        descricao.setText(descricaoEvento+" no dia de "+diaEvento+"/"+mesEvento+"/"+anoEvento);

        Glide.with(Event.this)
                .load(imagemEvento)
                .into(imagem);
    }

}