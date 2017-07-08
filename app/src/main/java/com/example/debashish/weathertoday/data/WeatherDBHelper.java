package com.example.debashish.weathertoday.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    public WeatherDBHelper(Context context)
    {
        super(context , DATABASE_NAME , null ,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME + " (" +

                        WeatherContract.WeatherEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        WeatherContract.WeatherEntry.COLUMN_DATE       + " INTEGER NOT NULL, " +

                        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL,"  +

                        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP   + " REAL NOT NULL, "    +
                        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP   + " REAL NOT NULL, "    +

                        WeatherContract.WeatherEntry.COLUMN_HUMIDITY   + " REAL NOT NULL, "    +
                        WeatherContract.WeatherEntry.COLUMN_PRESSURE   + " REAL NOT NULL, "    +

                        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, "    +
                        WeatherContract.WeatherEntry.COLUMN_DEGREES    + " REAL NOT NULL, "    +
                        /*
                 * To ensure this table can only contain one weather entry per date, we declare
                 * the date column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                 * SQLite that if we have a weather entry for a certain date and we attempt to
                 * insert another weather entry with that date, we replace the old weather entry.
                 */
                        " UNIQUE (" + WeatherContract.WeatherEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

       db.execSQL(SQL_CREATE_WEATHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + WeatherContract.WeatherEntry.TABLE_NAME);
        onCreate(db);

    }
}
