package com.example.chronos_hm40;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "course_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "courses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SUBTITLE = "subtitle";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_FREQUENCY = "frequency";
    private static final String COLUMN_HOUR_BEGIN = "hour_begin";
    private static final String COLUMN_HOUR_END = "hour_end";
    private static final String COLUMN_DATE_BEGIN = "date_begin";
    private static final String COLUMN_DATE_END = "date_end";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_SUBTITLE + " TEXT,"
                + COLUMN_COLOR + " INTEGER,"
                + COLUMN_FREQUENCY + " TEXT,"
                + COLUMN_HOUR_BEGIN + " TEXT,"
                + COLUMN_HOUR_END + " TEXT,"
                + COLUMN_DATE_BEGIN + " TEXT,"
                + COLUMN_DATE_END + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, course.getTitle());
        values.put(COLUMN_SUBTITLE, course.getSubTitle());
        values.put(COLUMN_COLOR, course.getColor());
        values.put(COLUMN_FREQUENCY, course.getFrequency());
        values.put(COLUMN_HOUR_BEGIN, course.getHourBegin());
        values.put(COLUMN_HOUR_END, course.getHourEnd());
        values.put(COLUMN_DATE_BEGIN, course.getDateBegin());
        values.put(COLUMN_DATE_END, course.getDateEnd());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

}
