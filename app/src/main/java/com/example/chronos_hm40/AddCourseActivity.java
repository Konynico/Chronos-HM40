package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddCourseActivity extends AppCompatActivity {
    ConstraintLayout mLayout;
    int mDefaultColor;
    Button mButton;
    Button buttonSave;
    Spinner spinnerFrequency;
    Spinner spinnerDay;
    ArrayAdapter<CharSequence> frequencyAdapter;
    ArrayAdapter<CharSequence> dayAdapter;
    EditText editTextDateBegin;
    EditText editTextDateEnd;
    EditText editTextHourBegin;
    EditText editTextHourEnd;
    private Button validateButton;


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

        validateButton = findViewById(R.id.buttonAddCourse);
        validateButton.setEnabled(false); // Désactiver le bouton initialement

        mLayout = findViewById(R.id.layout);
        mDefaultColor = ContextCompat.getColor(AddCourseActivity.this, R.color.colorPrimary);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        buttonSave = findViewById(R.id.buttonAddCourse);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCourse();
            }
        });
        EditText editTextHourBegin = findViewById(R.id.editTextHourBegin);
        editTextHourBegin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!isValidTimeFormat(input)) {
                    editTextHourBegin.setError("Veuillez entrer une heure valide au format HH:MM");
                } else {
                    editTextHourBegin.setError(null);
                }
            }
        });
        EditText editTextHourEnd = findViewById(R.id.editTextHourEnd);
        editTextHourEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!isValidTimeFormat(input)) {
                    editTextHourEnd.setError("Veuillez entrer une heure valide au format HH:MM");
                } else {
                    editTextHourEnd.setError(null);
                }
            }
        });

        editTextDateBegin = findViewById(R.id.editTextDateBegin);
        editTextDateBegin.addTextChangedListener(new TextWatcher() {
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
                // Pas besoin de cette méthode pour notre cas
            }
        });
        editTextDateEnd = findViewById(R.id.editTextDateBegin);
        editTextDateEnd.addTextChangedListener(new TextWatcher() {
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
                // Pas besoin de cette méthode pour notre cas
            }
        });

        editTextHourBegin.addTextChangedListener(new TextWatcher() {
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
        editTextHourEnd.addTextChangedListener(new TextWatcher() {
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
    }
    private boolean isValidTimeFormat(String input) {
        // Vérifiez si la valeur correspond au format HH:MM
        return input.matches("^([01]\\d|2[0-3]):([0-5]\\d)$");
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

    private void saveCourse() {
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextSubtitle = findViewById(R.id.editTextSubtitle);
        EditText editTextHourBegin = findViewById(R.id.editTextHourBegin);
        EditText editTextHourEnd = findViewById(R.id.editTextHourEnd);
        EditText editTextDateBegin = findViewById(R.id.editTextDateBegin);
        EditText editTextDateEnd = findViewById(R.id.editTextDateEnd);

        String title = editTextTitle.getText().toString();
        String subtitle = editTextSubtitle.getText().toString();
        int color = mDefaultColor;
        String frequency = spinnerFrequency.getSelectedItem().toString();
        String day = spinnerDay.getSelectedItem().toString();
        String hourBegin = editTextHourBegin.getText().toString();
        String hourEnd = editTextHourEnd.getText().toString();
        String dateBegin = editTextDateBegin.getText().toString();
        String dateEnd = editTextDateEnd.getText().toString();

        // Récupérez la date de départ sélectionnée par l'utilisateur
        int dayOfWeek;

        switch (day) {
            case "Lundi":
                dayOfWeek = Calendar.MONDAY;
                break;
            case "Mardi":
                dayOfWeek = Calendar.TUESDAY;
                break;
            case "Mercredi":
                dayOfWeek = Calendar.WEDNESDAY;
                break;
            case "Jeudi":
                dayOfWeek = Calendar.THURSDAY;
                break;
            case "Vendredi":
                dayOfWeek = Calendar.FRIDAY;
                break;
            case "Samedi":
                dayOfWeek = Calendar.SATURDAY;
                break;
            case "Dimanche":
                dayOfWeek = Calendar.SUNDAY;
                break;
            default:
                // Cas où le jour choisi n'est pas valide, vous pouvez gérer cela en conséquence
                // Par exemple, afficher un message d'erreur ou prendre une action par défaut.
                return;
        }
        // Parsez la date de départ en objet Calendar
        Calendar startDate = parseCalendar(dateBegin);

        // Vérifiez si la date de départ est déjà un mercredi
        if (startDate.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            // Recherchez le mercredi suivant à partir de la date de départ
            while (startDate.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
                startDate.add(Calendar.DAY_OF_WEEK, 1);
            }
        }

        // Formattez la nouvelle date de départ pour l'affichage
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
        String adjustedStartDate = sdf.format(startDate.getTime());

        // Affichez la nouvelle date de départ
        dateBegin = removeQuotes(adjustedStartDate);

        // Créez une instance de la classe Course avec les données du formulaire
        Course course = new Course(title, subtitle, color, day, frequency, hourBegin, hourEnd, dateBegin, dateEnd);

        writeCourseToCSV(course);

        finish();
    }


    private void writeCourseToCSV(Course course) {
        String[] data = {course.getTitle(), course.getSubTitle(), String.valueOf(course.getColor()), course.getDay(), course.getFrequency(), course.getHourBegin(), course.getHourEnd(), course.getDateBegin(), course.getDateEnd() }; // Remplacez ... par les autres attributs de la classe Course
        File directory = getExternalFilesDir(null);
        // Créez le fichier CSV dans le répertoire
        File file = new File(directory, "data_schedule_test.csv");

        try {
            FileWriter fileWriter = new FileWriter(file, true); // Mode append (ajout)
            CSVWriter writer = new CSVWriter(fileWriter); // Remplacez "chemin_vers_le_fichier.csv" par le chemin réel vers votre fichier CSV

            writer.writeNext(data);

            writer.close();

            Toast.makeText(AddCourseActivity.this, "Plage horaire ajoutée avec succès", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(AddCourseActivity.this, "Erreur lors de l'ajout des données au fichier CSV", Toast.LENGTH_SHORT).show();
        }
    }
    public void showDatePickerDialog1(View view) {
        // Obtenez la date actuelle
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        EditText textViewSelectedDate = findViewById(R.id.editTextDateBegin);

        // Créez une instance de DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                // La date sélectionnée par l'utilisateur
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);

                // Affichez la date sélectionnée dans le TextView
                textViewSelectedDate.setText(selectedDate);
            }
        }, year, month, dayOfMonth);

        // Affichez le DatePickerDialog
        datePickerDialog.show();
    }

    public void showDatePickerDialog2(View view) {
        // Récupérer la date de début précédente
        EditText textViewOrigin = findViewById(R.id.editTextDateBegin);
        EditText textViewSelectedDate = findViewById(R.id.editTextDateEnd);

        Calendar previousStartDate = parseCalendar(textViewOrigin.getText().toString());

        // Obtenez la date actuelle
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Créez une instance de DatePickerDialog avec la date minimale définie
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                // La date sélectionnée par l'utilisateur
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);

                // Affichez la date sélectionnée dans le TextView
                textViewSelectedDate.setText(selectedDate);
            }
        }, year, month, dayOfMonth);

        // Définissez la date minimale sur le DatePickerDialog
        if (previousStartDate != null) {
            datePickerDialog.getDatePicker().setMinDate(previousStartDate.getTimeInMillis());
        }

        // Affichez le DatePickerDialog
        datePickerDialog.show();
    }
    private Calendar parseCalendar(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkFieldsNotEmpty() {
        validateButton = findViewById(R.id.buttonAddCourse);
        EditText editTextField1 = findViewById(R.id.editTextDateBegin);
        EditText editTextField2 = findViewById(R.id.editTextDateEnd);
        EditText editTextField3 = findViewById(R.id.editTextHourBegin);
        EditText editTextField4 = findViewById(R.id.editTextHourEnd);

        boolean fieldsNotEmpty = !TextUtils.isEmpty(editTextField4.getText()) && !TextUtils.isEmpty(editTextField3.getText()) && !TextUtils.isEmpty(editTextField1.getText()) && !TextUtils.isEmpty(editTextField2.getText());
        validateButton.setEnabled(fieldsNotEmpty);
    }
    private String removeQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }



}