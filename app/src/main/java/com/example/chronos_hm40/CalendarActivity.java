package com.example.chronos_hm40;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Calendar;


public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;

    private ArrayList<String> events;
    private ArrayAdapter<String> eventsAdapter;
    private ListView lvEvents2;
    private File eventFile;

    private boolean isDarkModeOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recuper un argument au lancement de l'activité
        Intent intent = getIntent();
        boolean theme = intent.getBooleanExtra("theme", false);


        if (theme == true) {
            setContentView(R.layout.dark_activity_calendar);
            isDarkModeOn = true;
        }else{
            setContentView(R.layout.activity_calendar);
            isDarkModeOn = false;
        }

        lvEvents2 = findViewById(R.id.lvEvents2);
        events = MainActivity.getEvents();
        eventFile = MainActivity.getFile();

        readEvents();

        // Créer une nouvelle liste pour stocker les événements triés
        List<String> eventsBuffer = new ArrayList<>(events);

        List<String> eventsTodayOrLater = new ArrayList<>();

        // Obtenir la date actuelle
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Notez que les mois commencent à partir de zéro (0 = janvier)
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String currentDate = String.format("%02d/%02d/%04d", day, (month + 1), year);

        for (String event : eventsBuffer) {
            String[] parts = event.split("\n");
            String eventDate = parts[0];

            if (eventDate.compareTo(currentDate) >= 0) {
                eventsTodayOrLater.add(event);
            }
        }

        // Trier la liste des événements en utilisant le EventComparator
        Collections.sort(eventsTodayOrLater, new EventComparator());

        // Récupérer les trois premiers événements de la liste
        List<String> threeEvents = new ArrayList<>();
        int count = 0;
        for (String event : eventsTodayOrLater) {
            threeEvents.add(event);
            count++;
            if (count == 3) {
                break;
            }
        }

        // Définir l'adaptateur sur lvEvents
        eventsAdapter = new ArrayAdapter<String>(this, R.layout.data_form_event_date, R.id.textViewTitle, threeEvents) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String event = threeEvents.get(position);
                String[] parts = event.split("\n");

                TextView textViewTitle = view.findViewById(R.id.textViewTitle);
                TextView textViewDescription = view.findViewById(R.id.textViewDescription);
                TextView textViewTime = view.findViewById(R.id.textViewTime);
                TextView textViewDate = view.findViewById(R.id.textViewDate);

                if(textViewDescription.getText() == "Description")
                {
                    textViewDescription.setText("");
                }

                if(textViewTime.getText() == "HH:MM")
                {
                    textViewTime.setText("");
                }

                textViewTitle.setText(parts[1]);
                textViewDescription.setText(parts[2]);
                textViewTime.setText(parts[4]);
                textViewDate.setText(parts[0]);

                int color = Integer.parseInt(parts[3]);
                textViewTitle.setTextColor(color);
                textViewDescription.setTextColor(color);
                textViewTime.setTextColor(color);
                textViewDate.setTextColor(color);

                return view;
            }
        };
        lvEvents2.setAdapter(eventsAdapter);

        lvEvents2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer l'événement sélectionné
                String selectedEvent = threeEvents.get(position);

                // Créer une nouvelle intention pour lancer l'activité EventDetailsActivity
                Intent intent = new Intent(CalendarActivity.this, EditEventActivity.class);
                intent.putExtra("selectedEvent", selectedEvent); // Vous pouvez passer des données supplémentaires à l'activité si nécessaire
                intent.putExtra("theme", theme);
                startActivity(intent);
                finish();
            }
        });

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalendarActivity.this, ShowEvent.class);
                String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                intent.putExtra("selectedDate", date);
                intent.putExtra("theme", theme);
                startActivity(intent);
            }
        });
    }

    private void readEvents() {
        events.clear(); // Vide la liste events avant de lire les événements

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

    public  void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", isDarkModeOn);
        startActivity(intent);
        finish();
    }
}