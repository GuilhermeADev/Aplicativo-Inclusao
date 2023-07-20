package com.example.inclusao.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.inclusao.R;

public class FirstFragment extends Fragment {

    private ViewFlipper viewFlipper;
    private String imageUrl; // URL da imagem do Firebase Storage

    public FirstFragment() {
        // Required empty public constructor
    }

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        // Configurar o ViewFlipper
        viewFlipper = view.findViewById(R.id.viewFlipper);
        viewFlipper.setFlipInterval(5000); // Intervalo de troca das imagens (5000ms = 5 segundos)
        viewFlipper.startFlipping();

        // Recuperar a URL da imagem de SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        imageUrl = sharedPreferences.getString("imageUrl", "");

        // Se houver uma URL de imagem válida, exibir a imagem no ViewFlipper
        if (!imageUrl.isEmpty()) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageView);

            viewFlipper.addView(imageView);
        }


        CardView card1 = view.findViewById(R.id.card1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Na atividade atual
                Intent intent = new Intent(getActivity(), Noticias.class);
                // Adicione a URL da imagem como extra na Intent
                intent.putExtra("imageUrl", imageUrl);
                startActivity(intent);
            }
        });

        return view;
    }
}