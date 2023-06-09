package com.example.chronos_hm40;

import android.content.Context;
import android.provider.CalendarContract;
import android.util.Xml;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chronos_hm40.Event;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowEvent extends AppCompatActivity {

    private ArrayList<Event> events;
    private ListView lvEvents;
    private File eventFile;

    private static final String CSV_FILE_NAME = "data_event.csv";
    private static final String XML_FILE_NAME = "data_event.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_event);

        lvEvents = findViewById(R.id.lvEvents);
        events = new ArrayList<>();
        eventFile = new File(getFilesDir(), "todo.csv");

        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();

        // Charger les données depuis le fichier CSV et générer le fichier XML
        ArrayList<Event> eventList = readCsv(this);
        generateXml(eventList, this);

        setContentView(R.layout.data_event);

        // Récupérer les données de l'intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        int color = getIntent().getIntExtra("color", 0);
        String date = getIntent().getStringExtra("date");

        // Afficher les données dans les TextViews
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        colorTextView.setBackgroundColor(color);
        dateTextView.setText(date);
    }

    private void readEvents() {
        try {
            FileReader fileReader = new FileReader(eventFile);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvData = csvReader.readAll();

            for (String[] row : csvData) {
                String item = row[0] + "\n" + row[1] + "\n" + row[2];
                events.add(event);
            }

            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
}


