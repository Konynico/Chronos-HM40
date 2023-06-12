package com.example.chronos_hm40;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditEventActivity extends AppCompatActivity {

    Button deleteButton;
    Button modifyButton;
    EditText editTextTitle;
    EditText editTextDescription;
    Button editButtonSelectDate;
    TextView editTextViewDate;
    Button editButtonColor;
    int selectedColor;
    String selectedEvent;
    private ArrayList<String> events;
    private File eventFile;

    private ArrayAdapter<String> eventsAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        events = MainActivity.getEvents();
        eventFile = MainActivity.getFile();
        selectedEvent = getIntent().getStringExtra("selectedEvent");

        editButtonSelectDate = findViewById(R.id.editButtonSelectDate);
        editTextViewDate = findViewById(R.id.editTextViewDate);

        eventsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                String event = events.get(position);
                String[] parts = event.split("\n");
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(parts[0] + "\n" + parts[1]); // Affichez uniquement la première partie (le texte de l'élément) et ignorez la couleur
                int color = Integer.parseInt(parts[2]);
                textView.setTextColor(color);
                return view;
            }
        };

        editButtonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer la date actuelle
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Créer le DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Mettre à jour l'affichage avec la date sélectionnée
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        editTextViewDate.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);

                // Afficher le DatePickerDialog
                datePickerDialog.show();
            }
        });

        selectedColor = ContextCompat.getColor(EditEventActivity.this, R.color.colorPrimary);
        editButtonColor = findViewById(R.id.editButtonColor);

        editButtonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);

        // Remplir les champs avec les détails de l'événement
        String[] parts = selectedEvent.split("\n");
        editTextTitle.setText(parts[1]);
        editTextDescription.setText(parts[2]);
        editTextViewDate.setText(parts[0]);
        int color = Integer.parseInt(parts[3]);
        editButtonColor.setBackgroundColor(color);

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

        modifyButton = findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyEvent();
            }
        });
    }

    private void modifyEvent() {
        EditText neweditTextTitle = findViewById(R.id.editTextTitle);
        EditText neweditTextDescription = findViewById(R.id.editTextDescription);
        TextView neweditTextViewDate = findViewById(R.id.editTextViewDate);

        String title = neweditTextTitle.getText().toString();
        String description = neweditTextDescription.getText().toString();
        int color = selectedColor;
        String date = neweditTextViewDate.getText().toString();

        // Créer un SpannableString avec la couleur sélectionnée
        String modifiedEvent = date + "\n" + title + "\n" + description + "\n" + color;

        // Modifier l'événement sélectionné dans la liste des événements
        events.set(events.indexOf(selectedEvent), modifiedEvent);
        eventsAdapter.notifyDataSetChanged();

        // Appeler la méthode writeEvents() pour enregistrer les modifications dans le fichier CSV
        writeEvents();

        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextViewDate.setText("");

        // Fermez l'activité AddCourseActivity et retournez à l'activité précédente
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
                editButtonColor.setBackgroundColor(selectedColor);
            }
        });
        colorPicker.show();
    }

    public  void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}