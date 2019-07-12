package com.weatherwolf

import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class WeatherController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())

    def index() {
        def weatherService = new WeatherService()
        def searchResult = new SearchResult()
        searchResult.location = new Location()
        if (params.location) {
            searchResult.location = assignCityStateProvinceCountry((params.location).toString().trim(), searchResult.location)
            weatherService.fillWeather(searchResult)
            if (searchResult.currentWeather && searchResult.dayForecasts && params.units) {
                if(params.units == 'F') {
                    logger.debug("converting temps to F")
                    searchResult = convertTempsToF(searchResult)
                }
            } else {
                logger.warn("Not a valid search")
            }
        } else {
            logger.warn("no location to search")
            searchResult = new SearchResult()
        }

        render(view: '/weather/index', model: [searchResult: searchResult])
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
