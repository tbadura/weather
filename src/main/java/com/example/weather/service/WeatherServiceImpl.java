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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


/**
 * Weather API implementation of weather service
 */
@Service
public class WeatherServiceImpl implements WeatherService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String API_KEY = ""; // key not provided, get one at weatherapi.com
    private static final int STATUS_OK = 200;

    /**
     * Get the weather current conditions
     *
     * @return Weather object or <code>null</code> if connection error
     * @throws RuntimeException if weather service returns error
     */
    @Override
    public Weather getWeather() {

        Weather weather = null;

        try {

            String endpoint = String.format("http://api.weatherapi.com/v1/current.json?key=%s&q=%s",
                    API_KEY, getPublicIP());

            logger.debug("*** Calling Weather API web service: " + endpoint);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != STATUS_OK) {
                logger.error("Failure on GET " + endpoint + " : Status Code: " + response.statusCode() + " : Body: " + response.body());
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
        }

        return weather;

    }

    private String getPublicIP() throws IOException {
        // Find public IP address

        URL url_name = new URL("http://bot.whatismyipaddress.com");

        BufferedReader br = new BufferedReader(new InputStreamReader(url_name.openStream()));

        return br.readLine().trim();

    }


}
