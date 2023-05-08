package com.example.chronos_hm40;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class Calendar extends AppCompatActivity {

    CalendarView calendarView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);

    }

    public void onButtonClickCalendar(View view) {
        // Créer une nouvelle intention pour l'activité que vous souhaitez ouvrir
        Intent intent = new Intent(this, CreateEvenement.class);

        // Démarrer l'activité en utilisant cette intention
        startActivity(intent);
        //finish();
    }

}
