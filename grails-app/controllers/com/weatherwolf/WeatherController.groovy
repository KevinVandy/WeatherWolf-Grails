package com.weatherwolf

import com.weatherwolf.search.Location
import com.weatherwolf.search.SearchResult
import grails.plugin.springsecurity.annotation.Secured


class WeatherController {

    def index() {

    }

    def result() {
        def weatherService = new WeatherService()
        def searchResult = new SearchResult()
        searchResult.location = new Location()
        if (params.location) {
            searchResult.location = assignCityStateProvinceCountry((params.location).toString().trim(), searchResult.location)
            weatherService.fillWeather(searchResult)
        } else {
            println("no location to search")
        }

        if (params.units && params.units == 'F') {
            searchResult = convertTempsToF(searchResult)
        }

        searchResult.save() //log to db
        render(view: '/weather/result', model: [searchResult: searchResult])
    }

    private def assignCityStateProvinceCountry(String searchString, Location location) {
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
        println(location.toString())
        location
    }

    private def convertTempsToF(SearchResult sr) {
        sr.currentWeather.temperature = toF(sr.currentWeather.temperature)
        sr.dayForecasts.each {
            it.minTemp = toF(it.minTemp)
            it.maxTemp = toF(it.maxTemp)
        }
        sr
    }

    private def toF(Float c) {
        c * (9 / 5) + 32
    }
}
