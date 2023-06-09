package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddEventActivity extends AppCompatActivity {

    ConstraintLayout mLayout;
    int mDefaultColor;
    Button mButton;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Button buttonSelectDate = findViewById(R.id.buttonSelectDate);
        TextView textViewDate = findViewById(R.id.textViewDate);

        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer la date actuelle
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Créer le DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Mettre à jour l'affichage avec la date sélectionnée
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        textViewDate.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);

                // Afficher le DatePickerDialog
                datePickerDialog.show();
            }
        });

        mLayout = findViewById(R.id.layout);
        mDefaultColor = ContextCompat.getColor(AddEventActivity.this, R.color.colorPrimary);
        mButton = findViewById(R.id.buttonColor);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        addButton = findViewById(R.id.addButton);
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
        TextView textViewDate = findViewById(R.id.textViewDate);

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int color = mDefaultColor;
        String date = textViewDate.getText().toString();

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
