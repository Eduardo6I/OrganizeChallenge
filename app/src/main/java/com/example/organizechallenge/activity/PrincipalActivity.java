package com.example.organizechallenge.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.organizechallenge.adapter.AdapterTorneio;
import com.example.organizechallenge.config.ConfiguracaoFirebase;
import com.example.organizechallenge.helper.Base64Custom;
import com.example.organizechallenge.model.Torneio;
import com.example.organizechallenge.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizechallenge.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private DatabaseReference torneioRef;

    private MaterialCalendarView calendarView;
    private TextView textosaudacao;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerTorneios;
    private String mesAnoSelecionado;

    private RecyclerView recyclerView;
    private AdapterTorneio adapterTorneio;
    private List<Torneio> torneios = new ArrayList<>();
    private Torneio torneio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        textosaudacao = findViewById(R.id.textSaldacao);


        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerTorneios);
        
        configuraCalendarView();
        swipe();
        //configura Adapter
        adapterTorneio = new AdapterTorneio(torneios, this);
        //configura recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterTorneio);

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
                    excluirTorneio(viewHolder);
                }
            };
            new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

        }

        public void excluirTorneio(RecyclerView.ViewHolder viewHolder){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            //Configurar alertDialog
            alertDialog.setTitle("Excluir Torneio");
            alertDialog.setMessage("Você tem certeza que deseja concluir essa ação? Após a confirmação não poderá ser desfeito.");
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    torneio = torneios.get(position);

                    String emailUsuario = mAuth.getCurrentUser().getEmail();
                    String idUsuario = Base64Custom.codificarBase64(emailUsuario);

                    torneioRef = firebaseRef.child("torneios")
                            .child(idUsuario)
                            .child(mesAnoSelecionado);

                    torneioRef.child(torneio.getKey()).removeValue();
                    adapterTorneio.notifyItemRemoved(position);

                    Toast.makeText(PrincipalActivity.this, "Exclusão concluída.",
                            Toast.LENGTH_SHORT).show();

                }
            });

            alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(PrincipalActivity.this, "Ação Cancelada.",
                            Toast.LENGTH_SHORT).show();
                    adapterTorneio.notifyDataSetChanged();
                }
            });
            AlertDialog aler = alertDialog.create();
            aler.show();
        }

        public void recuperarTorneios(){
            String emailUsuario = mAuth.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarBase64(emailUsuario);

            torneioRef = firebaseRef.child("torneios")
                    .child(idUsuario)
                    .child(mesAnoSelecionado);
            valueEventListenerTorneios = torneioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    torneios.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()){
                        Torneio torneio = dados.getValue(Torneio.class);
                        torneio.setKey(dados.getKey());
                        torneios.add(torneio);
                    }
                    adapterTorneio.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    public void recuperaUsuario(){
        String emailUsuario = mAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios")
                .child(idUsuario);
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                textosaudacao.setText("Olá, "+ usuario.getNome() + "." + "\n Esta é sua lista de Torneios.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menusair :
                logout();
                finish();
                break;

            case R.id.menuinformacao :
                abrirInformacoes();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void logout(){
        try{
            mAuth.signOut();
            startActivity(new Intent( this, MainActivity.class));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void abrirInformacoes(){
        //startActivity(new Intent(this, InformacoesActivity.class));
    }
    public void adicionarTorneio(View view){
        startActivity(new Intent(this, AddTorneioActivity.class));

    }

    public void listarTorneio(View view){
        startActivity(new Intent(this, PrincipalActivity.class));

    }

    public void listarCampeao(View view){
        startActivity(new Intent(this, ListaCampeaoActivity.class));
    }

    public void configuraCalendarView(){
       CharSequence meses[] = { "Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"};
       calendarView.setTitleMonths(meses);
       CalendarDay dataAtual = calendarView.getCurrentDate();
       String mesSelecionado = String.format("%02d", (dataAtual.getMonth()));
       mesAnoSelecionado = String.valueOf( mesSelecionado + "" + dataAtual.getYear());
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth()));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());
                torneioRef.removeEventListener(valueEventListenerTorneios);
                recuperarTorneios();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaUsuario();
        recuperarTorneios();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        torneioRef.removeEventListener(valueEventListenerTorneios);
    }
}