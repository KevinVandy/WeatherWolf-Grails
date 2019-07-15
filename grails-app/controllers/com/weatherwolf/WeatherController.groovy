package com.weatherwolf

import com.weatherwolf.val.WeatherUtils
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
            searchResult.location = WeatherUtils.assignCityStateProvinceCountry((params.location).toString().trim(), searchResult.location)
            weatherService.fillWeather(searchResult)
            if (searchResult.currentWeather && searchResult.dayForecasts && params.units) {
                if(params.units == 'F') {
                    logger.debug("converting temps to F")
                    searchResult = WeatherUtils.convertTempsToF(searchResult)
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
}
