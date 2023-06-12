package com.example.chronos_hm40;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditEventActivity extends AppCompatActivity {

    Button deleteButton;
    Button modifyButton;
    EditText editTextTitle;
    EditText editTextDescription;
    Button buttonSelectDate;
    TextView textViewDate;
    Button buttonColor;
    int selectedColor;
    String selectedEvent;
    private ArrayList<String> events;
    private File eventFile;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        events = MainActivity.getEvents();
        eventFile = MainActivity.getFile();
        selectedEvent = getIntent().getStringExtra("selectedEvent");

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

        buttonColor = findViewById(R.id.buttonColor);
        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        modifyButton = findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyEvent();
            }
        });

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        textViewDate = findViewById(R.id.textViewDate);
        buttonColor = findViewById(R.id.buttonColor);

        // Remplir les champs avec les détails de l'événement
        String[] parts = selectedEvent.split("\n");
        editTextTitle.setText(parts[1]);
        editTextDescription.setText(parts[2]);
        textViewDate.setText(parts[0]);
        int color = Integer.parseInt(parts[3]);
        buttonColor.setBackgroundColor(color);
    }

    private void modifyEvent() {
        // Récupérer les valeurs modifiées
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String date = textViewDate.getText().toString();
        int color = selectedColor;


        // Construire la nouvelle ligne de l'événement
        String modifiedEvent = date + "\n" + title + "\n" + description + "\n" + color;

        // Modifier l'événement sélectionné dans la liste des événements
        events.set(events.indexOf(selectedEvent), modifiedEvent);

        // Appeler la méthode writeEvents() pour enregistrer les modifications dans le fichier CSV
        writeEvents();

        // Relancer l'activité précédente
        Intent intent = new Intent(this, ShowEvent.class);
        startActivity(intent);

        // Terminer l'activité actuelle et revenir à l'activité précédente
        finish();
    }

    private void deleteEvent() {
        // Supprimer l'événement sélectionné
        events.remove(selectedEvent);

        // Appeler la méthode writeEvents() pour enregistrer les modifications dans le fichier CSV
        writeEvents();

        // Relancer l'activité précédente
        Intent intent = new Intent(this, ShowEvent.class);
        startActivity(intent);

        // Terminer l'activité actuelle
        finish();
    }

    private void writeEvents() {
        try {
            FileWriter fileWriter = new FileWriter(eventFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            for (String event : events) {
                String[] row = event.split("\n");
                csvWriter.writeNext(row);
            }

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, selectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                selectedColor = color;
                buttonColor.setBackgroundColor(selectedColor);
            }
        });
        colorPicker.show();
    }

}
