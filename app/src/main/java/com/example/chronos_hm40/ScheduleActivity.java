package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {
    private TextView textViewSelectedDay;
    private Calendar currentDate = Calendar.getInstance();
    private LinearLayout squareContainer;

    private boolean isDarkModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean theme = intent.getBooleanExtra("theme", false);
        if (theme == true) {
            setContentView(R.layout.dark_activity_schedule);
            isDarkModeOn = true;
        }else{
            setContentView(R.layout.activity_schedule);
            isDarkModeOn = false;
        }
        textViewSelectedDay = findViewById(R.id.textViewSelectedDay);
        currentDate = Calendar.getInstance();
        updateSelectedDayText();
        loadCoursesFromCSV();
    }

    private void updateSelectedDayText() {
        String selectedDay = new SimpleDateFormat("EEEE dd MMMM", Locale.FRENCH).format(currentDate.getTime());
        selectedDay = selectedDay.substring(0, 1).toUpperCase() + selectedDay.substring(1);
        textViewSelectedDay.setText(selectedDay);
    }


    public void onClickPreviousDay(View view) {
        currentDate.add(Calendar.DAY_OF_MONTH, -1);
        squareContainer = findViewById(R.id.squareContainer);
        squareContainer.removeAllViews();
        updateSelectedDayText();
        loadCoursesFromCSV();
        // Afficher les plages horaires du jour choisi
        // showDaySchedule(currentDate);
    }

    public void onClickNextDay(View view) {
        currentDate.add(Calendar.DAY_OF_MONTH, 1);
        squareContainer = findViewById(R.id.squareContainer);
        squareContainer.removeAllViews();
        updateSelectedDayText();
        loadCoursesFromCSV();

        // Afficher les plages horaires du jour choisi
        // showDaySchedule(currentDate);
    }

    private void loadCoursesFromCSV() {
        try {
            List<Course> courseList = new ArrayList<>();
            File csvFile = new File(getExternalFilesDir(null), "data_schedule_test.csv");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
            String selectedDay = new SimpleDateFormat("EEEE", Locale.FRENCH).format(currentDate.getTime());
            selectedDay = selectedDay.substring(0, 1).toUpperCase() + selectedDay.substring(1);
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

                Calendar courseStartDate = Calendar.getInstance();
                courseStartDate.setTime(parseDate(course.getDateBegin()));
                Calendar courseEndDate = Calendar.getInstance();
                courseEndDate.setTime(parseDate(course.getDateEnd()));
                Calendar currentDateWithoutTime = removeTimeFromCalendar(currentDate);
                int daysBetween = daysBetween(courseStartDate, currentDateWithoutTime);

                if(course.getFrequency().equalsIgnoreCase("Quotidienne") && currentDateWithoutTime.after(courseEndDate)) {
                    courseList.add(course);
                }else if(course.getDay().equalsIgnoreCase(selectedDay) && !currentDateWithoutTime.after(courseEndDate)){
                    if(frequency.equalsIgnoreCase("Unique")){
                        courseList.add(course);
                    }else if(frequency.equalsIgnoreCase("Hebdomadaire") && daysBetween % 7 == 0){
                        courseList.add(course);
                    } else if (frequency.equalsIgnoreCase("Bimensuelle") && daysBetween % 14 == 0) {
                        courseList.add(course);
                    }else if(frequency.equalsIgnoreCase("Mensuelle") && daysBetween % 28 == 0 ){
                        courseList.add(course);
                    }
                }

            }
            Collections.sort(courseList);
            for (Course course : courseList) {
                createColorRectangle(course);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Calendar removeTimeFromCalendar(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private int daysBetween(Calendar startDate, Calendar endDate) {
        long startMillis = removeTimeFromCalendar(startDate).getTimeInMillis();
        long endMillis = removeTimeFromCalendar(endDate).getTimeInMillis();
        long differenceMillis = endMillis - startMillis;
        return (int) (differenceMillis / (24 * 60 * 60 * 1000));
    }

    private String removeQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
    private void createColorRectangle(Course course) {
        squareContainer = findViewById(R.id.squareContainer);
        // Obtenir la largeur de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        // Calculer la largeur du rectangle (par exemple, 80% de la largeur de l'écran)
        int rectangleWidth = (int) (screenWidth * 0.9);

        int height = calculHauteur(course); // Hauteur du rectangle (en pixels)
        ColorRectangleView rectangleView = new ColorRectangleView(this, course.getColor(), rectangleWidth, height,course.getTitle(),course.getSubTitle(),course.getHourBegin(),course.getHourEnd());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(rectangleWidth, height);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL; // Centrer le rectangle horizontalement
        layoutParams.setMargins(0, 0, 0, 10); // left, top, right, bottom
        rectangleView.setLayoutParams(layoutParams);
        squareContainer.addView(rectangleView);
    }

    private int calculHauteur(Course course){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date startTime = timeFormat.parse(course.getHourBegin());
            Date endTime = timeFormat.parse(course.getHourEnd());
            long duration = endTime.getTime() - startTime.getTime();

            // Calculer la hauteur proportionnelle à la durée
            float heightProportion = duration / (60 * 60 * 1000f); // Convertir la durée en heures
            heightProportion = heightProportion*200;
            return (int)heightProportion;

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public  void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
    }
}
