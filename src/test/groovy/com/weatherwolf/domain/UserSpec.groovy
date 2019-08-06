package com.weatherwolf.domain

import com.weatherwolf.security.User
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class UserSpec extends Specification implements DomainUnitTest<User> {

    void "test count users"(){
        expect: 'test gorm is set up'
        User.count() == 0
    }

    void "test save user"(){
        when:
        def u = new User(username: 'newuser', email: 'newuser@gmail.com', password: 'password', favoriteLocation: 'lincoln', dateCreated: new Date())
        u.save(flush: true)

        then:
        User.count() == 1
    }

    void "test new unique valid user"(){
        when:
        def u = new User(username: 'newuser', email: 'newuser@gmail.com', password: 'password', favoriteLocation: 'lincoln', dateCreated: new Date())

        then:
        u.validate()
        u.errors.errorCount == 0
        u.lang == 'en' //default
        u.units == 'F' //default
    }

    void "test new user with username too short"(){
        when:
        def u = new User(username: 'u1', email: 'newuser@gmail.com', password: 'password', favoriteLocation: 'lincoln', dateCreated: new Date())

        then:
        !u.validate()
        u.errors.errorCount == 1
        u.errors.getFieldError( 'username').code == 'size.toosmall'
    }

    void "test new user with email invalid"(){
        when:
        def u = new User(username: 'newuser', email: 'newuser.com', password: 'password', favoriteLocation: 'lincoln', dateCreated: new Date())

        then:
        !u.validate()
        u.errors.errorCount == 1
        u.errors.getFieldError( 'email').code == 'email.invalid'
    }

    void "test new user with password too short"(){
        when:
        def u = new User(username: 'newuser', email: 'newuser@gmail.com', password: 'pass', favoriteLocation: 'lincoln', dateCreated: new Date())

        then:
        !u.validate()
        u.errors.errorCount == 1
        u.errors.getFieldError( 'password').code == 'size.toosmall'
    }

    void "test new user with no favorite location"(){
        when:
        def u = new User(username: 'newuser', email: 'newuser@gmail.com', password: 'password', favoriteLocation: '', dateCreated: new Date())

        then:
        !u.validate()
        u.errors.errorCount == 1
        u.errors.getFieldError( 'favoriteLocation').code == 'nullable'
    }

    void "test new user with supported language"(){
        when:
        def u = new User(username: 'newuser', email: 'newuser@gmail.com', password: 'password', favoriteLocation: 'Lincoln', dateCreated: new Date(), lang: 'es')

        then:
        u.validate()
        u.errors.errorCount == 0
    }

    void "test new user with unsupported language"(){
        when:
        def u = new User(username: 'newuser', email: 'newuser@gmail.com', password: 'password', favoriteLocation: 'Lincoln', dateCreated: new Date(), lang: 'cs')

        then:
        !u.validate()
        u.errors.errorCount == 1
        u.errors.getFieldError( 'lang').code == 'not.inList'
    }

    void "test new user with supported units"(){
        when:
        def u = new User(username: 'newuser', email: 'newuser@gmail.com', password: 'password', favoriteLocation: 'Lincoln', dateCreated: new Date(), units: 'F')

        then:
        u.validate()
        u.errors.errorCount == 0
    }

    void "test new user with unsupported units"(){
        when:
        def u = new User(username: 'newuser', email: 'newuser@gmail.com', password: 'password', favoriteLocation: 'Lincoln', dateCreated: new Date(), units: 'K')

        then:
        !u.validate()
        u.errors.errorCount == 1
        u.errors.getFieldError( 'units').code == 'not.inList'
    }

}
