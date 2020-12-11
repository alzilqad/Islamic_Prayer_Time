package com.example.Prayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DownloadTask task;
    TextView display;
    String displayDate = "";
    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data =reader.read();
                while(data!=-1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try{
                JSONObject jsonObject =  new JSONObject(s);
                String data = jsonObject.getString("data");

                JSONObject jsonObject1 = new JSONObject(data);
                String timings = jsonObject1.getString("timings");

                DateFormat dateFormat = new SimpleDateFormat("hh:mm");

                JSONObject jsonObject2 = new JSONObject(timings);

                String sunrise = jsonObject2.getString("Sunrise");
                Date date= dateFormat.parse(sunrise);
                String sunrise1 = dateFormat.format(date);

                String fajr = jsonObject2.getString("Fajr");
                Date date1= dateFormat.parse(fajr);
                String fajr1 = dateFormat.format(date1);

                String dhuhr = jsonObject2.getString("Dhuhr");
                Date date2= dateFormat.parse(dhuhr);
                String dhuhr1 = dateFormat.format(date2);

                String asr = jsonObject2.getString("Asr");
                Date date3= dateFormat.parse(asr);
                String asr1 = dateFormat.format(date3);

                String maghrib = jsonObject2.getString("Maghrib");
                Date date4= dateFormat.parse(maghrib);
                String maghrib1 = dateFormat.format(date4);

                String isha = jsonObject2.getString("Isha");
                Date date5= dateFormat.parse(isha);
                String isha1 = dateFormat.format(date5);

                String result = "Date: " + displayDate + "\nFajr: " + fajr1 + "\nSunrise: " + sunrise1 + "\nDhuhr: " + dhuhr1 + "\nAsr: " + asr1 + "\nMaghrib: " + maghrib1 + "\nIsha: " + isha1;

                if(!result.equals("")){
                    display.setText(result);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void onClickFunction(View view){
        task = new DownloadTask();
        Calendar calendar = Calendar.getInstance();

        Button btn = (Button) findViewById(R.id.button);
        if(btn.getText().equals("Today")){
            btn.setText("Tomorrow");
        }
        else if(btn.getText().equals("Tomorrow")){
            btn.setText("Today");
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        displayDate = dateFormat.format(date);
        task.execute("https://api.aladhan.com/timingsByAddress/"+displayDate+"?address=Dhaka,Bangladesh&method=8&tune=2,3,4,5,2,3,4,5,-3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task = new DownloadTask();
        display = findViewById(R.id.displayInfo);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        displayDate = dateFormat.format(date);
        task.execute("https://api.aladhan.com/timingsByAddress/"+displayDate+"?address=Dhaka,Bangladesh&method=8&tune=2,3,4,5,2,3,4,5,-3");
    }
}