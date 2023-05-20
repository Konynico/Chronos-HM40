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

        // Ajouter un écouteur de sélection de date pour la vue CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Créer une instance de calendrier pour la date sélectionnée
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Créer une instance de calendrier pour la date sélectionnée
                selectedDate.set(year, month, dayOfMonth, 0, 0, 0); // Définir l'heure de début à 00:00:00
                selectedDate.set(Calendar.MILLISECOND, 0); // Définir les millisecondes à zéro pour éviter les problèmes de précision

                // Définir les paramètres de requête pour récupérer les événements à cette date
                long startOfDay = selectedDate.getTimeInMillis();
                selectedDate.add(Calendar.DAY_OF_MONTH, 1);
                long endOfDay = selectedDate.getTimeInMillis();
                String selection = "(" + CalendarContract.Events.DTSTART + ">= ? AND " + CalendarContract.Events.DTSTART + "< ?)";
                String[] selectionArgs = new String[] { Long.toString(startOfDay), Long.toString(endOfDay) };

                // Effectuer la requête pour récupérer les événements à cette date
                Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, selection, selectionArgs, null);

                // Afficher les événements à cette date dans la console
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                    @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                    @SuppressLint("Range") String startTimeStr = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART));
                    @SuppressLint("Range") String endTimeStr = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTEND));
                    Log.d(TAG, "Title: " + title + ", Description: " + description + ", Start Time: " + startTimeStr + ", End Time: " + endTimeStr);
                }

                cursor.close();
            }
        });
    }

    public void addEvent(View view) {
        // Générer un titre et une description aléatoires pour l'événement
        String[] titles = {"Réunion", "Rendez-vous", "Conférence", "Déjeuner", "Dîner", "Fête"};
        String[] descriptions = {"Avec le client XYZ", "Avec l'équipe de développement", "Sur le projet ABC", "Pour discuter des objectifs du trimestre"};

        Random random = new Random();
        int titleIndex = random.nextInt(titles.length);
        int descriptionIndex = random.nextInt(descriptions.length);

        String title = titles[titleIndex];
        String description = descriptions[descriptionIndex];

        // Générer une date et une heure aléatoires pour l'événement
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Calendar.HOUR_OF_DAY, random.nextInt(24));
        beginTime.set(Calendar.MINUTE, random.nextInt(60));
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, beginTime.get(Calendar.HOUR_OF_DAY) + random.nextInt(4) + 1);
        endTime.set(Calendar.MINUTE, random.nextInt(60));
        long endMillis = endTime.getTimeInMillis();

        // Ajouter l'événement au calendrier
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_COLOR, Color.BLUE);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        try {
            cr.insert(CalendarContract.Events.CONTENT_URI, values);
            Toast.makeText(this, "Événement ajouté au calendrier", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de l'ajout de l'événement", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onButtonClickCalendar(View view) {
        // Créer une nouvelle intention pour l'activité que vous souhaitez ouvrir
        Intent intent = new Intent(this, CreateEvenement.class);

        // Démarrer l'activité en utilisant cette intention
        startActivity(intent);
        //finish();
    }

}
