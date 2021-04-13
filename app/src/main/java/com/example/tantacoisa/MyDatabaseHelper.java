package com.example.tantacoisa;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATA_BASE_NAME = "Inventory.db";        //Designação da Base de Dados
    private static final int DATA_BASE_VERSION = 1;

    private static final String TABLE_NAME = "my_inventory";         //Designação da Tabela e das respetivas colunas
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_DCOMPRA = "data_adquirido";
    private static final String COLUMN_LOCALIZACAO = "localizacao";
    private  static final String COLUMN_ULTIMAVISTO = "data_visto";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {                            //Criação da base de dados e respetivas tabelas
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NOME + " TEXT, " +
                        COLUMN_DCOMPRA + " TEXT, " +
                        COLUMN_LOCALIZACAO + " TEXT, " +
                        COLUMN_ULTIMAVISTO + " TEXT);";
        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate((db));
    }

    void addRegisto(String nome, String dcompra, String localizacao, String ultimavisto){  //Função de Registar que recebe como parâmetros o que vai registar
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOME,nome);        //e associa cada parametro que recebeu à respetiva Coluna
        cv.put(COLUMN_DCOMPRA,dcompra);
        cv.put(COLUMN_LOCALIZACAO,localizacao);
        cv.put(COLUMN_ULTIMAVISTO,ultimavisto);
        long result = db.insert(TABLE_NAME, null, cv);   //Inserção na tabela

        if(result == -1){
            Toast.makeText(context, "Falha no registo", Toast.LENGTH_SHORT).show();          //Caso o registo seja mal efetuado
        }else{
            Toast.makeText(context, "Registo efetuado com sucesso", Toast.LENGTH_SHORT).show();         //Caso o registo seja bem efetuado
        }
    }

    Cursor readAllData(){  //Criação de um cursor
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String nome, String dAdquirido, String localizacao, String uVisto){  //Função de dar update à Base de dados
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOME, nome);
        cv.put(COLUMN_DCOMPRA, dAdquirido);              //Recebe como parâmetro o que já tinha na respetiva linha da base de dados e guarda as alterações
        cv.put(COLUMN_LOCALIZACAO, localizacao);
        cv.put(COLUMN_ULTIMAVISTO, uVisto);


        long result = db.update(TABLE_NAME, cv, "_id =?", new String[]{row_id});

        if(result == -1){
            Toast.makeText(context, "Falha na atualização.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Atualizado com sucesso.", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){         //Remoção de uma linha da tabela, recebe o id da linha a remover
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Remoção falhada.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Removido com sucesso.", Toast.LENGTH_SHORT).show();
        }
    }

    void restartDB(){              //Função que apaga todos os dados da base de dados
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public void importData(String dados){   //função de import, que executa uma query SQL presente no ficheiro a ler

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(dados);
        db.close();
    }

    public Cursor exportData(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return cursor;
    }

    public void getAllData(ArrayList<String> id, ArrayList<String> nome, ArrayList<String> local, ArrayList<String> dAdd, ArrayList<String> uvisto) {  //Função que vai buscar todos os dados da tabela
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while(cursor.moveToNext()){       //percorre a tabela e adiciona ao arrays consoante o indice da coluna
            id.add(String.valueOf(cursor.getInt(0)));
            nome.add(cursor.getString(1));
            dAdd.add(cursor.getString(2));
            local.add(cursor.getString(3));
            uvisto.add(cursor.getString(4));
        }
    }

    void addRegistoV2(int id ,String nome, String dcompra, String localizacao, String ultimavisto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID,id);
        cv.put(COLUMN_NOME,nome);
        cv.put(COLUMN_DCOMPRA,dcompra);
        cv.put(COLUMN_LOCALIZACAO,localizacao);
        cv.put(COLUMN_ULTIMAVISTO,ultimavisto);
        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1){
            Toast.makeText(context, "Falha no registo", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Registo efetuado com sucesso", Toast.LENGTH_SHORT).show();
        }
    }
}
