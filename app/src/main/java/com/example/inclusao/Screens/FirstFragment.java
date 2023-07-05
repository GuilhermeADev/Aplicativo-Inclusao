package com.example.inclusao.Screens;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.inclusao.Adm.adm_postnews;
import com.example.inclusao.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;


import org.checkerframework.common.subtyping.qual.Bottom;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class FirstFragment extends Fragment {

    private FirebaseFirestore db;
    private ListenerRegistration userListener;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Button bot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);


        CardView card1 = view.findViewById(R.id.card1);
        LinearLayout linearLayout4 = view.findViewById(R.id.linearLayout4);
        CardView card10 = view.findViewById(R.id.card10);

        // Verificar se o usuário é um administrador
        userListener = db.collection("Usuarios")
                .document(getCurrentUserUid())
                .addSnapshotListener((snapshot, exception) -> {
                    if (exception != null) {
                        // Tratar o erro, se necessário
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Boolean isAdmin = snapshot.getBoolean("isAdmin");

                        if (isAdmin != null && isAdmin) {
                            linearLayout4.setVisibility(View.VISIBLE); // Exibir o LinearLayout4 para administradores
                        } else {
                            linearLayout4.setVisibility(View.GONE); // Ocultar o LinearLayout4 para usuários não administradores
                        }
                    }
                });

        card1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScreenCardView.class);
            startActivity(intent);
        });

        card10.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), adm_postnews.class);
            startActivity(intent);
        });

        // Verificar se o usuário está autenticado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            // Usuário anônimo ou não autenticado, ocultar o botão
            linearLayout4.setVisibility(View.GONE);
        } else {
            // Usuário autenticado, exibir o botão
            linearLayout4.setVisibility(View.VISIBLE);
        }

        return view;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        // Cancelar a inscrição do listener do usuário
        if (userListener != null) {
            userListener.remove();
        }
    }

    // Método auxiliar para obter o UID do usuário atualmente autenticado
    private String getCurrentUserUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }

}


