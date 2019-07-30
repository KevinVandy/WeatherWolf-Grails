package com.weatherwolf.controller

import com.weatherwolf.HomeController
import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

@Integration
class HomeControllerSpec extends GebSpec {

    Class<?>[] getDomainClassesToMock() {
        return [User, SearchLog] as Class[]
    }

    def "test not logged in user on home page"(){
        when: "A not logged in user visits the home page"
        go '/'

        then: "A Login form appears"

        then:
        $('login').text().contains('Login to see')
    }

}
