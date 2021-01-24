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
    void serviceTest() {

        Weather weather = weatherService.getWeather();
        assertThat(weather).isNotNull();

    }


}
