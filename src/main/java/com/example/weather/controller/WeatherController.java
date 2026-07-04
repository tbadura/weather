package com.example.weather.controller;

import com.example.weather.model.Current;
import com.example.weather.model.Weather;
import com.example.weather.service.WeatherService;
import javax.servlet.http.HttpServletRequest;
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
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping("/weather")
    public String home(Locale locale, Model model, HttpServletRequest request) {
        logger.info("Entering HomeController class. The client locale is {}.", locale);

        // Tomcat's RemoteIpValve automatically extracts the true, validated client IP.
        // It updates the request object so getRemoteAddr() returns the safe IP directly.
        String clientIp = request.getRemoteAddr();

        // Pass the clientIp string to the weather service
        Weather weather = weatherService.getWeather(clientIp);

        if (weather != null) {
            Current currentConditions = weather.getCurrent();
            model.addAttribute("location", weather.getLocation());
            model.addAttribute("current", currentConditions);

            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(
                    currentConditions.getLast_updated_epoch(), 0, OffsetDateTime.now().getOffset()
            );
            DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("h:mm a");
            model.addAttribute("lastUpdatedTime", FORMATTER.format(localDateTime));
        }
        return "weather";
    }
}