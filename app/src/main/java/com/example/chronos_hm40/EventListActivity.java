package com.example.chronos_hm40;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    private EventDatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupérer les informations de la date sélectionnée depuis l'intent
        Intent intent = getIntent();
        int year = intent.getIntExtra("year", 0);
        int month = intent.getIntExtra("month", 0);
        int dayOfMonth = intent.getIntExtra("dayOfMonth", 0);

        CalendarActivity calendarActivity = (CalendarActivity) getApplicationContext();
        databaseHelper = calendarActivity.getDatabaseHelper();

        // Récupérer les événements associés à la date sélectionnée depuis la base de données
        List<Event> eventsForSelectedDate = databaseHelper.getEventsForSelectedDate(year, month, dayOfMonth);

        // Créer et attacher l'adaptateur à RecyclerView
        eventAdapter = new EventAdapter(eventsForSelectedDate);
        recyclerView.setAdapter(eventAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fermer la connexion avec la base de données lorsque l'activité est détruite
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

}