package fr.telecomnancy.lightmonitor.data;

import android.content.Context;
import androidx.room.*;
import fr.telecomnancy.lightmonitor.models.Sensor;

@Database(entities = {Sensor.class}, version = 1, exportSchema = false)
public abstract class SensorDatabase extends RoomDatabase {
    private static SensorDatabase instance;

    public abstract SensorDao sensorDao();

    public static synchronized SensorDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SensorDatabase.class,
                    "sensor_database"
            ).build();
        }
        return instance;
    }
}
