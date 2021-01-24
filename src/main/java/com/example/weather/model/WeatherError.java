package com.example.weather.model;

/**
 * Jackson-JSON deserialized root weather error object
 */
public class WeatherError {
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
