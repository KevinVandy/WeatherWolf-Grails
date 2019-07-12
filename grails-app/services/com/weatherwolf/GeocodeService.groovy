package com.weatherwolf

import com.weatherwolf.weather.Location
import grails.gorm.transactions.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Transactional
class GeocodeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    public static final String BASE = 'https://api.opencagedata.com/geocode/v1/xml'
    public static final String KEY = '5102abc471004e50ac56c239958c42cb'
    def root

    def fillLatLng(Location location) {

        //pull data from the castle and encode it for the URL
        String encoded = [location.city ?: '', location.stateProvince ?: '', location.country ?: ''].collect {
            URLEncoder.encode(it, 'UTF-8')
        }.join(',')

        //get rid of double commas caused by null values
        while (encoded.contains(',,')) {
            encoded = encoded.replace(',,', ',')
        }

        //main query part of URL
        String qs = "q=${encoded}"

        //optional paramaters of the URL
        String op = "no_dedupe=1&limit=1"

        //assemble the full URL
        String fullURL = "${BASE}?key=${KEY}&${qs}&${op}"
        logger.info("Geocode URL: ${fullURL}")

        try {
            //Parse the XML response
            root = new XmlSlurper().parse(fullURL)
            def loc = root.results[0].result[0].geometry

            //fill in data into the location object
            location.latitude = loc.lat.toFloat()
            location.longitude = loc.lng.toFloat()
        } catch (Exception e) {
            location = new Location()
            logger.warn("could not find location")
            logger.warn(e.toString())
        }
        location
    }
}
