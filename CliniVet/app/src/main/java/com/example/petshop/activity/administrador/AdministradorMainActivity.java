package com.example.petshop.activity.administrador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.activity.autenticacao.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdministradorMainActivity extends AppCompatActivity {

//    private ImageButton ib_menu;

    private LinearLayout menu_reservas_adm, menu_servicos_adm;
    private TextView txtView_nomeUsuario;
    private ImageButton btn_logout;

    private DatabaseReference mDatabase;
    private FirebaseAuth fAuth;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administradormainactivity);

        iniciaComponentes();

        DatabaseReference userNameReference = mDatabase.child("usuarios").child(userID).child("nome");

        userNameReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String value = task.getResult().getValue(String.class);
                    txtView_nomeUsuario.setText(value);
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        configCliques();
    }

    private void configCliques(){
//        ib_menu.setOnClickListener(view -> {
//            PopupMenu popupMenu = new PopupMenu(this, ib_menu);
//            popupMenu.getMenuInflater().inflate(R.menu.menu_home_administrador, popupMenu.getMenu());
//
//            popupMenu.setOnMenuItemClickListener(menuItem -> {
//                if(menuItem.getItemId() == R.id.menu_reservas){
//                    startActivity(new Intent(this, TodasASReservasActivity.class));
//                }else if(menuItem.getItemId() == R.id.menu_servicos){
//                    startActivity(new Intent(this, MeusServicosActivity.class));
//                }else if(menuItem.getItemId() == R.id.menu_minha_conta){
//                    Toast.makeText(this, "Minha conta", Toast.LENGTH_SHORT).show();
//                }
//
//                return true;
//            });
//
//            popupMenu.show();
//        });

        menu_reservas_adm.setOnClickListener(view ->
                startActivity(new Intent(this, TodasASReservasActivity.class))
        );

        menu_servicos_adm.setOnClickListener(view ->
                startActivity(new Intent(this, MeusServicosActivity.class))
        );

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(AdministradorMainActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void iniciaComponentes(){
//        ib_menu = findViewById(R.id.ib_menu);

        menu_reservas_adm = findViewById(R.id.menu_reservas_adm);
        menu_servicos_adm = findViewById(R.id.menu_servicos_adm);
        txtView_nomeUsuario = findViewById(R.id.txtView_nomeUsuario);
        btn_logout = findViewById(R.id.btn_logout);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        fAuth = FirebaseAuth.getInstance();

        userID = fAuth.getCurrentUser().getUid();

    }
}