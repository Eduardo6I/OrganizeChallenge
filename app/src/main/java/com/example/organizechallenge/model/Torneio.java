package com.example.organizechallenge.model;

import com.example.organizechallenge.config.ConfiguracaoFirebase;
import com.example.organizechallenge.helper.Base64Custom;
import com.example.organizechallenge.helper.DateUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Torneio {
    private String key;
    private String nome;
    private String data;
    private String ordem;
    private String estilo;

    public Torneio() {

    }

    public void salvar(String dataEscolhida){
        FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(mAuth.getCurrentUser().getEmail());
        String mesAno = DateUtil.mesAnoDataEscolhida(dataEscolhida);

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("torneios")
                .child(idUsuario)
                .child(mesAno)
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrdem() {
        return ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }
}
