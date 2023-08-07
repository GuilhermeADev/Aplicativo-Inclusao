package com.example.inclusao.Screens;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.inclusao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;





public class Cursos extends AppCompatActivity {

    private Button addcurso;
    private EditText editTitulo;
    private EditText editlink;
    private AppCompatButton buttonPublicar;

    private WebView videos;

    private CollectionReference cursosRef;

    private LinearLayout cursosContainer;


    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);
        cursosContainer = findViewById(R.id.cursosContainer);
        cursosContainer.setVisibility(View.VISIBLE);
        iniciarcomponentes();
        verificarPermissaoADM();
        carregarCursos();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        cursosRef = db.collection("Cursos");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Garantir que os cursos sejam carregados novamente ao voltar para a tela
        carregarCursos();
    }

    private void carregarCursos() {
        cursosContainer = findViewById(R.id.cursosContainer);
        cursosContainer.setVisibility(View.VISIBLE);
        // ... (carregar os cursos, atualizar a visibilidade do LinearLayout)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancelar o listener ou realizar outras operações de limpeza, se necessário
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }


    private void verificarPermissaoADM() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userID != null) {
            listenerRegistration = FirebaseFirestore.getInstance()
                    .collection("Usuarios")
                    .document(userID)
                    .addSnapshotListener((snapshot, exception) -> {
                        if (exception != null) {
                            // Tratar erro de leitura do banco de dados
                            Toast.makeText(Cursos.this, "Erro ao verificar permissão de ADM", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Boolean isAdmin = snapshot.getBoolean("isAdmin");

                            if (isAdmin != null && isAdmin) {
                                // Usuário é ADM, tornar o botão addnews visível
                                addcurso.setVisibility(View.VISIBLE);
                                // Adicionar OnClickListener para exibir os outros componentes quando o botão for clicado
                                addcurso.setOnClickListener(v -> {
                                    // Tornar os outros componentes visíveis
                                    findViewById(R.id.tituloCurso).setVisibility(View.VISIBLE);
                                    findViewById(R.id.linkcurso).setVisibility(View.VISIBLE);
                                    buttonPublicar.setVisibility(View.VISIBLE);
                                });
                            } else {
                                // Usuário não é ADM, tornar o botão addnews invisível
                                addcurso.setVisibility(View.GONE);
                                // Tornar os outros componentes invisíveis
                                findViewById(R.id.tituloCurso).setVisibility(View.GONE);
                                findViewById(R.id.linkcurso).setVisibility(View.GONE);
                                buttonPublicar.setVisibility(View.GONE);
                            }
                        } else {
                            // Usuário não possui um documento correspondente no Firestore, ocultar todos os componentes
                            addcurso.setVisibility(View.GONE);
                            findViewById(R.id.tituloCurso).setVisibility(View.GONE);
                            findViewById(R.id.linkcurso).setVisibility(View.GONE);
                            buttonPublicar.setVisibility(View.GONE);
                        }
                    });
        } else {
            // Usuário não está autenticado, ocultar todos os componentes
            addcurso.setVisibility(View.GONE);
            findViewById(R.id.tituloCurso).setVisibility(View.GONE);
            findViewById(R.id.linkcurso).setVisibility(View.GONE);
            buttonPublicar.setVisibility(View.GONE);
        }
    }

    private void iniciarcomponentes() {
        addcurso = findViewById(R.id.addcurso);
        editTitulo = findViewById(R.id.tituloCurso);
        editlink = findViewById(R.id.linkcurso);
        buttonPublicar = findViewById(R.id.bt_publicar);
        videos = findViewById(R.id.videos);

        videos.getSettings().setJavaScriptEnabled(true);
        videos.setWebChromeClient(new WebChromeClient());

        buttonPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = editTitulo.getText().toString();
                String link = editlink.getText().toString();

                String video = "<iframe width=\"100%\" height=\"100%\" src=\"" + link + "\" frameborder=\"0\" allowfullscreen></iframe>";
                videos.loadData(video, "text/html", "utf-8");

                // Salvar título e link no Firebase
                salvarCursoNoFirebase(titulo, link);

                TextView curso1 = findViewById(R.id.curso1);
                curso1.setText(titulo);

                LinearLayout cursoLayout = criarCursoLayout(titulo, link);
                cursosContainer.addView(cursoLayout);
                cursosContainer.setVisibility(View.VISIBLE);
            }
        });

    }

    private void salvarCursoNoFirebase(String titulo, String link) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();

            // Criar um novo documento na coleção "Cursos" com um ID gerado automaticamente
            cursosRef.add(new Curso(titulo, link, userID))
                    .addOnSuccessListener(documentReference -> {
                        // Sucesso ao salvar
                        Toast.makeText(Cursos.this, "Curso salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Falha ao salvar
                        Toast.makeText(Cursos.this, "Erro ao salvar o curso.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private LinearLayout criarCursoLayout(String titulo, String link) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 16); // Margem inferior entre os cursos
        LinearLayout cursoLayout = new LinearLayout(this);
        cursoLayout.setOrientation(LinearLayout.HORIZONTAL);
        cursoLayout.setLayoutParams(layoutParams);

        // Crie um TextView para exibir o título do curso
        TextView tituloTextView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        tituloTextView.setLayoutParams(textParams);
        tituloTextView.setText(titulo);

        // Crie um WebView para exibir o vídeo do curso
        WebView webView = new WebView(this);
        LinearLayout.LayoutParams webViewParams = new LinearLayout.LayoutParams(
                350, // Largura desejada da WebView
                250  // Altura desejada da WebView
        );
        webView.setLayoutParams(webViewParams);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"" + link + "\" frameborder=\"0\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");

        // Adicione o TextView e a WebView ao cursoLayout
        cursoLayout.addView(tituloTextView);
        cursoLayout.addView(webView);

        return cursoLayout;
    }

}

