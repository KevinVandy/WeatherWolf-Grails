package com.weatherwolf.domain

import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class SearchLogSpec extends Specification implements DomainUnitTest<SearchLog> {

    void "test valid search log"() {
        when:
        def sl = new SearchLog(searchString: 'Hickman, Nebraska, United States', date: new Date(), user: new User())

        then:
        sl.validate()
        sl.errors.errorCount == 0
    }

    void "test search log must belong to user"() {
        when:
        def sl = new SearchLog(searchString: 'Hickman, Nebraska, United States', date: new Date(), user: null)

        then:
        !sl.validate()
        sl.errors.errorCount == 1
        sl.errors.getFieldError( 'user').code == 'nullable'
    }

}
