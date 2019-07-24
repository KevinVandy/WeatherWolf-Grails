package com.weatherwolf

import com.weatherwolf.security.EmailLog
import com.weatherwolf.security.User
import org.apache.commons.lang.RandomStringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.annotation.Secured
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.web.WebAttributes


@Secured('permitAll')
class LoginController extends grails.plugin.springsecurity.LoginController {

    static allowedMethods = [
            resetpassword         : 'GET', //needs token from url that comes from email link
            sendpasswordresetemail: 'POST',
            updatepassword        : 'POST'
    ]

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    def mailService
    def user = new User()

    @Override
    def index() {
        if (isLoggedIn()) {
            redirect uri: conf.successHandler.defaultTargetUrl
        } else {
            render(view: '/login/index')
        }
    }

    //override the default spring security login form
    @Override
    def auth() {
        if (isLoggedIn()) {
            redirect(controller: 'home', action: 'index')
        } else {
            redirect(controller: 'login', action: 'index')
        }
    }

    @Override
    def authfail() {
        def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
        if (exception) {
            if (exception instanceof AccountExpiredException) {
                flash.error = messageSource.getMessage('springSecurity.errors.login.expired', null, "Account Expired", request.locale)
            } else if (exception instanceof CredentialsExpiredException) {
                flash.error = messageSource.getMessage('springSecurity.errors.login.passwordExpired', null, "Password Expired", request.locale)
            } else if (exception instanceof DisabledException) {
                flash.error = messageSource.getMessage('springSecurity.errors.login.disabled', null, "Account Disabled", request.locale)
            } else if (exception instanceof LockedException) {
                flash.error = messageSource.getMessage('springSecurity.errors.login.locked', null, "Account Locked", request.locale)
            } else {
                flash.error = messageSource.getMessage('springSecurity.errors.login.fail', null, "Authentication Failure", request.locale)
            }
        }
        render(view: '/login/index')
    }

    def forgotpassword() { //show forgot password page that will prompt to send email password reset
        logger.info("User visited login/forgotpassword")
    }

    //POST only
    def sendpasswordresetemail(String username, String email) {
        logger.info("username: ${username} and email: ${email} attempting to reset password")
        if (!User.findByUsernameAndEmail(username, email)) {
            flash.error = message(code: 'msg.couldnotfindusernameemail', default: 'Could not find Username, or username and email do not match')
            redirect(url: '/login/forgotpassword') //user error
        } else { //should be valid
            try {
                logger.info("attempting to send password reset to ${email}")
                user = User.findByUsername(username)
                user.forgotPasswordToken = RandomStringUtils.random(50, (('A'..'Z') + ('0'..'9')).join().toCharArray())
                user.save(flush: true, failOnError: true)
                def e = new EmailLog(toAddress: email, subject: 'Weather Wolf Password Reset', body: "Click <a href='http://localhost:9999/login/resetpassword?username=${user.username}&forgotPasswordToken=${user.forgotPasswordToken}'>here</a> to reset your password")
                mailService.sendMail {
                    to e.toAddress
                    subject e.subject
                    html e.body
                }
                e.timeSent = new Date()
                e.save(flush: true, failOnError: true)
                logger.info("Email sent to ${email}")
                flash.success = "An email to reset your password has been sent to ${email}"
                redirect(url: '/login/waitforemail') //success
            } catch (Exception ex) {
                logger.warn("Could not send email")
                logger.error(ex.toString())
                flash.error = message(code: 'msg.emailfail', default: 'Email failed to send for unknown reason. Try again later')
                redirect(url: '/login/forgotpassword') //unknown failure
            }
        }
    }

    def waitforemail() {
        logger.info("User visited login/waitforemail")
    }

    //secure from email validation hash
    //GET only
    def resetpassword() { //show reset password page
        logger.info("User ${params.username} visited account/resetpassword")
        boolean validToken = false
        if (!(params.username && params.forgotPasswordToken)) {
            flash.error = message(code: 'msg.invalidlink', default: 'Invalid Link') //user error
        } else {
            user = User.findByUsername(params.username)
            if (!(user && user.forgotPasswordToken && user.forgotPasswordToken.equalsIgnoreCase(params.forgotPasswordToken))) {
                flash.error = message(code: 'msg.invalidtoken', default: 'Invalid Token') //user error
            } else {
                validToken = true
            }
        }
        render(view: '/login/resetpassword', model: [validToken: validToken])
    }

    //not secured by role because this is for users who forgot their password
    //POST only
    def updatepassword(String username, String forgotPasswordToken, String newpassword, String newpasswordconfirm) {
        user = User.findByUsername(username)
        if (!user || !(user.username == username) || !(user.forgotPasswordToken == forgotPasswordToken)) {
            flash.error = message(code: 'msg.invalidtoken', default: 'Invalid Token')
            redirect(url: '/login/index') //user error
        } else {
            flash.error = Validators.valPassword(newpassword, newpasswordconfirm)
            if (flash.error) {
                redirect(url: "/login/resetpassword?username=${user.username}&forgotPasswordToken=${user.forgotPasswordToken}") //user error
            } else { //should be valid
                user.password = newpassword
                user.forgotPasswordToken = null
                try {
                    user.save(flush: true, failOnError: true)
                    flash.success = "${user.username}, your password was successfully reset. Try logging in with your new password"
                    redirect(username: "/login/index?username=${user.username}") //success
                } catch (Exception e) {
                    flash.error = message(code: 'msg.errorsavingpnewpassword', default: 'Error saving new password. Try again.')
                    logger.error(e.toString())
                    redirect(url: '/login/index') //unknown error
                }
            }
        }
    }
}
