package com.example.chronos_hm40;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;

    private static ArrayList<String> events;

    private static File eventFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalendarActivity.this, ShowEvent.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });

        eventFile = new File(getFilesDir(), "event.csv");
        events = new ArrayList<>();
    }

    public void onButtonClickCalendar(View view) {
        // Créer une nouvelle intention pour l'activité que vous souhaitez ouvrir
        Intent intent = new Intent(this, AddEventActivity.class);

        // Démarrer l'activité en utilisant cette intention
        startActivity(intent);
        //finish();
    }

    public static ArrayList<String> getEvents() {
        return events;
    }

    public static File getFile()
    {
        return eventFile;
    }
}