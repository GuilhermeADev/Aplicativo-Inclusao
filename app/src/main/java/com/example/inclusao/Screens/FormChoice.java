package com.example.inclusao.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.inclusao.R;

public class FormChoice extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_choice);

         AppCompatButton Button_login = findViewById(R.id.Button1);
         AppCompatButton Button_convidado = findViewById(R.id.Button2);

         Button_login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent (FormChoice.this, FormLogin.class);
                 startActivity(intent);
             }
         });

         Button_convidado.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 FirebaseAuth mAuth = FirebaseAuth.getInstance();
                 mAuth.signInAnonymously()
                         .addOnCompleteListener(FormChoice.this, new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()){
                                     //Aut anonima bem-sucedia
                                     FirebaseUser user = mAuth.getCurrentUser();
                                     Intent intent = new Intent( FormChoice.this, WelcomeScreen1.class);
                                     startActivity(intent);
                                 } else {
                                     //ocorreu um erro durante o auth
                                     Toast.makeText(FormChoice.this, "Autenticação anônima falhou.", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         });
             }
         });

    }

}