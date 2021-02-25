package com.cst2335.pate0844;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_relative);
        TextView text = findViewById(R.id.text);
        EditText edit = findViewById(R.id.editText);
        Button buttonclick = findViewById(R.id.button2);


        Button button = findViewById(R.id.button2);
        button.setOnClickListener((vw) -> {
            button.setText(getResources().getString(R.string.hello_message));
            Toast.makeText(MainActivity.this,
                    "Here is more information!", Toast.LENGTH_LONG).show();
        });
    }


}