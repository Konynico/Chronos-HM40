package com.example.chronos_hm40;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void onButtonClick(View view) {
         Intent otherActivity = new Intent(getApplicationContext(), Menu.class);
         startActivity(otherActivity);
         finish();
    }
}
