package com.weatherwolf.weather

class Location {

    Integer id
    String city = ''
    String stateProvince = ''
    String country = ''
    Float latitude = 0.0F
    Float longitude = 0.0F

    static belongsTo = [searchResult: SearchResult]

    static mapping = {
        version false
    }

    static constraints = {
        city blank: true
        stateProvince blank: true
        country blank: true
        latitude nullable: true, min: -90F, max: 90F
        longitude nullable: true, min: -180F, max: 180F
        searchResult nullable: true
    }

    String toString() {
        if (stateProvince && country) {
            "${city}, ${stateProvince}, ${country}"
        } else if (stateProvince && !country)
            "${city}, ${stateProvince}"
        else if (!stateProvince && country) {
            "${city}, ${country}"
        } else {
            "${city}"
        }
    }
}
