package com.weatherwolf.controller

import com.weatherwolf.AccountController
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class AccountControllerSpec extends Specification implements ControllerUnitTest<AccountController> {

    void "show login page for login user"(){

    }

    void "redirect to login/auth if non-logged in user tries to view account home page"() {

    }

    void "show login page"() {

    }

    void "show signup page"() {

    }

    void "show forgotpassword page" () {

    }

    void "show resetpassword page with valid hash"() {

    }

    void "don't show resetpassword page because of invalid hash"() {

    }

    void "show waitforemail page" () {

    }

    void "send password reset email"() {
        given:
        def email = "kvancott@talentplus.com"

        when:
        controller.sendpasswordresetemail(email)

        then:
        true
    }

    void "register valid account"() {

    }

    void "register invalid account with password too short"() {

    }

    void "change password, fail requirements with invalid old password" () {

    }

    void "change password, fail requirements with invalid new password" () {

    }

    void "change password, fail requirements with unmatching new password confirm" () {

    }

    void "change password successfully"() {

    }

}
