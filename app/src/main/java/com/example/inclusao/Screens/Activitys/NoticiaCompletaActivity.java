package com.example.inclusao.Screens.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.inclusao.R;

public class NoticiaCompletaActivity extends AppCompatActivity {
    private static final String TAG = "NoticiaCompletaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia_completa);

        // Recuperar os extras da Intent
        Intent intent = getIntent();
        if (intent != null) {
            String titulo = intent.getStringExtra("titulo");
            String conteudo = intent.getStringExtra("conteudo");
            String imageUrl = intent.getStringExtra("imageUrl");

            // Adicione os logs para verificar os valores recebidos
            Log.d(TAG, "Titulo: " + titulo);
            Log.d(TAG, "Conteudo: " + conteudo);
            Log.d(TAG, "ImageUrl: " + imageUrl);

            // Exibir o conteúdo da notícia nos TextViews
            TextView textViewTitulo = findViewById(R.id.textViewTituloCompleto);
            TextView textViewConteudo = findViewById(R.id.textViewConteudoCompleto);
            ImageView imageViewCompleto = findViewById(R.id.imageViewCompleto);

            textViewTitulo.setText(titulo);
            textViewConteudo.setText(conteudo);

            // Use uma biblioteca de carregamento de imagens, como o Glide, para carregar a imagem
            // na ImageView imageViewCompleto aqui
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageViewCompleto);
        } else {
            // Caso não haja extras na Intent
            Log.d(TAG, "Intent não contém extras.");
        }
    }
}


