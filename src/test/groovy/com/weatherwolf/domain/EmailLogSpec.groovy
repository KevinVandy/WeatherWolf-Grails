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

    void "test email log with invalid email address"(){
        when:
        def el = new EmailLog(id: 0, toAddress: 'bademail.com', subject: 'test', body: 'hello there', timeSent: new Date())

        then:
        !el.validate()
        el.errors.errorCount == 1
        el.errors.getFieldError( 'toAddress').code == 'email.invalid'
    }

    void "test email log with no subject"(){
        when:
        def el = new EmailLog(id: 0, toAddress: 'test@gmail.com', subject: '', body: 'hello there', timeSent: new Date())

        then:
        !el.validate()
        el.errors.errorCount == 1
        el.errors.getFieldError( 'subject').code == 'nullable'
    }

}
