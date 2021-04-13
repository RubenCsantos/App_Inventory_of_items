package com.example.tantacoisa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class goToMenu extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView sem_dados;

    MyDatabaseHelper myDB;
    ArrayList<String> id_id ,nome_id, dAdquirido_id, localizacao_id, uVisto_id;
    CustomAdapter customAdapter;

    //search bar
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_menu);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_view);
        sem_dados = findViewById(R.id.sem_dados);

        add_button.setOnClickListener(new View.OnClickListener() {         //Caso seja pressionado o botão de Registo, volta-se à Atividade de apresentação do inventário
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(goToMenu.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(goToMenu.this);
        id_id = new ArrayList<>();
        nome_id = new ArrayList<>();
        dAdquirido_id = new ArrayList<>();
        localizacao_id = new ArrayList<>();
        uVisto_id = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter( goToMenu.this,this, id_id, nome_id, localizacao_id, dAdquirido_id,uVisto_id);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(goToMenu.this));


        //search bar
        searchView = findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {        //Recrear a atividade, reduzindo o tempo de espera
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){                //Guardar os dados nos respetivos Arrays Lists , consoante a "columnIndex"
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);  //caso a base de dados esteja vazia, apresenta uma imagem e uma text view, caso tenha dados. estas automaticamente deseparecem
            sem_dados.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                id_id.add(cursor.getString( 0));
                nome_id.add(cursor.getString( 1));
                dAdquirido_id.add(cursor.getString( 2));
                localizacao_id.add(cursor.getString( 3));
                uVisto_id.add(cursor.getString( 4));
            }
            empty_imageview.setVisibility(View.GONE);
            sem_dados.setVisibility(View.GONE);               //Desaparecem consoante os dados na base de dados, ou seja, como mencionado em cima, caso a base de dados tenha informação ou não
        }
    }

    public void settingsMenu(View v) {
        Intent settings = new Intent(this, definicoes.class);  //Intent para ir para a atividade das Settings/Definições.
        startActivity(settings);
    }

    @Override
    protected void onResume() {            //Método onResume
        super.onResume();

        customAdapter = new CustomAdapter( goToMenu.this,this, id_id, nome_id, localizacao_id, dAdquirido_id,uVisto_id);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(goToMenu.this));
    }
}
