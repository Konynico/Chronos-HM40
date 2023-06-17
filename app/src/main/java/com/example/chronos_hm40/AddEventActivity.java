package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.TimePicker;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

        // Récupérer les références des vues
        Button buttonSelectDate = findViewById(R.id.buttonSelectDate);
        Button buttonSelectTime = findViewById(R.id.buttonSelectTime);
        TextView textViewDate = findViewById(R.id.textViewDate);
        TextView textViewTime = findViewById(R.id.textViewTime);
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        addButton = findViewById(R.id.addButton);
        addButton.setEnabled(false);

        // Initialiser les variables
        events = new ArrayList<>();

        // Lire les événements à partir du fichier CSV
        readEvents();

        // Créer l'adaptateur pour la liste d'événements
        eventsAdapter = new ArrayAdapter<String>(this, R.layout.data_form_event, R.id.textViewTitle) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Personnaliser l'apparence de chaque élément de la liste
                View view = super.getView(position, convertView, parent);

                // Récupérer les éléments de l'événement à la position spécifiée
                String event = events.get(position);
                String[] parts = event.split("\n");

                // Récupérer les références des vues dans l'élément de la liste
                TextView textViewTitle = view.findViewById(R.id.textViewTitle);
                TextView textViewDescription = view.findViewById(R.id.textViewDescription);
                TextView textViewTime = view.findViewById(R.id.textViewTime);

                // Mettre à jour les valeurs des vues avec les données de l'événement
                textViewTitle.setText(parts[1]);
                textViewDescription.setText(parts[2]);
                textViewTime.setText(parts[4]);

                // Définir la couleur du texte en fonction de l'événement
                int color = Integer.parseInt(parts[3]);
                textViewTitle.setTextColor(color);
                textViewDescription.setTextColor(color);
                textViewTime.setTextColor(color);

                return view;
            }
        };

        // Ajouter des écouteurs d'événements aux boutons de sélection de la date et de l'heure
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

        // Gérer la sélection de la couleur
        mDefaultColor = ContextCompat.getColor(AddEventActivity.this, R.color.colorPrimary);
        mButton = findViewById(R.id.buttonColor);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        // Vérifier si les champs ne sont pas vides pour activer le bouton d'ajout
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

        // Ajouter un écouteur d'événement pour le bouton d'ajout
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });
    }

    // Méthode pour ouvrir le sélecteur de couleur
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

    // Méthode pour sauvegarder un événement
    private void saveEvent() {
        // Récupérer les valeurs des champs de texte et de l'heure sélectionnée
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        TextView textViewDate = findViewById(R.id.textViewDate);
        TextView textViewTime = findViewById(R.id.textViewTime);

        if(editTextDescription.getText().toString().equals(""))
        {
            editTextDescription.setText("Description");
        }

        if(textViewTime.getText().toString().equals(""))
        {
            textViewTime.setText("HH:MM");
        }

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int color = mDefaultColor;
        String date = textViewDate.getText().toString();
        String time = textViewTime.getText().toString();

        // Créer une chaîne représentant le nouvel événement
        String newEvent = date + "\n" + title + "\n" + description + "\n" + color + "\n" + time;

        // Ajouter l'événement à la liste et mettre à jour l'adaptateur
        events.add(0, newEvent);
        eventsAdapter.notifyDataSetChanged();

        // Écrire les événements dans le fichier CSV
        writeEvents();

        // Réinitialiser les champs de texte
        editTextTitle.setText("");
        editTextDescription.setText("");
        textViewDate.setText("");

        // Fermez l'activité AddCourseActivity et retournez à l'activité précédente
        finish();
    }

    // Méthode pour écrire les événements dans un fichier CSV
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

    // Méthode pour lire les événements à partir d'un fichier CSV
    private void readEvents() {
        events.clear(); // Vide la liste events avant de lire les événements
        File directory = getExternalFilesDir(null);
        eventFile = new File(directory, "events.csv");
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

    // Méthode pour vérifier si les champs ne sont pas vides et activer le bouton d'ajout
    private void checkFieldsNotEmpty() {
        addButton = findViewById(R.id.addButton);
        EditText editTextField1 = findViewById(R.id.editTextTitle);
        TextView editTextField3 = findViewById(R.id.textViewDate);

        boolean fieldsNotEmpty = !TextUtils.isEmpty(editTextField3.getText()) && !TextUtils.isEmpty(editTextField1.getText());
        addButton.setEnabled(fieldsNotEmpty);
    }

    // Méthode appelée lorsque le bouton de retour est cliqué (utilisé pour revenir à l'activité principale)
    public void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
        finish();
    }
}