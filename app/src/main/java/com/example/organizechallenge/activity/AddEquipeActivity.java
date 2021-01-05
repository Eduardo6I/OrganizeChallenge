package com.example.organizechallenge.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizechallenge.R;
import com.example.organizechallenge.config.ConfiguracaoFirebase;
import com.example.organizechallenge.model.Equipe;
import com.example.organizechallenge.model.Torneio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AddEquipeActivity extends AppCompatActivity {
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

    private EditText campoNomeEquipe;
    private Equipe equipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipe);

        campoNomeEquipe = findViewById(R.id.editNomeEquipe);

    }

    public void salvarEquipe(View view) {
        if (validarCampos()) {

            equipe = new Equipe();
            String nome = campoNomeEquipe.getText().toString();
            equipe.setNome(nome);
            equipe.salvarEquipe(nome);

            finish();
        }
    }

    public Boolean validarCampos() {
        String textoNomeEquipe = campoNomeEquipe.getText().toString();

        if (!textoNomeEquipe.isEmpty()) {
            return  true;
        } else {
            Toast.makeText(this, "Por favor preencha o Campo Nome",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}