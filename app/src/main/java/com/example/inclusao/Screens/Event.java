package com.example.inclusao.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inclusao.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

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

        nav_view =(NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.getout) {

                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(Event.this, FormLogin.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return true;
                }
                if(id == R.id.share){
                    // Criar o texto que você deseja compartilhar
                    String textoParaCompartilhar = "Experimente este aplicativo incrível!";

                    // Criar uma intenção de compartilhamento
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, textoParaCompartilhar);

                    // Iniciar o seletor de compartilhamento
                    startActivity(Intent.createChooser(intent, "Compartilhar via"));
                }

                return true;
            }
        });




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