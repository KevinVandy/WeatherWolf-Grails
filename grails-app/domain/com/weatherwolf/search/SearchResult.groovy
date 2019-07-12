package com.weatherwolf.search

import com.weatherwolf.weather.CurrentWeather
import com.weatherwolf.weather.DayForecast
import com.weatherwolf.security.User


class SearchResult {

    Integer id
    Date dateSearched
    Location location
    CurrentWeather currentWeather
    List<DayForecast> dayForecasts

    static hasOne = [location: Location, currentWeather: CurrentWeather]
    static hasMany = [dayForecasts: DayForecast]

    static mapping = {
        version false
    }

    static constraints = {
        
    }
}
