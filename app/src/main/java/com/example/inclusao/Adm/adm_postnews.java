package com.example.inclusao.Adm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;

import com.example.inclusao.R;

public class adm_postnews extends AppCompatActivity {

    AppCompatButton buttonAddImage;
    EditText editTextContent;
    AppCompatButton bt_publicar;
    AppCompatButton bt_notificar;


    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_postnews);
        iniciarcomponentes();

        buttonAddImage.setOnClickListener(v -> {
            // Abrir a galeria ou câmera para selecionar uma imagem
            // e processar a imagem selecionada (por exemplo, exibi-la no layout)

            // Exemplo de código para abrir a galeria e selecionar uma imagem:
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            // Processar a imagem selecionada (por exemplo, exibi-la no layout)
        }
    }

    private void iniciarcomponentes() {
        buttonAddImage = findViewById(R.id.buttonAddImage);
        editTextContent = findViewById(R.id.editTextContent);
        bt_notificar = findViewById(R.id.bt_notificar);
        bt_publicar = findViewById(R.id.bt_publicar);
    }
}



