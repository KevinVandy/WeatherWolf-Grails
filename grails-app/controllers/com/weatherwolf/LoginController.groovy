package com.weatherwolf

import com.weatherwolf.security.EmailLog
import com.weatherwolf.security.User
import org.apache.commons.lang.RandomStringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.annotation.Secured


@Secured('permitAll')
class LoginController extends grails.plugin.springsecurity.LoginController {

    static allowedMethods = [
            resetpassword: 'GET', //needs token from url that comes from email link
            sendpasswordresetemail: 'POST',
            updatepassword: 'POST',
            changepassword: 'POST',
            updateemail: 'POST',
            savesettings: 'POST',
            updatelocation: 'POST',
            register: 'POST'
    ]

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    def mailService
    String msg = ''
    def user = new User()

    @Override
    def index(){
        if (isLoggedIn()) {
            redirect uri: conf.successHandler.defaultTargetUrl
        }
        else {
            render(view: '/login/index')
        }
    }

    @Override
    def auth() {
        if (isLoggedIn()) {
            redirect(controller: 'home', action: 'index')
            return
        }
        redirect(controller: 'login', action: 'index')
    }

    @Secured("permitAll")
    def forgotpassword() { //show forgot password page that will prompt to send email password reset
        logger.info("User visited login/forgotpassword")
    }

    //secure from email validation hash
    @Secured("permitAll") //GET only
    def resetpassword() { //show reset password page
        logger.info("User ${params.username} visited account/resetpassword")
        if (!(params.username && params.forgotPasswordToken)) {
            msg = message(code: 'msg.invalidlink', default: 'Invalid Link') //user error
        } else {
            user = User.findByUsername(params.username)
            if (!(user && user.forgotPasswordToken && user.forgotPasswordToken.equalsIgnoreCase(params.forgotPasswordToken))) {
                msg = message(code: 'msg.invalidtoken', default: 'Invalid Token') //user error
            } else {
                msg = '' //success
            }
        }
        render(view: '/login/resetpassword', model: [msg: msg])
    }

    @Secured("permitAll")
    def waitforemail() {
        logger.info("User visited login/waitforemail")
    }

    @Secured("permitAll") //POST only
    def sendpasswordresetemail(String username, String email) {
        logger.info("username: ${username} and email: ${email} attempting to reset password")
        if (!User.findByUsernameAndEmail(username, email)) {
            msg = message(code: 'msg.couldnotfindusernameemail', default: 'Could not find Username, or username and email do not match')
            render(view: '/login/forgotpassword', model: [msg: msg]) //user error
        } else { //should be valid
            try {
                logger.info("attempting to send password reset to ${email}")
                user = User.findByUsername(username)
                user.forgotPasswordToken = RandomStringUtils.random(50, (('A'..'Z') + ('0'..'9')).join().toCharArray())
                user.save(flush: true, failOnError: true)
                def e = new EmailLog(toAddress: email, subject: 'Weather Wolf Password Reset', body: "Click <a href='http://localhost:9999/account/resetpassword?username=${user.username}&forgotPasswordToken=${user.forgotPasswordToken}'>here</a> to reset your password")
                mailService.sendMail {
                    to e.toAddress
                    subject e.subject
                    html e.body
                }
                e.timeSent = new Date()
                e.save(flush: true, failOnError: true)
                logger.info("Email sent to ${email}")
                render(view: '/login/waitforemail', model: [email: email]) //success
            } catch (Exception ex) {
                logger.warn("Could not send email")
                logger.error(ex.toString())
                msg = message(code: 'msg.emailfail', default: 'Email failed to send for unknown reason. Try again later')
                render(view: '/login/forgotpassword', model: [msg: msg]) //unknown failure
            }
        }
    }

    //not secured because this is for users who forgot their password
    @Secured("permitAll") //POST only
    //for not logged in users with forgotPasswordToken
    def updatepassword(String username, String forgotPasswordToken, String newpassword, String newpasswordconfirm) {
        user = User.findByUsername(username)
        if (!user || !user.username == username || !(user.forgotPasswordToken == forgotPasswordToken)) {
            msg = message(code: 'msg.invalidtoken', default: 'Invalid Token')
            render(view: '/login/index', model: [msg: msg]) //user error
        } else if (!Validators.valPassword(newpassword, newpasswordconfirm)) {
            msg = message(code: 'msg.passwordsdonotmatch', default: 'Passwords do not match or password requirements were not met.')
            render(view: '/login/resetpassword', model: [msg: msg, params: params]) //user error
        } else { //should be valid
            user.password = newpassword
            user.forgotPasswordToken = ''
            try {
                user.save(flush: true, failOnError: true)
                msg = "${user.username}, your password was successfully reset. Try loggin in with your new password"
                render(view: '/login/index', model: [msg: msg, username: user.username]) //success
            } catch (Exception e) {
                msg = message(code: 'msg.errorsavingpnewpassword', default: 'Error saving new password. Try again.')
                logger.error(e.toString())
                render(view: '/login/index', model: [msg: msg]) //unknown error
            }
        }
    }
}
