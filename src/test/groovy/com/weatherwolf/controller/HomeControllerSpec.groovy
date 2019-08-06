package com.weatherwolf.controller

import com.weatherwolf.HomeController
import com.weatherwolf.WeatherService
import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import grails.testing.gorm.DataTest
import grails.testing.mixin.integration.Integration
import grails.testing.web.controllers.ControllerUnitTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
class HomeControllerSpec extends Specification implements DataTest, ControllerUnitTest<HomeController> {

    Class<?>[] getDomainClassesToMock() {
        return [User, SearchLog] as Class[]
    }

    @Autowired WeatherService weatherService


    def "test not logged in user on home page"(){
        setup:

        when: "A not logged in user visits the home page"
        controller.index()

        then: "A Login form appears"

        then:
        view == '/home/index'
    }

}
