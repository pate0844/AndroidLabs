package com.cst2335.pate0844;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME="activity_profile";
    static final int REQUEST_IMAGE_CAPTURE=1;

    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    //   super.onCreate(savedInstanceState);
    // setContentView(R.layout.activity_profile);

    // String email = getIntent().getStringExtra("EMAIL");
    // EditText emailField = findViewById(R.id.editText1);
    //emailField.setText(email);
    //}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        EditText emailEditText=(EditText)findViewById(R.id.editText1) ;

        Intent fromMain=getIntent();
        String recievedEmail= fromMain.getStringExtra("EMAIL");
        emailEditText.setText(recievedEmail);


        ImageButton takePictureBtn = (ImageButton) findViewById(R.id.button2);
        takePictureBtn.setOnClickListener(c -> {


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        });
        Button ChatButton = (Button) findViewById(R.id.button3);
        ChatButton.setOnClickListener( c -> {

            Intent goToProfile  = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            startActivityForResult( goToProfile,345);
        });
        Button WeatherButton = (Button) findViewById(R.id.button4);
        WeatherButton.setOnClickListener( c -> {

            Intent goToWeather  = new Intent(ProfileActivity.this, WeatherForecast.class);
            startActivityForResult( goToWeather,345);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageButton takePictureBtn = (ImageButton) findViewById(R.id.button2);
            takePictureBtn.setImageBitmap(imageBitmap);
        }
        Log.d(ACTIVITY_NAME, "In function: onActivityResult()");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(ACTIVITY_NAME, "In function: onStart()");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ACTIVITY_NAME, "In function: onResume()");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(ACTIVITY_NAME, "In function: onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(ACTIVITY_NAME, "In function: onStop()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ACTIVITY_NAME, "In function: onDestroy()");
    }
}
