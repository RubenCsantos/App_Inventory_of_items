package com.example.tantacoisa;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    public void popUpMenu(View v) {
        Intent menu = new Intent(this, goToMenu.class);
        startActivity(menu);
    }

    EditText nome_input, localizacao_input, dAdquirido_input, Uvisto_input;
    Button update_button, delete_button;

    String id, nome, localizacao, data_adquirido, ultima_vez_visto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        nome_input = findViewById(R.id.nome_obj2);
        localizacao_input = findViewById(R.id.local2);
        dAdquirido_input = findViewById(R.id.data_compra2);
        Uvisto_input = findViewById(R.id.ultima_vez_visto2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        //Primeiro chama-mos esta função
        getAndSetIntentData();

        //Set actionBAr title after getandsetdata
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(nome);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //E só depois podemos chamar esta
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                nome = nome_input.getText().toString().trim();
                localizacao = localizacao_input.getText().toString().trim();
                data_adquirido = dAdquirido_input.getText().toString().trim();
                ultima_vez_visto = Uvisto_input.getText().toString().trim();
                myDB.updateData(id, nome, data_adquirido, localizacao, ultima_vez_visto);

            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });


    }

    void getAndSetIntentData(){            //Vais meter ou buscar os dados do inventário, consoante a necessidade
        if(getIntent().hasExtra("id") && getIntent().hasExtra("nome") &&
            getIntent().hasExtra("localizacao") && getIntent().hasExtra("data_adquirido") &&
            getIntent().hasExtra("ultima_visto")){

            //get data do Intent
            id = getIntent().getStringExtra("id");
            nome = getIntent().getStringExtra("nome");
            localizacao = getIntent().getStringExtra("localizacao");
            data_adquirido = getIntent().getStringExtra("data_adquirido");
            ultima_vez_visto = getIntent().getStringExtra("ultima_visto");

            //Set Intent data
            nome_input.setText(nome);
            localizacao_input.setText(localizacao);
            dAdquirido_input.setText(data_adquirido);
            Uvisto_input.setText(ultima_vez_visto);


        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){      //Função para ter as caixas de dialogo entre o utilizador e a máquina
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover " + nome + " ?");
        builder.setMessage("Tem a certeza que quer remover " + nome + " ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}