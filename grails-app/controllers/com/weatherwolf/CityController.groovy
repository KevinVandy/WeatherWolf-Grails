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
            def l = new Location()
            l = WeatherUtils.assignCityStateProvinceCountry(q, l)
            if (l.city && !l.country && !l.stateProvince) {
                query = Location.where {
                    (city ==~ "%${l.city}%")
                }
            } else if (l.city && l.country && !l.stateProvince) {
                query = Location.where {
                    (city ==~ "%${l.city}%") && ((country ==~ "%${l.country}%") || (stateProvince ==~ "%${l.country}%"))
                }
            } else if (l.city && l.country && l.stateProvince) {
                query = Location.where {
                    (city ==~ "%${l.city}%") && (country ==~ "%${l.country}%") && (stateProvince ==~ "%${l.stateProvince}%")
                }
            }
            respond query.list(max: Math.min(max ?: 10, 100))
        }
    }
}
