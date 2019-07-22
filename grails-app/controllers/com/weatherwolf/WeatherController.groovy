package com.weatherwolf

import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder


class WeatherController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    String currentUsername
    User user

    @Secured("permitAll")
    def index() {
        def weatherService = new WeatherService()
        def searchResult = new SearchResult()
        searchResult.location = new Location()
        if (!params.location) {
            logger.warn("no location to search")
            searchResult = new SearchResult()
        } else {
            searchResult.location = WeatherUtils.assignCityStateProvinceCountry((params.location).toString().trim(), searchResult.location)
            weatherService.fillWeather(searchResult)
            if (!searchResult.currentWeather || !searchResult.dayForecasts) {
                logger.warn("Not a valid search")
            } else if (params.units && params.units == 'F') {
                logger.debug("converting temps to F")
                searchResult = WeatherUtils.convertTempsToF(searchResult)
            }
        }
        if (!isLoggedIn()) {
            render(view: '/weather/index', model: [searchResult: searchResult])
        } else {
            currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
            user = User.findByUsername(currentUsername)
            SearchLog sl = new SearchLog(searchString: params.location, date: new Date(), user: user)
            sl.save(flush: true, failOnError: true)
            render(view: '/weather/index', model: [searchResult: searchResult, user: user])
        }
    }
}
