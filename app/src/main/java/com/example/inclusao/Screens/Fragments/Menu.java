package com.example.inclusao.Screens.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

import com.example.inclusao.R;
import com.example.inclusao.Screens.Noticias;
import com.example.inclusao.Screens.WelcomeScreen1;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class Menu extends Fragment {

    private String imageUrl; // URL da imagem do Firebase Storage

    public Menu() {
        // Required empty public constructor
    }

    public static Menu newInstance() {
        return new Menu();
    }

    @Override
    public void onResume() {
        super.onResume();
        androidx.appcompat.widget.Toolbar toolbar=requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Inclusão - Menu");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        //Configuração do carousel de imagens
        ImageCarousel carousel = view.findViewById(R.id.carousel);
        List<CarouselItem> list = new ArrayList<>();

        carousel.setAutoPlay(true);
        carousel.setAutoPlayDelay(3000);

        carousel.setCarouselListener(new CarouselListener() {
            @Override
            public void onBindViewHolder(@NonNull ViewBinding viewBinding, @NonNull CarouselItem carouselItem, int i) {

            }

            @Nullable
            @Override
            public ViewBinding onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup parent) {

                return null;
            }



            @Override
            public void onLongClick(int position, @NotNull CarouselItem dataObject) {

            }

            @Override
            public void onClick(int position, @NotNull CarouselItem carouselItem) {
                Toast.makeText(getContext(), "Você clicou no item " + position, Toast.LENGTH_SHORT).show();
            }
        });
        carousel.registerLifecycle(getLifecycle());

        //Pegando dados do banco para colocar no carousel
        DatabaseReference referencia= FirebaseDatabase.getInstance().getReference();

        DatabaseReference dadosRef = referencia.child("Events");
        dadosRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //pegando eventos do firebase e setando no linear layout
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String imagem = userSnapshot.child("Imagem").getValue(String.class);
                    list.add(
                            new CarouselItem(
                                    imagem
                            )
                    );

                }
                carousel.setData(list);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tratar erro na leitura dos dados
            }
        });

        //Imagens do firebase
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference eventsRef = db.collection("Noticias");
//
//        eventsRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                List<CarouselItem> list = new ArrayList<>();
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    String imageUrl = document.getString("ImageUrl");
//                    if (imageUrl != null) {
//                        list.add(new CarouselItem(imageUrl));
//                    }
//                }
//                carousel.setData(list);
//            }
//        });


        //Ações dos cardviews

        CardView ic_Noticias = view.findViewById(R.id.ic_noticias);
        ic_Noticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Na atividade atual
                Intent intent = new Intent(getActivity(), Noticias.class);
                // Adicione a URL da imagem como extra na Intent
                intent.putExtra("imageUrl", imageUrl);
                startActivity(intent);
            }
        });

        CardView ic_Sobrenos = view.findViewById(R.id.ic_sobrenos);

        ic_Sobrenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomeScreen1.class);
                startActivity(intent);
            }
        });
        CardView ic_Cabi = view.findViewById(R.id.ic_cabi);

        ic_Cabi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment hostFragment = getParentFragment(); // ou use getActivity() para obter o Fragment da atividade

                // Use o NavController do Fragment hospedeiro
                if (hostFragment != null) {
                    NavHostFragment.findNavController(hostFragment)
                            .navigate(R.id.cabi);
                }
            }
        });

        CardView ic_SalasAEE = view.findViewById(R.id.ic_salasaee);

        ic_SalasAEE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment hostFragment = getParentFragment(); // ou use getActivity() para obter o Fragment da atividade

                // Use o NavController do Fragment hospedeiro
                if (hostFragment != null) {
                    NavHostFragment.findNavController(hostFragment)
                            .navigate(R.id.salas_AEE);
                }
            }
        });
        CardView ic_Projetos = view.findViewById(R.id.ic_projetos);

        ic_Projetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment hostFragment = getParentFragment(); // ou use getActivity() para obter o Fragment da atividade

                // Use o NavController do Fragment hospedeiro
                if (hostFragment != null) {
                    NavHostFragment.findNavController(hostFragment)
                            .navigate(R.id.projetos);
                }
            }
        });

        CardView ic_RedesSociais = view.findViewById(R.id.ic_redessociais);

        ic_RedesSociais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment hostFragment = getParentFragment(); // ou use getActivity() para obter o Fragment da atividade

                // Use o NavController do Fragment hospedeiro
                if (hostFragment != null) {
                    NavHostFragment.findNavController(hostFragment)
                            .navigate(R.id.redes_sociais);
                }
            }
        });

        CardView ic_FaleConosco = view.findViewById(R.id.ic_faleconosco);

        ic_FaleConosco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment hostFragment = getParentFragment(); // ou use getActivity() para obter o Fragment da atividade

                // Use o NavController do Fragment hospedeiro
                if (hostFragment != null) {
                    NavHostFragment.findNavController(hostFragment)
                            .navigate(R.id.faleConosco);
                }
            }
        });

        //Cards que ativaram um item do bottomnabigationsview
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView); // Substitua R.id.bottomNavigationView pelo ID correto do seu BottomNavigationView

        CardView ic_Eventos = view.findViewById(R.id.ic_eventos);

        ic_Eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trocar o ícone selecionado
                bottomNavigationView.findViewById(R.id.secondFragment).performClick(); // Substitua menu_item_id pelo ID do item de menu que você deseja clicar
            }
        });

        CardView ic_Parcerias = view.findViewById(R.id.ic_parcerias);

        ic_Parcerias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trocar o ícone selecionado
                bottomNavigationView.findViewById(R.id.thirdFragment).performClick(); // Substitua menu_item_id pelo ID do item de menu que você deseja clicar
            }
        });

        return view;
    }
}