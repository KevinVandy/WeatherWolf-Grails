package com.weatherwolf.security

class EmailLog {

    Integer id
    String toAddress
    String fromAddress = 'weatherwolfgrails@gmail.com'
    String subject
    String body
    Date timeSent

    static constraints = {
        toAddress blank: false
        fromAddress blank: false
        subject blank: false
        body blank: false
        timeSent nullable: false
    }

    static mapping = {
        version false
    }
}
