package com.example.chronos_hm40;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chronos_hm40.R;

import java.util.Calendar;
import java.util.TimeZone;

public class CreateEvenement extends AppCompatActivity {

    private long calendarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenement);

        // Retrieve the calendar ID from the intent extras
        calendarId = getIntent().getLongExtra("calendarId", -1);
    }

    public void onSaveButtonClick(View view) {
        // Get the selected date from the intent extras
        int year = getIntent().getIntExtra("year", 0);
        int month = getIntent().getIntExtra("month", 0);
        int dayOfMonth = getIntent().getIntExtra("dayOfMonth", 0);

        // Save and display the event for the selected date
        saveEventToCalendar(year, month, dayOfMonth);
    }

    private void saveEventToCalendar(int year, int month, int dayOfMonth) {
        // Get the content resolver
        ContentResolver contentResolver = getContentResolver();

        // Define the event values
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, "My Event");
        values.put(CalendarContract.Events.DESCRIPTION, "Event Description");
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId); // Use the calendar ID from intent
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        // Set the start and end time of the event
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        long startTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        long endTime = calendar.getTimeInMillis();
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);

        // Insert the event
        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);

        // Check if the event was saved successfully
        if (uri != null) {
            Toast.makeText(this, "Event saved to calendar", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save event to calendar", Toast.LENGTH_SHORT).show();
        }
    }
}
