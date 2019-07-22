package com.weatherwolf.weather

class CurrentWeather {

    Integer id
    String condition
    String icon
    Float temperature
    Integer humidity
    Float windSpeed
    String windDirection

    static belongsTo = [searchResult: SearchResult]

    static mapping = {
        version false
        condition column: 'weather_condition'
    }

    static constraints = {
        condition blank: false
        icon blank: false
        temperature min: -100F, max: 200F
        humidity min: 0, max: 100
        windSpeed min: 0F, max: 500F
        windDirection blank: false
    }
}
