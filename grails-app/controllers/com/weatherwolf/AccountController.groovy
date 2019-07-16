package com.weatherwolf

import com.weatherwolf.security.Role
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.mail.MailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder


class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    String msg = ''
    String currentUsername
    def user = new User()
    def springSecurityService

    AccountController() {
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
    }

    @Secured(['ROLE_CLIENT'])
    def index() { //show user's account settings page
        logger.info("User visited account/index")
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        render(view: '/account/index', model: [user: user])
    }

    def login() { //show login page
        logger.info("User visited account/login")
    }

    def signup() { //show signup page
        logger.info("User visited account/signup")
    }

    def forgotpassword() { //show forgot password page that will prompt to send email password reset
        logger.info("User visited account/forgotpassword")
    }

    //secure from email validation hash
    def resetpassword() { //show reset password page
        logger.info("User visited account/resetpassword")
    }

    def waitforemail() {
        logger.info("User visited account/waitforemail")
    }

    def sendpasswordresetemail(String email) {
        logger.info("attempting to send password reset to ${email}")
        try {
            sendMail {
                to "kvancott@talentplus.com"
                subject "Weather Wolf Password Reset"
                text "Click here to reset your password"
            }
        } catch (Exception e) {
            logger.warn("Could not send email")
            logger.error(e.toString())
        }
        render(view: '/account/waitforemail', model: [email: email])
    }

    @Secured(['ROLE_CLIENT'])
    def changepassword(String oldpassword, String newpassword, String passwordconfirm) {
        if (springSecurityService.passwordEncoder.isPasswordValid(user.password, oldpassword, null) && Validators.valPassword(newpassword, passwordconfirm)) {
            user.password = newpassword
            user.passwordExpired = false
            try {
                user.save(flush: true, failOnError: true)
                msg = 'Password was changed'
            } catch (Exception e) {
                msg = 'Error saving new password'
                logger.error(e.toString())
            }
        } else {
            msg = 'Failed password requirements'
        }
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    //not secured because this is for users who forgot their password
    def updatepassword(String password, String passwordconfirm) {
        if (Validators.valPassword(password, passwordconfirm)) {
            user.password = password
            user.passwordExpired = false
            try {
                user.save(flush: true, failOnError: true)
            } catch (Exception e) {
                msg = 'Error saving new password'
                logger.error(e.toString())
            }
        } else {
            msg = 'Failed password requirements'
        }
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    @Secured(['ROLE_CLIENT'])
    def updateemail(String email) {
        user.email = email
        user.save(flush: true, failOnError: true)
        msg = 'Email Updated'
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

    def register(String username, String email, String password, String passwordconfirm, String favoritelocation) {
        //the action of signing up for account

        def u, ur

        logger.info("New user, ${params.username} attempting to register")

        if (Validators.validateSignup(username, email, password, passwordconfirm)) {
            try {
                logger.info("Creating user: ${username}")
                u = new User(username: username, email: email, password: password, favoriteLocation: favoritelocation)
                logger.info("saving user")
                u.save(flush: true, failOnError: true)
                logger.info("user saved")
                if (u) {
                    try {
                        logger.info("Assigning ${username} to role")
                        ur = new UserRole(user: u, role: Role.findByAuthority('ROLE_CLIENT'))
                        logger.info("Saving user role")
                        ur.save(flush: true, failOnError: true)
                        logger.info("User Role saved")
                    } catch (Exception ex) {
                        msg = 'Sign up error'
                        logger.warn("Could not assign ${username} to role")
                        logger.error(ex.toString())
                        render(view: '/account/signup', model: [msg: msg])
                        return
                    }
                }
                msg = "${username}, your account has been created. Now just Login."
                render(view: '/account/login', model: [msg: msg])
            } catch (Exception e) {
                msg = 'Sign up error'
                logger.warn("Could not create user: ${username}")
                logger.error(e.toString())
                render(view: '/account/signup', model: [msg: msg])
            }
        } else {
            render(view: '/account/signup', model: [msg: msg])
        }
    }
}
