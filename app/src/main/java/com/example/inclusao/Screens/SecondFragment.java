package com.example.inclusao.Screens;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

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

    Button but, sendImage ;
    EditText nomeSugest;
    EditText descSugest;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("Eventos");


    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private DatabaseReference referencia= FirebaseDatabase.getInstance().getReference();

    String IMAGEM;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

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
                        Toast.makeText(requireContext(), imageUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
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

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pega imagem da galeria
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(intent);
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
        nomeSugest=view.findViewById(R.id.nomeSugestevent);
        descSugest=view.findViewById(R.id.descSugestevent);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Configurando animação de entrada do BottomSheetDialog
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void carregaDados(){
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
                for (int cont = 0; cont < 2; cont++) {
                    //Pega Strings
                    String titulo = "titulo";
                    String descricao = "desc";
                    String name = "name";

                    //Cria carview
                    CardView cardView = new CardView(requireContext());

                    float cornerRadius = 16f;
                    cardView.setRadius(cornerRadius);

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

                    // Adicione um ImageView
                    ImageView imageView = new ImageView(requireContext());
                    imageView.setImageResource(R.drawable.ball); // Substitua "imagem_exemplo" pelo ID da imagem em "res/drawable"
                    LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            600 // Altura da imagem em pixels, você pode ajustar conforme necessário
                    );
                    imageView.setLayoutParams(imageLayout);
                    containerLayout.addView(imageView);


                    //Configurando LinearLayout horizontal
                    LinearLayout containerLayoutH = new LinearLayout(requireContext());
                    containerLayoutH.setOrientation(LinearLayout.HORIZONTAL);

                    //Configurando fonte
                    Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "Aclonica.ttf");


                    //Configurando data
                    TextView newTextdata = new TextView(requireContext());
                    newTextdata.setText("Sep" + "\n" + " 18 ");
                    newTextdata.setTypeface(customFont);
                    newTextdata.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutParams.setMargins(40, 0, 0, 0);


                    //Configurando textview
                    TextView newTextView = new TextView(requireContext());
                    newTextView.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit amet, consectetur adipiscing");
                    newTextView.setTypeface(customFont);
                    newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); // Substitua 18 pelo tamanho desejado em "sp"
                    LinearLayout.LayoutParams textLayoutParamsdata = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    textLayoutParamsdata.setMargins(40, 0, 0, 0);


                    //Colocando o texto dentro do container horizontal
                    containerLayoutH.addView(newTextdata, textLayoutParamsdata);
                    containerLayoutH.addView(newTextView, textLayoutParams);
                    //Colocando o container horizontal dentro do vertical
                    containerLayout.addView(containerLayoutH);


                    cardView.addView(containerLayout);
                    constr.addView(cardView);
                }
            }
        });

    }





}