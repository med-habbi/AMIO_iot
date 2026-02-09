package fr.telecomnancy.lightmonitor;

public class SensorPoint {
    private long timestamp;
    private String label;
    private double value;
    private String mote;

    public long getTimestamp() { return timestamp; }
    public String getLabel() { return label; }
    public double getValue() { return value; }
    public String getMote() { return mote; }
}
