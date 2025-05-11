package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherApp {

    // This method gets an array from an object using a key
    public static JSONArray getArray(JSONObject obj, String key) {
        return obj.getJSONArray(key);
    }

    public static HashMap<Integer, String> setWeatjerHashMap() {
        HashMap<Integer, String> weatherCodeHashMap = new HashMap<Integer, String>();

        weatherCodeHashMap.put(0, "Clear sky");
        weatherCodeHashMap.put(1, "Mainly clear");
        weatherCodeHashMap.put(2, "Partly cloudy");
        weatherCodeHashMap.put(3, "Overcast");
        weatherCodeHashMap.put(45, "Fog");
        weatherCodeHashMap.put(48, "Depositing rime fog");
        weatherCodeHashMap.put(51, "Drizzle: Light");
        weatherCodeHashMap.put(53, "Drizzle: Moderate");
        weatherCodeHashMap.put(55, "Drizzle: Dense");
        weatherCodeHashMap.put(56, "Freezing Drizzle: Light");
        weatherCodeHashMap.put(57, "Freezing Drizzle: Dense");
        weatherCodeHashMap.put(61, "Rain: Slight");
        weatherCodeHashMap.put(63, "Rain: Moderate");
        weatherCodeHashMap.put(65, "Rain: Heavy");
        weatherCodeHashMap.put(66, "Freezing Rain: Light");
        weatherCodeHashMap.put(67, "Freezing Rain: Heavy");
        weatherCodeHashMap.put(71, "Snow fall: Slight");
        weatherCodeHashMap.put(73, "Snow fall: Moderate");
        weatherCodeHashMap.put(75, "Snow fall: Heavy");
        weatherCodeHashMap.put(77, "Snow fall: Snow grains");
        weatherCodeHashMap.put(80, "Rain showers: Slight");
        weatherCodeHashMap.put(81, "Rain showers: Moderate");
        weatherCodeHashMap.put(82, "Rain showers: Violent");
        weatherCodeHashMap.put(85, "Snow showers slight");
        weatherCodeHashMap.put(86, "Snow showers heavy");

        return weatherCodeHashMap;
    }

    // Throw exception if error happens in main
    public static void main(String[] args) throws IOException {
        // Set the Api url
        URL url = new URL(
                "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&daily=temperature_2m_max,temperature_2m_min,weather_code&hourly=temperature_2m&temperature_unit=fahrenheit");

        // Connect to the api with a get request
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Get the result and store it in a string
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        // Disconnect from the api
        con.disconnect();

        // Change the result from a string to a JSON OBJECT
        JSONObject jsonObject = new JSONObject(content.toString());
        // Grab the min and max temperature for the next 7 days
        JSONObject weatherdata = jsonObject.getJSONObject("daily");

        // Store the min max temperature and the date in seprate json arrays
        JSONArray tempMax = getArray(weatherdata, "temperature_2m_max");
        JSONArray tempMin = getArray(weatherdata, "temperature_2m_min");
        JSONArray date = getArray(weatherdata, "time");
        JSONArray weatherCode = getArray(weatherdata, "weather_code");

        // Make a hashmap for all the weather codes
        HashMap<Integer, String> weatherCodeHashMap = setWeatjerHashMap();

        // Set a format to change a string date into the weekday names
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        // Print out the header for the forecast
        System.out.println("Here is the 7 day forecast:");

        // Loop through the arrays to display date with temperatures and weather
        // conditions
        for (int i = 0; i < tempMax.length(); i++) {
            // Get the weekdays in short from from the date array
            String day = LocalDate.parse(date.opt(i).toString(), formatter).getDayOfWeek()
                    .getDisplayName(TextStyle.SHORT, Locale.getDefault());
            System.out.println(day + ": " + tempMax.opt(i) + "°F " + tempMin.opt(i) + "°F "
                    + weatherCodeHashMap.get(weatherCode.opt(i)));
        }
    }
}