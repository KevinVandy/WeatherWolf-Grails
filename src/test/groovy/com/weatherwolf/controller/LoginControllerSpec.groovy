package com.weatherwolf.controller

import com.weatherwolf.LoginController
import com.weatherwolf.security.EmailLog
import com.weatherwolf.security.User
import grails.plugins.mail.MailService
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification


class LoginControllerSpec extends Specification implements DataTest, ControllerUnitTest<LoginController> {

    Class<?>[] getDomainClassesToMock() {
        return [User, EmailLog] as Class[]
    }

    //the unit test does not work, but it is fine in app
    void "send password reset email"() {
        given:
        def mockService = Mock(MailService)
        controller.mailService = mockService
        params.username = 'kevinvandy'
        params.email = 'kvancott@talentplus.com'

        when:
        controller.sendpasswordresetemail(params.username, params.email)

        then:
        response.redirectedUrl == '/login/waitforemail'
    }
}
