package org.pounter;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("City Name: ");
            String city = scanner.nextLine();

            try {
                String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1";

                // HTTP Request
                CloseableHttpClient httpClient = HttpClients.createDefault();
                JsonNode locationData = new ObjectMapper().readTree(
                        EntityUtils.toString(httpClient.execute(new HttpGet(url)).getEntity())
                );

                // Get
                double lat = locationData.get("results").get(0).get("latitude").asDouble();
                double lon = locationData.get("results").get(0).get("longitude").asDouble();

                // Weather Status Data
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?" +
                        "latitude=" + lat + "&longitude=" + lon +
                        "&current_weather=true&hourly=temperature_2m";

                JsonNode weatherData = new ObjectMapper().readTree(
                        EntityUtils.toString(httpClient.execute(new HttpGet(weatherUrl)).getEntity())
                );

                // Response
                System.out.println("\n--- " + city.toUpperCase() + " Weather ---");
                System.out.println("Heat: " + weatherData.get("current_weather").get("temperature") + "°C");
                System.out.println("Wind Speed: " + weatherData.get("current_weather").get("windspeed") + " km/h");
                System.out.println("Air Code: " + weatherData.get("current_weather").get("weathercode"));


            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
