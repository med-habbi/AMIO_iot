package fr.telecomnancy.lightmonitor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private EditText emailEditText, weekdayStartEditText, weekdayEndEditText;
    private EditText weekendStartEditText, weekendEndEditText;
    private EditText nightStartEditText, nightEndEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Récupérer les vues
        emailEditText = findViewById(R.id.emailEditText);
        weekdayStartEditText = findViewById(R.id.weekdayStartEditText);
        weekdayEndEditText = findViewById(R.id.weekdayEndEditText);
        weekendStartEditText = findViewById(R.id.weekendStartEditText);
        weekendEndEditText = findViewById(R.id.weekendEndEditText);
        nightStartEditText = findViewById(R.id.nightStartEditText);
        nightEndEditText = findViewById(R.id.nightEndEditText);

        // Charger les paramètres sauvegardés
        loadSettings();

        // Bouton sauvegarder
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveSettings());
    }

    private void loadSettings() {
        emailEditText.setText(prefs.getString("email", ""));
        weekdayStartEditText.setText(prefs.getString("weekday_start", "19:00"));
        weekdayEndEditText.setText(prefs.getString("weekday_end", "23:00"));
        weekendStartEditText.setText(prefs.getString("weekend_start", "19:00"));
        weekendEndEditText.setText(prefs.getString("weekend_end", "23:00"));
        nightStartEditText.setText(prefs.getString("night_start", "23:00"));
        nightEndEditText.setText(prefs.getString("night_end", "06:00"));
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("weekday_start", weekdayStartEditText.getText().toString());
        editor.putString("weekday_end", weekdayEndEditText.getText().toString());
        editor.putString("weekend_start", weekendStartEditText.getText().toString());
        editor.putString("weekend_end", weekendEndEditText.getText().toString());
        editor.putString("night_start", nightStartEditText.getText().toString());
        editor.putString("night_end", nightEndEditText.getText().toString());
        editor.apply();

        Toast.makeText(this, "✅ Paramètres sauvegardés !", Toast.LENGTH_LONG).show();
        finish();
    }
}
