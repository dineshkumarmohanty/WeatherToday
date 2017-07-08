package com.example.debashish.weathertoday.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.debashish.weathertoday.data.WeatherContract;
import com.example.debashish.weathertoday.data.WeatherPreference;
import com.example.debashish.weathertoday.utils.NetworkUtils;
import com.example.debashish.weathertoday.utils.NotificationUtils;
import com.example.debashish.weathertoday.utils.OpenWeatherJSONUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class WeatherSyncTasks {


    public static void syncWeather(Context context){


        String location = WeatherPreference.getPreferredWeatherLocation(context);
        URL url = NetworkUtils.buildUrl(location);
        String response = null;
        try {

           response = NetworkUtils.getResponseFromHttpUrl(url);


        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null) {

            ContentValues[] contentValues = null;

            try {
             contentValues =  OpenWeatherJSONUtils.getWeatherContentValuesFromJson(context, response);
                if (contentValues != null && contentValues.length != 0) {

                    ContentResolver contentResolver = context.getContentResolver();
                    contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI , null , null);
                    int rowsInserted = contentResolver.
                            bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, contentValues);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NotificationUtils.notifyUserOfNewWeather(context);


        }


    }

}
