package com.weatherwolf.weather

import com.weatherwolf.security.User


class SearchLog {

    Integer id
    String searchString
    String locationString
    String weatherAPIURL
    Date date

    static belongsTo = [user: User]

    static constraints = {
    }
}
