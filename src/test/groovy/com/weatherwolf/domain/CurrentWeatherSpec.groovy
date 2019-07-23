package com.weatherwolf.domain

import com.weatherwolf.weather.CurrentWeather
import com.weatherwolf.weather.SearchResult
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class CurrentWeatherSpec extends Specification implements DomainUnitTest<CurrentWeather> {

    void "test valid current weather"() {
        when:
        def cw = new CurrentWeather(id: 0, condition: 'Windy', icon: '//clouds.png', temperature: 21.3, humidity: 54, windSpeed: 23, windDirection: 'SE', searchResult: new SearchResult())

        then:
        cw.validate()
        cw.errors.errorCount == 0
    }

    void "test current weather must belong to search result"() {
        when:
        def cw = new CurrentWeather(id: 0, condition: 'Windy', icon: '//clouds.png', temperature: 21.3, humidity: 54, windSpeed: 23, windDirection: 'SE', searchResult: null)

        then:
        !cw.validate()
        cw.errors.errorCount == 1
        cw.errors.getFieldError( 'searchResult').code == 'nullable'
    }

    void "test current weather must have valid ranges"() {
        when:
        def cw = new CurrentWeather(id: 0, condition: 'Windy', icon: '//clouds.png', temperature: 254, humidity: 103, windSpeed: -45, windDirection: 'SE', searchResult: new SearchResult())

        then:
        !cw.validate()
        cw.errors.errorCount == 3
        cw.errors.getFieldError( 'temperature').code == 'max.exceeded'
        cw.errors.getFieldError( 'humidity').code == 'max.exceeded'
        cw.errors.getFieldError( 'windSpeed').code == 'min.notmet'
    }

}
