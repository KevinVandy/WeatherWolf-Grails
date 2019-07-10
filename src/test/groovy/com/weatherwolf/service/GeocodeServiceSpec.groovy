package com.weatherwolf.service

import com.weatherwolf.GeocodeService
import com.weatherwolf.search.Location
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class GeocodeServiceSpec extends Specification implements DataTest, ServiceUnitTest<GeocodeService>{

    //city, state, country
    void "verify lat/lng from Lincoln, Nebraska, United States"() {
        given:
        Location lincoln = new Location(city: 'Lincoln', stateProvince: 'Nebraska', country: 'United States')

        when:
        service.fillLatLng(lincoln)

        then:
        (lincoln.latitude - 40.86).abs() < 0.1
        (lincoln.longitude - -96.71).abs() < 0.1
    }

    //city, country - null stateProvince
    void "verify lat/lng from Berlin, Germany"() {
        given:
        Location berlin = new Location(city: 'Berlin', country: 'Germany')

        when:
        service.fillLatLng(berlin)

        then:
        (berlin.latitude - 52.52).abs() < 0.1
        (berlin.longitude - 13.41).abs() < 0.1
    }
}
