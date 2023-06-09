package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {
    private TextView textViewSelectedDay;
    private Calendar currentDate = Calendar.getInstance();

    private AppDatabase appDatabase;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        textViewSelectedDay = findViewById(R.id.textViewSelectedDay);
        currentDate = Calendar.getInstance();

        gridLayout = findViewById(R.id.gridLayout);

        for (int hour = 0; hour < 24; hour++) {
            TextView hourTextView = new TextView(this);
            hourTextView.setText(String.format(Locale.getDefault(), "%02d:00", hour));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(hour + 1, GridLayout.TOP);
            params.columnSpec = GridLayout.spec(0, GridLayout.LEFT);
            hourTextView.setLayoutParams(params);
            gridLayout.addView(hourTextView);
        }

        //appDatabase = AppDatabaseSingleton.getInstance(getApplicationContext());
        updateSelectedDayText();
        loadCoursesFromCSV();
    }

    private void updateSelectedDayText() {
        String selectedDay = new SimpleDateFormat("EEEE dd MMMM", Locale.FRENCH).format(currentDate.getTime());
        textViewSelectedDay.setText(selectedDay);
    }

    public void onClickPreviousDay(View view) {
        currentDate.add(Calendar.DAY_OF_MONTH, -1);
        updateSelectedDayText();
        loadCoursesFromCSV();
        // Afficher les plages horaires du jour choisi
        // showDaySchedule(currentDate);
    }

    public void onClickNextDay(View view) {
        currentDate.add(Calendar.DAY_OF_MONTH, 1);
        updateSelectedDayText();
        loadCoursesFromCSV();

        // Afficher les plages horaires du jour choisi
        // showDaySchedule(currentDate);
    }

    public void showCourses() {
        String selectedDay = new SimpleDateFormat("EEEE", Locale.FRENCH).format(currentDate.getTime());
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(currentDate.getTime());

        List<Course> courses = appDatabase.courseDao().getCoursesForDayAndDate(selectedDay, selectedDate);
        /*
        for (Course course : courses) {
            // Créez un TextView pour représenter le cours
            TextView textView = new TextView(this);
            textView.setText(course.getTitle() + "\n" + course.getSubTitle());
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setBackgroundColor(course.getColor());

            // Définissez les paramètres de mise en page pour le TextView
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(getRowForTime(course.getHourBegin()));
            params.columnSpec = GridLayout.spec(1, getColumnSpanForCourse(course));
            params.setMargins(8, 8, 8, 8); // Espacement autour du TextView
            textView.setLayoutParams(params);

            // Ajoutez le TextView à la grille
            gridLayout.addView(textView);
        }*/
    }


    private int getRowForTime(String time) {
        List<String> timeSlots = Arrays.asList(
                "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00",
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"
        );

        // Recherche de l'index correspondant à l'heure de début dans la liste des plages horaires
        int index = timeSlots.indexOf(time);

        // Retourne l'index de ligne correspondant à l'heure de début du cours
        return index; // Aucun ajout nécessaire pour prendre en compte la ligne d'en-tête des heures
    }

    private int getColumnSpanForCourse(Course course) {
        // Calcul de la durée du cours en minutes
        long startTimeInMillis = parseTimeToMillis(course.getHourBegin());
        long endTimeInMillis = parseTimeToMillis(course.getHourEnd());
        long durationInMillis = endTimeInMillis - startTimeInMillis;
        int durationInMinutes = (int) (durationInMillis / (1000 * 60));

        // Calcul du nombre de colonnes en fonction de la durée
        int columnSpan;
        if (durationInMinutes <= 60) {
            // Cours d'une heure ou moins, occupe une colonne
            columnSpan = 1;
        } else if (durationInMinutes <= 120) {
            // Cours de plus d'une heure et jusqu'à deux heures, occupe deux colonnes
            columnSpan = 2;
        } else {
            // Cours de plus de deux heures, occupe trois colonnes
            columnSpan = 3;
        }

        return columnSpan;
    }

    // Méthode pour convertir une heure au format HH:mm en millisecondes
    private long parseTimeToMillis(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void getAllCourses() {
        List<Course> courses = appDatabase.courseDao().getAllCourses();
        for (Course course : courses) {
            // Faites quelque chose avec chaque course
            Toast.makeText(this, course.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clearInstance(Context context) {
        // Supprimer la base de données existante
        context.getApplicationContext().deleteDatabase("course-db");
        Toast.makeText(ScheduleActivity.this, "table suprimée", Toast.LENGTH_SHORT).show();

    }

    private void loadCoursesFromCSV() {
        try {
            File csvFile = new File(getExternalFilesDir(null), "data_schedule.csv");
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

                // Exemple : Ajoutez la plage horaire à la grille
                addCourseToGridLayout(course);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCourseToGridLayout(Course course) {
        // Vérifier si le cours correspond à la date sélectionnée
        String selectedDay = new SimpleDateFormat("EEEE", Locale.FRENCH).format(currentDate.getTime());
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(currentDate.getTime());
        if (!course.getDay().equalsIgnoreCase(selectedDay) || !course.getDateBegin().equals(selectedDate)) {
            return; // Le cours ne correspond pas à la date sélectionnée, ne l'ajoutez pas à la grille
        }

        // Vérifier si le cours correspond à la fréquence
        String frequency = course.getFrequency();
        if (!frequency.equalsIgnoreCase("Unique")) {
            // Vérifier si le cours correspond à la fréquence quotidienne, hebdomadaire, bimensuelle ou mensuelle
            Calendar courseStartDate = Calendar.getInstance();
            courseStartDate.setTime(parseDate(course.getDateBegin()));
            Calendar courseEndDate = Calendar.getInstance();
            courseEndDate.setTime(parseDate(course.getDateEnd()));
            Calendar currentDateWithoutTime = removeTimeFromCalendar(currentDate);

            if (frequency.equalsIgnoreCase("Quotidienne")) {
                // Vérifier si la date actuelle est comprise entre la date de début et la date de fin du cours
                if (currentDateWithoutTime.before(courseStartDate) || currentDateWithoutTime.after(courseEndDate)) {
                    return; // Le cours ne correspond pas à la date actuelle, ne l'ajoutez pas à la grille
                }
            } else if (frequency.equalsIgnoreCase("Hebdomadaire")) {
                // Vérifier si la date actuelle est un multiple de 7 jours à partir de la date de début du cours
                int daysBetween = daysBetween(courseStartDate, currentDateWithoutTime);
                if (daysBetween % 7 != 0 || currentDateWithoutTime.after(courseEndDate)) {
                    return; // Le cours ne correspond pas à la date actuelle, ne l'ajoutez pas à la grille
                }
            } else if (frequency.equalsIgnoreCase("Bimensuelle")) {
                // Vérifier si la date actuelle est un multiple de 14 jours à partir de la date de début du cours
                int daysBetween = daysBetween(courseStartDate, currentDateWithoutTime);
                if (daysBetween % 14 != 0 || currentDateWithoutTime.after(courseEndDate)) {
                    return; // Le cours ne correspond pas à la date actuelle, ne l'ajoutez pas à la grille
                }
            } else if (frequency.equalsIgnoreCase("Mensuelle")) {
                // Vérifier si la date actuelle est un multiple de 30 jours à partir de la date de début du cours
                int daysBetween = daysBetween(courseStartDate, currentDateWithoutTime);
                if (daysBetween % 28 != 0 || currentDateWithoutTime.after(courseEndDate)) {
                    return; // Le cours ne correspond pas à la date actuelle, ne l'ajoutez pas à la grille
                }
            }
        }

        TextView textView = new TextView(this);
        textView.setText(course.getTitle() + "\n" + course.getSubTitle());
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textView.setBackgroundColor(course.getColor());

        GridLayout.Spec rowSpec = GridLayout.spec(getRowForTime(course.getHourBegin()));
        GridLayout.Spec columnSpec = GridLayout.spec(1, getColumnSpanForCourse(course));
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        params.setMargins(8, 8, 8, 8); // Espacement autour du TextView
        textView.setLayoutParams(params);

        gridLayout.addView(textView);
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
}
