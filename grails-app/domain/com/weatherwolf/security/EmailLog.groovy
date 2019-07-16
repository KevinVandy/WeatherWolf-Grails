package com.weatherwolf.security

class EmailLog {

    Integer id
    String toAddress
    String fromAddress = 'weatherwolfgrails@gmail.com'
    String subject
    String body
    Date timeSent

    static constraints = {
    }

    static mapping = {
        version false
    }
}
