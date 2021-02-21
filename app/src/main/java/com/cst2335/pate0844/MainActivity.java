package com.cst2335.pate0844;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.BreakIterator;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editt = findViewById(R.id.email);
        sharedPreferences = getSharedPreferences("sign-in", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("e-mail", "");
        editt.setText(data);
        Button buttonLogin = findViewById(R.id.button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilePage = new Intent(MainActivity.this, ProfileActivity.class);
                profilePage.putExtra("email", editt.getText().toString());
                startActivity(profilePage);
            }
        });
    }

        @Override
    protected void onStop(){super.onStop(); }
    @Override
    protected void onPause() {
        super.onPause();
        EditText email = findViewById(R.id.email);
        SharedPreferences.Editor ed=sharedPreferences.edit();
        ed.putString("e-mail",email.getText().toString());
        ed.apply();
    }
}