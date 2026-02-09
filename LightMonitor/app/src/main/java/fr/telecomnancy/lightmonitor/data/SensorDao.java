package fr.telecomnancy.lightmonitor.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import fr.telecomnancy.lightmonitor.models.Sensor;
import java.util.List;

@Dao
public interface SensorDao {
    @Query("SELECT * FROM sensors ORDER BY isLightActive DESC, luminosity DESC")
    LiveData<List<Sensor>> getAllSensors();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Sensor> sensors);

    @Query("SELECT * FROM sensors WHERE isLightActive = 1")
    List<Sensor> getActiveLights();

    @Query("DELETE FROM sensors")
    void deleteAll();
}
