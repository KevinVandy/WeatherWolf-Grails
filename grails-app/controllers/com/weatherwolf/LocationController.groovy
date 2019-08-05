package com.weatherwolf

import com.weatherwolf.weather.Location
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Secured("permitAll")
class LocationController extends RestfulController<Location> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    def geocodeService

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

    /** GET only
     *
     * Returns json for up to 1000 locations
     *
     * @param offset
     * @param max
     * @return
     */
    def index(int offset, int max) {
        if (!max || max > 1000) {
            logger.info("Invalid parameters: max - ${max}, offset: ${offset}")
            respond([])
        } else {
            respond Location.findAll(max: max, offset: offset ?: 0)
        }
    }

    /** GET only
     *
     * Returns json for up to 20 locations while intelligently searching by city, region, and country
     *
     * @param q
     * @return
     */
    def search(String q) {
        def query
        if (!q || q.length() < 2) {
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

    /** GET only
     *
     * Searches only cities and returns a max of 20 cities
     *
     * @param q
     * @return
     */
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

    /** GET only
     *
     * Searches only state/provinces and returns a max of 20 state/provinces
     *
     * @param q
     * @return
     */
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

    /** GET only
     *
     * Searches only countries and returns a max of 20 countries
     *
     * @param q
     * @return
     */
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

    /** GET only
     *
     * Takes an incomplete location object and fills in missing information using Geocoding service.
     * Returns a full location object in JSON
     *
     * @param city
     * @param stateProvince
     * @param country
     */
    def fill(String city, String stateProvince, String country, Float lat, Float lng){
        def l = new Location(city: city, stateProvince: stateProvince, country: country, latitude: lat, longitude: lng)
        geocodeService.fill(l)
        respond l
    }
}
