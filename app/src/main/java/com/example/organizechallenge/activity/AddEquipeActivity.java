package com.example.organizechallenge.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizechallenge.R;
import com.example.organizechallenge.adapter.AdapterEquipe;
import com.example.organizechallenge.adapter.AdapterTorneio;
import com.example.organizechallenge.config.ConfiguracaoFirebase;
import com.example.organizechallenge.helper.Base64Custom;
import com.example.organizechallenge.model.Equipe;
import com.example.organizechallenge.model.Torneio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddEquipeActivity extends AppCompatActivity {
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference equipeRef;

    private EditText campoNomeEquipe;
    private ValueEventListener valueEventListenerEquipe;

    private RecyclerView recyclerViewEquipe;
    private AdapterEquipe adapterEquipe;
    private List<Equipe> equipes = new ArrayList<>();
    private Equipe equipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipe);

        campoNomeEquipe = findViewById(R.id.editNomeEquipe);
        recyclerViewEquipe = findViewById(R.id.recyclerEquipe);

        swipe();
        //configura Adapter
        adapterEquipe = new AdapterEquipe(equipes, this);
        //configura recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewEquipe.setLayoutManager(layoutManager);
        recyclerViewEquipe.setHasFixedSize(true);
        recyclerViewEquipe.setAdapter(adapterEquipe);

    }



    public void salvarEquipe(View view) {
        if (validarCampos()) {

            String nomeEquipe = campoNomeEquipe.getText().toString();
            if(!nomeEquipe.isEmpty()){
                Equipe equipe = new Equipe();
                equipe.setNome(nomeEquipe);
                equipe.salvar(nomeEquipe);
                startActivity(new Intent(getApplicationContext(), AddEquipeActivity.class));

                finish();
            }

        }
    }

    public Boolean validarCampos() {
        String textoNomeEquipe = campoNomeEquipe.getText().toString();

        if (!textoNomeEquipe.isEmpty()) {
            return  true;
        } else {
            Toast.makeText(this, "Por favor preencha o Campo Equipe",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void swipe(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);

            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirEquipe(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewEquipe);

    }

    public void excluirEquipe(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        //Configurar alertDialog
        alertDialog.setTitle("Excluir Equipe");
        alertDialog.setMessage("Você tem certeza que deseja concluir essa ação? Após a confirmação não poderá ser desfeito.");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                equipe = equipes.get(position);

                String emailUsuario = mAuth.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);

                equipeRef = firebaseRef.child("equipes")
                        .child(idUsuario)
                        .child("torneio");

                equipeRef.child(equipe.getKey()).removeValue();
                adapterEquipe.notifyItemRemoved(position);

                Toast.makeText(AddEquipeActivity.this, "Exclusão concluída.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddEquipeActivity.this, "Ação Cancelada.",
                        Toast.LENGTH_SHORT).show();
                adapterEquipe.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
    public void recuperarEquipes(){
        String emailUsuario = mAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        equipeRef = firebaseRef.child("equipes")
                .child(idUsuario)
                .child("torneio");
        valueEventListenerEquipe = equipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                equipes.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Equipe equipe = dados.getValue(Equipe.class);
                    equipe.setKey(dados.getKey());
                    equipes.add(equipe);
                }
                adapterEquipe.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarEquipes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        equipeRef.removeEventListener(valueEventListenerEquipe);
    }
}