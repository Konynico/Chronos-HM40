package com.example.chronos_hm40;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEvenement extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenement);
    }

    public void addEventToCalendar(long startMillis, long endMillis, String title, String description, int color) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, title);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Location");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis);
        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.EVENT_COLOR, color);
        intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }
}
