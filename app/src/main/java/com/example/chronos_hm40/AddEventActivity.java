package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddEventActivity extends AppCompatActivity {

    ConstraintLayout mLayout;
    int mDefaultColor;
    Button mButton;
    Button addButton;
    Spinner spinnerFrequency;
    Spinner spinnerDay;
    ArrayAdapter<CharSequence> frequencyAdapter;
    ArrayAdapter<CharSequence> dayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        spinnerDay = findViewById(R.id.spinnerDay);
        frequencyAdapter = ArrayAdapter.createFromResource(this, R.array.frequency_options, android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(frequencyAdapter);

        dayAdapter = ArrayAdapter.createFromResource(this, R.array.day_options, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter);

        mLayout = findViewById(R.id.layout);
        mDefaultColor = ContextCompat.getColor(AddEventActivity.this, R.color.colorPrimary);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        addButton = findViewById(R.id.buttonAddCourse);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                mButton.setBackgroundColor(mDefaultColor);
            }
        });
        colorPicker.show();
    }

    private void saveEvent() {
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextDate = findViewById(R.id.editTextDate);

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int color = mDefaultColor;
        String date = editTextDate.getText().toString();

        // Créez une instance de la classe Course avec les données du formulaire
        Event event = new Event(title, description, color, date);
        writeEventToCSV(event);

        // Fermez l'activité AddCourseActivity et retournez à l'activité précédente
        finish();
    }

    private void writeEventToCSV(Event event) {
        String[] data = {event.getTitle(), event.getDescription(), String.valueOf(event.getColor()), event.getDate() }; // Remplacez ... par les autres attributs de la classe Course
        File directory = getExternalFilesDir(null);
        // Créez le fichier CSV dans le répertoire
        File file = new File(directory, "data_event.csv");

        try {
            FileWriter fileWriter = new FileWriter(file, true); // Mode append (ajout)
            CSVWriter writer = new CSVWriter(fileWriter); // Remplacez "chemin_vers_le_fichier.csv" par le chemin réel vers votre fichier CSV

            writer.writeNext(data);

            writer.close();

            Toast.makeText(AddEventActivity.this, "Données ajoutées au fichier CSV avec succès", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(AddEventActivity.this, "Erreur lors de l'ajout des données au fichier CSV", Toast.LENGTH_SHORT).show();
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                // Traitez chaque ligne du fichier CSV ici
                Toast.makeText(AddEventActivity.this, line , Toast.LENGTH_SHORT).show();

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
