package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    public void onButtonClickMenu(View view) {
        // Créer une nouvelle intention pour l'activité que vous souhaitez ouvrir
        Intent intent = new Intent(this, Calendar.class);

        // Démarrer l'activité en utilisant cette intention
        startActivity(intent);
        //finish();
    }
}