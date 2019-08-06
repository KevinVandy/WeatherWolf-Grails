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

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    def weatherService
    User currentUser

    private User refreshCurrentUser() {
        currentUser = User.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
    }

    /**
     * The landing spot for a successful login.
     * If there is a forgotPasswordToken for the user, then it gets deleted here to prevent security risk
     * Set's the logged in user's language from their settings into the current session.
     *
     * @redirect /home?lang&units
     */
    @Secured(['ROLE_CLIENT', 'ROLE_ADMIN'])
    def success() {
        refreshCurrentUser()
        if (currentUser.forgotPasswordToken) {
            try {
                currentUser.forgotPasswordToken = null
                currentUser.save(flush: true, failOnError: true)
            } catch (Exception e) {
                logger.warn("Could not wipe forgot password token")
            }
        }
        LocaleContextHolder.setLocale(new Locale(currentUser.lang))
        redirect(url: "/home?lang=${currentUser.lang}&units=${currentUser.units}")
    }

    /**
     * Shows the home page for the application.
     * A user can be logged in or not logged in.
     * If a user is logged in, a weather report for their favorite location will be displayed below the search bar.
     * If a user is not logged in, a login form will be displayed below the search bar instead.
     *
     * @render /home/index with or without info for a logged in user
     */
    @Secured("permitAll")
    def index() {
        def searchResult = new SearchResult()
        searchResult.location = new Location()
        if (!isLoggedIn()) {
            logger.debug("User is not logged in")
            render(view: "/home/index")
        } else { //if is logged in, show custom info
            refreshCurrentUser()
            LocaleContextHolder.setLocale(new Locale(currentUser.lang)) //load language from user's settings
            searchResult.location = new Location()
            searchResult.location = WeatherUtils.assignCityStateProvinceCountry(currentUser.favoriteLocation, searchResult.location)
            weatherService.fillWeather(searchResult)
            if (searchResult.currentWeather && searchResult.dayForecasts && currentUser.units == 'F') {
                logger.debug("converting temps to F")
                searchResult = WeatherUtils.convertTempsToF(searchResult)
            }
            logger.info("showing favorite weather to ${currentUser.username}")
            render(view: "/home/index", model: [searchResult: searchResult, user: currentUser])
        }
    }
}
