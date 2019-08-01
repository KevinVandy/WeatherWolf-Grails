package com.weatherwolf.domain

import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class LocationSpec extends Specification implements DomainUnitTest<Location> {

    void "test valid location"() {
        when:
        def l = new Location(city: 'Montreat', stateProvince: 'North Carolina', country: 'United States', latitude: 23, longitude: 44, searchResult: new SearchResult())

        then:
        l.validate()
        l.errors.errorCount == 0
    }

    void "test location does not have to belong to search result"() {
        when:
        def l = new Location(city: 'Montreat', stateProvince: 'North Carolina', country: 'United States', latitude: 23, longitude: 44)

        then:
        l.validate()
        l.errors.errorCount == 0
    }

    void "test valid location with no lat and long"() {
        when:
        def l = new Location(city: 'Montreat', stateProvince: 'North Carolina', country: 'United States')

        then:
        l.validate()
        l.errors.errorCount == 0
    }

    void "test valid location with no country"() {
        when:
        def l = new Location(city: 'Montreat', stateProvince: 'North Carolina')

        then:
        l.validate()
        l.errors.errorCount == 0
    }

    void "test valid location with no state/province"() {
        when:
        def l = new Location(city: 'Montreat')

        then:
        l.validate()
        l.errors.errorCount == 0
    }

    void "test location must have valid ranges"() {
        when:
        def l = new Location(city: 'Montreat', stateProvince: 'North Carolina', country: 'United States', latitude: 193, longitude: -900, searchResult: new SearchResult())

        then:
        !l.validate()
        l.errors.errorCount == 2
        l.errors.getFieldError('latitude').code == 'max.exceeded'
        l.errors.getFieldError('longitude').code == 'min.notmet'
    }

}
