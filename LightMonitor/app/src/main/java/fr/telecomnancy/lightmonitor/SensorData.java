package fr.telecomnancy.lightmonitor;

import java.util.List;

public class SensorData {
    private List<SensorPoint> data;

    public List<SensorPoint> getData() {
        return data;
    }

    // Renvoie le dernier point de la liste (le plus récent)
    public SensorPoint getLastPoint() {
        if (data == null || data.isEmpty()) return null;
        return data.get(data.size() - 1);
    }
}
