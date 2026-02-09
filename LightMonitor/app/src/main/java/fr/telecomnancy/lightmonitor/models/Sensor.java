package fr.telecomnancy.lightmonitor.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sensors")
public class Sensor {
    @PrimaryKey
    @NonNull
    private String sensorId;
    private String name;
    private double luminosity;
    private boolean isLightActive;
    private long lastUpdate;

    public Sensor(@NonNull String sensorId, String name, double luminosity) {
        this.sensorId = sensorId;
        this.name = name;
        this.luminosity = luminosity;
        this.isLightActive = luminosity > 300; // Seuil à ajuster
        this.lastUpdate = System.currentTimeMillis();
    }

    // Getters et Setters
    @NonNull
    public String getSensorId() { return sensorId; }
    public void setSensorId(@NonNull String sensorId) { this.sensorId = sensorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLuminosity() { return luminosity; }
    public void setLuminosity(double luminosity) {
        this.luminosity = luminosity;
        this.isLightActive = luminosity > 300;
    }

    public boolean isLightActive() { return isLightActive; }
    public void setLightActive(boolean lightActive) { isLightActive = lightActive; }

    public long getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
}
