package com.example.chronos_hm40;

import java.util.Comparator;

public class EventComparator implements Comparator<String> {
    @Override
    public int compare(String event1, String event2) {
        // Extraire les dates et heures des événements
        String[] eventDetails1 = event1.split("\n");
        String[] eventDetails2 = event2.split("\n");
        String date1 = eventDetails1[0];
        String date2 = eventDetails2[0];
        String heure1 = eventDetails1[4];
        String heure2 = eventDetails2[4];

        // Comparer les dates
        int dateComparison = date1.compareTo(date2);
        if (dateComparison != 0) {
            return dateComparison;
        }

        // Comparer les heures
        return heure1.compareTo(heure2);
    }
}

