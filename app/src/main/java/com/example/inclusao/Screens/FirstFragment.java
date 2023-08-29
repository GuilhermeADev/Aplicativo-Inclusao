package com.example.inclusao.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.inclusao.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FirstFragment extends Fragment {

    private Handler handler = new Handler();
    private final int INTERVAL = 3000; // Intervalo em milissegundos (3 segundos)
    private int currentIndex = 0; // Índice inicial


    private ViewFlipper viewFlipper;
    private String imageUrl; // URL da imagem do Firebase Storage

    public FirstFragment() {
        // Required empty public constructor
    }

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        androidx.appcompat.widget.Toolbar toolbar=requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Inclusão - Menu");

    }
    private int[] backgroundImages = {
            R.drawable.adolescentes,
            R.drawable.exit,
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentIndex = adapter.getNextIndex(currentIndex);
            viewPager.setCurrentItem(currentIndex);
            handler.postDelayed(this, INTERVAL);
        }
    };
    BackgroundCarouselAdapter adapter;
    ViewPager2 viewPager;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

         viewPager =view.findViewById(R.id.viewPager);
         adapter = new BackgroundCarouselAdapter(backgroundImages);
        viewPager.setAdapter(adapter);

        handler.postDelayed(runnable, INTERVAL);

        adapter.setOnItemClickListener(new BackgroundCarouselAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Executar ação com base na posição clicada
                switch (position) {
                    case 0:
                        Toast.makeText(getContext(), "0!", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getContext(), "1!", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getContext(), "2!", Toast.LENGTH_SHORT).show();
                        break;
                    // ... adicione casos para as outras imagens
                }
            }
        });
        viewPager.setAdapter(adapter);

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



        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView); // Substitua R.id.bottomNavigationView pelo ID correto do seu BottomNavigationView

        CardView card2 = view.findViewById(R.id.card2);

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trocar o ícone selecionado
                bottomNavigationView.findViewById(R.id.secondFragment).performClick(); // Substitua menu_item_id pelo ID do item de menu que você deseja clicar
            }
        });

        CardView card8= view.findViewById(R.id.card8);

        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trocar o ícone selecionado
                bottomNavigationView.findViewById(R.id.thirdFragment).performClick(); // Substitua menu_item_id pelo ID do item de menu que você deseja clicar
            }
        });


        return view;
    }
}