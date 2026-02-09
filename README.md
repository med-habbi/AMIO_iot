Surveillance IoTLab Nancy - Application Android
[
[
[

Application de surveillance temps réel des capteurs IoTLab Telecom Nancy (Grenoble node). Récupération des données via REST API + affichage moderne Material Design 3.

🎯 Fonctionnalités
Capteur	Type	Unité	Statut
Light 1	Luxmètre	lux	✅ Live
Light 2	Luxmètre	lux	✅ Live
Température	Thermomètre	°C	✅ Live
Humidité	Hygromètre	%	✅ Live
Battery Voltage	Batterie	V	✅ Live
Services actifs :

 MonitoringService (Foreground, 30s intervalle)

 Room Database (cache local)

 REST API IoTLab (iotlab.telecomnancy.eu:8080)

 Material You (thème dynamique)

🏗️ Architecture
text
MainActivity → RecyclerView (SensorAdapter)
         ↓
MonitoringService → fetchRealSensors() → RoomDB
         ↓
IoTLabApi → Retrofit2 → REST endpoints
Endpoints IoTLab utilisés
text
✅ GET /rest/data/1/light1/last
✅ GET /rest/data/1/light2/last  
✅ GET /rest/data/1/temperature/last
✅ GET /rest/data/1/humidity/last
✅ GET /rest/data/1/battery_Voltage/last
📱 Screenshots
Écran principal	Surveillance active	Capteurs IoTLab
🚀 Installation
Prérequis
text
Android Studio Koala | 2024.1.1
API Level 34 (Android 14)
VPN Telecom Nancy (iotlab.telecomnancy.eu)
Dépendances Gradle
text
// Core
implementation 'androidx.core:core-ktx:1.13.1'
implementation 'androidx.appcompat:appcompat:1.7.0'
implementation 'com.google.android.material:material:1.12.0'

// Network
implementation 'com.squareup.retrofit2:retrofit:2.11.0'
implementation 'com.squareup.retrofit2:converter-gson:2.11.0'

// Database
implementation 'androidx.room:room-runtime:2.6.1'
kapt 'androidx.room:room-compiler:2.6.1'

// RecyclerView
implementation 'androidx.recyclerview:recyclerview:1.3.2'
🔧 Configuration
API Base URL (SensorClient.java) :

java
BASE_URL = "http://iotlab.telecomnancy.eu:8080/iotlab/"
Room Database (SensorDatabase.java) :

java
@Database(entities = {Sensor.class}, version = 1)
Permissions (AndroidManifest.xml) :

xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
📊 Modèle de données
Sensor.java (Entity Room)
java
@Entity(tableName = "sensors")
public class Sensor {
    @PrimaryKey private String id;
    private String name;
    private float luminosity;
    private boolean lightActive;
    // getters/setters
}
SensorData.java (API Response)
java
public class SensorData {
    @SerializedName("data") private List<SensorPoint> data;
    public SensorPoint getLastPoint() {
        return data != null && !data.isEmpty() ? data.get(0) : null;
    }
}
🧪 Tests effectués (09/02/2026)
text
✅ Light1: 187.14 lux ✓
✅ Light2: 388.40 lux ✓  
✅ Température: 21.5°C ✓
✅ Humidité: 45.2% ✓
✅ Battery: 2.54V ✓
✅ Service persistant ✓
✅ Base Room nettoyée ✓
✅ Pas de capteurs simulés ✓
Latence moyenne : 1.2s (30s polling)

🔮 Roadmap Étape 2
text
[ ] Seuil configurable Light1 (SeekBar)
[ ] Notifications push (>500 lux)
[ ] Graphiques 24h (Chart.js)
[ ] Widget home screen
[ ] Export CSV
[ ] Dark/Light auto
👨‍💻 Auteur
HABBI Mohammed
BELFAYEZ Rayen

📄 Licence
MIT License - Free for educational & research use.

Testé sur Pixel 3A API 28 & Galaxy S23 Ultra API 34
