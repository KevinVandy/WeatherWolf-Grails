package com.weatherwolf.domain

import com.weatherwolf.security.User
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class UserSpec extends Specification implements DomainUnitTest<User> {

    void "test new unique valid user"(){
        when:
        def u = new User(username: 'newuser', )
    }

}
