# weather

This web application will display current local weather conditions returned from [Weather API](https://www.weatherapi.com/). The source code is written in Java and created with
Spring Boot.

If you want to run this application, you will need to [sign up](https://www.weatherapi.com/signup.aspx) for your own Weather API Key. The [free service](https://www.weatherapi.com/pricing.aspx) allows for 1,000,000 API calls per month. Then set your environment variable to use the new API key. 

For Windows, press the Windows Key, type environment variables, and select Edit the system environment variables. Create a new variable called WEATHER_API_KEY and assign it your API key.

For Linux, use export instead:

```
export WEATHER_API_KEY=your_key
```

It is also possible to set the environment variable in the command line to start the application:

```
java -jar target/weather-1.0.0-SNAPSHOT.war --weather.api.key=your_key
```







