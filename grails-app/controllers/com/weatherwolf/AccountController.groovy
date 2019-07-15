package com.weatherwolf

import com.weatherwolf.security.Role
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import com.weatherwolf.val.Validators
import grails.plugins.mail.MailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder


class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    String msg = ''
    Authentication authentication
    String currentUsername
    User user

    AccountController() {
        authentication = SecurityContextHolder.getContext().getAuthentication()
        currentUsername = authentication.getName()
        user = User.findByUsername(currentUsername)
    }

    @Secured(['ROLE_CLIENT'])
    def index() { //show user's account settings page
        logger.info("User visited account/index")
        render(view: '/account/index', model: [user: user])
    }

    def login() { //show login page
        logger.info("User visited account/login")
    }

    def signup() { //show signup page
        logger.info("User visited account/signup")
    }

    def forgotpassword() { //show forgot password page
        logger.info("User visited account/forgotpassword")
    }

    @Secured(['ROLE_CLIENT'])
    def changepassword() {
        logger.info("User visited account/changepassword")
    }

    //secure from email validation hash
    def resetpassword() { //show reset password page
        logger.info("User visited account/resetpassword")
    }

    def waitforemail() {
        logger.info("User visited account/waitforemail")
    }

    def sendpasswordresetemail(String email) {
        def mailService = new MailService()
        sendMail {
            to email: email
            subject "Reset Weather Wolf Password"
            text "Reset your password. click here"
        }
        render(view: '/account/waitforemail')
    }

    @Secured(['ROLE_CLIENT'])
    def updatepassword(String password) {
        user.password = password
        user.passwordExpired = false
        user.save(flush: true, failOnError: true)
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    @Secured(['ROLE_CLIENT'])
    def updateemail(String email) {
        user.email = email
        user.save(flush: true, failOnError: true)
        msg= 'Email Updated'
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    @Secured(['ROLE_CLIENT'])
    def savesettings(String lang, String units, String location) {
        user.lang = lang
        user.units = units
        user.favoriteLocation = location
        user.save(flush: true, failOnError: true)
        msg = 'Settings Saved'
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    def register(String username, String email, String password) { //the action of signing up for account

        def u, ur

        logger.info("New user, ${params.username} attempting to register")

        if (Validators.validateSignup(username, email, password)) {
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
            msg = "${username}, your account has been created. Now just Login."
            //redirect(view: '/login/authenticate', params: params)
            render(view: '/account/login', model: [msg: msg])
        } else {
            msg = 'Sign up error'
            render(view: '/account/signup', model: [msg: msg])
        }
    }

}
