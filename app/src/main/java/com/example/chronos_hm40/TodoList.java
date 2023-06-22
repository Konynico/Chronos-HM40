package com.example.chronos_hm40;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class TodoList extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private File todoFile;
    private int selectedColor;
    private Button btnColorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recuper un argument au lancement de l'activité
        Intent intent = getIntent();
        boolean theme = intent.getBooleanExtra("theme", false);

        if (theme == true){
            setContentView(R.layout.dark_activity_todo_list);}
        else{
            setContentView(R.layout.activity_todo_list);}

        btnColorPicker = findViewById(R.id.button);

        selectedColor = ContextCompat.getColor(TodoList.this, R.color.colorPrimary);
        btnColorPicker.setBackgroundColor(selectedColor);

        lvItems = findViewById(R.id.lvItems);
        items = new ArrayList<>();
        todoFile = new File(getFilesDir(), "todo.csv");

        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                String item = items.get(position);
                String[] parts = item.split("\n");
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(parts[0] + "\n" + parts[1]); // Affichez uniquement la première partie (le texte de l'élément) et ignorez la couleur
                int color = Integer.parseInt(parts[2]);
                textView.setTextColor(color);
                return view;
            }
        };
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }


    public void onClick(View v) {
        openColorPicker();
    }

    private void setupListViewListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TodoList.this);


                //si le dark mode est activé, on change le theme de la boite de dialogue
                Intent intent = getIntent();
                boolean theme = intent.getBooleanExtra("theme", false);

                View dialogView;

                if (theme == true){
                     dialogView = getLayoutInflater().inflate(R.layout.dark_dialog_edit_todo, null);}
                else{
                     dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_todo, null);}


                builder.setView(dialogView);

                EditText etEditItem = dialogView.findViewById(R.id.etEditItem);
                EditText etEditDate = dialogView.findViewById(R.id.etEditDate);

                Button btnColor = dialogView.findViewById(R.id.buttonColor2);
                Button btnDelete = dialogView.findViewById(R.id.btnDelete);
                Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                Button btnSave = dialogView.findViewById(R.id.btnSave);

                String item = items.get(position);
                String[] parts = item.split("\n");
                String currentItem = parts[0];
                String currentDate = parts[1];
                int currentColor = Integer.parseInt(parts[2]);

                etEditItem.setText(currentItem);
                etEditDate.setText(currentDate);

                AlertDialog dialog = builder.create();
                dialog.show();

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteConfirmBuilder = new AlertDialog.Builder(TodoList.this);
                        deleteConfirmBuilder.setTitle("Confirmation");
                        deleteConfirmBuilder.setMessage("Êtes-vous sûr de vouloir supprimer cet élément de votre liste de tâches ?");
                        deleteConfirmBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                removeItem(position);
                            }
                        });dialog.dismiss();
                        deleteConfirmBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // ferme simplement le popup
                            }
                        });

                        AlertDialog deleteConfirmDialog = deleteConfirmBuilder.create();
                        deleteConfirmDialog.show();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openColorPicker2(btnColor);
                    }
                                            });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String editedItem = etEditItem.getText().toString();
                        String editedDate = etEditDate.getText().toString();

                        if (isValidDate(editedDate)) {
                            editedItem += "\n" + editedDate + "\n" + selectedColor;
                            editItem(position, editedItem);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(TodoList.this, "Date invalide. Date trop ancienne ou mauvais format de date : (jj/mm/aaaa)", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void onSelectDate(View v) {
        // Récupérer la date actuelle
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Créer le DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(TodoList.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                // Mettre à jour l'affichage avec la date sélectionnée
                EditText etNewDate = findViewById(R.id.etNewDate);
                String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                etNewDate.setText(formattedDate);
            }
        }, year, month, dayOfMonth);

        // Afficher le DatePickerDialog
        datePickerDialog.show();
    }
    public void onAddItem(View v) {
        EditText etNewItem = findViewById(R.id.etNewItem);
        EditText etNewDate = findViewById(R.id.etNewDate);
        String itemText = etNewItem.getText().toString();
        String dateText = etNewDate.getText().toString();

        if (!itemText.isEmpty() && isValidDate(dateText)) {
            String newItem = itemText + "\n" + dateText + "\n" + selectedColor;

            items.add(0, newItem);
            itemsAdapter.notifyDataSetChanged();

            writeItems();

            etNewItem.setText("");
            etNewDate.setText("");
        } else {
            // Afficher un message d'erreur à l'utilisateur
            Toast.makeText(this, "Date invalide. Date trop ancienne ou mauvais format de date : (jj/mm/aaaa) ", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidDate(String date) {
        String[] dateParts = date.split("/");
        if (dateParts.length != 3) {
            return false;
        }
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        // Obtenir la date d'aujourd'hui
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Les mois dans Calendar sont indexés à partir de 0
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Vérifier si la date est valide et supérieure à aujourd'hui
        if (day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= currentYear) {
            if (year > currentYear || (year == currentYear && month > currentMonth) || (year == currentYear && month == currentMonth && day >= currentDay)) {
                return true;
            }
        }
        return false;
    }

    private void addItem(String item) {
        items.add(item);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }

    private void removeItem(int position) {
        items.remove(position);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }

    private void editItem(int position, String newItem) {
        items.set(position, newItem);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }

    public  void onChronoClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeItems();
    }

    private void readItems() {
        try {
            FileReader fileReader = new FileReader(todoFile);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvData = csvReader.readAll();

            for (String[] row : csvData) {
                String item = row[0] + "\n" + row[1] + "\n" + row[2];
                items.add(item);
            }

            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, selectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                selectedColor = color;
                btnColorPicker.setBackgroundColor(selectedColor);
            }
        });
        colorPicker.show();
    }

    public void openColorPicker2(Button btnColor) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, selectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                selectedColor = color;
                btnColor.setBackgroundColor(selectedColor);
            }
        });
        colorPicker.show();
    }

    private void writeItems() {
        try {
            FileWriter fileWriter = new FileWriter(todoFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            for (String item : items) {
                String[] row = item.split("\n");
                csvWriter.writeNext(row);
            }

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
