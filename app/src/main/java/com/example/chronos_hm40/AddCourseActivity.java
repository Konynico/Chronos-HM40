package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddCourseActivity extends AppCompatActivity {
    private AppDatabase appDatabase;

    ConstraintLayout mLayout;
    int mDefaultColor;
    Button mButton;
    Button buttonSave;

    Spinner spinnerFrequency;
    ArrayAdapter<CharSequence> frequencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        frequencyAdapter = ArrayAdapter.createFromResource(this, R.array.frequency_options, android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(frequencyAdapter);

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
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "course-db")
                    .allowMainThreadQueries()
                    .build();
        }

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
        String hourBegin = editTextHourBegin.getText().toString();
        String hourEnd = editTextHourEnd.getText().toString();
        String dateBegin = editTextDateBegin.getText().toString();
        String dateEnd = editTextDateEnd.getText().toString();

        // Créez une instance de la classe Course avec les données du formulaire
        Course course = new Course(title, subtitle, color, frequency, hourBegin, hourEnd, dateBegin, dateEnd);

        // Ajoutez la course à la base de données en utilisant Room
        appDatabase.courseDao().insertCourse(course);

        // Affichez un message ou effectuez toute autre action après avoir ajouté la course à la base de données
        Toast.makeText(AddCourseActivity.this, "Course ajoutée avec succès", Toast.LENGTH_SHORT).show();
        getAllCourses();
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
}