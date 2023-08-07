package com.example.inclusao.Screens;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.inclusao.R;
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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    boolean  isAdministrador=false;

    private String mParam2;



    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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

    BottomSheetDialog dialog;
    CalendarView calendarView;
    Calendar calendar;
    LinearLayout constr;

    Button but, sendImage, sendDate ;
    EditText nomeSugest;
    EditText descSugest;

    Context context;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("Eventos");


    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private DatabaseReference referencia= FirebaseDatabase.getInstance().getReference();

    String IMAGEM;
    int dia, mes, ano;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        context=requireContext();
        Verificacao(view);

        config();
        constr=view.findViewById(R.id.lin);
        // Adicione um ImageView
        ImageView image = new ImageView(requireContext());
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                600 // Altura da imagem em pixels, você pode ajustar conforme necessário
        );
        image.setLayoutParams(imageLayoutParams);

        //pega imagem
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        // Agora, envie a imagem para o Firebase Storage
                        StorageReference imageRef = imagesRef.child(imageUri.getLastPathSegment());
                        imageRef.putFile(imageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    // Upload concluído com sucesso
                                    Toast.makeText(requireContext(), "Upload bem-sucedido!", Toast.LENGTH_SHORT).show();

                                    // Obtém o URL da imagem
                                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                         IMAGEM = uri.toString();

                                    }).addOnFailureListener(e -> {
                                        // Ocorreu um erro ao obter o URL da imagem
                                        Toast.makeText(requireContext(), "Erro ao obter URL da imagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    // Ocorreu um erro durante o upload
                                    Toast.makeText(requireContext(), "Erro ao fazer upload: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    }
                });


        FloatingActionButton show = view.findViewById(R.id.fab);


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deixa transparente para que só possa se ver o background
                dialog.show();


            }
        });

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //Ao clicar no botão de adicionar imagem
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pega imagem da galeria
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(intent);
            }
        });

        //Ao clicar no botão de adicionar data

        sendDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pega a data atual
                final Calendar calendar = Calendar.getInstance();
                int initialYear = calendar.get(Calendar.YEAR);
                int initialMonth = calendar.get(Calendar.MONTH);
                int initialDay = calendar.get(Calendar.DAY_OF_MONTH);

                // Cria o DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dia=dayOfMonth;
                                mes=month+1;
                                ano=year;
                                Toast.makeText(requireContext(), "Data selecionada: " + year + "-" + (month + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
                            }
                        },
                        initialYear, initialMonth, initialDay);

                // Exibe o DatePickerDialog
                datePickerDialog.show();

            }
        });


        //ao clicar no botão de submit
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fecha o dialog
                dialog.dismiss();
                //Ao apertar no botão de subimit
                String titulo = nomeSugest.getText().toString();
                String descricao = descSugest.getText().toString();
                String image = "";

                DatabaseReference dado = referencia.child("Events").child(titulo);

                // Salvar os dados no Realtime Database no nó sugestões
                dado.child("Descrição").setValue(descricao);
                dado.child("Titulo").setValue(titulo);
                dado.child("Imagem").setValue(IMAGEM);
                dado.child("Dia").setValue(dia);
                dado.child("Mês").setValue(mes);
                dado.child("Ano").setValue(ano);
                Toast.makeText(getContext(), "Sugestao gravada!", Toast.LENGTH_SHORT).show();

            }
    });


        calendarView=view.findViewById(R.id.calenderView);
        calendar=Calendar.getInstance();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                Toast.makeText(getContext(),"day "+ day+" mounth "+month+" year "+year ,Toast.LENGTH_SHORT).show();
            }
        });

        //CARREGANDO DADOS
        carregaDados();

            return view;
    }
    public void config(){

        dialog = new BottomSheetDialog(requireContext(),R.style.MyTransparentBottomSheetDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_event, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        but = view.findViewById(R.id.submitevent);
        sendImage = view.findViewById(R.id.sendImage);
        sendDate = view.findViewById(R.id.sendDate);
        nomeSugest=view.findViewById(R.id.nomeSugestevent);
        descSugest=view.findViewById(R.id.descSugestevent);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Configurando animação de entrada do BottomSheetDialog
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void carregaDados(){


        //HashMap para transformar o número do mês em seu respectivo nome
        HashMap<String, String> mesesAbreviados = new HashMap<>();

        mesesAbreviados.put("1", "Jan");
        mesesAbreviados.put("2", "Fev");
        mesesAbreviados.put("3", "Mar");
        mesesAbreviados.put("4", "Abr");
        mesesAbreviados.put("5", "Mai");
        mesesAbreviados.put("6", "Jun");
        mesesAbreviados.put("7", "Jul");
        mesesAbreviados.put("8", "Ago");
        mesesAbreviados.put("9", "Set");
        mesesAbreviados.put("10", "Out");
        mesesAbreviados.put("11", "Nov");
        mesesAbreviados.put("12", "Dez");
        //Pegando linearlayout
        //HERE

        DatabaseReference dadosRef = referencia.child("Events");
        dadosRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int leftMargin = 00; // margem esquerda em pixels
                int topMargin = 20; // margem superior em pixels
                int rightMargin = 50; // margem direita em pixels
                int bottomMargin = 0;

                //pegando eventos do firebase e setando no linear layout
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    //Pega Strings
                    String titulo = userSnapshot.child("Titulo").getValue(String.class);
                    String descricao = userSnapshot.child("Descrição").getValue(String.class);
                    String imagem = userSnapshot.child("Imagem").getValue(String.class);
                    String dia =""+ userSnapshot.child("Dia").getValue(Long.class);
                    String mes =""+ userSnapshot.child("Mês").getValue(Long.class);

                    //Cria carview
                    CardView cardView = new CardView(requireContext());

                    float cornerRadius = 16f;
                    cardView.setCardElevation(0);


                    cardView.setRadius(cornerRadius);
                    cardView.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));


                    //Configurando CardView
                    ConstraintLayout.LayoutParams cardLayoutParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                    cardLayoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                    cardView.setLayoutParams(cardLayoutParams);

                    //Configurando LinearLayout vertical
                    LinearLayout containerLayout = new LinearLayout(requireContext());
                    containerLayout.setOrientation(LinearLayout.VERTICAL);

                    //Configurando LinearLayout vertical pro titulo e desc
                    LinearLayout containerLayoutV = new LinearLayout(requireContext());
                    containerLayoutV.setOrientation(LinearLayout.VERTICAL);

                    //Configurando LinearLayout vertical pra data
                    LinearLayout containerLayoutdata = new LinearLayout(requireContext());
                    containerLayoutdata.setOrientation(LinearLayout.VERTICAL);

                    // Adicione um ImageView
                    ImageView imageView = new ImageView(requireContext());
                    Glide.with(context)
                            .load(imagem)
                            .into(imageView); // Substitua "imagem_exemplo" pelo ID da imagem em "res/drawable"

                    LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            600 // Altura da imagem em pixels, você pode ajustar conforme necessário
                    );
                    imageView.setLayoutParams(imageLayout);
                    containerLayout.addView(imageView);


                    //Configurando LinearLayout horizontal
                    LinearLayout containerLayoutH = new LinearLayout(requireContext());
                    containerLayoutH.setOrientation(LinearLayout.HORIZONTAL);
                    containerLayoutH.setGravity(Gravity.CENTER_VERTICAL); // Centraliza os componentes verticalmente


                    //Configurando fonte
                    Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "Aclonica.ttf");


                    //Configurando textview titulo
                    TextView newTextView = new TextView(requireContext());
                    newTextView.setGravity(Gravity.CENTER);

                    newTextView.setText(titulo);
                    newTextView.setTypeface(customFont);
                    newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutParamsdata = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutParamsdata.setMargins(20, 10, 0, 0); // Removendo as margens superiores para centralizar o texto verticalmente

                    newTextView.setLayoutParams(textLayoutParamsdata);

                    //Configurando textview desc
                    TextView newTextdesc = new TextView(requireContext());
                    newTextdesc.setGravity(Gravity.LEFT);

                    newTextdesc.setText(descricao);

                    newTextdesc.setTypeface(customFont);
                    newTextdesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutParamsdesc = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutParamsdesc.setMargins(40, 0, 0, 0); // Removendo as margens superiores para centralizar o texto verticalmente

                    newTextdesc.setLayoutParams(textLayoutParamsdesc);





                    //Configurando mes
                    TextView newTextdata = new TextView(requireContext());


                    newTextdata.setText(mesesAbreviados.get(mes));


                    newTextdata.setTypeface(customFont);
                    newTextdata.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutParams.setMargins(20, 0, 0, 0);

                    //Configurando dia
                    TextView newTextdia = new TextView(requireContext());
                    newTextdia.setText(dia);


                    newTextdia.setTypeface(customFont);
                    newTextdia.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutParamsdia = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutParamsdia.setMargins(20, 0, 0, 0);





                    Button button= new Button(requireContext());;
                    LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );;
                    //BOTÃO DE EXCLUIR
                    if(isAdministrador){

                        //Configurando Button
                        button = new Button(requireContext());
                        button.setText("Excluir");

                        buttonLayoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        buttonLayoutParams.gravity = Gravity.END; // Alinha o botão no canto direito


                        //Configurando botão de excluir
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                constr.removeView(cardView);

                                //Exclui informações do nó atual no banco
                                String userId = userSnapshot.getKey();
                                DatabaseReference dado = referencia.child("Events").child(userId);
                                dado.removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // A exclusão foi bem-sucedida
                                                // Faça qualquer ação adicional necessária aqui
                                                Toast.makeText(getContext(), "Sugestao excluida!", Toast.LENGTH_SHORT).show();

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

                    //Texto para o titulo e descrição
                    containerLayoutV.addView(newTextView);
                    containerLayoutV.addView(newTextdesc);

                    //Texto para o mes e o dia
                    containerLayoutdata.addView(newTextdata, textLayoutParams);
                    containerLayoutdata.addView(newTextdia, textLayoutParamsdia);


                    //Colocando o texto dentro do container horizontal
                    containerLayoutH.addView(containerLayoutdata, textLayoutParamsdata);
                    containerLayoutH.addView(containerLayoutV, textLayoutParams);
                    //Colocando o container horizontal dentro do vertical
                    containerLayout.addView(containerLayoutH);
                    //Colocando o botão excluir
                    if(isAdministrador)
                    containerLayout.addView(button, buttonLayoutParams);

                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dentro do onClick do botão
                            Intent intent = new Intent(requireContext(), Event.class);

                            String titulo = userSnapshot.child("Titulo").getValue(String.class);
                            String descricao = userSnapshot.child("Descrição").getValue(String.class);
                            String imagem = userSnapshot.child("Imagem").getValue(String.class);
                            String dia = ""+userSnapshot.child("Dia").getValue(Integer.class);
                            String mes = ""+userSnapshot.child("Mês").getValue(Integer.class);
                            String ano = ""+userSnapshot.child("Ano").getValue(Integer.class);

                            intent.putExtra("tituloEvento", titulo);
                            intent.putExtra("descricaoEvento", descricao);
                            intent.putExtra("imagemEvento", imagem);
                            intent.putExtra("diaEvento", dia);
                            intent.putExtra("mesEvento", mes);
                            intent.putExtra("anoEvento", ano);

                            startActivity(intent);

                        }
                    });



                    cardView.addView(containerLayout);
                    constr.addView(cardView);
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
                            isAdministrador=true;
                        } else {
                            // Tornando o textView escrito Aceitos invisivel
                            FloatingActionButton show = view.findViewById(R.id.fab);
                            // Para tornar o TextView invisível (ainda ocupa espaço no layout)
                            show.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }





}