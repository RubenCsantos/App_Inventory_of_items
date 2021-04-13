package com.example.tantacoisa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.*;
import android.content.Intent;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void popUpMenu(View v){    //Intent para ir para a atividade do Menu, ou seja, para o inventário.
        Intent menu = new Intent(this,goToMenu.class);
        startActivity(menu);
    }
}