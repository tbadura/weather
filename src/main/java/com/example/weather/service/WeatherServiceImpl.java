package com.example.weather.service;

import com.example.weather.model.Weather;
import com.example.weather.model.WeatherError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cedarsoftware.util.io.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.io.IOException;

/**
 * Weather API implementation of weather service
 */
@Service
public class WeatherServiceImpl implements WeatherService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String API_KEY = ""; // key not provided, get one at weatherapi.com
    private static final int STATUS_OK = 200;

    /**
     * Get the weather current conditions
     *
     * @return Weather object or <code>null</code> if connection error
     * @throws RuntimeException if weather service returns error in JSON response body
     */
    @Override
    public Weather getWeather(String clientIp) {
        Weather weather = null;
        try {
            // If the application receives a local network IP, look up your router's actual public IP
            if (isLocalOrPrivateIp(clientIp)) {
                logger.info("Local/Private IP detected [{}]. Resolving public outbound IP...", clientIp);
                clientIp = fetchPublicIp();
            }

            String endpoint = String.format("https://api.weatherapi.com/v1/current.json?key=%s&q=%s", API_KEY, clientIp);
            logger.debug("*** Calling Weather API web service: {}", endpoint);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != STATUS_OK) {
                logger.error("Failure on GET {} : Status Code: {} : Body: {}", endpoint, response.statusCode(), response.body());
                ObjectMapper om = new ObjectMapper();
                WeatherError weatherError = om.readValue(response.body(), WeatherError.class);
                String serviceErrorMessage = weatherError.getError().getMessage();
                throw new RuntimeException("Error returned by weather service: " + serviceErrorMessage);
            }

            String jsonText = JsonWriter.formatJson(response.body());
            logger.debug(jsonText);

            ObjectMapper om = new ObjectMapper();
            weather = om.readValue(jsonText, Weather.class);

        } catch (IOException | InterruptedException e) {
            logger.error("Error connecting to weather service: ", e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); // Restore interrupted status safely
            }
        }
        return weather;
    }

    /**
     * Checks if an IP string is loopback (127.0.0.1, ::1) or private LAN range (192.168.x.x, 10.x.x.x, 172.16-31.x.x)
     */
    private boolean isLocalOrPrivateIp(String ip) {
        if (ip == null || ip.isBlank()) {
            return true;
        }
        String cleanIp = ip.trim();
        return cleanIp.equals("127.0.0.1") ||
                cleanIp.equals("0:0:0:0:0:0:0:1") ||
                cleanIp.startsWith("192.168.") ||
                cleanIp.startsWith("10.") ||
                cleanIp.matches("^172\\.(1[6-9]|2[0-9]|3[0-1])\\..*");
    }

    /**
     * Synchronously fetches the public WAN IP of your local testing environment
     */
    private String fetchPublicIp() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.ipify.org"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == STATUS_OK) {
                return response.body().trim();
            }
        } catch (Exception e) {
            logger.warn("Failed to automatically discover public IP, falling back to 'auto:ip'", e);
        }
        return "auto:ip"; // Safe fallback that lets Weather API detect your local network's real public IP
    }
}
