package com.example.inclusao.Screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inclusao.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {

    private EditText edit_nome, edit_sobronme, edit_email, edit_senha, edit_phone;
    private Button bt_cadastrar;

    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso"};
    String usuarioID;
    String rua, bairro, cidade, estado;
    RadioGroup radioGroupRespostas;

    EditText editText;
    EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        IniciarComponentes();
        Intent intent = getIntent();


        if (intent != null) {
            rua = intent.getStringExtra("rua");
            bairro = intent.getStringExtra("bairro");
            cidade = intent.getStringExtra("cidade");
            estado = intent.getStringExtra("estado");
        }

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edit_nome.getText().toString();
                String sobrenome = edit_sobronme.getText().toString();
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if (nome.isEmpty() || email.isEmpty() || sobrenome.isEmpty() || senha.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    CadastrarUsuario(v, nome, sobrenome, email, senha);
                }
            }
        });

        radioGroupRespostas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonSim) {
                    editText.setVisibility(View.VISIBLE);
                    editText1.setVisibility(View.VISIBLE);
                } else {
                    editText.setVisibility(View.GONE);
                    editText1.setVisibility(View.GONE);
                }
            }
        });



    }

    private void CadastrarUsuario(View v, String nome, String sobrenome, String email, String senha) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SalvarDadosUsuario(nome, sobrenome, email, rua, bairro, cidade, estado);

                            Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent( FormCadastro.this, FormLogin.class);
                                    startActivity(intent);
                                }
                            }, 2000);


                        } else {
                            String erro;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erro = "Digite uma senha com no mínimo 6 caracteres";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erro = "E-mail já cadastrado";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "E-mail inválido";
                            } catch (Exception e) {
                                erro = "Erro ao cadastrar usuário, tente novamente.";
                            }

                            Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                    }
                });
    }
    private void SalvarDadosUsuario(String nome, String sobrenome, String email,
                                    String rua, String bairro, String cidade, String estado) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("Nome", nome);
        usuarios.put("Sobrenome", sobrenome);
        usuarios.put("Email", email);
        usuarios.put("Rua", rua);
        usuarios.put("Bairro", bairro);
        usuarios.put("Cidade", cidade);
        usuarios.put("Estado", estado);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Erro ao salvar os dados" + e.toString());
            }
        });
    }
    private void IniciarComponentes() {
        edit_nome = findViewById(R.id.nome);
        edit_sobronme = findViewById(R.id.sobrenome);
        edit_email = findViewById(R.id.email);
        edit_senha = findViewById(R.id.senha);
        edit_phone = findViewById(R.id.telefone);
        bt_cadastrar = findViewById(R.id.Button);
        radioGroupRespostas = findViewById(R.id.radioGroupRespostas);
        editText = findViewById(R.id.editTextDeficiencia);
        editText1 = findViewById(R.id.editTextDeficiencia2);
    }
}
