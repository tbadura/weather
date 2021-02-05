<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Weather</title>

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
    <div class="container">
        <h2>The current weather conditions <c:if test="${not empty location}">at ${location.name} ${location.region}</c:if></h2>
        <c:choose>
            <c:when test="${not empty current}">
                <div>
                    <img style="vertical-align:middle" src="${current.condition.icon}" alt="">
                    <span style='font-size: x-large;'>${current.condition.text}</span>
                </div>
                <pre>
            Temperature: ${current.temp_f} F
            Feels like: ${current.feelslike_f} F
            Wind: ${current.wind_mph} mph ${current.wind_dir}
            Humidity: ${current.humidity}%
            Pressure (inches): ${current.pressure_in}
            Precipitation (inches): ${current.precip_in}

            Last updated at ${lastUpdatedTime}
                </pre>
            </c:when>
            <c:otherwise>
                <p>Unable to get current weather conditions at this moment. Please try again.</p>
            </c:otherwise>
        </c:choose>
        <hr/>
            Powered by <a href="https://www.weatherapi.com/" title="Weather API">WeatherAPI.com</a>
    </div>
</body>
</html>
