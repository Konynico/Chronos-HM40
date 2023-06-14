package com.example.chronos_hm40;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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

    Button editButtonSelectTime;
    TextView editTextViewTime;
    Button editButtonColor;
    int selectedColor;
    String selectedEvent;
    private ArrayList<String> events;
    private File eventFile;

    private ArrayAdapter<String> eventsAdapter;

    private boolean isDarkModeOn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //recuper un argument au lancement de l'activité
        Intent intent = getIntent();
        boolean theme = intent.getBooleanExtra("theme", false);

        if (theme == true) {
            setContentView(R.layout.dark_activity_edit_event);
            isDarkModeOn = true;
        }else{
            setContentView(R.layout.activity_edit_event);
            isDarkModeOn = false;
        }

        events = MainActivity.getEvents();
        eventFile = MainActivity.getFile();
        selectedEvent = getIntent().getStringExtra("selectedEvent");

        eventsAdapter = new ArrayAdapter<String>(this, R.layout.data_form_event, R.id.textViewTitle) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String event = events.get(position);
                String[] parts = event.split("\n");

                TextView textViewTitle = view.findViewById(R.id.textViewTitle);
                TextView textViewDescription = view.findViewById(R.id.textViewDescription);
                TextView textViewTime = view.findViewById(R.id.textViewTime);

                if(textViewDescription.getText().toString().equals("Description"))
                {
                    textViewDescription.setText("");
                }

                if(textViewTime.getText().toString().equals("HH:MM"))
                {
                    textViewTime.setText("");
                }

                textViewTitle.setText(parts[1]);

                int color = Integer.parseInt(parts[3]);
                textViewTitle.setTextColor(color);
                textViewDescription.setTextColor(color);
                textViewTime.setTextColor(color);

                return view;
            }
        };

        editButtonSelectDate = findViewById(R.id.editButtonSelectDate);
        editTextViewDate = findViewById(R.id.editTextViewDate);

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

        editButtonSelectTime = findViewById(R.id.editButtonSelectTime);
        editTextViewTime = findViewById(R.id.editTextViewTime);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);


        modifyButton = findViewById(R.id.modifyButton);

        editButtonSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer l'heure actuelle
                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Créer le TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHourOfDay, int selectedMinute) {
                        // Mettre à jour l'affichage avec l'heure sélectionnée
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHourOfDay, selectedMinute);
                        editTextViewTime.setText(formattedTime);
                    }
                }, hourOfDay, minute, true);

                // Afficher le TimePickerDialog
                timePickerDialog.show();
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

        // Remplir les champs avec les détails de l'événement
        String[] parts = selectedEvent.split("\n");
        if(editTextDescription.getText().toString().equals("Description"))
        {
            editTextDescription.setText("");
        }
        else {
            editTextDescription.setText(parts[2]);
        }

        if(editTextViewTime.getText().toString().equals("HH:MM"))
        {
            editTextViewTime.setText("");
        }
        else{
            editTextViewTime.setText(parts[4]);
        }

        editTextTitle.setText(parts[1]);
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

        editTextViewDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Pas besoin de cette méthode pour notre cas
                checkFieldsNotEmpty();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Appeler la méthode de vérification
                checkFieldsNotEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldsNotEmpty();
            }
        });

        editTextViewTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Pas besoin de cette méthode pour notre cas
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Appeler la méthode de vérification
                checkFieldsNotEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldsNotEmpty();
            }
        });

        editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Pas besoin de cette méthode pour notre cas
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Appeler la méthode de vérification
                checkFieldsNotEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldsNotEmpty();
            }
        });

        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Pas besoin de cette méthode pour notre cas
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Appeler la méthode de vérification
                checkFieldsNotEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldsNotEmpty();
            }
        });
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
        TextView neweditTextViewTime = findViewById(R.id.editTextViewTime);

        if(neweditTextDescription.getText().toString().equals(""))
        {
            neweditTextDescription.setText("Description");
        }

        if(neweditTextViewTime.getText().toString().equals(""))
        {
            neweditTextViewTime.setText("HH:MM");
        }

        String title = neweditTextTitle.getText().toString();
        String description = neweditTextDescription.getText().toString();
        int color = selectedColor;
        String date = neweditTextViewDate.getText().toString();
        String time = neweditTextViewTime.getText().toString();

        // Créer un SpannableString avec la couleur sélectionnée
        String modifiedEvent = date + "\n" + title + "\n" + description + "\n" + color + "\n" + time;

        // Modifier l'événement sélectionné dans la liste des événements
        events.set(events.indexOf(selectedEvent), modifiedEvent);
        eventsAdapter.notifyDataSetChanged();

        // Appeler la méthode writeEvents() pour enregistrer les modifications dans le fichier CSV
        writeEvents();

        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextViewDate.setText("");
        editTextViewTime.setText("");

        // Fermez l'activité EditEventActivity et retournez à l'activité précédente
        finish();
    }

    private void deleteEvent() {
        // Supprimer l'événement sélectionné
        events.remove(selectedEvent);

        // Appeler la méthode writeEvents() pour enregistrer les modifications dans le fichier CSV
        writeEvents();

        // Terminer l'activité actuelle
        finish();
    }

    private void writeEvents() {
        File directory = getExternalFilesDir(null);
        eventFile = new File(directory, "events.csv");
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

    private void checkFieldsNotEmpty() {
        modifyButton = findViewById(R.id.modifyButton);
        EditText editTextField1 = findViewById(R.id.editTextTitle);
        TextView editTextField3 = findViewById(R.id.editTextViewDate);

        boolean fieldsNotEmpty = !TextUtils.isEmpty(editTextField3.getText()) && !TextUtils.isEmpty(editTextField1.getText());
        modifyButton.setEnabled(fieldsNotEmpty);
    }

    public  void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
        finish();
    }
}