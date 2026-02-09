package fr.telecomnancy.lightmonitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

public class TimeChecker {
    private SharedPreferences prefs;

    public TimeChecker(Context context) {
        prefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
    }

    public AlertType checkAlertType() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        boolean isWeekend = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);

        // Parser les heures (HH:mm -> heure)
        int weekdayStart = parseHour(prefs.getString("weekday_start", "19:00"));
        int weekdayEnd = parseHour(prefs.getString("weekday_end", "23:00"));

        Log.d("TimeChecker", "Heure: " + hour + " Weekend: " + isWeekend +
                " Plage semaine: " + weekdayStart + "-" + weekdayEnd);

        // Semaine 19h-23h → Notification
        if (!isWeekend && hour >= weekdayStart && hour < weekdayEnd) {
            return AlertType.NOTIFICATION;
        }
        // Weekend 19h-23h OU Nuit 23h-6h → Email
        else if ((isWeekend && hour >= weekdayStart && hour < weekdayEnd) ||
                hour >= 23 || hour < 6) {
            return AlertType.EMAIL;
        }

        return AlertType.NONE;
    }

    private int parseHour(String timeStr) {
        try {
            return Integer.parseInt(timeStr.split(":")[0]);
        } catch (Exception e) {
            return 19; // Default
        }
    }

    public enum AlertType {
        NOTIFICATION, EMAIL, NONE
    }
}
