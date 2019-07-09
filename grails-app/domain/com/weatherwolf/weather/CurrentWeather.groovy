package com.weatherwolf.weather

import com.weatherwolf.search.SearchResult


class CurrentWeather {

    Integer id
    String condition
    String icon
    Float temperature
    Short humidity
    Float windSpeed
    String windDirection

    static belongsTo = [searchResult: SearchResult]

    static mapping = {
        version false
    }

    static constraints = {
    }
}
