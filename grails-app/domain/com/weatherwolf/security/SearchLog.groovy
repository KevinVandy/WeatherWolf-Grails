package com.weatherwolf.security

import com.weatherwolf.security.User


class SearchLog {

    Integer id
    String searchString
    Date date

    static belongsTo = [user: User]

    static mapping = {
        version false
    }

    static constraints = {
        searchString blank: false
    }
}
