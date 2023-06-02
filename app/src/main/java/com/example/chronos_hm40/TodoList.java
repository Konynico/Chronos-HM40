package com.example.chronos_hm40;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoList extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private File todoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        lvItems = findViewById(R.id.lvItems);
        items = new ArrayList<>();
        todoFile = new File(getFilesDir(), "todo.txt");

        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TodoList.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Êtes-vous sûr de vouloir supprimer cet élément de votre todo liste ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeItem(pos);
                    }
                });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Ne faites rien ici - fermez simplement le popup
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    }

    public void onAddItem(View v) {
        EditText etNewItem = findViewById(R.id.etNewItem);
        EditText etNewDate = findViewById(R.id.etNewDate);
        String itemText = etNewItem.getText().toString();
        String dateText = etNewDate.getText().toString();

        if (!itemText.isEmpty() && !dateText.isEmpty()) {
            String newItem = itemText + "\n" + dateText;
            addItem(newItem);

            etNewItem.setText("");
            etNewDate.setText("");
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        writeItems();
    }

    private void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
