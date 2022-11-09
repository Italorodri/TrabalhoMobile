package com.example.petshop.activity.administrador;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.helper.FirebaseHelper;
import com.example.petshop.model.Reserva;
import com.google.firebase.database.DatabaseReference;

public class EditarStatusReservaActivity extends AppCompatActivity {

    Reserva reserva;
    TextView text_servico, text_dia, text_horario, text_pet,text_dono,text_telefone;
    private Spinner reserva_spinner;
    String[] statusOptions = new String[]{"cancelada","reservada","pendente","concluida"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_status_reserva);

        iniciaComponentes();

        configCliques();

        reserva = (Reserva) getIntent().getSerializableExtra("reserva");

        //config status spinner
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                statusOptions);

        reserva_spinner.setAdapter(adapterStatus);

        configDados();
    }

    private void configDados(){
        if(reserva != null){
            text_servico.setText(reserva.getServicoNome());
            text_dia.setText(reserva.getDia());
            text_horario.setText(reserva.getHora());
            text_pet.setText(reserva.getPetNome());
            text_dono.setText(reserva.getDonoNome());
            text_telefone.setText(reserva.getTelefoneDono());

            if(reserva.getStatus().equals("reservado")){
                reserva_spinner.setSelection(1);
            }else{
                reserva_spinner.setSelection(2);
            }


        }else{
            Toast.makeText(this, "Não foi possível recuperar as informações.", Toast.LENGTH_SHORT).show();
        }
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());

        reserva_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(reserva_spinner.getSelectedItem().equals("pendente")){
                    reserva.setStatus("pendente");
                }else if(reserva_spinner.getSelectedItem().equals("reservada")){
                    reserva.setStatus("reservada");
                }else if(reserva_spinner.getSelectedItem().equals("cancelada")){
                    reserva.setStatus("cancelada");
                }else if(reserva_spinner.getSelectedItem().equals("concluida")){
                    reserva.setStatus("concluida");
                }

                DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                        .child("reservas")
                        .child(reserva.getIdDono())
                        .child(reserva.getId());
                reference.setValue(reserva);

                DatabaseReference reference2 = FirebaseHelper.getDatabaseReference()
                        .child("reservas_all")
                        .child(reserva.getId());
                reference2.setValue(reserva);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void iniciaComponentes(){
        text_servico = findViewById(R.id.text_servico);
        text_dia = findViewById(R.id.text_dia);
        text_horario = findViewById(R.id.text_horario);
        text_pet = findViewById(R.id.text_pet);
        text_dono = findViewById(R.id.text_dono);
        text_telefone = findViewById(R.id.text_telefone);
        reserva_spinner = findViewById(R.id.reserva_spinner);

        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Detalhes da Reserva");
    }
}