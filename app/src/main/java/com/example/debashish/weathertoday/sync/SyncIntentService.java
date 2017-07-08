package com.example.debashish.weathertoday.sync;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class SyncIntentService extends IntentService{

    public SyncIntentService()
    {
        super("syncIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        WeatherSyncTasks.syncWeather(this);
    }
}
