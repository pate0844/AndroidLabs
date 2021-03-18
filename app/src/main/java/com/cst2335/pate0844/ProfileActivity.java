package com.cst2335.pate0844;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private ImageButton button;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton mImageButton;
    TextView emailEditText;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    private Button buttonChat;
    private Button Weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        button = findViewById(R.id.buttonSnap);
        mImageButton = findViewById(R.id.buttonSnap);
        emailEditText= findViewById(R.id.editTextEmail);
        buttonChat=(Button)findViewById(R.id.buttonChat);
        Weather=(Button)findViewById(R.id.WeatherForechat);

        button.setOnClickListener(e -> {
            dispatchTakePictureIntent();
        });

        Weather.setOnClickListener(e ->{
            Intent goToWeather = new Intent(ProfileActivity.this, WeatherForecast.class);
            startActivity(goToWeather);

        });

        buttonChat.setOnClickListener(e ->{
            Intent goToChat = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            startActivity(goToChat);
        });

        Intent fromMain = getIntent(); //declare intent//get info from MainActivity
        emailEditText.setText(fromMain.getStringExtra("EMAIL"));//put info into EditText (dong` email: Enter your email

        Log.e(ACTIVITY_NAME, "In function: onCreate");

    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME, "In function: onActivityResult");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function: onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy");
    }


}
