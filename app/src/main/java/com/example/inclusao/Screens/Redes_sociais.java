package com.example.inclusao.Screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.inclusao.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Redes_sociais#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Redes_sociais extends Fragment {


    private ImageView logofacebook;
    private ImageView logoyoutube;
    private ImageView logoinstagram;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public Redes_sociais() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Redes_sociais.
     */
    // TODO: Rename and change types and number of parameters
    public static Redes_sociais newInstance(String param1, String param2) {
        Redes_sociais fragment = new Redes_sociais();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniciar_componentes();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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