package com.weatherwolf

import com.weatherwolf.weather.Location
import grails.rest.RestfulController


class LocationController extends RestfulController {

    static allowedMethods = [
            search: ['GET', 'POST']
    ]

    LocationController() {
        super(Location)
    }

    def search(String q, Integer limit) {
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
            respond query.list(max: Math.min(limit ?: 10, 100))
        }
    }
}
