package com.example.tantacoisa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class definicoes extends AppCompatActivity {


    // Permissoes Import e Export

    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private static final int STORAGE_REQUEST_CODE_IMPORT = 2;
    private String[] storagePermissions;
    MyDatabaseHelper db = new MyDatabaseHelper(definicoes.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

            //IMPORT E EXPORT
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private boolean checkStoragePermission(){                    // ver se storage permissions está enable ou não e dar return True/False
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStiragePermissionImport(){
        //pedir permissão para o Import
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE_IMPORT);
    }

    private void requestStiragePermissionExport(){
        //pedir permissão para o Export
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE_EXPORT);
    }





    public void popUpMenu(View v) {                   //Intent para ir para a atividade do Menu, ou seja, a apresentação do inventário.
        Intent menu = new Intent(this, goToMenu.class);
        startActivity(menu);
    }

    public void home(View v) {                     //Intent para ir para a atividade Home.
        Intent backHome = new Intent(this, MainActivity.class);
        startActivity(backHome);
    }


        //Reset ao Inventário
        public void confirmDialog(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset");
        builder.setMessage("Deseja dar reset ao inventário?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(definicoes.this);
                myDB.restartDB();                        //Função de restar à Base de Dados
                Toast.makeText(definicoes.this,"Restart efetuado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                                                     //Caso seja "não", nada acontece
            }
        });
        builder.create().show();
    }

    //Import ao Inventário
    public void confirmDialog2(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importar");
        builder.setMessage("Deseja importar dados para o inventário?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(definicoes.this);
                String data = "";
                StringBuffer sBuffer = new StringBuffer();

                InputStream ficheiro = definicoes.this.getResources().openRawResource(R.raw.impdata);  //abrir ficheiros que se encontram na path "raw"

                BufferedReader reader = new BufferedReader(new InputStreamReader(ficheiro));

                if(ficheiro != null) {                    //Caso o ficheiro não seja null, ou seja, exista

                    try{
                        while((data=reader.readLine()) != null){           //Vai se ler linha a linha
                            if(!data.equals(""))
                                myDB.importData(data);                  //e importar os dados do ficheiro para a base de dados (Ver função importData no MyDataBaseHelper)
                        }
                        ficheiro.close();                 //fecha-se o ficheiro
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Caso seja "não", nada acontece
            }
        });
        builder.create().show();
    }

    //Export ao Inventário
    public void confirmDialog3(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exportar");
        builder.setMessage("Deseja exportar dados do inventário?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //permission allowed
                if(checkStoragePermission()){
                    exportCSV();
                }
                else{
                    //permission not allowed
                    requestStiragePermissionExport();
                    exportCSV();
                }
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Caso seja "não", nada acontece
            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {  //Resultado do request pelas permissões
        //handle resultado da permissão
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case STORAGE_REQUEST_CODE_EXPORT:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permissão garantida
                    exportCSV();
                }
                else{
                    //permissão negada
                    Toast.makeText(definicoes.this,"Storage permission required...", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE_IMPORT:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permissão garantida
                }
                else{
                    //permissão negada
                    Toast.makeText(definicoes.this,"Storage permission required..", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void exportCSV() {           //função de export, consiste em exportar os dados da base de dados para um ficheiro.csv
        //path do ficheiro csv
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + "SQLiteBackup"); //nome do ficheiro

        boolean isFolderCreated = false;
        if(!folder.exists()){
            isFolderCreated = folder.mkdir(); // criar ficheiro caso não exista
        }

        Log.d("CSC_TAG", "exportCSV:"+isFolderCreated);

        //nome do ficheiro
        String csvFileName = "SQLiteBAckup.csv";

        //path completo e nome
        String filePathName = folder.toString() + "/" + csvFileName;

        //get Arrays
        ArrayList id = new ArrayList();
        ArrayList nome = new ArrayList();
        ArrayList local = new ArrayList();
        ArrayList dAdd = new ArrayList();
        ArrayList Uvisto = new ArrayList();

        db.getAllData(id, nome, local, dAdd, Uvisto);  // Vai buscar toda a informação presente na tabela, linha a linha através dos index's das linhas

        try{
            //escrever ficheiro csv
            FileWriter fw = new FileWriter(filePathName);
            for(int i=0; i<id.size();i++){
                fw.append(""+id.get(i));
                fw.append(",");
                fw.append(""+nome.get(i)); //nome
                fw.append(",");
                fw.append(""+local.get(i)); //localização
                fw.append(",");
                fw.append(""+dAdd.get(i)); //Data Adquirido
                fw.append(",");
                fw.append(""+Uvisto.get(i)); //Ultima vez visto
                fw.append("\n");
            }
            fw.flush();
            fw.close();

            Toast.makeText(definicoes.this,"Exported", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){

            Toast.makeText(definicoes.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}