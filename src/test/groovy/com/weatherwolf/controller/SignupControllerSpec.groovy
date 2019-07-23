package com.weatherwolf.controller

import com.weatherwolf.SignupController
import com.weatherwolf.security.EmailLog
import com.weatherwolf.security.Role
import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import com.weatherwolf.weather.CurrentWeather
import com.weatherwolf.weather.DayForecast
import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class SignupControllerSpec extends Specification implements DataTest, ControllerUnitTest<SignupController> {

    Class<?>[] getDomainClassesToMock() {
        return [User, Role, UserRole, SearchLog, EmailLog, CurrentWeather, DayForecast, Location, SearchResult] as Class[]
    }

    void "show signup page"() {

    }

    void "test valid signup"() {

    }

    void "test too short username"() {

    }

    void "test invalid email"() {

    }

    void "test duplicate email"() {

    }

    void "test too short password"() {

    }
}
