package com.cst2335.pate0844;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    public TextView current;
    public TextView min;
    public TextView max;
    public TextView uv;
    public ImageView weather;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.bar1);
        progressBar.setVisibility(View.VISIBLE);

        current = findViewById(R.id.Current);
        min = findViewById(R.id.Min);
        max = findViewById(R.id.Max);
        uv = findViewById(R.id.UV);
        weather = findViewById(R.id.picture);

        ForecastQuery req = new ForecastQuery();
        req.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }

    class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String currentUV;
        private String minimum;
        private String maximum;
        private String currentTemp;
        private Bitmap currentWeather;

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                String iconName = null;

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        if (xpp.getName().equals("temperature")) {
                            minimum = xpp.getAttributeValue(null, "min");
                            publishProgress(25);
                            maximum = xpp.getAttributeValue(null, "max");
                            publishProgress(50);
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(75);
                        } else if (xpp.getName().equals("weather")) {
                            iconName = xpp.getAttributeValue(null, "icon");
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                url = new URL("https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");

                //open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                response = urlConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject jObject = new JSONObject(result);

                //get the double associated with "value"
                currentUV = jObject.getString("value");
                publishProgress(100);

                Log.i("WeatherForecast", "Getting" + iconName + ".png");
                url = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    currentWeather = BitmapFactory.decodeStream(urlConnection.getInputStream());
                }
                if (fileExistance(iconName + ".png")) {

                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    currentWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } else {
                    Log.i("WeatherForecast", "Already exists, retrieving " + iconName + ".png");
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(getBaseContext().getFileStreamPath(iconName + ".png").toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    currentWeather = BitmapFactory.decodeStream(fis);
                }


            } catch (Exception e) {

            }

            return "Done";
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public void onProgressUpdate(Integer... args) {
            progressBar.setProgress(args[0]);
        }
        @Override
        public void onPostExecute(String fromDoInBackground) {
            super.onPostExecute(fromDoInBackground);
            current.setText("currentTemp:"+ Math.round(Float.parseFloat(currentTemp)));
            min.setText("Minimum:"+Math.round(Float.parseFloat(minimum)));
            max.setText("maximum:"+Math.round(Float.parseFloat(maximum)));
            uv.setText("currentUV:"+ Math.round(Float.parseFloat(currentUV)));
            weather.setImageBitmap(currentWeather);
            progressBar.setVisibility(View.INVISIBLE);



        }
    }
}