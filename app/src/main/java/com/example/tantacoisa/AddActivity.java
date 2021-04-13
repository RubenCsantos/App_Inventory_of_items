package com.example.tantacoisa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    EditText nome_input, dAquisicao_input, localizacao_input, ultimavisto_input;
    Button registo_button; //botão de registo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Edit's Text's dos respetivos parâmetros, onde se vai adicionar informação
        nome_input = findViewById(R.id.nome_obj);
        dAquisicao_input = findViewById(R.id.data_compra);
        localizacao_input = findViewById(R.id.local);
        ultimavisto_input = findViewById(R.id.ultima_vez_visto);
        registo_button = findViewById(R.id.registo_button);


        registo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                //função de registar
                myDB.addRegisto(nome_input.getText().toString().trim(),
                        dAquisicao_input.getText().toString().trim(),
                        localizacao_input.getText().toString().trim(),
                        ultimavisto_input.getText().toString().trim());
            }
        });

    }

    public void popUpMenu(View v) {
        Intent menu = new Intent(this, goToMenu.class);
        startActivity(menu);   //Intent que permite regressar ao Inventário depois de registar algo
    }

}