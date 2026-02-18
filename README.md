Surveillance IoTLab Nancy - Application Android

Application de surveillance temps réel des capteurs IoTLab Telecom Nancy. Récupération des données via REST API + affichage moderne.

🎯 Fonctionnalités
| Capteur         | Type        | Unité | Statut |
| --------------- | ----------- | ----- | ------ |
| Light 1         | Luxmètre    | lux   | ✅ Live |
| Light 2         | Luxmètre    | lux   | ✅ Live |
| Température     | Thermomètre | °C    | ✅ Live |
| Humidité        | Hygromètre  | %     | ✅ Live |
| Battery Voltage | Batterie    | V     | ✅ Live |

Services actifs :

✅MonitoringService (Foreground, 30s intervalle)

✅Room Database (cache local)

✅REST API IoTLab (iotlab.telecomnancy.eu:8080)

Endpoints IoTLab utilisés:

✅ GET /rest/data/1/light1/last

✅ GET /rest/data/1/light2/last  

✅ GET /rest/data/1/temperature/last

✅ GET /rest/data/1/humidity/last

✅ GET /rest/data/1/battery_Voltage/last

📱 Screenshots
Écran principal:
![page principale](/LightMonitor/img1.png)



Surveillance active:
![surveillance active](/LightMonitor/img2.png)

Paramètres:
![Paramètres](/LightMonitor/img3.png)

🚀 Installation
Prérequis

[Android Studio](https://developer.android.com/studio)

API Level 34 (Android 14)

[VPN Telecom Nancy](http://iotlab.telecomnancy.eu:8080/)


```
Dépendances Gradle

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


```

🔧 Configuration

API Base URL (SensorClient.java) :

```java
BASE_URL = "http://iotlab.telecomnancy.eu:8080/iotlab/"

```
```xml

<uses-permission android:name="android.permission.INTERNET" />

<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

🧪 Tests effectués (09/02/2026)

✅ Light1: 187.14 lux ✓

✅ Light2: 388.40 lux ✓  

✅ Température: 21.5°C ✓

✅ Humidité: 45.2% ✓

✅ Battery: 2.54V ✓

✅ Service persistant ✓

✅ Base Room nettoyée ✓

✅ Pas de capteurs simulés ✓

Latence moyenne : 1.2s (30s polling)

👨‍💻 Auteur
HABBI Mohammed
BELFAYEZ Rayen

📄 Licence
MIT License - Free for educational & research use.

Testé sur Pixel 3A API 28 & Galaxy S23 Ultra API 34
