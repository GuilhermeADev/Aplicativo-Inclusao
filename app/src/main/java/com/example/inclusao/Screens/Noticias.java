package com.example.inclusao.Screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.inclusao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;


import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Noticias extends AppCompatActivity {
    private Button addnews;
    private ImageView imageViewSelectedImage;
    private ImageView publicImage;
    private EditText editTitulo;
    private ListenerRegistration listenerRegistration;

    boolean isOptionsVisible = false;

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
                                    findViewById(R.id.editTitulo).setVisibility(View.VISIBLE);
                                });
                            } else {
                                // Usuário não é ADM, tornar o botão addnews invisível
                                addnews.setVisibility(View.GONE);
                                // Tornar os outros componentes invisíveis
                                findViewById(R.id.buttonAddImage).setVisibility(View.GONE);
                                findViewById(R.id.editTextContent).setVisibility(View.GONE);
                                findViewById(R.id.bt_publicar).setVisibility(View.GONE);
                                findViewById(R.id.bt_notificar).setVisibility(View.GONE);
                                findViewById(R.id.editTitulo).setVisibility(View.GONE);
                            }
                        } else {
                            // Usuário não possui um documento correspondente no Firestore, ocultar todos os componentes
                            addnews.setVisibility(View.GONE);
                            findViewById(R.id.buttonAddImage).setVisibility(View.GONE);
                            findViewById(R.id.editTextContent).setVisibility(View.GONE);
                            findViewById(R.id.bt_publicar).setVisibility(View.GONE);
                            findViewById(R.id.bt_notificar).setVisibility(View.GONE);
                            findViewById(R.id.editTitulo).setVisibility(View.GONE);
                        }
                    });
        } else {
            // Usuário não está autenticado, ocultar todos os componentes
            addnews.setVisibility(View.GONE);
            findViewById(R.id.buttonAddImage).setVisibility(View.GONE);
            findViewById(R.id.editTextContent).setVisibility(View.GONE);
            findViewById(R.id.bt_publicar).setVisibility(View.GONE);
            findViewById(R.id.bt_notificar).setVisibility(View.GONE);
            findViewById(R.id.editTitulo).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        iniciarcomponentes();
        ocultarComponentes();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference noticiasRef = db.collection("Noticias");

        // Consultar todas as notícias no Firestore
        noticiasRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                // Obter os dados da notícia
                String titulo = documentSnapshot.getString("titulo");
                String imageUrl = documentSnapshot.getString("imageUrl");

                // Exibir a notícia no aplicativo
                exibirNoticia(titulo, imageUrl);
            }
        }).addOnFailureListener(e -> {
            // Falha ao consultar as notícias no Firestore
            Toast.makeText(Noticias.this, "Falha ao carregar as notícias", Toast.LENGTH_SHORT).show();
        });
    }

    private void exibirNoticia(String titulo, String imageUrl) {
        LinearLayout container = findViewById(R.id.containercomponents);

        // Crie uma View de linha preta
        View linha = new View(this);
        LinearLayout.LayoutParams linhaParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2 // Altura desejada da linha (2dp)
        );
        linhaParams.setMargins(0, 8, 0, 8); // Margem superior e inferior da linha
        linha.setLayoutParams(linhaParams);
        linha.setBackgroundColor(Color.BLACK);

        // Adicione a linha abaixo da última notícia (se houver)
        if (container.getChildCount() > 0) {
            container.addView(linha);
        }

        // Crie um novo LinearLayout para a notícia
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 16); // Margem inferior entre as notícias
        linearLayout.setLayoutParams(layoutParams);

        // Crie um novo ImageView para a imagem da notícia
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                350, // Largura desejada da imagem
                350  // Altura desejada da imagem
        );
        imageView.setLayoutParams(imageParams);
        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .override(300, 300)
                .into(imageView);

        // Crie um novo TextView para o título da notícia
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.setMargins(50, 80, 0, 0); // Margem esquerda do texto
        textView.setLayoutParams(textParams);
        Typeface font = ResourcesCompat.getFont(this, R.font.timesbd);
        textView.setTypeface(font);
        textView.setText(titulo);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setMaxLines(2); // Define o número máximo de linhas do texto
        textView.setEllipsize(TextUtils.TruncateAt.END); // Define o comportamento de truncamento

        // Adicione a imagem e o título ao LinearLayout da notícia
        linearLayout.addView(imageView);
        linearLayout.addView(textView);

        // Adicione o LinearLayout da notícia ao LinearLayout principal (containercomponents)
        container.addView(linearLayout);
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
        editTitulo = findViewById(R.id.editTitulo);
        publicImage = findViewById(R.id.publicImage);
        AppCompatButton buttonAddImage = findViewById(R.id.buttonAddImage);
        AppCompatButton buttonPublicar = findViewById(R.id.bt_publicar);

        buttonAddImage.setOnClickListener(v -> {
            // Lógica para lidar com o clique do botão de adicionar imagem
            selecionarImagemDaGaleria();
        });

        buttonPublicar.setOnClickListener(v -> {
            // Lógica para lidar com o clique do botão de publicar
            String titulo = editTitulo.getText().toString();
            TextView textViewTitulo = findViewById(R.id.textViewTitulo);
            textViewTitulo.setText(titulo);

            // Exibir a imagem selecionada ao lado
            Drawable selectedImage = imageViewSelectedImage.getDrawable();
            publicImage.setImageDrawable(selectedImage);
            publicImage.setVisibility(View.VISIBLE);

            // Fazer upload da imagem selecionada para o Firebase Storage
            if (selectedImage != null) {
                // Gerar um nome de arquivo único para a imagem
                String fileName = "image_" + System.currentTimeMillis() + ".jpg";

                // Referência ao local de armazenamento no Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imageRef = storageRef.child("Noticias").child(fileName);

                // Converter a imagem Drawable em um formato suportado (por exemplo, Bitmap)
                Bitmap bitmap = drawableToBitmap(selectedImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                byte[] imageData = baos.toByteArray();

                // Fazer o upload da imagem para o Firebase Storage
                UploadTask uploadTask = imageRef.putBytes(imageData);
                uploadTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // O upload da imagem foi concluído com sucesso
                        // Obter a URL de download da imagem
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // Salvar a URL da imagem no Firestore
                            salvarNoticiaNoFirestore(titulo, imageUrl);
                        });
                    } else {
                        // O upload da imagem falhou
                        Toast.makeText(Noticias.this, "Falha ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        addnews.setOnClickListener(v -> {
            // Alternar a visibilidade das opções
            if (isOptionsVisible) {
                // As opções estão visíveis, então ocultá-las
                ocultarComponentes();
            } else {
                // As opções estão ocultas, então mostrá-las
                mostrarComponentes();
            }

            isOptionsVisible = !isOptionsVisible; // Alternar o estado das opções
        });




        verificarPermissaoADM(); // Verificar a permissão do usuário ADM

        if (addnews.getVisibility() == View.VISIBLE) {
            // O usuário é ADM, tornar os outros componentes invisíveis inicialmente
            ocultarComponentes();
        } else {
            // O usuário não é ADM, ocultar o botão addnews
            addnews.setVisibility(View.GONE);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void ocultarComponentes() {
        findViewById(R.id.buttonAddImage).setVisibility(View.GONE);
        findViewById(R.id.editTextContent).setVisibility(View.GONE);
        findViewById(R.id.bt_publicar).setVisibility(View.GONE);
        findViewById(R.id.bt_notificar).setVisibility(View.GONE);
        findViewById(R.id.editTitulo).setVisibility(View.GONE);
    }
    private void mostrarComponentes() {
        // Resto do seu código...

        // Definir a visibilidade dos componentes como visíveis
        findViewById(R.id.buttonAddImage).setVisibility(View.VISIBLE);
        findViewById(R.id.editTextContent).setVisibility(View.VISIBLE);
        findViewById(R.id.bt_publicar).setVisibility(View.VISIBLE);
        findViewById(R.id.bt_notificar).setVisibility(View.VISIBLE);
        findViewById(R.id.editTitulo).setVisibility(View.VISIBLE);
    }

    private void selecionarImagemDaGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    private void salvarNoticiaNoFirestore(String titulo, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> noticia = new HashMap<>();
        noticia.put("titulo", titulo);
        noticia.put("imageUrl", imageUrl);

        db.collection("Noticias")
                .add(noticia)
                .addOnSuccessListener(documentReference -> {
                    // A notícia foi salva com sucesso no Firestore
                    Toast.makeText(Noticias.this, "Notícia publicada", Toast.LENGTH_SHORT).show();
                    recreate();
                })
                .addOnFailureListener(e -> {
                    // Falha ao salvar a notícia no Firestore
                    Toast.makeText(Noticias.this, "Falha ao publicar notícia", Toast.LENGTH_SHORT).show();
                });
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