package com.example.inclusao.Screens;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.inclusao.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        calendarView=view.findViewById(R.id.calenderView);
        calendar=Calendar.getInstance();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                Toast.makeText(getContext(),"day "+ day+" mounth "+month+" year "+year ,Toast.LENGTH_SHORT).show();
            }
        });

        //Pegando linearlayout
        constr=view.findViewById(R.id.lin);

        int leftMargin = 00; // margem esquerda em pixels
        int topMargin = 20; // margem superior em pixels
        int rightMargin = 50; // margem direita em pixels
        int bottomMargin = 0;

        for(int cont=0; cont<2; cont++) {
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
            LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    600 // Altura da imagem em pixels, você pode ajustar conforme necessário
            );
            imageView.setLayoutParams(imageLayoutParams);
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


            return view;
    }


}