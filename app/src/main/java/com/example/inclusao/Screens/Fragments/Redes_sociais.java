package com.example.inclusao.Screens.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inclusao.R;

public class Redes_sociais extends Fragment {
    private ImageView logofacebook;
    private ImageView logoyoutube;
    private ImageView logoinstagram;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciar_componentes();


        logoinstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirInstagram();
            }
        });

        logofacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFacebook();
            }
        });

        logoyoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirYoutube();
            }
        });

        // Resto do código
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_redes_sociais, container, false);
    }
    private void iniciar_componentes(){
        logoinstagram = getView().findViewById(R.id.logoinstagram);
        logofacebook = getView().findViewById(R.id.logofacebook);
        logoyoutube = getView().findViewById(R.id.logoyoutube);
    }

    private void abrirInstagram(){
        Uri uri = Uri.parse("https://www.instagram.com/educacaoinclusivaibirite/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void abrirFacebook(){
        Uri uri = Uri.parse("https://www.facebook.com/educacaoinclusivaibirite/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    private void abrirYoutube(){
        Uri uri = Uri.parse("https://www.youtube.com/channel/UCra5Oym5f9FrcKj6Y_61tSg?app=desktop");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}