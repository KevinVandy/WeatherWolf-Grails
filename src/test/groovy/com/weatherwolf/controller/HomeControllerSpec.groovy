package com.weatherwolf.controller

import com.weatherwolf.HomeController
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class HomeControllerSpec extends Specification implements ControllerUnitTest<HomeController> {

    def "test not logged in user on home page"(){
        when:
        controller.index()

        then:
        response.text.contains("Login to see Personalized Weather")
    }

}
