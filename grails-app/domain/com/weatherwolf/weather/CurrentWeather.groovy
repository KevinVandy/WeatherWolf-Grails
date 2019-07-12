package com.weatherwolf.weather

class CurrentWeather {

    Integer id
    String condition
    String icon
    Float temperature
    Short humidity
    Float windSpeed
    String windDirection

    //static belongsTo = [searchResult: SearchResult]

    static mapping = {
        version false
    }

    static constraints = {
    }
}
