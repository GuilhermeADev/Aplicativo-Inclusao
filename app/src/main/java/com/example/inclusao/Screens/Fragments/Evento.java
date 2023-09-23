package com.example.inclusao.Screens.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inclusao.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Evento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Evento extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Evento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_event.
     */
    // TODO: Rename and change types and number of parameters
    public static Evento newInstance(String param1, String param2) {
        Evento fragment = new Evento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    TextView tituloText;
    TextView descricaoText;
    ImageView imageView;

    String dia;
    String mes;
    String ano;
    String imagem, titulo, descricao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_event, container, false);


        tituloText=view.findViewById(R.id.titulo);
        descricaoText=view.findViewById(R.id.descricao);
        imageView=view.findViewById(R.id.imagem);

        Bundle args = getArguments();
        if (args != null) {
             titulo = args.getString("tituloEvento");
             descricao = args.getString("descricaoEvento");
             imagem = args.getString("imagemEvento");
             dia = args.getString("diaEvento");
             mes = args.getString("mesEvento");
             ano = args.getString("anoEvento");

            // Use os valores recuperados para atualizar a interface do usuário
        }

        tituloText.setText(titulo);
        descricaoText.setText(descricao+" no dia de "+dia+"/"+mes+"/"+ano);

        Glide.with(requireContext())
                .load(imagem)
                .into(imageView);

        return view;
    }
}