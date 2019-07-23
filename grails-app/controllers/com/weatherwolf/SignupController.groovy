package com.weatherwolf

import com.weatherwolf.security.Role
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class SignupController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    String msg = ''

    def index() {
        logger.info("user visited /signup/index")
    }

    @Secured("permitAll") //POST only
    def register(String username, String email, String password, String passwordconfirm, String favoritelocation) {
        def u, ur
        logger.info("New user, ${username} attempting to register")
        msg = Validators.validateSignup(username, email, password, passwordconfirm)
        logger.info("message: " + msg)
        if (msg) {
            render(view: '/signup/index', model: [msg: msg, username: username, email: email, favoriteLocation: favoritelocation]) //user error
        } else { //should be valid
            logger.info("Creating user: ${username}")
            u = new User(username: username, email: email, password: password, favoriteLocation: favoritelocation)
            try {
                logger.debug("saving user")
                u.save(flush: true, failOnError: true)
                logger.debug("user saved")
                ur = new UserRole(user: u, role: Role.findByAuthority('ROLE_CLIENT'))
                ur.save(flush: true, failOnError: true)
                logger.info("User Role saved")
                msg = "${u.username}" + message(code: 'msg.youraccountcreated', default: ", Your Account has been Created")
                render(view: '/login/index', model: [msg: msg]) //success
            } catch (Exception e) {
                msg = message(code: 'msg.invalidsignup', default: 'Signup not valid.')
                logger.warn("Could not create user: ${username}")
                logger.error(e.toString())
                render(view: '/signup/index', model: [msg: msg]) //unknown error
            }
        }
    }
}
