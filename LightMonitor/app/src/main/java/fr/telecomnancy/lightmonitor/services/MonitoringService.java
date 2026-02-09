package fr.telecomnancy.lightmonitor;

import android.app.Notification;
import fr.telecomnancy.lightmonitor.NotificationHelper;
import java.util.ArrayList;
import fr.telecomnancy.lightmonitor.TimeChecker;
import fr.telecomnancy.lightmonitor.EmailSender;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Response;

import fr.telecomnancy.lightmonitor.SensorData;  // Nouveau modèle
import fr.telecomnancy.lightmonitor.ApiClient; // Nouveau client
import retrofit2.Response;
import fr.telecomnancy.lightmonitor.SensorData;
import fr.telecomnancy.lightmonitor.SensorPoint;
import fr.telecomnancy.lightmonitor.ApiClient;

import android.app.NotificationChannel;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.os.Handler;
import android.os.Looper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import fr.telecomnancy.lightmonitor.data.SensorDatabase;
import fr.telecomnancy.lightmonitor.models.Sensor;

import java.util.ArrayList;
import java.util.List;

public class MonitoringService extends Service {
    private static final String TAG = "MonitoringService";
    private static final int NOTIFICATION_ID = 1;
    private static final long CHECK_INTERVAL = 60000; // 1 minute
    private Handler handler = new Handler();
    private SensorDatabase database;
    private SharedPreferences prefs;
    private Runnable monitoringRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service démarré");

        database = SensorDatabase.getInstance(this);
        prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Créer notification foreground
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());

        // Démarrer surveillance
        startMonitoring();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "monitoring_channel",
                    "Surveillance capteurs",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, "monitoring_channel")
                .setContentTitle("🔍 Surveillance active")
                .setContentText("Vérification des capteurs toutes les minutes")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    private void startMonitoring() {
        monitoringRunnable = new Runnable() {
            @Override
            public void run() {
                fetchRealSensors();   // au lieu de simulateSensorCheck();
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        };

        handler.postDelayed(monitoringRunnable, 5000); // Premier check dans 5s
    }

    private void simulateSensorCheck() {

    }


    // Méthode utilitaire
    private boolean wasAlreadyActive(Sensor newSensor, List<Sensor> oldActive) {
        for (Sensor old : oldActive) {
            if (old.getSensorId().equals(newSensor.getSensorId())) {
                return true;
            }
        }
        return false;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null && monitoringRunnable != null) {
            handler.removeCallbacks(monitoringRunnable);
        }
        Log.d(TAG, "Service arrêté");
    }
    private void fetchRealSensors() {
        new Thread(() -> {
            try {
                Log.d(TAG, "📡 Appel IoTLab (multi-capteurs)...");

                List<Sensor> newSensors = new ArrayList<>();

                // 1) Light 1
                Response<SensorData> light1Resp =
                        ApiClient.getApi().getLight1History().execute();
                if (light1Resp.isSuccessful() && light1Resp.body() != null) {
                    SensorPoint last = light1Resp.body().getLastPoint();
                    if (last != null) {
                        newSensors.add(new Sensor("light1", "Light 1", last.getValue()));
                        Log.d(TAG, "Light1 = " + last.getValue() + " lux");
                    }
                }

                // 2) Light 2
                Response<SensorData> light2Resp =
                        ApiClient.getApi().getLight2History().execute();
                if (light2Resp.isSuccessful() && light2Resp.body() != null) {
                    SensorPoint last = light2Resp.body().getLastPoint();
                    if (last != null) {
                        newSensors.add(new Sensor("light2", "Light 2", last.getValue()));
                        Log.d(TAG, "Light2 = " + last.getValue() + " lux");
                    }
                }

                // 3) Température
                Response<SensorData> tempResp =
                        ApiClient.getApi().getTemperatureHistory().execute();
                if (tempResp.isSuccessful() && tempResp.body() != null) {
                    SensorPoint last = tempResp.body().getLastPoint();
                    if (last != null) {
                        // On stocke aussi comme "luminosité" mais libellé différent
                        newSensors.add(new Sensor("temp", "Température (°C)", last.getValue()));
                        Log.d(TAG, "Temp = " + last.getValue() + " °C");
                    }
                }

                // 4) Humidité
                Response<SensorData> humResp =
                        ApiClient.getApi().getHumidityHistory().execute();
                if (humResp.isSuccessful() && humResp.body() != null) {
                    SensorPoint last = humResp.body().getLastPoint();
                    if (last != null) {
                        newSensors.add(new Sensor("hum", "Humidité (%)", last.getValue()));
                        Log.d(TAG, "Humidity = " + last.getValue() + " %");
                    }
                }

                // 5) Tension batterie
                Response<SensorData> battResp =
                        ApiClient.getApi().getBatteryHistory().execute();
                if (battResp.isSuccessful() && battResp.body() != null) {
                    SensorPoint last = battResp.body().getLastPoint();
                    if (last != null) {
                        newSensors.add(new Sensor("batt", "Tension batterie (V)", last.getValue()));
                        Log.d(TAG, "Battery = " + last.getValue() + " V");
                    }
                }

                // Si aucun capteur remonté → fallback simulation
                if (newSensors.isEmpty()) {
                    Log.w(TAG, "Aucun capteur IoTLab, fallback simulation");
                    simulateSensorCheck();
                    return;
                }

                // Mise à jour DB
                database.sensorDao().insertAll(newSensors);

                // Détection des lumières (uniquement pour light1/light2)
                List<Sensor> oldActive = database.sensorDao().getActiveLights();
                TimeChecker timeChecker = new TimeChecker(this);
                TimeChecker.AlertType alertType = timeChecker.checkAlertType();

                for (Sensor sensor : newSensors) {
                    // on ne déclenche l’alerte que pour les capteurs de lumière
                    if ((sensor.getSensorId().equals("light1") ||
                            sensor.getSensorId().equals("light2"))
                            && sensor.isLightActive()
                            && !wasAlreadyActive(sensor, oldActive)) {

                        Log.d(TAG, "🔔 NOUVELLE LUMIÈRE : " + sensor.getName()
                                + " type=" + alertType);

                        if (alertType == TimeChecker.AlertType.NOTIFICATION) {
                            NotificationHelper.sendLightNotification(this, sensor);
                        } else if (alertType == TimeChecker.AlertType.EMAIL) {
                            NotificationCompat.Builder builder =
                                    new NotificationCompat.Builder(this, "light_alerts")
                                            .setContentTitle("📧 Email à envoyer")
                                            .setContentText("Lumière : " + sensor.getName())
                                            .setSmallIcon(android.R.drawable.stat_notify_error)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setAutoCancel(true);
                            NotificationManager manager = getSystemService(NotificationManager.class);
                            manager.notify(100 + sensor.getSensorId().hashCode(), builder.build());
                        }
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Erreur IoTLab → fallback simulation", e);
                simulateSensorCheck();
            }
        }).start();
    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
