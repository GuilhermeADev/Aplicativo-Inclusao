package com.example.inclusao.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inclusao.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {
    private TextView cadastro;
    private EditText edit_email, edit_senha;
    private Button bt_entrar;
    private ProgressBar progressBar;

    private TextView esqueciSenha;

    private TextView convidado;

    String[] mensagens = {"Preencha todos os campos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        iniciarcomponents();

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, FormCadastro2.class);
                startActivity(intent);
            }
        });

        convidado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signInAnonymously()
                        .addOnCompleteListener(FormLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    //Aut anonima bem-sucedia
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent( FormLogin.this, WelcomeScreen1.class);
                                    startActivity(intent);
                                } else {
                                    //ocorreu um erro durante o auth
                                    Toast.makeText(FormLogin.this, "Autenticação anônima falhou.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else {
                    AutenticarUsuario(v);
                }
            }
        });

    }
    private void AutenticarUsuario(View view){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    }, 3000);
                }
                else {
                    String erro;

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Email ou senha incorretos";
                    } catch (Exception e){
                        erro = "Email ou senha incorretos";
                    }

                    Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            TelaPrincipal();
        }
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(FormLogin.this, WelcomeScreen1.class);
        startActivity(intent);
        finish();
    }



    private void iniciarcomponents(){
        cadastro = findViewById(R.id.cadastro);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.progressbar);
        convidado = findViewById(R.id.Convidado);
        esqueciSenha = findViewById(R.id.esqueciSenha);

    }
}