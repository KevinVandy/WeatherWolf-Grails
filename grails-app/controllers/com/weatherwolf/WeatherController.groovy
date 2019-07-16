package com.weatherwolf

import com.weatherwolf.security.User
import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder


class WeatherController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    String currentUsername
    User user

    def index() {
        def weatherService = new WeatherService()
        def searchResult = new SearchResult()
        searchResult.location = new Location()
        if (params.location) {
            searchResult.location = WeatherUtils.assignCityStateProvinceCountry((params.location).toString().trim(), searchResult.location)
            weatherService.fillWeather(searchResult)
            if (searchResult.currentWeather && searchResult.dayForecasts && params.units) {
                if (params.units == 'F') {
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
        if (isLoggedIn()) {
            currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
            user = User.findByUsername(currentUsername)
            render(view: '/weather/index', model: [searchResult: searchResult, user: user])
        } else {
            render(view: '/weather/index', model: [searchResult: searchResult])
        }

    }
}
