package com.example.inclusao.Adm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.inclusao.R;

public class adm_postnews extends AppCompatActivity {

    AppCompatButton buttonAddImage;
    EditText editTextContent;
    AppCompatButton bt_publicar;
    AppCompatButton bt_notificar;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2;



    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_postnews);
        iniciarcomponentes();

        // Verificar se a permissão de leitura externa está concedida
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permissão não concedida, solicitar permissão em tempo de execução
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Permissão já concedida, abrir a galeria
            openGallery();
        }

        buttonAddImage.setOnClickListener(v -> {
            // Abrir a galeria ou câmera para selecionar uma imagem
            // e processar a imagem selecionada (por exemplo, exibi-la no layout)

            // Exemplo de código para abrir a galeria e selecionar uma imagem:
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, abrir a galeria
                openGallery();
            } else {
                // Permissão negada, exibir uma mensagem para o usuário informando que a permissão é necessária
                Toast.makeText(this, "A permissão para acessar a galeria é necessária.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        // Abrir a galeria
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            // Definir a imagem selecionada no ImageView
            ImageView imageViewSelectedImage = findViewById(R.id.imageViewSelectedImage);
            imageViewSelectedImage.setImageURI(imageUri);
        }
    }


    private void iniciarcomponentes() {
        buttonAddImage = findViewById(R.id.buttonAddImage);
        editTextContent = findViewById(R.id.editTextContent);
        bt_notificar = findViewById(R.id.bt_notificar);
        bt_publicar = findViewById(R.id.bt_publicar);
    }
}



