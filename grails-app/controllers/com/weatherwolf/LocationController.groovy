package com.weatherwolf

import com.weatherwolf.weather.Location
import grails.rest.RestfulController


class LocationController extends RestfulController<Location> {

    static responseFormats = ['json']

    static allowedMethods = [
            search: ['GET']
    ]

    LocationController() {
        super(Location)
    }

    def search(String q) {
        def query
        if (!q) {
            respond([])
        } else {
            def l = new Location()
            l = WeatherUtils.assignCityStateProvinceCountry(q, l)
            if (l.city && !l.country && !l.stateProvince) {
                query = Location.where {
                    (city ==~ "%${q}%")
                }
            } else if (l.city && l.country && !l.stateProvince) {
                query = Location.where {
                    (city ==~ "%${l.city}%") && ((country ==~ "%${l.country}%") || (stateProvince ==~ "%${l.country}%"))
                }
            } else if (l.city && l.country && l.stateProvince) {
                query = Location.where {
                    (city ==~ "%${l.city}%") && (country ==~ "%${l.country}%") && (stateProvince ==~ "%${l.stateProvince}%")
                }
            } else {
                query = Location.where {
                    (city ==~ "%${q}%")
                }
            }
            respond query.list(max: 20)
        }
    }
}
