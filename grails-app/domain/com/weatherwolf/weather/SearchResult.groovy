package com.weatherwolf.weather

class SearchResult {

    Integer id
    static hasOne = [currentWeather: CurrentWeather, location : Location]
    static hasMany = [dayForecasts: DayForecast]

    static mapping = {
        version false
    }

    static constraints = {
        location nullable: false
        currentWeather nullable: true
        dayForecasts nullable: true
    }
}
