package com.weatherwolf

import com.weatherwolf.security.EmailLog
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.*
import spock.lang.Specification

@Integration(applicationClass = Application.class)
@Rollback
class BrowseSpec extends Specification {

    //not rollback
    def setup() {
        new EmailLog(id: 0, timeSent: new Date(), fromAddress: 'email@email.com', toAddress: 'email@email.com', subject: 'test', body: 'hello there')
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == true
    }
}
