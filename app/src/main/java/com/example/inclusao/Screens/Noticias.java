package com.example.inclusao.Screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.inclusao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class Noticias extends AppCompatActivity {
    private ImageView addnews;
    private ImageView imageViewSelectedImage;
    private ListenerRegistration listenerRegistration;

    private static final int PICK_IMAGE_REQUEST = 1;

    private void verificarPermissaoADM() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userID != null) {
            listenerRegistration = FirebaseFirestore.getInstance()
                    .collection("Usuarios")
                    .document(userID)
                    .addSnapshotListener((snapshot, exception) -> {
                        if (exception != null) {
                            // Tratar erro de leitura do banco de dados
                            Toast.makeText(Noticias.this, "Erro ao verificar permissão de ADM", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Boolean isAdmin = snapshot.getBoolean("isAdmin");

                            if (isAdmin != null && isAdmin) {
                                // Usuário é ADM, tornar o botão addnews visível
                                addnews.setVisibility(View.VISIBLE);
                                // Adicionar OnClickListener para exibir os outros componentes quando a imagem for clicada
                                addnews.setOnClickListener(v -> {
                                    // Tornar os outros componentes visíveis
                                    findViewById(R.id.buttonAddImage).setVisibility(View.VISIBLE);
                                    findViewById(R.id.editTextContent).setVisibility(View.VISIBLE);
                                    findViewById(R.id.bt_publicar).setVisibility(View.VISIBLE);
                                    findViewById(R.id.bt_notificar).setVisibility(View.VISIBLE);
                                });
                            } else {
                                // Usuário não é ADM, tornar o botão addnews invisível
                                addnews.setVisibility(View.GONE);
                                // Tornar os outros componentes invisíveis
                                findViewById(R.id.buttonAddImage).setVisibility(View.GONE);
                                findViewById(R.id.editTextContent).setVisibility(View.GONE);
                                findViewById(R.id.bt_publicar).setVisibility(View.GONE);
                                findViewById(R.id.bt_notificar).setVisibility(View.GONE);
                            }
                        } else {
                            // Usuário não possui um documento correspondente no Firestore, ocultar todos os componentes
                            addnews.setVisibility(View.GONE);
                            findViewById(R.id.buttonAddImage).setVisibility(View.GONE);
                            findViewById(R.id.editTextContent).setVisibility(View.GONE);
                            findViewById(R.id.bt_publicar).setVisibility(View.GONE);
                            findViewById(R.id.bt_notificar).setVisibility(View.GONE);
                        }
                    });
        } else {
            // Usuário não está autenticado, ocultar todos os componentes
            addnews.setVisibility(View.GONE);
            findViewById(R.id.buttonAddImage).setVisibility(View.GONE);
            findViewById(R.id.editTextContent).setVisibility(View.GONE);
            findViewById(R.id.bt_publicar).setVisibility(View.GONE);
            findViewById(R.id.bt_notificar).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        iniciarcomponentes();
        verificarPermissaoADM();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    private void iniciarcomponentes() {
        addnews = findViewById(R.id.addnews);
        imageViewSelectedImage = findViewById(R.id.imageViewSelectedImage);
        AppCompatButton buttonAddImage = findViewById(R.id.buttonAddImage);

        buttonAddImage.setOnClickListener(v -> {
            // Lógica para lidar com o clique do botão de adicionar imagem
            selecionarImagemDaGaleria();
        });
    }

    private void selecionarImagemDaGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Exibir a imagem selecionada acima do botão de adicionar imagem
            imageViewSelectedImage.setImageURI(imageUri);
        }
    }
}
