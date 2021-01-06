package com.example.organizechallenge.model;

import com.example.organizechallenge.config.ConfiguracaoFirebase;
import com.example.organizechallenge.helper.Base64Custom;
import com.example.organizechallenge.helper.DateUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Equipe {
    private String key;
    private String nome;

    public Equipe() {

    }
    public void salvar(String nome){
        FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(mAuth.getCurrentUser().getEmail());

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("equipes")
                .child(idUsuario)
                .child("torneio")
                .push()
                .setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
