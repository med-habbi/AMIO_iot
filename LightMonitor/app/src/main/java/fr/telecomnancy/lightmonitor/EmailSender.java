package fr.telecomnancy.lightmonitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import fr.telecomnancy.lightmonitor.models.Sensor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EmailSender {
    private static final String TAG = "EmailSender";

    public static void sendAlertEmail(Context context, Sensor sensor) {
        SharedPreferences prefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");

        if (email.isEmpty()) {
            Log.w(TAG, "Email non configuré");
            return;
        }

        String subject = "🚨 ALERTE LUMIÈRE - " + sensor.getName();
        String body = "Lumière détectée sur " + sensor.getName() +
                "\nLuminosité: " + String.format("%.0f lux", sensor.getLuminosity()) +
                "\nHeure: " + new SimpleDateFormat("dd/MM HH:mm", Locale.FRANCE).format(new Date());

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(emailIntent, "Envoyer alerte"));
            Log.d(TAG, "Email intent lancé pour " + sensor.getName());
        } catch (Exception e) {
            Log.e(TAG, "Erreur envoi email", e);
        }
    }
}
