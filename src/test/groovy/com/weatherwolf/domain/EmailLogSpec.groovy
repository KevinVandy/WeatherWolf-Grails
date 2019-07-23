package com.weatherwolf.domain

import com.weatherwolf.security.EmailLog
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class EmailLogSpec extends Specification implements DomainUnitTest<EmailLog> {

    void "test valid email log"(){
        when:
        def el = new EmailLog(id: 0, toAddress: 'test@gmail.com', subject: 'test', body: 'hello there', timeSent: new Date())

        then:
        el.validate()
        el.errors.errorCount == 0
    }

}
