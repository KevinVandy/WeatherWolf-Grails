package com.weatherwolf

import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult


class WeatherUtils {

    static def assignCityStateProvinceCountry(String searchString, Location location) {
        String[] searchStringArray = searchString.split(",")
        if (searchStringArray.length == 2) {
            location.city = searchStringArray[0].trim()
            location.country = searchStringArray[1].trim()
        } else if (searchStringArray.length == 3) {
            location.city = searchStringArray[0].trim()
            location.stateProvince = searchStringArray[1].trim()
            location.country = searchStringArray[2].trim()
        } else {
            location.city = searchString
        }
        location
    }

    static def convertTempsToF(SearchResult sr) {
        sr.currentWeather.temperature = (Float) toF(sr.currentWeather.temperature)
        sr.dayForecasts.each {
            it.minTemp = (Float) toF(it.minTemp)
            it.maxTemp = (Float) toF(it.maxTemp)
        }
        sr
    }

    static toF(Float c) {
        (Float) c * (9 / 5) + 32
    }
}
