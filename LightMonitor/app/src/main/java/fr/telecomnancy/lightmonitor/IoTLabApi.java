package fr.telecomnancy.lightmonitor;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IoTLabApi {

    // Light 1
    @GET("rest/data/1/light1/last")
    Call<SensorData> getLight1History();

    // Light 2
    @GET("rest/data/1/light2/last")
    Call<SensorData> getLight2History();

    // Température
    @GET("rest/data/1/temp/last")        // ou "temperature" selon l’URL exacte
    Call<SensorData> getTemperatureHistory();

    // Humidité
    @GET("rest/data/1/humidity/last")
    Call<SensorData> getHumidityHistory();

    // Tension batterie
    @GET("rest/data/1/battery_Voltage/last")     // ou "batteryVoltage" etc.
    Call<SensorData> getBatteryHistory();
}
