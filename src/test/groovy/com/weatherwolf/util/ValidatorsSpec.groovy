package com.weatherwolf.util

import com.weatherwolf.Validators
import com.weatherwolf.security.User
import grails.testing.gorm.DataTest
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification


class ValidatorsSpec extends Specification implements DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [User] as Class[]
    }

    void "test valid signup"() {
        given:
        def u = new User(username: "tester${RandomStringUtils.random(5, (('A'..'Z')).join().toCharArray())}", email: "${RandomStringUtils.random(8, (('A'..'Z')).join().toCharArray())}@tester.com", password: 'hellothere')

        when:
        def msg = Validators.validateSignup(u.username, u.email, u.password, u.password)

        then:
        msg == '' //no error
    }

    void "test too short username"() {
        given:
        def username = 'ab'

        when:
        def msg = Validators.valUsername(username)

        then:
        msg != '' //should have error
    }

    void "test duplicate username"() {
        given:
        def username = 'kevinvandy'
        if (!User.findByUsername(username)) {
            def u1 = new User(username: username, email: 'fdas@talentplus.com', password: 'hellothere', favoriteLocation: 'new york')
            u1.save(flush: true, failOnError: true)
        }

        when:
        def msg = Validators.valUsername(username)

        then:
        msg != '' //should have error
    }

    void "test valid email"() {
        given:
        def email = 'valid@email.com'

        when:
        def msg = Validators.valEmail(email)

        then:
        msg == ''
    }

    void "test invalid email"() {
        given:
        def email = 'notvalidemail'

        when:
        def msg = Validators.valEmail(email)

        then:
        msg != '' //should have error
    }

    void "test duplicate email"() {
        given:
        if (!User.findByUsername('kevinvandy')) {
            def u1 = new User(username: 'kevinvandy', email: 'fdas@talentplus.com', password: 'hellothere')
            u1.save()
        }
        def u2 = new User(username: 'kevinvandy', email: 'asdf@talentplus.com', password: 'hellothere')

        when:
        def msg = Validators.valEmail(u2.username)

        then:
        msg != '' //should have error
    }

    void "test valid matching passwords"() {
        given:
        def password1 = 'thisisapassword'
        def password2 = 'thisisapassword'

        when:
        def msg = Validators.valPassword(password1, password2)

        then:
        msg == ''
    }

    void "test too short password"() {
        given:
        def password = 'pass'

        when:
        def msg = Validators.valPassword(password, password)

        then:
        msg != '' //should have error
    }

    void "test mismatching passwords"() {
        given:
        def password1 = 'thisisapassword1'
        def password2 = 'thisisapassword2'

        when:
        def msg = Validators.valPassword(password1, password2)

        then:
        msg != '' //should have error
    }

}
