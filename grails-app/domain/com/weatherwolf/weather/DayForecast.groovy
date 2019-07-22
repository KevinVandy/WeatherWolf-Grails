package com.weatherwolf.weather

class DayForecast {

    Integer id
    Date date
    String condition
    String iconURL
    Float minTemp
    Float maxTemp
    Float precipitation
    Float windSpeed

    static belongsTo = [searchResult: SearchResult]

    static mapping = {
        version false
        condition column: 'weather_condition'
    }

    static constraints = {
        date min: new Date()
        condition blank: false
        iconURL blank: false
        minTemp min: -100F, max: 200F
        maxTemp min: -100F, max: 200F
        precipitation min: 0F, max: 100F
        windSpeed min: 0F, max: 500F
    }
}
