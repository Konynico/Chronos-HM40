package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

    int mDefaultColor;
    Button mButton;
    Button addButton;

    private File eventFile;

    private ArrayAdapter<String> eventsAdapter;

    private ArrayList<String> events;

    private boolean isDarkModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recuper un argument au lancement de l'activité
        Intent intent = getIntent();
        boolean theme = intent.getBooleanExtra("theme", false);

        if (theme == true) {
            setContentView(R.layout.dark_activity_add_event);
            isDarkModeOn = true;
        }else{
            setContentView(R.layout.activity_add_event);
            isDarkModeOn = false;
        }

        Button buttonSelectDate = findViewById(R.id.buttonSelectDate);
        Button buttonSelectTime = findViewById(R.id.buttonSelectTime);
        TextView textViewDate = findViewById(R.id.textViewDate);
        TextView textViewTime = findViewById(R.id.textViewTime);

        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDescription = findViewById(R.id.editTextDescription);

        addButton = findViewById(R.id.addButton);
        addButton.setEnabled(false);

        eventFile = MainActivity.getFile();
        events = MainActivity.getEvents();

        eventsAdapter = new ArrayAdapter<String>(this, R.layout.data_form_event, R.id.textViewTitle) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String event = events.get(position);
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

        buttonSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer l'heure actuelle
                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Créer le TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHourOfDay, int selectedMinute) {
                        // Mettre à jour l'affichage avec l'heure sélectionnée
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHourOfDay, selectedMinute);
                        textViewTime.setText(formattedTime);
                    }
                }, hourOfDay, minute, true);

                // Afficher le TimePickerDialog
                timePickerDialog.show();
            }
        });


        mDefaultColor = ContextCompat.getColor(AddEventActivity.this, R.color.colorPrimary);
        mButton = findViewById(R.id.buttonColor);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        textViewDate.addTextChangedListener(new TextWatcher() {
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

        textViewTime.addTextChangedListener(new TextWatcher() {
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
        TextView textViewTime = findViewById(R.id.textViewTime);

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int color = mDefaultColor;
        String date = textViewDate.getText().toString();
        String time = textViewTime.getText().toString();


        // Créer un SpannableString avec la couleur sélectionnée
        String newEvent = date + "\n" + title + "\n" + description + "\n" + color + "\n" + time;

        events.add(0, newEvent);
        eventsAdapter.notifyDataSetChanged();

        writeEvents();

        editTextTitle.setText("");
        editTextDescription.setText("");
        textViewDate.setText("");

        // Fermez l'activité AddCourseActivity et retournez à l'activité précédente
        finish();
    }

    private void writeEvents() {
        try {
            //FileWriter fileWriter = new FileWriter(eventFile);
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

    private void checkFieldsNotEmpty() {
        addButton = findViewById(R.id.addButton);
        EditText editTextField1 = findViewById(R.id.editTextTitle);
        EditText editTextField2 = findViewById(R.id.editTextDescription);
        TextView editTextField3 = findViewById(R.id.textViewDate);
        TextView editTextField4 = findViewById(R.id.textViewTime);

        boolean fieldsNotEmpty = !TextUtils.isEmpty(editTextField4.getText()) && !TextUtils.isEmpty(editTextField3.getText()) && !TextUtils.isEmpty(editTextField1.getText()) && !TextUtils.isEmpty(editTextField2.getText());
        addButton.setEnabled(fieldsNotEmpty);
    }

    protected void onPause() {
        super.onPause();
        writeEvents();
    }

    public  void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
        finish();
    }
}
