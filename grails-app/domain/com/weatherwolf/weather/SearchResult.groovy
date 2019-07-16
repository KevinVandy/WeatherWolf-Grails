package com.weatherwolf.weather

class SearchResult {

    Integer id
    Location location
    CurrentWeather currentWeather
    List<DayForecast> dayForecasts

    static mapping = {
        version false
    }

    static constraints = {
        
    }
}
