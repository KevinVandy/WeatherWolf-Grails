package com.weatherwolf

import com.weatherwolf.search.Location
import grails.gorm.transactions.Transactional

@Transactional
class GeocodeService {

    public static final String BASE = 'https://api.opencagedata.com/geocode/v1/xml'
    public static final String KEY = '5102abc471004e50ac56c239958c42cb'

    def fillLatLng(Location location) {

        //pull data from the castle and encode it for the URL
        String encoded = [location.city, location.stateProvince ?: '', location.country ?: ''].collect {
            URLEncoder.encode(it, 'UTF-8')
        }.join(',').replace(',,', ',')

        //main query part of URL
        String qs = "q=${encoded}";

        //optional paramaters of the URL
        String op = "no_dedupe=1&limit=1"

        //assemble the full URL
        String fullURL = "${BASE}?key=${KEY}&${qs}&${op}"
        println(fullURL) //debug

        try {
            //Parse the XML response
            def root = new XmlSlurper().parse(fullURL)
            def loc = root.results[0].result[0].geometry

            //fill in data into the location object
            location.latitude = loc.lat.toFloat()
            location.longitude = loc.lng.toFloat()
        } catch(Exception e){
            location = null
            println("could not find location\n" + e.toString())
        }
        location
    }
}
