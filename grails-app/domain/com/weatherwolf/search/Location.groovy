package com.weatherwolf.search

class Location {

    String city
    String stateProvince
    String country
    Float latitude = 0.0F
    Float longitude = 0.0F

    static belongsTo = [searchResult: SearchResult]

    static mapping = {
        version false
    }

    static constraints = {
    }
}