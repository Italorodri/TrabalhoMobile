package com.example.petshop.activity.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshop.R;
import com.example.petshop.activity.administrador.AdministradorMainActivity;
import com.example.petshop.activity.cliente.ClienteMainActivity;
import com.example.petshop.helper.FirebaseHelper;
import com.example.petshop.model.Usuario;

public class CriarContaActivity extends AppCompatActivity {

    private EditText edit_nome, edit_email, edit_telefone, edit_senha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        configCliques();

        iniciaComponentes();
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void iniciaComponentes(){
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_telefone = findViewById(R.id.edit_telefone);
        edit_senha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressBar);

        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Crie sua conta");
    }

    public void validaDados(View view){
        String nome = edit_nome.getText().toString();
        String email = edit_email.getText().toString();
        String telefone = edit_telefone.getText().toString();
        String senha = edit_senha.getText().toString();

        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!telefone.isEmpty()){
                    if(!senha.isEmpty()){
                        progressBar.setVisibility(View.VISIBLE);

                        Usuario usuario = new Usuario();
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setTelefone(telefone);
                        usuario.setSenha(senha);

                        cadastrarUsuario(usuario);
                    }else{
                        edit_senha.requestFocus();
                        edit_senha.setError("Informe sua senha");
                    }
                }else{
                    edit_telefone.requestFocus();
                    edit_telefone.setError("Informe o seu telefone");
                }
            }else{
                edit_email.requestFocus();
                edit_email.setError("Informe o seu email");
            }
        }else{
            edit_nome.requestFocus();
            edit_nome.setError("Informe o seu nome");
        }

    }

    private void cadastrarUsuario(Usuario usuario){
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String idUser = task.getResult().getUser().getUid();
                usuario.setId(idUser);
                usuario.salvar();

                String partes[] = edit_email.getText().toString().split("@");

                if(partes[1].equals("clinivet.com")){
                    finish();
                    startActivity(new Intent(this, AdministradorMainActivity.class));
                }else{
                    finish();
                    startActivity(new Intent(this, ClienteMainActivity.class));
                }
            }else{
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}