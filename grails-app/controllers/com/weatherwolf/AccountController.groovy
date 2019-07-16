package com.weatherwolf

import com.weatherwolf.security.EmailLog
import com.weatherwolf.security.Role
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder


class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
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
        logger.info("User visited account/resetpassword")
    }

    @Secured("permitAll")
    def waitforemail() {
        logger.info("User visited account/waitforemail")
    }

    @Secured("permitAll")
    def sendpasswordresetemail(String username, String email) {
        logger.info("username: ${username} and email: ${email} attempting to reset password")
        def e = new EmailLog(toAddress: email, subject: 'Weather Wolf Password Reset', body: 'Click here to reset your password')
        if (User.findByUsername(username)) {
            if (User.findByEmail(email) && User.findByEmail(email).username == username) {
                try {
                    logger.info("attempting to send password reset to ${e.toAddress}")
                    mailService.sendMail {
                        to e.toAddress
                        subject e.subject
                        text e.body
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
        render(view: '/account/waitforemail', model: [email: e.toAddress])
    }

    @Secured(['ROLE_CLIENT'])
    def changepassword(String oldpassword, String newpassword, String passwordconfirm) {
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
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
    @Secured("permitAll")
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
