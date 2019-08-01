package com.weatherwolf.service

import com.weatherwolf.GeocodeService
import com.weatherwolf.weather.Location
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class GeocodeServiceSpec extends Specification implements DataTest, ServiceUnitTest<GeocodeService>{

    //city, state, country
    void "verify lat/lng from Lincoln, Nebraska, United States"() {
        given:
        Location lincoln = new Location(city: 'Lincoln', stateProvince: 'Nebraska', country: 'United States')

        when:
        service.fill(lincoln)

        then:
        (lincoln.latitude - 40.86).abs() < 0.1
        (lincoln.longitude - -96.71).abs() < 0.1
    }

    //city, country - null stateProvince
    void "verify lat/lng from Berlin, Germany"() {
        given:
        Location berlin = new Location(city: 'Berlin', country: 'Germany')

        when:
        service.fill(berlin)

        then:
        (berlin.latitude - 52.52).abs() < 0.1
        (berlin.longitude - 13.41).abs() < 0.1
    }

    //city only - null stateProvince, country
    void "verify lat/lng from Topeka"() {
        given:
        Location topeka = new Location(city: 'Topeka')

        when:
        service.fill(topeka)

        then:
        (topeka.latitude - 39.04).abs() < 0.1
        (topeka.longitude - -95.68).abs() < 0.1
    }

    void "verify lat/lng is 0 when bad search string"() {
        given:
        Location l = new Location(city: 'a')

        when:
        service.fill(l)

        then:
        l.latitude.round().toInteger() == 0
        l.longitude.round().toInteger() == 0
    }

    void "verify city, region, country filled in when provided lat and long"() {
        given:
        Location l = new Location(latitude: 40.86, longitude: -96.71)

        when:
        service.fill(l)

        then:
        l.city == 'Lincoln'
        l.stateProvince == 'Nebraska'
        l.country == 'United States'
    }
}
