package com.weatherwolf

import com.weatherwolf.security.User
import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.core.context.SecurityContextHolder


class HomeController {

    static allowedMethods = [
            index: 'GET'
    ]

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    String currentUsername
    User user

    @Secured("permitAll")
    def index() {
        def weatherService = new WeatherService()
        def searchResult = new SearchResult()
        searchResult.location = new Location()

        if (!isLoggedIn()) {
            logger.info("User is not logged in")
            render(view: "/home/index")
        } else { //if is logged in, show custom info
            currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
            user = User.findByUsername(currentUsername)
            LocaleContextHolder.setLocale(new Locale(user.lang)) //load language from user's settings
            searchResult.location = new Location()
            searchResult.location = WeatherUtils.assignCityStateProvinceCountry(user.favoriteLocation, searchResult.location)
            weatherService.fillWeather(searchResult)
            if (searchResult.currentWeather && searchResult.dayForecasts && user.units == 'F') {
                logger.debug("converting temps to F")
                searchResult = WeatherUtils.convertTempsToF(searchResult)
            }
            logger.info("showing favorite weather")
            render(view: "/home/index", model: [searchResult: searchResult, user: user])
        }
    }
}
