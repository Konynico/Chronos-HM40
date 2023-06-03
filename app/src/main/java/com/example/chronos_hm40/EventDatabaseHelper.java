package com.example.chronos_hm40;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class EventDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "event_database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_DAY_OF_MONTH = "day_of_month";

    public EventDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_EVENTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_YEAR + " INTEGER, "
                + COLUMN_MONTH + " INTEGER, "
                + COLUMN_DAY_OF_MONTH + " INTEGER)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si vous avez besoin de mettre à jour la structure de la base de données dans les futures versions, implémentez ici la logique de migration des données.
        // Pour cet exemple, nous supprimons simplement et recréons la table.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, event.getTitle());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_YEAR, event.getYear());
        values.put(COLUMN_MONTH, event.getMonth());
        values.put(COLUMN_DAY_OF_MONTH, event.getDayOfMonth());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public List<Event> getEventsForSelectedDate(int year, int month, int dayOfMonth) {
        List<Event> eventList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_YEAR + " = ? AND " +
                COLUMN_MONTH + " = ? AND " +
                COLUMN_DAY_OF_MONTH + " = ?";

        String[] selectionArgs = {String.valueOf(year), String.valueOf(month), String.valueOf(dayOfMonth)};

        Cursor cursor = db.query(
                TABLE_EVENTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int eventId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") int eventYear = cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR));
                @SuppressLint("Range") int eventMonth = cursor.getInt(cursor.getColumnIndex(COLUMN_MONTH));
                @SuppressLint("Range") int eventDayOfMonth = cursor.getInt(cursor.getColumnIndex(COLUMN_DAY_OF_MONTH));

                Event event = new Event(eventId, title, description, eventYear, eventMonth, eventDayOfMonth);
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return eventList;
    }
}
