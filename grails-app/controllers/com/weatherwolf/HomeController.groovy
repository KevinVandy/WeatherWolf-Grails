package com.weatherwolf

import com.weatherwolf.security.User
import com.weatherwolf.val.WeatherUtils
import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder


class HomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    Authentication authentication
    String currentUsername
    User user

    def index() {
        def weatherService = new WeatherService()
        def searchResult = new SearchResult()
        searchResult.location = new Location()

        if (isLoggedIn()) {
            authentication = SecurityContextHolder.getContext().getAuthentication()
            currentUsername = authentication.getName()
            user = User.findByUsername(currentUsername)

            searchResult.location = new Location()
            searchResult.location = WeatherUtils.assignCityStateProvinceCountry(user.favoriteLocation, searchResult.location)
            weatherService.fillWeather(searchResult)
            if (searchResult.currentWeather && searchResult.dayForecasts && user.units == 'F') {
                logger.debug("converting temps to F")
                searchResult = WeatherUtils.convertTempsToF(searchResult)
            }
            logger.info("showing favorite weather")
            render(view: "/home/index", model: [searchResult: searchResult, user: user])
        } else {
            logger.info("User is not logged in")
            render(view: "/home/index")
        }
    }
}
