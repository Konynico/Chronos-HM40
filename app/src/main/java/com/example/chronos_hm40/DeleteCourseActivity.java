package com.example.chronos_hm40;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class DeleteCourseActivity extends AppCompatActivity {
    private LinearLayout squareContainer;

    private boolean isDarkModeOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean theme = intent.getBooleanExtra("theme", false);
        if (theme == true) {
            setContentView(R.layout.dark_activity_delete_course);
            isDarkModeOn = true;
        }else{
            setContentView(R.layout.activity_delete_course);
            isDarkModeOn = false;
        }
        try {
            File csvFile = new File(getExternalFilesDir(null), "data_schedule_test.csv");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Divisez la ligne CSV en valeurs individuelles
                String[] values = line.split(",");

                // Récupérez les valeurs individuelles
                String title = removeQuotes(values[0]);
                String subtitle = removeQuotes(values[1]);
                int color = Integer.parseInt(removeQuotes(values[2]));
                String day = removeQuotes(values[3]);
                String frequency = removeQuotes(values[4]);
                String hourBegin = removeQuotes(values[5]);
                String hourEnd = removeQuotes(values[6]);
                String dateBegin = removeQuotes(values[7]);
                String dateEnd = removeQuotes(values[8]);

                // Créez une instance de la classe Course avec les données du fichier CSV
                Course course = new Course(title, subtitle, color, day, frequency, hourBegin, hourEnd, dateBegin, dateEnd);
                createColorRectangle(course);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createColorRectangle(Course course) {
        squareContainer = findViewById(R.id.squareContainer);
        // Obtenir la largeur de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        // Calculer la largeur du rectangle (par exemple, 80% de la largeur de l'écran)
        int rectangleWidth = (int) (screenWidth * 0.9);

        int height = 400; // Hauteur du rectangle (en pixels)
        ColorRectangleView rectangleView = new ColorRectangleView(this, course.getColor(), rectangleWidth, height, course.getTitle(), course.getSubTitle(), course.getHourBegin(), course.getHourEnd());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(rectangleWidth, height);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL; // Centrer le rectangle horizontalement
        layoutParams.setMargins(0, 0, 0, 10); // left, top, right, bottom
        rectangleView.setLayoutParams(layoutParams);
        rectangleView.setTag(course);
        rectangleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = (Course)view.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteCourseActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Voulez-vous supprimer ce cours ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeCourseFromCSV(course);
                        // Supprimer le rectangle de la vue
                        squareContainer.removeView(rectangleView);
                    }
                });
                builder.setNegativeButton("Non", null);
                builder.show();
            }
        });

        squareContainer.addView(rectangleView);
    }

    private String removeQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
    private void removeCourseFromCSV(Course course) {
        try {
            File csvFile = new File(getExternalFilesDir(null), "data_schedule_test.csv");
            File tempFile = new File(getExternalFilesDir(null), "temp_schedule_test.csv");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                String title = removeQuotes(values[0]);
                String subtitle = removeQuotes(values[1]);

                if (!(title.equals(course.getTitle()) && subtitle.equals(course.getSubTitle()))) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }

            bufferedReader.close();
            bufferedWriter.close();

            // Renommer le fichier temporaire pour écraser le fichier d'origine
            boolean renameSuccess = tempFile.renameTo(csvFile);
            if (!renameSuccess) {
                // Échec du renommage du fichier temporaire, gestion de l'erreur ici
            }
            Toast.makeText(DeleteCourseActivity.this, "Plage horaire supprimée avec succès", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
    }
}
