package com.weatherwolf

import com.weatherwolf.weather.Location
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Secured("permitAll")
class LocationController extends RestfulController<Location> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())

    static responseFormats = ['json']

    static allowedMethods = [
            index  : ['GET'],
            search: ['GET'],
            searchcity: ['GET'],
            searchstateprovince: ['GET'],
            searchcountry: ['GET']
    ]

    LocationController() {
        super(Location)
    }

    def index(int offset, int max) {
        if (!max || max > 1000) {
            logger.info("Invalid parameters: max - ${max}, offset: ${offset}")
            respond([])
        } else {
            respond Location.findAll(max: max, offset: offset ?: 0)
        }
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
                    (city ==~ "${q}%")
                }
            } else if (l.city && l.country && !l.stateProvince) {
                query = Location.where {
                    (city ==~ "${l.city}%") && ((country ==~ "${l.country}%") || (stateProvince ==~ "${l.country}%"))
                }
            } else if (l.city && l.country && l.stateProvince) {
                query = Location.where {
                    (city ==~ "${l.city}%") && (country ==~ "${l.country}%") && (stateProvince ==~ "${l.stateProvince}%")
                }
            } else {
                query = Location.where {
                    (city ==~ "${q}%")
                }
            }
            respond query.list(max: 20)
        }
    }

    def searchcity(String q) {
        def query
        if (!q || q.length() <= 3) {
            respond([])
        } else {
            query = Location.where {
                (city ==~ "${q}%")
            }.distinct('city')
            respond query.list(max: 20)
        }
    }

    def searchstateprovince(String q) {
        def query
        if (!q || q.length() <= 2) {
            respond([])
        } else {
            query = Location.where {
                (stateProvince ==~ "${q}%")
            }.distinct('stateProvince')
            respond query.list(max: 20)
        }
    }

    def searchcountry(String q) {
        def query
        if (!q || q.length() <= 1) {
            respond([])
        } else {
            query = Location.where {
                (country ==~ "${q}%")
            }.distinct('country')
            respond query.list(max: 20)
        }
    }
}
