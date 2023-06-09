package com.example.chronos_hm40;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowEvent extends AppCompatActivity {

    private ArrayList<String> events;
    private ArrayAdapter<String> eventsAdapter;
    private ListView lvEvents;
    private File eventFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_event);

        lvEvents = findViewById(R.id.lvEvents);
        events = new ArrayList<>();
        eventFile = new File(getFilesDir(), "data.csv");

        readEvents();

        eventsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                String event = events.get(position);
                String[] parts = event.split("\n");
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(parts[0] + ", " + parts[1] + ", " + parts[2] + ", " + parts[3]);
                return view;
            }
        };
        lvEvents.setAdapter(eventsAdapter);
    }

    private void readEvents() {
        try {
            FileReader fileReader = new FileReader(eventFile);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvData = csvReader.readAll();

            for (String[] row : csvData) {
                String event = row[0] + "\n" + row[1] + "\n" + row[2];
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
        startActivity(intent);
    }
}