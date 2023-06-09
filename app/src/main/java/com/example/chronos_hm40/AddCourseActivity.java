package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.content.Context;
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
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddCourseActivity extends AppCompatActivity {
    private AppDatabase appDatabase;

    ConstraintLayout mLayout;
    int mDefaultColor;
    Button mButton;
    Button buttonSave;
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

        // Initialisez la base de données Room
        //appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "course.db").build();
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

        // Créez une instance de la classe Course avec les données du formulaire
        Course course = new Course(title, subtitle, color, day, frequency, hourBegin, hourEnd, dateBegin, dateEnd);
        writeCourseToCSV(course);

        // Ajoutez la course à la base de données en utilisant Room
        //appDatabase.courseDao().insertCourse(course);

        // Affichez un message ou effectuez toute autre action après avoir ajouté la course à la base de données
        //Toast.makeText(AddCourseActivity.this, "Plage horaire ajoutée avec succès", Toast.LENGTH_SHORT).show();

        // Fermez l'activité AddCourseActivity et retournez à l'activité précédente
        finish();
    }

    private void getAllCourses() {
        List<Course> courses = appDatabase.courseDao().getAllCourses();
        for (Course course : courses) {
            // Faites quelque chose avec chaque course
            Toast.makeText(this, course.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
    public  void clearInstance(Context context) {
        // Supprimer la base de données existante
        context.getApplicationContext().deleteDatabase("course-db");
        Toast.makeText(AddCourseActivity.this, "table suprimée", Toast.LENGTH_SHORT).show();

    }
    private void writeCourseToCSV(Course course) {
        String[] data = {course.getTitle(), course.getSubTitle(), String.valueOf(course.getColor()), course.getDay(), course.getFrequency(), course.getHourBegin(), course.getHourEnd(), course.getDateBegin(), course.getDateEnd() }; // Remplacez ... par les autres attributs de la classe Course
        File directory = getExternalFilesDir(null);
        // Créez le fichier CSV dans le répertoire
        File file = new File(directory, "data_schedule.csv");

        try {
            FileWriter fileWriter = new FileWriter(file, true); // Mode append (ajout)
            CSVWriter writer = new CSVWriter(fileWriter); // Remplacez "chemin_vers_le_fichier.csv" par le chemin réel vers votre fichier CSV

            writer.writeNext(data);

            writer.close();

            Toast.makeText(AddCourseActivity.this, "Données ajoutées au fichier CSV avec succès", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(AddCourseActivity.this, "Erreur lors de l'ajout des données au fichier CSV", Toast.LENGTH_SHORT).show();
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                // Traitez chaque ligne du fichier CSV ici
                Toast.makeText(AddCourseActivity.this, line , Toast.LENGTH_SHORT).show();

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}