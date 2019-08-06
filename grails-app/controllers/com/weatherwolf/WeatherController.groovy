package com.weatherwolf

import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder


@Secured("permitAll")
class WeatherController {

    static allowedMethods = [
            index: 'GET' //needs url parameters to search
    ]

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    private def currentUser = new User()

    private User refreshCurrentUser() {
        currentUser = User.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
    }

    /** GET only
     *
     * Performs a search for weather using the weather service.
     * Shows the current weather and forecast for a search result.
     * Logs a search log if the user is logged in. Log!
     *
     * @return
     */
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
            refreshCurrentUser()
            try {
                SearchLog sl = new SearchLog(searchString: params.location, date: new Date(), user: currentUser)
                sl.save(flush: true, failOnError: true)
            } catch (Exception e) {
                logger.error(e.toString())
            }
            render(view: '/weather/index', model: [searchResult: searchResult, user: currentUser])
        }
    }
}
