package com.example.debashish.weathertoday;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.debashish.weathertoday.data.WeatherContract;
import com.example.debashish.weathertoday.databinding.ActivityDestinationBinding;
import com.example.debashish.weathertoday.utils.TempratureUtils;
import com.example.debashish.weathertoday.utils.WeatherDateUtils;

public class DestinationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";


    public static final String[] WEATHER_DETAIL_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };


    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_HUMIDITY = 3;
    public static final int INDEX_WEATHER_PRESSURE = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_DEGREES = 6;
    public static final int INDEX_WEATHER_CONDITION_ID = 7;


    private static final int ID_DETAIL_LOADER = 353;

    private String mForecastSummary;

    private Uri mUri;


    private ActivityDestinationBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//      COMPLETED (4) Remove the call to setContentView
//      COMPLETED (5) Remove all the findViewById calls

//      COMPLETED (6) Instantiate mDetailBinding using DataBindingUtil
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_destination);

        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");


        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

        switch (loaderId) {

            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        WEATHER_DETAIL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
        int weatherImageId = TempratureUtils.getLargeArtResourceIdForWeatherCondition(weatherId);

        mDetailBinding.primaryInfo.weatherIcon.setImageResource(weatherImageId);
        long localDateMidnightGmt = data.getLong(INDEX_WEATHER_DATE);
        String dateText = WeatherDateUtils.getFriendlyDateString(this, localDateMidnightGmt, true);


        mDetailBinding.primaryInfo.date.setText(dateText);

        String description = TempratureUtils.getStringForWeatherCondition(this, weatherId);


        String descriptionA11y = getString(R.string.a11y_forecast, description);


        mDetailBinding.primaryInfo.weatherDescription.setText(description);
        mDetailBinding.primaryInfo.weatherDescription.setContentDescription(descriptionA11y);
        mDetailBinding.primaryInfo.weatherIcon.setContentDescription(descriptionA11y);

        double highInCelsius = data.getDouble(INDEX_WEATHER_MAX_TEMP);

        String highString = TempratureUtils.formatTemperature(this, highInCelsius);


        String highA11y = getString(R.string.a11y_high_temp, highString);

        mDetailBinding.primaryInfo.highTemperature.setText(highString);
        mDetailBinding.primaryInfo.highTemperature.setContentDescription(highA11y);


        double lowInCelsius = data.getDouble(INDEX_WEATHER_MIN_TEMP);

        String lowString = TempratureUtils.formatTemperature(this, lowInCelsius);
        String lowA11y = getString(R.string.a11y_low_temp, lowString);


        mDetailBinding.primaryInfo.lowTemperature.setText(lowString);
        mDetailBinding.primaryInfo.lowTemperature.setContentDescription(lowA11y);


        float humidity = data.getFloat(INDEX_WEATHER_HUMIDITY);
        String humidityString = getString(R.string.format_humidity, humidity);


        String humidityA11y = getString(R.string.a11y_humidity, humidityString);

        mDetailBinding.extraDetails.humidity.setText(humidityString);
        mDetailBinding.extraDetails.humidity.setContentDescription(humidityA11y);
        mDetailBinding.extraDetails.humidityLabel.setContentDescription(humidityA11y);


        float windSpeed = data.getFloat(INDEX_WEATHER_WIND_SPEED);
        float windDirection = data.getFloat(INDEX_WEATHER_DEGREES);
        String windString = TempratureUtils.getFormattedWind(this, windSpeed, windDirection);


        String windA11y = getString(R.string.a11y_wind, windString);


        mDetailBinding.extraDetails.windMeasurement.setText(windString);
        mDetailBinding.extraDetails.windMeasurement.setContentDescription(windA11y);


        mDetailBinding.extraDetails.windLabel.setContentDescription(windA11y);

        float pressure = data.getFloat(INDEX_WEATHER_PRESSURE);

        String pressureString = getString(R.string.format_pressure, pressure);


        String pressureA11y = getString(R.string.a11y_pressure, pressureString);


        mDetailBinding.extraDetails.pressure.setText(pressureString);
        mDetailBinding.extraDetails.pressure.setContentDescription(pressureA11y);


        mDetailBinding.extraDetails.pressureLabel.setContentDescription(pressureA11y);


        mForecastSummary = String.format("%s - %s - %s/%s",
                dateText, description, highString, lowString);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
