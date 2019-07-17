package com.weatherwolf

import com.weatherwolf.security.EmailLog
import com.weatherwolf.security.Role
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import org.apache.commons.lang.RandomStringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.context.MessageSource


class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    MessageSource messageSource
    String msg = ''
    String currentUsername
    def user = new User()
    def springSecurityService
    def mailService

    @Secured(['ROLE_CLIENT'])
    def index() { //show user's account settings page
        logger.info("User visited account/index")
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        render(view: '/account/index', model: [user: user])
    }

    @Secured("permitAll")
    def login() { //show login page
        logger.info("User visited account/login")
    }

    @Secured("permitAll")
    def signup() { //show signup page
        logger.info("User visited account/signup")
    }

    @Secured("permitAll")
    def forgotpassword() { //show forgot password page that will prompt to send email password reset
        logger.info("User visited account/forgotpassword")
    }

    //secure from email validation hash
    @Secured("permitAll")
    def resetpassword() { //show reset password page
        logger.info("User ${params.username} visited account/resetpassword")
        if (params.username && params.forgotPasswordToken) {
            user = User.findByUsername(params.username)
            if (user.forgotPasswordToken.equalsIgnoreCase(params.forgotPasswordToken)) {
                msg = 'valid token'
            } else {
                msg = 'invalid token'
            }
        } else {
            msg = 'invalid link'
            msg = message(code: "msg.invalidlink")
        }
        render(view: '/account/resetpassword', model: [msg: msg])
    }

    @Secured("permitAll")
    def waitforemail() {
        logger.info("User visited account/waitforemail")
    }

    @Secured("permitAll")
    def sendpasswordresetemail(String username, String email) {
        logger.info("username: ${username} and email: ${email} attempting to reset password")
        if (User.findByUsername(username)) {
            if (User.findByEmail(email) && User.findByEmail(email).username == username) {
                try {
                    logger.info("attempting to send password reset to ${email}")
                    user = User.findByUsername(username)
                    user.forgotPasswordToken = RandomStringUtils.random(50, (('A'..'Z') + ('0'..'9')).join().toCharArray())
                    user.save(flush: true, failOnError: true)
                    def e = new EmailLog(toAddress: email, subject: 'Weather Wolf Password Reset', body: "Click <a href='http://localhost:8080/account/resetpassword?username=${user.username}&forgotPasswordToken=${user.forgotPasswordToken}'>here</a> to reset your password")
                    mailService.sendMail {
                        to e.toAddress
                        subject e.subject
                        html e.body
                    }
                    e.timeSent = new Date()
                    e.save(flush: true, failOnError: true)
                    logger.info("Email sent to ${email}")
                } catch (Exception ex) {
                    logger.warn("Could not send email")
                    logger.error(ex.toString())
                    msg = 'Email failed to send for unknown reason. Try again later'
                    render(view: '/account/forgotpassword', model: [msg: msg])
                    return
                }
            } else {
                msg = 'Email and Username do not match'
                render(view: '/account/forgotpassword', model: [msg: msg])
                return
            }
        } else {
            msg = 'Could not find Username'
            render(view: '/account/forgotpassword', model: [msg: msg])
            return
        }
        render(view: '/account/waitforemail', model: [email: email])
    }

    @Secured(['ROLE_CLIENT'])
    def changepassword(String oldpassword, String newpassword, String passwordconfirm) {
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        if (oldpassword && Validators.valPassword(newpassword, passwordconfirm)) {
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
    @Secured("permitAll")
    def updatepassword(String newpassword, String newpasswordconfirm) {
        user = User.findByUsername(params.username)
        if (user && user.username == params.username && user.forgotPasswordToken == params.forgotPasswordToken) {
            if (Validators.valPassword(newpassword, newpasswordconfirm)) {
                user.password = newpassword
                user.forgotPasswordToken = ''
                try {
                    user.save(flush: true, failOnError: true)
                    msg = "${user.username}, your password was successfully reset. Try loggin in with your new password"
                    render(view: '/account/login', model: [msg: msg])
                } catch (Exception e) {
                    msg = 'Error saving new password'
                    logger.error(e.toString())
                }
            } else {
                msg = 'Failed password requirements'
                render(view: '/account/resetpassword', model: [msg: msg])
            }
        } else {
            msg = 'Invalid Token'
            render(view: '/account/login', model: [msg: msg])
        }
    }

    @Secured(['ROLE_CLIENT'])
    def updateemail(String email) {
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        user.email = email
        user.save(flush: true, failOnError: true)
        msg = 'Email Updated'
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    @Secured(['ROLE_CLIENT'])
    def savesettings(String lang, String units, String location) {
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        user.lang = lang
        user.units = units
        user.favoriteLocation = location
        user.save(flush: true, failOnError: true)
        msg = 'Settings Saved'
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    @Secured("permitAll")
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
