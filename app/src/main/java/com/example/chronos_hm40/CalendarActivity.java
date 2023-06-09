package com.example.chronos_hm40;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;

    private EventDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        databaseHelper = new EventDatabaseHelper(this);

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalendarActivity.this, EventListActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });
    }

    public void onButtonClickCalendar(View view) {
        // Créer une nouvelle intention pour l'activité que vous souhaitez ouvrir
        Intent intent = new Intent(this, AddEventActivity.class);

        // Démarrer l'activité en utilisant cette intention
        startActivity(intent);
        //finish();
    }

    public EventDatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}