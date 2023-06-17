package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private boolean isDarkModeOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Définit le mode nuit par défaut sur "MODE_NIGHT_NO" (mode clair)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Récupère l'intention de l'activité
        Intent intent = getIntent();

        // Récupère la valeur booléenne "theme" à partir de l'intention, par défaut false
        boolean theme = intent.getBooleanExtra("theme", false);

        // Vérifie si "theme" est true
        if (theme == true) {
            // Si true, utilise le layout "dark_activity_main" pour le contenu de l'activité
            setContentView(R.layout.dark_activity_main);
            isDarkModeOn = true;
        } else {
            // Sinon, utilise le layout "activity_main" pour le contenu de l'activité
            setContentView(R.layout.activity_main);
            isDarkModeOn = false;
        }
    }

    public void onChronoClick(View view) {
        // Crée une nouvelle intention (Intent) pour l'activité MainActivity
        Intent intent = new Intent(this, MainActivity.class);

        // Démarre l'activité MainActivity
        startActivity(intent);
    }

    public void onTodoClick(View view) {
        // Crée une nouvelle intention (Intent) pour l'activité TodoList
        Intent intent = new Intent(this, TodoList.class);

        // Ajoute la valeur du mode (isDarkModeOn) à l'intention avec la clé "theme"
        intent.putExtra("theme", isDarkModeOn);

        // Démarre l'activité TodoList
        startActivity(intent);
    }

    public void onEDTClick(View view) {
        // Crée une nouvelle intention (Intent) pour l'activité CalendarActivity
        Intent intent = new Intent(this, CalendarActivity.class);

        // Ajoute la valeur du mode (isDarkModeOn) à l'intention avec la clé "theme"
        intent.putExtra("theme", isDarkModeOn);

        // Démarre l'activité CalendarActivity
        startActivity(intent);
    }

    public void onScheduleClick(View view) {
        // Crée une nouvelle intention (Intent) pour l'activité ScheduleActivity
        Intent intent = new Intent(this, ScheduleActivity.class);

        // Ajoute la valeur du mode (isDarkModeOn) à l'intention avec la clé "theme"
        intent.putExtra("theme", isDarkModeOn);

        // Démarre l'activité ScheduleActivity
        startActivity(intent);
    }

    public void onAddCourseClick(View view) {
        // Crée une nouvelle intention (Intent) pour l'activité AddCourseActivity
        Intent intent = new Intent(this, AddCourseActivity.class);

        // Ajoute la valeur du mode (isDarkModeOn) à l'intention avec la clé "theme"
        intent.putExtra("theme", isDarkModeOn);

        // Démarre l'activité AddCourseActivity
        startActivity(intent);
    }

    public void onDeleteCourseClick(View view) {
        // Crée une nouvelle intention (Intent) pour l'activité DeleteCourseActivity
        Intent intent = new Intent(this, DeleteCourseActivity.class);

        // Ajoute la valeur du mode (isDarkModeOn) à l'intention avec la clé "theme"
        intent.putExtra("theme", isDarkModeOn);

        // Démarre l'activité DeleteCourseActivity
        startActivity(intent);
    }

    public void onAddEventClick(View view) {
        // Crée une nouvelle intention (Intent) pour l'activité AddEventActivity
        Intent intent = new Intent(this, AddEventActivity.class);

        // Ajoute la valeur du mode (isDarkModeOn) à l'intention avec la clé "theme"
        intent.putExtra("theme", isDarkModeOn);

        // Démarre l'activité AddEventActivity
        startActivity(intent);
    }

    public void onChangeMode(View view) {
        // Vérifie l'état du mode sombre (isDarkModeOn)
        if (isDarkModeOn) {
            // Si le mode sombre est activé, utilise le layout "activity_main" pour le contenu de l'activité
            setContentView(R.layout.activity_main);
            isDarkModeOn = false;
        } else {
            // Sinon, utilise le layout "dark_activity_main" pour le contenu de l'activité
            setContentView(R.layout.dark_activity_main);
            isDarkModeOn = true;
        }
    }
}