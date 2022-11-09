package com.example.petshop.activity.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.activity.administrador.DetalhesServicoActivity;
import com.example.petshop.adapter.AdapterServicos;
import com.example.petshop.helper.FirebaseHelper;
import com.example.petshop.model.Servico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AgendarAtendimento extends AppCompatActivity implements AdapterServicos.OnClick {

    private RecyclerView rv_servicos;
    private TextView text_info;
    private ProgressBar progressBar;
    private ImageButton imageButton_voltar;

    private List<Servico> servicoList = new ArrayList<>();
    private AdapterServicos adapterServicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_atendimento);

        iniciaComponentes();

        configCliques();

        configRv();

        recuperaServicos();

    }

    private void configRv() {

        rv_servicos.setLayoutManager(new LinearLayoutManager(this));
        rv_servicos.setHasFixedSize(true);
        adapterServicos = new AdapterServicos(servicoList, this);
        rv_servicos.setAdapter(adapterServicos);

    }

    private void configCliques(){
        imageButton_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void recuperaServicos() {

        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("servicos");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    servicoList.clear();
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Servico servico = snap.getValue(Servico.class);
                        servicoList.add(servico);
                    }
                    text_info.setText("");
                }else{
                    text_info.setText("Nenhum serviço cadastrado.");
                }
                progressBar.setVisibility(View.GONE);
                Collections.reverse(servicoList);
                adapterServicos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void iniciaComponentes(){
        rv_servicos = findViewById(R.id.rv_servicos);
        text_info = findViewById(R.id.text_info);
        progressBar = findViewById(R.id.progressBar);
        imageButton_voltar = findViewById(R.id.imageButton_voltar);
    }


    @Override
    public void OnClickListener(Servico servico) {
        Intent intent = new Intent(this, DetalhesServicoActivity.class);
        intent.putExtra("servico", servico);
        startActivity(intent);
    }
}