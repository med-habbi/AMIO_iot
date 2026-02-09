package fr.telecomnancy.lightmonitor;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.appbar.MaterialToolbar;
import fr.telecomnancy.lightmonitor.data.SensorDatabase;
import fr.telecomnancy.lightmonitor.models.Sensor;
import fr.telecomnancy.lightmonitor.ui.SensorAdapter;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SensorAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private SensorDatabase database;

    private Button toggleServiceButton;
    private boolean serviceRunning = false;


    // Méthode toggle



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleServiceButton = findViewById(R.id.toggleServiceButton);
        toggleServiceButton.setOnClickListener(v -> toggleService());

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = SensorDatabase.getInstance(this);
        new Thread(() -> database.sensorDao().deleteAll()).start();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SensorAdapter();
        recyclerView.setAdapter(adapter);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> {
            loadData();
            swipeRefresh.setRefreshing(false);
        });

        // Observer les changements de la base de données
        database.sensorDao().getAllSensors().observe(this, new Observer<List<Sensor>>() {
            @Override
            public void onChanged(List<Sensor> sensors) {
                adapter.setSensors(sensors);
            }
        });

        loadData();
    }

    private void toggleService() {
        Intent serviceIntent = new Intent(this, MonitoringService.class);
        if (serviceRunning) {
            stopService(serviceIntent);
            toggleServiceButton.setText("Démarrer Surveillance");
            serviceRunning = false;
            Toast.makeText(this, "Surveillance arrêtée", Toast.LENGTH_SHORT).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
            toggleServiceButton.setText("Arrêter Surveillance");
            serviceRunning = true;
            Toast.makeText(this, "Surveillance démarrée", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            //TODO : Étape 13 - Créer SettingsActivity
            startActivity(new Intent(this, SettingsActivity.class));
            //Toast.makeText(this, "Paramètres (bientôt)", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
