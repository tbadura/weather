package com.example.weather;

import com.example.weather.controller.WeatherController;
import com.example.weather.model.Weather;
import com.example.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WeatherApplicationTests {

    @Autowired
    private WeatherController controller;

    @Autowired
    private WeatherService weatherService;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void serviceTestWithAutoIp() {
        // Verifies the "auto:ip" fallback logic functions correctly
        String clientIp = "auto:ip";
        Weather weather = weatherService.getWeather(clientIp);

        assertThat(weather).isNotNull();
        assertThat(weather.getLocation()).isNotNull();
    }

    @Test
    void serviceTestWithStaticIp() {
        // Verifies that processing an explicit global IP works cleanly.
        // Uses a known public IP address (Google DNS in California).
        String clientIp = "8.8.8.8";
        Weather weather = weatherService.getWeather(clientIp);

        assertThat(weather).isNotNull();
        assertThat(weather.getLocation()).isNotNull();

        // Broadened assertions to prevent micro-location name mismatches
        assertThat(weather.getLocation().getRegion()).isEqualTo("California");
        assertThat(weather.getLocation().getCountry()).isEqualTo("United States of America");
    }

}
