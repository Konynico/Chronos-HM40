package com.example.chronos_hm40;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.content.Intent;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.TimeZone;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ListView eventListView;
    private SimpleCursorAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Récupérer la vue CalendarView à partir de la ressource XML
        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.eventListView);
    }

    public void onButtonClickCalendar(View view) {
        // Créer une nouvelle intention pour l'activité que vous souhaitez ouvrir
        Intent intent = new Intent(this, CreateEvenement.class);

        // Démarrer l'activité en utilisant cette intention
        startActivity(intent);
        //finish();
    }

}
