package com.example.weather.controller;

import com.example.weather.model.Current;
import com.example.weather.model.Weather;
import com.example.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Handles requests for the application home page.
 */
@Controller
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping("/weather")
    public String home(Locale locale, Model model) {
        logger.info("Entering HomeController class. The client locale is {}.", locale);

        Weather weather = weatherService.getWeather();
        if (weather != null) {
            Current currentConditions = weather.getCurrent();

            model.addAttribute("location", weather.getLocation());
            model.addAttribute("current", currentConditions);

            LocalDateTime localDateTime =
                    LocalDateTime.ofEpochSecond(currentConditions.getLast_updated_epoch(), 0, OffsetDateTime.now().getOffset());

            DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("h:mm a");
            model.addAttribute("lastUpdatedTime", FORMATTER.format(localDateTime));
        }

        return "weather";
    }


}