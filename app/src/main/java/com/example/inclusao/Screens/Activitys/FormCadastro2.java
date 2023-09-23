package com.example.inclusao.Screens.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.inclusao.Data.Endereco;
import com.example.inclusao.Data.ViacepAPI;
import com.example.inclusao.Data.ViacepService;
import com.example.inclusao.R;
import com.google.android.material.snackbar.Snackbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormCadastro2 extends AppCompatActivity {

    private EditText editTextcep;
    private TextView textViewRua;
    private TextView textViewCidade;
    private TextView textViewEstado;
    private TextView textViewBairro;
    private ViacepService viacepService;
    private ProgressBar progressBar;

    private Button btavancar;

    String[] mensagens = {"Informe um CEP válido", "CEP não encontrado", "Erro na busca do CEP", "Falha na requisição", "Preencha todos os campos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniciarcomponentes();
        editTextcep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cep = s.toString();
                buscarEndereco(cep);
            }
        });

        btavancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String viewRua = textViewRua.getText().toString();
                String viewBairro = textViewBairro.getText().toString();
                String viewCidade = textViewCidade.getText().toString();
                String viewEstado = textViewEstado.getText().toString();

                if(viewRua.isEmpty() || viewBairro.isEmpty() || viewCidade.isEmpty() || viewEstado.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,mensagens[4],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
                else{
                    Intent intent = new Intent(FormCadastro2.this, FormCadastro.class);
                    intent.putExtra("rua", viewRua);
                    intent.putExtra("bairro", viewBairro);
                    intent.putExtra("cidade", viewCidade);
                    intent.putExtra("estado", viewEstado);
                    startActivity(intent);
                }
            }
        });

    }
    private void buscarEndereco(String cep){
        if (cep.isEmpty()){
            textViewRua.setText("");
            textViewCidade.setText("");
            textViewBairro.setText("");
            textViewEstado.setText("");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        ViacepAPI viacepAPI = viacepService.getViacepAPI();
        Call<Endereco> call = viacepAPI.buscarEnderecoPorCEP(cep);
        call.enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {

                progressBar.setVisibility(View.INVISIBLE);

                if(response.isSuccessful()){
                    Endereco endereco = response.body();
                    if(endereco != null){
                        preencherCampos(endereco);
                    } else {
                        Snackbar snackbar = Snackbar.make(editTextcep, mensagens[1],Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);

                Snackbar snackbar = Snackbar.make(editTextcep, mensagens[3],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }

    private void preencherCampos(Endereco endereco) {
        textViewRua.setText(endereco.getLogradouro());
        textViewCidade.setText(endereco.getCidade());
        textViewBairro.setText(endereco.getBairro());
        textViewEstado.setText(endereco.getEstado());
    }

    private void iniciarcomponentes(){
        setContentView(R.layout.activity_form_cadastro2);
        btavancar = findViewById(R.id.Btavancar);
        progressBar = findViewById(R.id.progressBar);
        editTextcep = findViewById(R.id.editTextCep);
        textViewRua = findViewById(R.id.textViewRua);
        textViewCidade = findViewById(R.id.textViewCidade);
        textViewEstado = findViewById(R.id.textViewEstado);
        textViewBairro = findViewById(R.id.textViewBairro);
        btavancar = findViewById(R.id.Btavancar);
        viacepService = new ViacepService();

    }

}
