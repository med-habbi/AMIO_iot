package fr.telecomnancy.lightmonitor.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import android.graphics.Color;
import java.util.Locale;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import fr.telecomnancy.lightmonitor.R;
import fr.telecomnancy.lightmonitor.models.Sensor;
import java.util.ArrayList;
import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {
    private List<Sensor> sensors = new ArrayList<>();

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sensor, parent, false);
        return new SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        Sensor sensor = sensors.get(position);
        holder.bind(sensor);
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
        notifyDataSetChanged();
    }

    static class SensorViewHolder extends RecyclerView.ViewHolder {
        private TextView sensorName, luminosity, status;
        private ImageView statusIcon;
        private MaterialCardView cardView;

        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);
            sensorName = itemView.findViewById(R.id.sensorName);
            luminosity = itemView.findViewById(R.id.luminosity);
            status = itemView.findViewById(R.id.status);
            statusIcon = itemView.findViewById(R.id.statusIcon);
            cardView = itemView.findViewById(R.id.cardView);
        }

        public void bind(Sensor sensor) {
            // Nom du capteur
            sensorName.setText(sensor.getName());

            // Choix de l'unité en fonction de l'id du capteur
            String unit = " lux";
            String statusText = "";
            int bgColor;
            int valueColor;

            String id = sensor.getSensorId();  // "light1", "light2", "temp", "hum", "batt", etc.

            if (id.startsWith("light")) {
                unit = " lux";

                if (sensor.isLightActive()) {
                    statusText = "⚠️ Lumière active détectée";
                    bgColor = Color.parseColor("#FFEBEE");      // rouge très clair
                    valueColor = Color.parseColor("#D32F2F");   // rouge
                    statusIcon.setImageResource(android.R.drawable.presence_busy);
                } else {
                    statusText = "✓ Normal";
                    bgColor = Color.parseColor("#E8F5E9");      // vert très clair
                    valueColor = Color.parseColor("#388E3C");   // vert
                    statusIcon.setImageResource(android.R.drawable.presence_online);
                }
            } else if (id.equals("temp")) {
                unit = " °C";
                statusText = "Capteur de température";
                bgColor = Color.parseColor("#E3F2FD");          // bleu clair
                valueColor = Color.parseColor("#1976D2");       // bleu
                statusIcon.setImageResource(android.R.drawable.ic_menu_compass);
            } else if (id.equals("hum")) {
                unit = " %";
                statusText = "Capteur d'humidité";
                bgColor = Color.parseColor("#FFF3E0");          // orange clair
                valueColor = Color.parseColor("#F57C00");       // orange
                statusIcon.setImageResource(android.R.drawable.ic_menu_gallery);
            } else if (id.equals("batt")) {
                unit = " V";
                statusText = "Tension de la batterie";
                bgColor = Color.parseColor("#F3E5F5");          // violet clair
                valueColor = Color.parseColor("#7B1FA2");       // violet
                statusIcon.setImageResource(android.R.drawable.ic_lock_idle_charging);
            } else {
                // Cas générique
                unit = "";
                statusText = "Capteur";
                bgColor = Color.WHITE;
                valueColor = Color.DKGRAY;
                statusIcon.setImageResource(android.R.drawable.presence_invisible);
            }

            // Valeur + unité
            luminosity.setText(String.format(Locale.FRANCE, "%.2f%s", sensor.getLuminosity(), unit));

            // Couleurs et texte de statut
            cardView.setCardBackgroundColor(bgColor);
            luminosity.setTextColor(valueColor);
            status.setText(statusText);
        }

    }
}
