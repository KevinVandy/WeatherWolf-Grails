package com.weatherwolf.controller

import com.weatherwolf.LocationController
import com.weatherwolf.weather.Location
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class LocationControllerSpec extends Specification implements ControllerUnitTest<LocationController> {

    Class<?>[] getDomainClassesToMock() {
        return [Location] as Class[]
    }

    void "test valid search"(){
        given:
        def q = "?offset=0&max=10"

        when:
        controller.index(0, 100)

        then:
        response.text == ''
    }

}
