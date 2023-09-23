package com.example.inclusao.Screens.Fragments;

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

import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inclusao.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;

public class Eventos extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        androidx.appcompat.widget.Toolbar toolbar=requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Inclusão - Eventos");

    }
    private BottomSheetDialog dialog;
    private CalendarView calendarView;
    private Calendar calendar;
    private LinearLayout constr;

    private Button bt_submit;
    private Button sendImage;
    private Button sendDate;

    private EditText nomeSugest;
    private EditText descSugest;

    private boolean isAdministrador = false;
    private boolean preenchido = false;
    private Context context;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private StorageReference imagesRef = storageRef.child("Eventos");

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    private String linkImagem;
    private int dia, mes, ano;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);
        context=requireContext();
        preenchido=false;
        Verificacao(view);

        config();

        //ProgressBar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000);

        constr=view.findViewById(R.id.lin);
        // Adicione um ImageView
        ImageView image = new ImageView(requireContext());
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                600 // Altura da imagem em pixels, você pode ajustar conforme necessário
        );
        image.setLayoutParams(imageLayoutParams);


        FloatingActionButton FloatingButton = view.findViewById(R.id.floatingbutton);


        FloatingButton.setOnClickListener(new View.OnClickListener() {
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
                progressBar.setVisibility(View.VISIBLE);

            }
        });


        //pega imagem do BOTTON SHEET
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
                                    progressBar.setVisibility(view.INVISIBLE);
                                    preenchido=true;

                                    // Obtém o URL da imagem
                                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        linkImagem = uri.toString();

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
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = nomeSugest.getText().toString();
                String descricao = descSugest.getText().toString();
                if(preenchido) {
                    if (!descricao.isEmpty() && !titulo.isEmpty()) {
                        //fecha o dialog
                        dialog.dismiss();
                        //Ao apertar no botão de subimit

                        String image = "";

                        DatabaseReference dado = referencia.child("Events").child(titulo);

                        // Salvar os dados no Realtime Database no nó sugestões
                        dado.child("Descrição").setValue(descricao);
                        dado.child("Titulo").setValue(titulo);
                        dado.child("Imagem").setValue(linkImagem);
                        dado.child("Dia").setValue(dia);
                        dado.child("Mês").setValue(mes);
                        dado.child("Ano").setValue(ano);
                        Toast.makeText(getContext(), "Sugestao gravada!", Toast.LENGTH_SHORT).show();
                    } else  Toast.makeText(getContext(), "Preencha os campos!", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(), "Adicione uma imagem!", Toast.LENGTH_SHORT).show();
            }
    });

        //Calendario
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
        bt_submit = view.findViewById(R.id.submitevent);
        sendImage = view.findViewById(R.id.sendImage);
        sendDate = view.findViewById(R.id.sendDate);
        nomeSugest=view.findViewById(R.id.nomeSugestevent);
        descSugest=view.findViewById(R.id.descSugestevent);
        progressBar=view.findViewById(R.id.progressbarSheet);


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

        //Carregando dados para o linearlayout

        DatabaseReference dadosRef = referencia.child("Events");
        dadosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //pegando eventos do firebase e setando no linear layout
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    //Pega Strings
                    String titulo = userSnapshot.child("Titulo").getValue(String.class);
                    String descricao = userSnapshot.child("Descrição").getValue(String.class);
                    String linkImagem = userSnapshot.child("Imagem").getValue(String.class);
                    String dia =""+ userSnapshot.child("Dia").getValue(Long.class);
                    String mes =""+ userSnapshot.child("Mês").getValue(Long.class);

                    //Cria carview
                    CardView cardView = new CardView(requireContext());
                    cardView.setCardElevation(0);
                    cardView.setRadius(16f);
                    cardView.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //Configurando CardView
                    ConstraintLayout.LayoutParams cardLayoutParams = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                    cardLayoutParams.setMargins(0, 20, 0, 0);
                    cardView.setLayoutParams(cardLayoutParams);

                    //Configurando LinearLayout vertical
                    LinearLayout LayoutVerticalEvent = new LinearLayout(requireContext());
                    LayoutVerticalEvent.setOrientation(LinearLayout.VERTICAL);

                    //Configurando LinearLayout vertical pro titulo e desc
                    LinearLayout LayoutVerticalTexto = new LinearLayout(requireContext());
                    LayoutVerticalTexto.setOrientation(LinearLayout.VERTICAL);

                    //Configurando LinearLayout vertical pra data
                    LinearLayout LayoutVerticalData = new LinearLayout(requireContext());
                    LayoutVerticalData.setOrientation(LinearLayout.VERTICAL);

                    // Adicione um ImageView
                    ImageView imageView = new ImageView(requireContext());
                    Glide.with(context)
                            .load(linkImagem)
                            .into(imageView); // carregando imagem no imageview

                    LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            600 // Altura da imagem em pixels, você pode ajustar conforme necessário
                    );
                    imageView.setLayoutParams(imageLayout);
                    LayoutVerticalEvent.addView(imageView);

                    //Configurando LinearLayout horizontal
                    LinearLayout LayoutHorizontal = new LinearLayout(requireContext());
                    LayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                    LayoutHorizontal.setGravity(Gravity.CENTER_VERTICAL); // Centraliza os componentes verticalmente

                    //Configurando fonte
                    Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "Aclonica.ttf");

                    //Configurando textview titulo
                    TextView text_titulo = new TextView(requireContext());
                    text_titulo.setGravity(Gravity.CENTER);
                    text_titulo.setText(titulo);
                    text_titulo.setTypeface(customFont);
                    text_titulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutTitulo = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutTitulo.setMargins(20, 10, 0, 0); // Removendo as margens superiores para centralizar o texto verticalmente
                    text_titulo.setLayoutParams(textLayoutTitulo);

                    //Configurando textview desc
                    TextView text_Descricao = new TextView(requireContext());
                    text_Descricao.setGravity(Gravity.LEFT);
                    text_Descricao.setText(descricao);
                    text_Descricao.setTypeface(customFont);
                    text_Descricao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutParamsdesc = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutParamsdesc.setMargins(40, 0, 0, 0); // Removendo as margens superiores para centralizar o texto verticalmente
                    text_Descricao.setLayoutParams(textLayoutParamsdesc);

                    //Configurando mes
                    TextView text_mes = new TextView(requireContext());
                    text_mes.setText(mesesAbreviados.get(mes));
                    text_mes.setTypeface(customFont);
                    text_mes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutMes = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutMes.setMargins(20, 0, 0, 0);

                    //Configurando dia
                    TextView text_dia = new TextView(requireContext());
                    text_dia.setText(dia);
                    text_dia.setTypeface(customFont);
                    text_dia.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutDia = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutDia.setMargins(20, 0, 0, 0);

                    //BOTÃO DE EXCLUIR
                    Button but_excluir= new Button(requireContext());
                    LinearLayout.LayoutParams butLayoutExcluir = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    if(isAdministrador){
                        but_excluir = new Button(requireContext());
                        but_excluir.setText("Excluir");
                        butLayoutExcluir = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        butLayoutExcluir.gravity = Gravity.END; // Alinha o botão no canto direito

                        //Configurando botão de excluir
                        but_excluir.setOnClickListener(new View.OnClickListener() {
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
                                                Toast.makeText(getContext(), "Sugestao excluida!", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                            }
                        });
                    }

                    //Texto para o titulo e descrição
                    LayoutVerticalTexto.addView(text_titulo);
                    LayoutVerticalTexto.addView(text_Descricao);

                    //Texto para o mes e o dia
                    LayoutVerticalData.addView(text_mes, textLayoutMes);
                    LayoutVerticalData.addView(text_dia, textLayoutDia);

                    //Colocando o texto dentro do container horizontal
                    LayoutHorizontal.addView(LayoutVerticalData, textLayoutTitulo);
                    LayoutHorizontal.addView(LayoutVerticalTexto, textLayoutMes);

                    //Colocando o container horizontal dentro do vertical
                    LayoutVerticalEvent.addView(LayoutHorizontal);
                    //Colocando o botão excluir
                    if(isAdministrador)
                    LayoutVerticalEvent.addView(but_excluir, butLayoutExcluir);

                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String titulo = userSnapshot.child("Titulo").getValue(String.class);
                            String descricao = userSnapshot.child("Descrição").getValue(String.class);
                            String linkImagem = userSnapshot.child("Imagem").getValue(String.class);
                            String dia = ""+userSnapshot.child("Dia").getValue(Integer.class);
                            String mes = ""+userSnapshot.child("Mês").getValue(Integer.class);
                            String ano = ""+userSnapshot.child("Ano").getValue(Integer.class);

                            Bundle args = new Bundle();
                            args.putString("tituloEvento", titulo);
                            args.putString("descricaoEvento", descricao);
                            args.putString("imagemEvento", linkImagem);
                            args.putString("diaEvento", dia);
                            args.putString("mesEvento", mes);
                            args.putString("anoEvento", ano);

                            Fragment hostFragment = getParentFragment(); // ou use getActivity() para obter o Fragment da atividade

                            // Use o NavController do Fragment hospedeiro
                            if (hostFragment != null) {
                                NavHostFragment.findNavController(hostFragment)
                                        .navigate(R.id.fragment_event, args);
                            }

                        }
                    });

                    cardView.addView(LayoutVerticalEvent);
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
                            FloatingActionButton show = view.findViewById(R.id.floatingbutton);
                            // Para tornar o TextView invisível (ainda ocupa espaço no layout)
                            show.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }





}