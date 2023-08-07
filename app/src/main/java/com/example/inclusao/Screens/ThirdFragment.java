package com.example.inclusao.Screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.inclusao.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {

    String nome="none";
    BottomSheetDialog dialog;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference referencia= FirebaseDatabase.getInstance().getReference();

    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    LinearLayout constr, constr2;
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
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
    EditText nomeSugest, descSugest;
    Button but;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Remova essas linhas

        View view = inflater.inflate(R.layout.fragment_third, container, false);
        constr = view.findViewById(R.id.linear);
        constr2 = view.findViewById(R.id.linear2);

        Verificacao(view);
        carregarAceitos();

        //Configurando o BottomSheet
        Button show = view.findViewById(R.id.show);

        config();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deixa transparente para que só possa se ver o background
                dialog.show();


            }
        });

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fecha o dialog
                dialog.dismiss();
                //Ao apertar no botão de subimit
                //Realtime Database
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            nome = documentSnapshot.getString("Nome");
                            // Faça o que precisa com o valor do campo "Nome"


                            // Salvar os dados no Realtime Database no nó sugestões
                            DatabaseReference dado = referencia.child("Sugests").child(nome);
                            dado.child("Descrição").setValue(descSugest.getText().toString());
                            dado.child("Titulo").setValue(nomeSugest.getText().toString());
                            dado.child("Nome").setValue(nome);
                            Toast.makeText(getContext(), "Sugestao gravada!", Toast.LENGTH_SHORT).show();

                        } else {
                            // Trate o caso em que o documento não existe
                        }
                    }
                });
            }

        });
        return view;
    }
    boolean isAdministrador=false;

    private void Verificacao(View view) {

        FirebaseFirestore dbr = FirebaseFirestore.getInstance();

        ListenerRegistration userListener;

        userListener = dbr.collection("Usuarios")
                .document(getCurrentUserUid())
                .addSnapshotListener((snapshot, exception) -> {
                    if (exception != null) {
                        // Tratar o erro, se necessário
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Boolean isAdmin = snapshot.getBoolean("isAdmin");

                        if (isAdmin != null && isAdmin) {
                            carregarSugestoes();
                            isAdministrador=true;
                        } else {
                            // Tornando o textView escrito Aceitos invisivel
                            TextView textView = view.findViewById(R.id.sugest);
                            // Para tornar o TextView invisível (ainda ocupa espaço no layout)
                            textView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void carregarSugestoes() {

        DatabaseReference dadosRef = referencia.child("Sugests");
        dadosRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int leftMargin = 100; // margem esquerda em pixels
                int topMargin = 20; // margem superior em pixels
                int rightMargin = 50; // margem direita em pixels
                int bottomMargin = 0;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    //Pega Strings
                    String titulo = userSnapshot.child("Titulo").getValue(String.class);
                    String descricao = userSnapshot.child("Descrição").getValue(String.class);
                    String name = userSnapshot.child("Nome").getValue(String.class);

                    CardView cardView = new CardView(requireContext());

                    float cornerRadius = 16f;
                    cardView.setRadius(cornerRadius);

                    int backgroundColor = ContextCompat.getColor(requireContext(), R.color.light_orange);
                    cardView.setCardBackgroundColor(backgroundColor);

                    //Configurando CardView
                    ConstraintLayout.LayoutParams cardLayoutParams = new ConstraintLayout.LayoutParams(
                            900,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                    cardLayoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                    cardView.setLayoutParams(cardLayoutParams);

                    //Configurando LinearLayout
                    LinearLayout containerLayout = new LinearLayout(requireContext());
                    containerLayout.setOrientation(LinearLayout.VERTICAL);

                    TextView newTextView = new TextView(requireContext());
                    newTextView.setText("Nome: " + titulo + "\n" + "Descrição: " + descricao);

                    LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    containerLayout.addView(newTextView, textLayoutParams);

                    //Configurando Button
                    Button button = new Button(requireContext());
                    button.setText("Confirmar");
                    Button btt = new Button(requireContext());
                    btt.setText("Recusar");

                    LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    buttonLayoutParams.gravity = Gravity.END; // Alinha o botão no canto direito

                    containerLayout.addView(button, buttonLayoutParams);
                    containerLayout.addView(btt, buttonLayoutParams);

                    cardView.addView(containerLayout);
                    constr.addView(cardView);

                    //Se o botão de recusar for apertado
                    btt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "Sugestao recusada!", Toast.LENGTH_SHORT).show();

                            constr.removeView(cardView);

                            //Exclui informações do nó atual no banco
                            String userId = userSnapshot.getKey();
                            DatabaseReference dado = referencia.child("Sugests").child(userId);
                            dado.removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // A exclusão foi bem-sucedida
                                            // Faça qualquer ação adicional necessária aqui
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Ocorreu um erro ao tentar excluir os dados
                                            // Lide com o erro adequadamente
                                        }
                                    });
                        }
                    });

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "Sugestao aceita!", Toast.LENGTH_SHORT).show();

                            constr.removeView(cardView);

                            //Exclui informações do nó atual no banco
                            String userId = userSnapshot.getKey();
                            DatabaseReference dado = referencia.child("Sugests").child(userId);
                            dado.removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // A exclusão foi bem-sucedida
                                            // Faça qualquer ação adicional necessária aqui
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Ocorreu um erro ao tentar excluir os dados
                                            // Lide com o erro adequadamente
                                        }
                                    });

                            //Realtime Database
                            FirebaseFirestore db = FirebaseFirestore.getInstance();


                            String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
                            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                        // Faça o que precisa com o valor do campo "Nome"


                                        // Salvar os dados no Realtime Database no nó sugestões
                                        DatabaseReference dado = referencia.child("Aceitos").child(name);
                                        dado.child("Descrição").setValue(descricao);
                                        dado.child("Titulo").setValue(titulo);
                                        dado.child("Nome").setValue(name);


                                    } else {
                                        // Trate o caso em que o documento não existe
                                    }
                                }
                            });


                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tratar erro na leitura dos dados
            }
        });
    }
    private void carregarAceitos() {
        DatabaseReference dadosRef = referencia.child("Aceitos");
        dadosRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int leftMargin = 100; // margem esquerda em pixels
                int topMargin = 20; // margem superior em pixels
                int rightMargin = 50; // margem direita em pixels
                int bottomMargin = 0;
                // Limpar a view antes de carregar os dados

                // Percorrer os filhos do nó "dados"
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    //Pega Strings
                    String titulo = userSnapshot.child("Titulo").getValue(String.class);
                    String descricao = userSnapshot.child("Descrição").getValue(String.class);
                    String name = userSnapshot.child("Nome").getValue(String.class);

                    CardView event = new CardView(requireContext());

                    float cornerRadius = 16f;
                    event.setRadius(cornerRadius);
                    int backgroundColor = ContextCompat.getColor(requireContext(), R.color.lighter_orange);
                    event.setCardBackgroundColor(backgroundColor);

                    //Configurando CardView
                    ConstraintLayout.LayoutParams cardLayoutParamss = new ConstraintLayout.LayoutParams(
                            900,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                    cardLayoutParamss.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                    event.setLayoutParams(cardLayoutParamss);

                    //Configurando LinearLayout
                    LinearLayout containerLayoutt = new LinearLayout(requireContext());
                    containerLayoutt.setOrientation(LinearLayout.VERTICAL);
                    event.addView(containerLayoutt);


                    //Configurando TextView
                    TextView newTextVieww = new TextView(requireContext());
                    newTextVieww.setText("Nome:" + name + "\n" + "Titulo: " + titulo + "\n" + "Descrição: " + descricao);

                    LinearLayout.LayoutParams textLayoutParamss = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    containerLayoutt.addView(newTextVieww, textLayoutParamss);

                    //Se o usuário ativp é um admin
                    if(isAdministrador){

                        //Configurando Button
                        Button button = new Button(requireContext());
                        button.setText("Excluir");

                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        buttonLayoutParams.gravity = Gravity.END; // Alinha o botão no canto direito

                        containerLayoutt.addView(button, buttonLayoutParams);

                        //Configurando botão de excluir
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Sugestao excluida!", Toast.LENGTH_SHORT).show();

                                constr.removeView(event);

                                //Exclui informações do nó atual no banco
                                String userId = userSnapshot.getKey();
                                DatabaseReference dado = referencia.child("Aceitos").child(userId);
                                dado.removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // A exclusão foi bem-sucedida
                                                // Faça qualquer ação adicional necessária aqui
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Ocorreu um erro ao tentar excluir os dados
                                                // Lide com o erro adequadamente
                                            }
                                        });
                            }
                        });
                }


                    //Adicionando botão no layout
                    constr2.addView(event);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tratar erro na leitura dos dados
            }
        });
    }
    private String getCurrentUserUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }

    public void config(){

         dialog = new BottomSheetDialog(requireContext(),R.style.MyTransparentBottomSheetDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        but = view.findViewById(R.id.submit);
        nomeSugest=view.findViewById(R.id.nomeSugest);
        descSugest=view.findViewById(R.id.descSugest);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Configurando animação de entrada do BottomSheetDialog
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }


}