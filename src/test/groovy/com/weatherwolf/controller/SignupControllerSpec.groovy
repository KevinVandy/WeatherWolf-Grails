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

//    boolean loadExternalBeans() {
//        true
//    }
//
//    Set<String> getIncludePlugins() {
//        ["springSecurityCore"].toSet()
//    }

    void "show signup page"() {
        when:
        controller.index()

        then:
        view == '/signup/index'
    }

    void "test valid signup"() {
        when:
        params.username = 'kevinvandy'
        params.email = 'kvancott@talentplus.com'
        params.password = 'hellothere'
        params.passwordconfirm = 'hellothere'
        params.favoritelocation = 'lincoln,ne'
        controller.register()

        then:
        response.redirectedUrl == '/login/index'
    }

    void "test too short username"() {
        setup:
        controller.metaClass.error = {args -> "mockMessage"}

        when:
        controller.register('ke', 'kvancott@talentplus.com', 'hellothere', 'hellothere', 'lincoln,ne')

        then:
        controller.request.error == 'Username must be between 3 and 30 characters long'

    }

    void "test invalid email"() {

    }

    void "test duplicate email"() {

    }

    void "test too short password"() {

    }
}
