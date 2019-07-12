package com.weatherwolf

import com.weatherwolf.security.Role
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import com.weatherwolf.val.Validators

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())

    String msg = ''

    @Secured(['ROLE_CLIENT'])
    def index() { //show user's account settings page
        logger.info("User visited account/index")
    }

    def login() { //show login page
        logger.info("User visited account/login")
    }

    def signup() { //show signup page
        logger.info("User visited account/signup")
    }

    def register(String username, String email, String password) { //the action of signing up for account

        def u, ur

        logger.info("New user, ${params.username} attempting to register")

        if (validateSignup(username, email, password)) {
            try {
                logger.info("Creating user: ${username}")
                u = new User(username: username, email: email, password: password)
                logger.info("saving user")
                u.save(flush: true, failOnError: true)
                logger.info("user saved")
            } catch (Exception e) {
                logger.warn("Could not create user: ${username}")
                logger.error(e.toString())
            }
            if (u) {
                try {
                    logger.info("Assigning ${username} to role")
                    ur = new UserRole(user: u, role: Role.findByAuthority('ROLE_CLIENT'))
                    logger.info("Saving user role")
                    ur.save(flush: true, failOnError: true)
                    logger.info("User Role saved")
                } catch (Exception e) {
                    logger.warn("Could not assign ${username} to role")
                    logger.error(e.toString())
                }
            }
            msg = "${username}, your account has been created.<br /> Now just Login."
            login()
            //render(view: '/account/login', model: [msg: msg])
        } else {
            msg = 'Sign up error'
            render(view: '/account/signup', model: [msg: msg])
        }
    }

    def updatePassword() {

    }

    private boolean validateSignup(String username, String email, String password) {
        Validators.valUsername(username) &&
                Validators.valEmail(email) &&
                Validators.valPassword(password)
    }

}
