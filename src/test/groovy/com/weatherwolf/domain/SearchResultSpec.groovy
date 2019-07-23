package com.weatherwolf.domain

import com.weatherwolf.weather.CurrentWeather
import com.weatherwolf.weather.DayForecast
import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class SearchResultSpec extends Specification implements DomainUnitTest<SearchResult> {

    void "test valid search result"(){
        when:
        def sr = new SearchResult(location: new Location())

        then:
        sr.validate()
        sr.errors.errorCount == 0
    }

}
