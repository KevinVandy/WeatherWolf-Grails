package com.weatherwolf.weather

class SearchResult {

    Integer id
    Location location
    CurrentWeather currentWeather
    List<DayForecast> dayForecasts

    //static hasOne = [location: Location, currentWeather: CurrentWeather]
    static hasMany = [dayForecasts: DayForecast]

    static mapping = {
        version false
    }

    static constraints = {
        
    }
}
