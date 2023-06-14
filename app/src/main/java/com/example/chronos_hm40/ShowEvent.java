package com.example.chronos_hm40;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ShowEvent extends AppCompatActivity {

    private ArrayList<String> events;
    private ArrayAdapter<String> eventsAdapter;
    private ListView lvEvents;
    private File eventFile;

    private boolean isDarkModeOn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recuper un argument au lancement de l'activité
        Intent intent = getIntent();
        boolean theme = intent.getBooleanExtra("theme", false);

        if (theme == true) {
            setContentView(R.layout.dark_data_event);
            isDarkModeOn = true;
        }else{
            setContentView(R.layout.data_event);
            isDarkModeOn = false;
        }

        lvEvents = findViewById(R.id.lvEvents);
        events = MainActivity.getEvents();
        eventFile = MainActivity.getFile();

        readEvents();
        String selectedDate = getIntent().getStringExtra("selectedDate");

        List<String> filteredEvents = new ArrayList<>();
        for (String event : events) {
            String[] parts = event.split("\n");
            if (selectedDate.equals(parts[0])) {
                filteredEvents.add(event);
            }
        }

        eventsAdapter = new ArrayAdapter<String>(this, R.layout.data_form_event, R.id.textViewTitle, filteredEvents) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String event = filteredEvents.get(position);
                String[] parts = event.split("\n");

                TextView textViewTitle = view.findViewById(R.id.textViewTitle);
                TextView textViewDescription = view.findViewById(R.id.textViewDescription);
                TextView textViewTime = view.findViewById(R.id.textViewTime);

                textViewTitle.setText(parts[1]);
                textViewDescription.setText(parts[2]);
                textViewTime.setText(parts[4]);

                int color = Integer.parseInt(parts[3]);
                textViewTitle.setTextColor(color);
                textViewDescription.setTextColor(color);
                textViewTime.setTextColor(color);

                return view;
            }
        };
        lvEvents.setAdapter(eventsAdapter);


        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer l'événement sélectionné
                String selectedEvent = filteredEvents.get(position);

                // Créer une nouvelle intention pour lancer l'activité EventDetailsActivity
                Intent intent = new Intent(ShowEvent.this, EditEventActivity.class);
                intent.putExtra("selectedEvent", selectedEvent); // Vous pouvez passer des données supplémentaires à l'activité si nécessaire
                intent.putExtra("theme", theme);
                startActivity(intent);
                finish();
            }
        });
    }


    private void readEvents() {
        events.clear(); // Vide la liste events avant de lire les événements

        try {
            FileReader fileReader = new FileReader(eventFile);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvData = csvReader.readAll();

            for (String[] row : csvData) {
                String event = row[0] + "\n" + row[1] + "\n" + row[2] + "\n" + row[3] + "\n" + row[4];
                events.add(event);
            }

            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }


    public  void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
        finish();
    }
}