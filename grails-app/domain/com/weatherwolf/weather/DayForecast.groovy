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

    //static belongsTo = [searchResult: SearchResult]

    static mapping = {
        version false
    }

    static constraints = {
    }
}
