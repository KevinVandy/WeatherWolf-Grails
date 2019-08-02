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

    /**
     * Shows the login page for non-logged in users.
     * If a user is already logged in, then it redirects to the success login page
     *
     * @return
     */
    @Override
    def index() {
        if (isLoggedIn()) {
            redirect uri: conf.successHandler.defaultTargetUrl
        } else {
            render(view: '/login/index')
        }
    }

    /**
     * Whenever a user tries to view a page that they are not permitted to view, they go through this method
     * Overriding the Spring security method that shows its own login form.
     * Now this just redirects to our own custom login form.
     *
     * @return
     */
    @Override
    def auth() {
        if (isLoggedIn()) {
            redirect(controller: 'home', action: 'index')
        } else {
            redirect(controller: 'login', action: 'index')
        }
    }

    /**
     * Whenever a user fails to login, they go through this method.
     * Overriding the Spring Security method that shows its own login errors and form.
     * Now this shows the login error message on owr own login form
     *
     * @return
     */
    @Override
    def authfail() {
        def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
        if (exception) {
            if (exception instanceof AccountExpiredException) {
                flash.error = message(code: 'springSecurity.errors.login.expired', default: "Account Expired") as String
            } else if (exception instanceof CredentialsExpiredException) {
                flash.error = message(code: 'springSecurity.errors.login.passwordExpired', default: "Password Expired") as String
            } else if (exception instanceof DisabledException) {
                flash.error = message(code: 'springSecurity.errors.login.disabled', default: "Account Disabled") as String
            } else if (exception instanceof LockedException) {
                flash.error = message(code: 'springSecurity.errors.login.locked', default: "Account Locked") as String
            } else {
                flash.error = message(code: 'springSecurity.errors.login.fail', default: "Authentication Failure") as String
            }
        }
        render(view: '/login/index')
    }

    /**
     * Shows the Forgot Password page that has a form to request a password reset
     *
     * @return
     */
    def forgotpassword() { //show forgot password page that will prompt to send email password reset
        logger.info("User visited login/forgotpassword")
    }

    /** POST only
     *
     * If a user provides a matching email and username
     * Then a password reset will be sent to that email.
     * Sends the email via the grails email service.
     * Generates a forgotPasswordToken for the user,
     * puts the token in the email link and in the user database.
     *
     * @param username
     * @param email
     * @return
     */
    def sendpasswordresetemail(String username, String email) {
        logger.info("username: ${username} and email: ${email} attempting to reset password")
        if (!User.findByUsernameAndEmail(username, email)) {
            flash.error = message(code: 'msg.couldnotfindusernameemail', default: 'Could not find Username, or username and email do not match') as String
            redirect(url: '/login/forgotpassword') //user error
        } else { //should be valid
            try {
                logger.info("attempting to send password reset to ${email}")
                user = User.findByUsername(username)
                user.forgotPasswordToken = RandomStringUtils.random(50, (('A'..'Z') + ('0'..'9')).join().toCharArray())
                user.save(flush: true, failOnError: true)
                def e = new EmailLog(toAddress: email, subject: 'Weather Wolf Password Reset', body: "Click <a href='http://localhost:8080/login/resetpassword?username=${user.username}&forgotPasswordToken=${user.forgotPasswordToken}'>here</a> to reset your password")
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
                flash.error = message(code: 'msg.emailfail', default: 'Email failed to send for unknown reason. Try again later') as String
                redirect(url: '/login/forgotpassword') //unknown failure
            }
        }
    }

    /**
     * After a password reset email is sent, this page will display
     *
     * @return
     */
    def waitforemail() {
        logger.info("User visited login/waitforemail")
    }

    /** GET only
     *
     * After a user clicks the link from a password reset email, this page will show.
     * The reset password form will only show to the user if they have a valid username and token.
     * A valid token will also be needed submitting the new password.
     *
     * @return
     */
    def resetpassword() {
        logger.info("User ${params.username} visited account/resetpassword")
        boolean validToken = false
        if (!(params.username && params.forgotPasswordToken)) {
            flash.error = message(code: 'msg.invalidlink', default: 'Invalid Link')  as String //user error
        } else {
            user = User.findByUsername(params.username)
            if (!(user && user.forgotPasswordToken && user.forgotPasswordToken.equalsIgnoreCase(params.forgotPasswordToken))) {
                flash.error = message(code: 'msg.invalidtoken', default: 'Invalid Token')  as String //user error
            } else {
                validToken = true
            }
        }
        render(view: '/login/resetpassword', model: [validToken: validToken])
    }

    /** POST only
     *
     * Changes the password for a user who submitted a reset password request
     * The username and forgotPasswordToken must match the database before the new password is accepted
     * The new password must pass the password requirements
     *
     * @param username
     * @param forgotPasswordToken
     * @param newpassword
     * @param newpasswordconfirm
     * @return
     */
    def updatepassword(String username, String forgotPasswordToken, String newpassword, String newpasswordconfirm) {
        user = User.findByUsername(username)
        if (!user || !(user.username == username) || !(user.forgotPasswordToken == forgotPasswordToken)) {
            flash.error = message(code: 'msg.invalidtoken', default: 'Invalid Token') as String
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
                    redirect(url: "/login/index?username=${user.username}") //success
                } catch (Exception e) {
                    flash.error = message(code: 'msg.errorsavingpnewpassword', default: 'Error saving new password. Try again.') as String
                    logger.error(e.toString())
                    redirect(url: '/login/index') //unknown error
                }
            }
        }
    }
}
