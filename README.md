# Wireless Network Sniffer

## Overview
This Android smartphone application periodically measures WiFi and Cellular network availability and quality without user intervention. The collected data is stored in an SQLite database for further analysis. 

## Details
### Background Service
The application runs as a background service and collects WiFi and Cellular network data every 1 minute.
### WiFi Data Collection
- Uses the WiFiManager Android API to scan for all surrounding WiFi access points.
- Gathers geotagged information about each encountered WiFi access point:
  - BSSID
  - SSID
  - Frequency
  - Channel width
  - RSSI level
- Collects latitude and longitude of the scan location.
### Cellular Data Collection
- Utilizes the Telephony API to scan available surrounding cells.
- Gathers geotagged information about each cell in the vicinity:
  - Cell ID
  - RSSI
  - Technology (2G, 3G, 4G, 5G)
  - Operational frequency
- Collects latitude and longitude of the scan location.

## Usage
1. Installation
    - Clone or download the project repository.
    - Open the project in Android Studio.
2. Running the App
    - Build and run the app on any Android device.
    - Use buttons to control what data is collected at any given time.
      - Initiate a network scan by clicking the respective "Start" button for Wifi and Cellular scans.
      - Stop the scanning process by clicking the "Stop" button.
3. Viewing Data
    - Collected data (WiFi and Cellular network information) is stored locally in an SQLite database.
    - To access collected data, use the "App Inspection" feature of Android Studio for database exploration and analysis.
