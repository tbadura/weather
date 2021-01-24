package com.example.weather.service;

import com.example.weather.model.Weather;

/**
 * Weather Service interface.
 */
public interface WeatherService {

    /**
     * Get the current weather conditions
     *
     *
     * @return Weather object or <code>null</code> if connection error
     */
    Weather getWeather();

}

