package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.nio.file.attribute.AclEntry;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<String> events;

    private static File eventFile;
    private boolean isDarkModeOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Intent intent = getIntent();
        boolean theme = intent.getBooleanExtra("theme", false);
        if (theme == true) {
            setContentView(R.layout.dark_activity_main);
            isDarkModeOn = true;
        }else{
            setContentView(R.layout.activity_main);
            isDarkModeOn = false;
        }
        //eventFile = new File(getFilesDir(), "event.csv");
        File directory = getExternalFilesDir(null);
        eventFile = new File(directory, "events.csv");
        events = new ArrayList<>();
    }

    public  void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public  void onTodoClick(View view){
        Intent intent = new Intent(this, TodoList.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
    }

    public void onEDTClick(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
    }

    public void onScheduleClick(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
    }

    public void onAddCourseClick(View view){
        Intent intent = new Intent(this, AddCourseActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
    }

    public void onDeleteCourseClick(View view){
        Intent intent = new Intent(this, DeleteCourseActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
    }

    public void onAddEventClick(View view)
    {
        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
    }

    public void onChangeMode(View view)
    {
        if(isDarkModeOn)
        {
            setContentView(R.layout.activity_main);
            isDarkModeOn = false;
        }
        else
        {
            setContentView(R.layout.dark_activity_main);
            isDarkModeOn = true;
        }
    }

    public static ArrayList<String> getEvents() {
        return events;
    }

    public static File getFile()
    {
        return eventFile;
    }
}