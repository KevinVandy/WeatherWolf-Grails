package com.weatherwolf.service

import com.weatherwolf.WeatherService
import com.weatherwolf.search.Location
import com.weatherwolf.search.SearchResult
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification


class WeatherServiceSpec extends Specification implements DataTest, ServiceUnitTest<WeatherService> {

    void "verify non null current weather from Lincoln, Nebraska, United States"() {
        given:
        def searchResult = new SearchResult()
        searchResult.location = new Location(city: 'Lincoln', stateProvince: 'Nebraska', country: 'United States')

        when:
        service.fillWeather(searchResult)

        then:
        searchResult.currentWeather.condition != ''
    }

}
