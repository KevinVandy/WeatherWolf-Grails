package com.weatherwolf.domain

import com.weatherwolf.weather.DayForecast
import com.weatherwolf.weather.SearchResult
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class DayForecastSpec extends Specification implements DomainUnitTest<DayForecast> {

    void "test valid day forecast"() {
        when:
        def df = new DayForecast(id: 0, date: new Date().plus(1), condition: 'sunny', iconURL: '//sun.png', minTemp: 15, maxTemp: 24, precipitation: 0.1, windSpeed: 6, searchResult: new SearchResult())

        then:
        df.validate()
        df.errors.errorCount == 0
    }

    void "test day forecast must belong to search result"() {
        when:
        def df = new DayForecast(id: 0, date: new Date().plus(1), condition: 'sunny', iconURL: '//sun.png', minTemp: 15, maxTemp: 24, precipitation: 0.1, windSpeed: 6, searchResult: null)

        then:
        !df.validate()
        df.errors.errorCount == 1
        df.errors.getFieldError( 'searchResult').code == 'nullable'
    }

    void "test day forecast must valid ranges"() {
        when:
        def df = new DayForecast(id: 0, date: new Date().plus(1), condition: 'sunny', iconURL: '//sun.png', minTemp: 212, maxTemp: -150, precipitation: 456, windSpeed: -38, searchResult: new SearchResult())

        then:
        !df.validate()
        df.errors.errorCount == 4
        df.errors.getFieldError( 'minTemp').code == 'max.exceeded'
        df.errors.getFieldError( 'maxTemp').code == 'min.notmet'
        df.errors.getFieldError( 'precipitation').code == 'max.exceeded'
        df.errors.getFieldError( 'windSpeed').code == 'min.notmet'
    }

}
