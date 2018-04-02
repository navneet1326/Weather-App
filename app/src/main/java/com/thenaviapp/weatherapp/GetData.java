package com.thenaviapp.weatherapp;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.thenaviapp.weatherapp.MainActivity;
import com.thenaviapp.weatherapp.entity.WeatherResponse;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Navneet on 01-04-2018.
 */

public class GetData extends AsyncTask<String,Void,String> {
    private MainActivity mainActivity;

    public GetData(MainActivity ma) {

        mainActivity = ma;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while(data != -1){
                char current = (char) data;
                result += current;
                data = reader.read();

            }
            return result;


        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            Gson gson = new Gson();
            WeatherResponse response = gson.fromJson(result, WeatherResponse.class);

            String temperature = response.getMain().getTemp();
            String placeName = response.getName();
            String maxTemperature = response.getMain().getTemp_max();
            String minTemperature = response.getMain().getTemp_min();
            String weatherInfo = response.getWeather()[0].getDescription();
            String cod = response.getWeather()[0].getId();
            String pressure = response.getMain().getPressure();
            String humidity = response.getMain().getHumidity();

            mainActivity.updateUI(temperature, placeName, maxTemperature, minTemperature, weatherInfo, cod, pressure, humidity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
