package com.example.petshop.activity.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshop.R;
import com.example.petshop.adapter.AdapterPets;
import com.example.petshop.helper.FirebaseHelper;
import com.example.petshop.model.Pet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeusPetsActivity extends AppCompatActivity implements AdapterPets.OnClick {

    private List<Pet> petList = new ArrayList<>();

    private ProgressBar progressBar;
    private TextView text_info;
    private SwipeableRecyclerView rv_pets;
    private AdapterPets adapterPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_pets);

        iniciaComponentes();

        configRv();

        configCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperaPets();
    }

    private void configCliques(){
        findViewById(R.id.ib_add).setOnClickListener(view -> {
            startActivity(new Intent(this, FormAdicionarPet.class));
        });
        findViewById(R.id.ib_voltar).setOnClickListener(view -> {
            finish();
        });
    }

    private void configRv(){
        rv_pets.setLayoutManager(new LinearLayoutManager(this));
        rv_pets.setHasFixedSize(true);
        adapterPets = new AdapterPets(petList, this);
        rv_pets.setAdapter(adapterPets);

        rv_pets.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {
                showDialogDelete(position);
            }
        });
    }

    private void showDialogDelete(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Pet");
        builder.setMessage("aperte em Sim para confirmar ou em Não para cancelar.");
        builder.setNegativeButton("Não", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            adapterPets.notifyDataSetChanged();
        });
        builder.setPositiveButton("Sim", (dialogInterface, i) -> {
            petList.get(pos).deletar();
            adapterPets.notifyItemRemoved(pos);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void recuperaPets(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("pets")
                .child(FirebaseHelper.getIdFirebase());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    petList.clear();
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Pet pet = snap.getValue(Pet.class);
                        petList.add(pet);
                    }
                    text_info.setText("");
                }else{
                    text_info.setText("Nenhum pet cadastrado.");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(petList);
                adapterPets.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniciaComponentes(){
        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Meus Pets");

        progressBar = findViewById(R.id.progressBar);
        text_info = findViewById(R.id.text_info);
        rv_pets = findViewById(R.id.rv_pets);
    }

    @Override
    public void OnClickListener(Pet pet) {
        Intent intent = new Intent(this, FormAdicionarPet.class);
        intent.putExtra("pet", pet);
        startActivity(intent);
    }
}