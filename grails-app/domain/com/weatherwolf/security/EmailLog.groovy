package com.weatherwolf.security

class EmailLog {

    Integer id
    String toAddress
    String fromAddress = 'weatherwolfgrails@gmail.com'
    String subject
    String body
    Date timeSent

    static constraints = {
        toAddress blank: false, email: true
        fromAddress blank: false, email: true
        subject blank: false
        body blank: false
        timeSent nullable: false
    }

    static mapping = {
        version false
    }
}
