package com.example.chronos_hm40;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
                    addEventToDatabase();

                    // Afficher un message de succès ou effectuer d'autres actions nécessaires

                    finish();
                } else {
                    // Afficher un message d'erreur ou effectuer d'autres actions nécessaires
                }
            }
        });
    }

    private void addEventToDatabase() {
        // Récupérer les valeurs des champs de saisie
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Vérifier si les champs sont vides
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer une instance de la classe EventDatabaseHelper
        EventDatabaseHelper databaseHelper = new EventDatabaseHelper(this);

        // Créer un nouvel événement avec les valeurs saisies

        int year = 0;
        int month = 0;
        int dayOfMonth = 0;
        Event event = new Event(title, description, year, month, dayOfMonth);

        // Ajouter l'événement à la base de données
        databaseHelper.addEvent(event);

        // Afficher un message de succès
        Toast.makeText(this, "Événement ajouté avec succès", Toast.LENGTH_SHORT).show();

        // Terminer l'activité et revenir à l'écran précédent
        finish();
    }
}
