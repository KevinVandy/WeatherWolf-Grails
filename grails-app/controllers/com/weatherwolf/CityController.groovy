package com.weatherwolf

import com.weatherwolf.weather.City
import com.weatherwolf.weather.Location
import grails.rest.RestfulController
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class CityController extends RestfulController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    static responseFormats = ['json', 'xml']

    CityController() {
        super(City)
    }

    def search(String q, Integer max) {
        def query
        if (!q) {
            respond([])
        } else {
            def c = new Location()
            c = WeatherUtils.assignCityStateProvinceCountry(q, c)
            if (c.city && !c.country) {
                query = City.where {
                    (name ==~ "%${c.city}%")
                }
            } else {
                query = City.where {
                    (name ==~ "%${c.city}%") && (country ==~ "%${c.country}%")
                }
            }
            respond query.list(max: Math.min(max ?: 10, 100))
        }
    }
}
