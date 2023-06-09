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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddEventActivity extends AppCompatActivity {

    ConstraintLayout mLayout;
    int mDefaultColor;
    Button mButton;
    Button addButton;

    private File eventFile;

    private ArrayList<String> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Button buttonSelectDate = findViewById(R.id.buttonSelectDate);
        TextView textViewDate = findViewById(R.id.textViewDate);

        eventFile = new File(getFilesDir(), "event.csv");
        events = new ArrayList<>();


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
        writeEventToCSV();

        // Fermez l'activité AddCourseActivity et retournez à l'activité précédente
        finish();
    }

    private void writeEventToCSV() {
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

}
