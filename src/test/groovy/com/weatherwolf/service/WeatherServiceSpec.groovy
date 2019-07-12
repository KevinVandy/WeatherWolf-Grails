package com.weatherwolf.service

import com.weatherwolf.WeatherService
import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification


class WeatherServiceSpec extends Specification implements DataTest, ServiceUnitTest<WeatherService> {

    //city, state, country
    void "verify non null weather from Lincoln, Nebraska, United States"() {
        given:
        def searchResult = new SearchResult()
        searchResult.location = new Location(city: 'Lincoln', stateProvince: 'Nebraska', country: 'United States')

        when:
        service.fillWeather(searchResult)

        then:
        searchResult.currentWeather.condition != ''
        searchResult.dayForecasts.size() == 5
    }

    //city, state
    void "verify non null weather from Omaha, Nebraska"() {
        given:
        def searchResult = new SearchResult()
        searchResult.location = new Location(city: 'Omaha', stateProvince: 'Nebraska')

        when:
        service.fillWeather(searchResult)

        then:
        searchResult.currentWeather.condition != ''
        searchResult.dayForecasts.size() == 5
    }

    //city
    void "verify non null weather from Paris"() {
        given:
        def searchResult = new SearchResult()
        searchResult.location = new Location(city: 'Paris')

        when:
        service.fillWeather(searchResult)

        then:
        searchResult.currentWeather.condition != ''
        searchResult.dayForecasts.size() == 5
    }

    void "verify null search result for bad query"() {
        given:
        def searchResult = new SearchResult()
        searchResult.location = new Location(city: 'a')

        when:
        service.fillWeather(searchResult)

        then:
        searchResult.currentWeather == null
        searchResult.dayForecasts == null
    }

    void "verify null search results for null query"() {
        given:
        def searchResult = new SearchResult()
        searchResult.location = new Location()

        when:
        service.fillWeather(searchResult)

        then:
        searchResult.currentWeather == null
        searchResult.dayForecasts == null
    }

}
