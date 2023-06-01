package com.example.chronos_hm40;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Create and start the CreateEvenement activity
                Intent intent = new Intent(CalendarActivity.this, CreateEvenement.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });
    }

    public void onButtonClickCalendar(View view) {
        // Créer une nouvelle intention pour l'activité que vous souhaitez ouvrir
        Intent intent = new Intent(this, CreateEvenement.class);

        // Démarrer l'activité en utilisant cette intention
        startActivity(intent);
        //finish();
    }
}