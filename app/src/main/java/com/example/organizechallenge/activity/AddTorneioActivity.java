package com.example.organizechallenge.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.organizechallenge.R;
import com.example.organizechallenge.config.ConfiguracaoFirebase;
import com.example.organizechallenge.helper.DateUtil;
import com.example.organizechallenge.model.Torneio;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;

public class AddTorneioActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    //atributos firebase
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

    //atributos a serem salvos
    private TextInputEditText  campoData;
    private EditText campoNomeTorneio;
    private Spinner spinnerOrdem, spinnerEstilo;
    private Torneio torneio;
    private String itemOrdem;
    private String itemEstilo;

    //atributos Spinner
    String []  ordem = {"Aleatório", "Manual"};
    String []  estilo = {"Eliminatórias", "Pontos Corridos","GP+Eliminatórias"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_torneio);

        campoNomeTorneio = findViewById(R.id.editNomeTorneio);
        campoData = findViewById(R.id.editData);

        spinnerOrdem = findViewById(R.id.spinnerOrdem);
        spinnerOrdem.setOnItemSelectedListener(this);

        spinnerEstilo = findViewById(R.id.spinnerEstilo);
        spinnerEstilo.setOnItemSelectedListener(this);

        campoData.setText( DateUtil.dataAtual() );

        ArrayAdapter arrayOrdem = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ordem);
        arrayOrdem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerOrdem.setAdapter(arrayOrdem);

        ArrayAdapter arrayEstilo = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estilo);
        arrayEstilo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerEstilo.setAdapter(arrayEstilo);

    }

    public void salvarTorneio(View view){
        if(validarCampos()){

            torneio = new Torneio();
            String data = campoData.getText().toString();
            torneio.setNome(campoNomeTorneio.getText().toString());
            torneio.setData(data);
            torneio.setOrdem(itemOrdem);
            torneio.setEstilo(itemEstilo);
            torneio.salvar(data);

            finish();
        }
    }

    public Boolean validarCampos(){
        String textoNomeTorneio = campoNomeTorneio.getText().toString();
        String textoData = campoData.getText().toString();

        if(!textoNomeTorneio.isEmpty()){
            if(!textoData.isEmpty()){
                return true;
            }else{
                Toast.makeText(this, "Por favor preencha o Campo Data",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(this, "Por favor preencha o Campo Nome",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemOrdem = spinnerOrdem.getSelectedItem().toString();
        itemEstilo = spinnerEstilo.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}