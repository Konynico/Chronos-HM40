package com.example.chronos_hm40;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import java.util.Calendar;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEvenement extends AppCompatActivity {

    private EditText eventTitleEditText;
    private EditText eventDescriptionEditText;
    private DatePicker eventStartDatePicker;
    private DatePicker eventEndDatePicker;
    private RadioGroup colorRadioGroup;
    private Button addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenement);

        eventTitleEditText = findViewById(R.id.eventTitle);
        eventDescriptionEditText = findViewById(R.id.eventDescription);
        eventStartDatePicker = findViewById(R.id.eventStartDate);
        eventEndDatePicker = findViewById(R.id.eventEndDate);
        colorRadioGroup = findViewById(R.id.colorRadioGroup);
        addEventButton = findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des informations de l'événement
                EditText titleEditText = findViewById(R.id.eventTitle);
                EditText descriptionEditText = findViewById(R.id.eventDescription);
                DatePicker startDatePicker = findViewById(R.id.eventStartDate);
                DatePicker endDatePicker = findViewById(R.id.eventEndDate);
                RadioGroup colorRadioGroup = findViewById(R.id.colorRadioGroup);

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                int startYear = startDatePicker.getYear();
                int startMonth = startDatePicker.getMonth();
                int startDay = startDatePicker.getDayOfMonth();

                int endYear = endDatePicker.getYear();
                int endMonth = endDatePicker.getMonth();
                int endDay = endDatePicker.getDayOfMonth();

                // Convertir les dates en millisecondes
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.set(startYear, startMonth, startDay);
                long startMillis = startCalendar.getTimeInMillis();

                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(endYear, endMonth, endDay);
                long endMillis = endCalendar.getTimeInMillis();

                // Ajout de l'événement à l'agenda
                //addEventToCalendar(startMillis, endMillis, title, description);
            }

        });
    }

    public void addEventToCalendar(long startMillis, long endMillis, String title, String description) {
        // Récupérer l'ID du calendrier
        String calID = "calendarView";

        // Ajouter l'événement au calendrier
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_COLOR, Color.RED);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());

        // Ouvrir l'application de calendrier
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri calendarUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        intent.setData(calendarUri);
        startActivity(intent);
    }

}
