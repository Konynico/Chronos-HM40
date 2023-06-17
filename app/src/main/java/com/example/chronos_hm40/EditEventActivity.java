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

        // Récupérer un argument au lancement de l'activité
        Intent intent = getIntent();
        boolean theme = intent.getBooleanExtra("theme", false);

        if (theme == true) {
            // Utiliser le layout sombre
            setContentView(R.layout.dark_activity_edit_event);
            isDarkModeOn = true;
        } else {
            // Utiliser le layout par défaut
            setContentView(R.layout.activity_edit_event);
            isDarkModeOn = false;
        }

        // Initialiser les variables
        events = new ArrayList<>();
        readEvents();
        selectedEvent = getIntent().getStringExtra("selectedEvent");

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

                // Vérifier et réinitialiser les champs de texte si nécessaire
                if (textViewDescription.getText().toString().equals("Description")) {
                    textViewDescription.setText("");
                }

                if (textViewTime.getText().toString().equals("HH:MM")) {
                    textViewTime.setText("");
                }

                // Mettre à jour les valeurs des vues avec les données de l'événement
                textViewTitle.setText(parts[1]);
                int color = Integer.parseInt(parts[3]);
                textViewTitle.setTextColor(color);
                textViewDescription.setTextColor(color);
                textViewTime.setTextColor(color);

                return view;
            }
        };

        // Récupérer les références des vues
        editButtonSelectDate = findViewById(R.id.editButtonSelectDate);
        editTextViewDate = findViewById(R.id.editTextViewDate);
        editButtonSelectTime = findViewById(R.id.editButtonSelectTime);
        editTextViewTime = findViewById(R.id.editTextViewTime);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        modifyButton = findViewById(R.id.modifyButton);

        // Ajouter des écouteurs d'événements aux boutons de sélection de la date et de l'heure
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
                // Appeler la méthode openColorPicker() lorsque le bouton est cliqué
                openColorPicker();
            }
        });

        // Récupérer et afficher les détails de l'événement sélectionné
        String[] parts = selectedEvent.split("\n");
        editTextTitle.setText(parts[1]);
        editTextDescription.setText(parts[2]);
        editTextViewDate.setText(parts[0]);
        editTextViewTime.setText(parts[4]);
        int color = Integer.parseInt(parts[3]);
        editButtonColor.setBackgroundColor(color);

        // Vérifier et réinitialiser les champs de texte si nécessaire
        if(editTextDescription.getText().toString().equals("Description"))
        {
            editTextDescription.setText("");
        }

        if(editTextViewTime.getText().toString().equals("HH:MM"))
        {
            editTextViewTime.setText("");
        }

        // Ajouter un écouteur d'événement au bouton de suppression
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Supprimer l'événement
                deleteEvent();
            }
        });

        // Ajouter des écouteurs de modification pour les champs de texte
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

        // Ajouter un écouteur d'événement au bouton de modification
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Modifier l'événement
                modifyEvent();
            }
        });
    }

    private void modifyEvent() {
        // Récupérer les références des vues EditText et TextView
        EditText neweditTextTitle = findViewById(R.id.editTextTitle);
        EditText neweditTextDescription = findViewById(R.id.editTextDescription);
        TextView neweditTextViewDate = findViewById(R.id.editTextViewDate);
        TextView neweditTextViewTime = findViewById(R.id.editTextViewTime);

        // Vérifier si les champs de description et d'heure sont vides, puis les remplacer par des valeurs par défaut si nécessaire
        if (neweditTextDescription.getText().toString().equals("")) {
            neweditTextDescription.setText("Description");
        }

        if (neweditTextViewTime.getText().toString().equals("")) {
            neweditTextViewTime.setText("HH:MM");
        }

        // Récupérer les valeurs des champs de saisie
        String title = neweditTextTitle.getText().toString();
        String description = neweditTextDescription.getText().toString();
        int color = selectedColor;
        String date = neweditTextViewDate.getText().toString();
        String time = neweditTextViewTime.getText().toString();

        // Créer une chaîne de caractères représentant l'événement modifié
        String modifiedEvent = date + "\n" + title + "\n" + description + "\n" + color + "\n" + time;

        // Modifier l'événement sélectionné dans la liste des événements
        events.set(events.indexOf(selectedEvent), modifiedEvent);
        eventsAdapter.notifyDataSetChanged();

        // Appeler la méthode writeEvents() pour enregistrer les modifications dans le fichier CSV
        writeEvents();

        // Réinitialiser les champs de saisie
        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextViewDate.setText("");
        editTextViewTime.setText("");

        // Fermer l'activité EditEventActivity et retourner à l'activité précédente
        finish();
    }

    private void deleteEvent() {
        // Supprimer l'événement sélectionné de la liste des événements
        events.remove(selectedEvent);

        // Appeler la méthode writeEvents() pour enregistrer les modifications dans le fichier CSV
        writeEvents();

        // Terminer l'activité actuelle
        finish();
    }

    private void writeEvents() {
        // Obtenir le répertoire de stockage des fichiers
        File directory = getExternalFilesDir(null);
        eventFile = new File(directory, "events.csv");

        try {
            FileWriter fileWriter = new FileWriter(eventFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            // Parcourir la liste des événements et écrire chaque événement dans le fichier CSV
            for (String event : events) {
                String[] row = event.split("\n");
                csvWriter.writeNext(row);
            }

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readEvents() {
        // Vider la liste des événements avant de lire les événements à partir du fichier CSV
        events.clear();

        // Obtenir le répertoire de stockage des fichiers
        File directory = getExternalFilesDir(null);
        eventFile = new File(directory, "events.csv");

        try {
            FileReader fileReader = new FileReader(eventFile);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvData = csvReader.readAll();

            // Parcourir les données CSV et ajouter chaque événement à la liste des événements
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

    public void openColorPicker() {
        // Créer une boîte de dialogue de sélection de couleur
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, selectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Actions à effectuer lors de l'annulation de la sélection de couleur
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // Mettre à jour la couleur sélectionnée et définir la couleur de fond du bouton correspondant
                selectedColor = color;
                editButtonColor.setBackgroundColor(selectedColor);
            }
        });

        // Afficher la boîte de dialogue de sélection de couleur
        colorPicker.show();
    }

    private void checkFieldsNotEmpty() {
        // Obtenir la référence du bouton de modification
        modifyButton = findViewById(R.id.modifyButton);

        // Obtenir les références des champs de saisie
        EditText editTextField1 = findViewById(R.id.editTextTitle);
        TextView editTextField3 = findViewById(R.id.editTextViewDate);

        // Vérifier si les champs de saisie sont vides
        boolean fieldsNotEmpty = !TextUtils.isEmpty(editTextField3.getText()) && !TextUtils.isEmpty(editTextField1.getText());

        // Activer ou désactiver le bouton de modification en fonction de l'état des champs de saisie
        modifyButton.setEnabled(fieldsNotEmpty);
    }

    public void onChronoClick(View view) {
        // Créer une intention pour naviguer vers l'activité MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", isDarkModeOn);

        // Démarrer l'activité MainActivity et fermer l'activité actuelle
        startActivity(intent);
        finish();
    }
}