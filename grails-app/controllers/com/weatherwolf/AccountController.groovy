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


class AccountController {

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
    String currentUsername
    def user = new User()

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
                msg = 'valid token' //success
            }
        }
        render(view: '/account/resetpassword', model: [msg: msg])
    }

    @Secured("permitAll")
    def waitforemail() {
        logger.info("User visited account/waitforemail")
    }

    @Secured("permitAll") //POST only
    def sendpasswordresetemail(String username, String email) {
        logger.info("username: ${username} and email: ${email} attempting to reset password")
        if (!User.findByUsernameAndEmail(username, email)) {
            msg = message(code: 'msg.couldnotfindusernameemail', default: 'Could not find Username, or username and email do not match')
            render(view: '/account/forgotpassword', model: [msg: msg]) //user error
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
                render(view: '/account/waitforemail', model: [email: email]) //success
            } catch (Exception ex) {
                logger.warn("Could not send email")
                logger.error(ex.toString())
                msg = message(code: 'msg.emailfail', default: 'Email failed to send for unknown reason. Try again later')
                render(view: '/account/forgotpassword', model: [msg: msg]) //unknown failure
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
            render(view: '/account/login', model: [msg: msg]) //user error
        } else if (!Validators.valPassword(newpassword, newpasswordconfirm)) {
            msg = message(code: 'msg.passwordsdonotmatch', default: 'Passwords do not match or password requirements were not met.')
            render(view: '/account/resetpassword', model: [msg: msg, params: params]) //user error
        } else { //should be valid
            user.password = newpassword
            user.forgotPasswordToken = ''
            try {
                user.save(flush: true, failOnError: true)
                msg = "${user.username}, your password was successfully reset. Try loggin in with your new password"
                render(view: '/account/login', model: [msg: msg, username: user.username]) //success
            } catch (Exception e) {
                msg = message(code: 'msg.errorsavingpnewpassword', default: 'Error saving new password. Try again.')
                logger.error(e.toString())
                render(view: '/account/login', model: [msg: msg]) //unknown error
            }
        }
    }

    @Secured(['ROLE_CLIENT']) //POST only
    def changepassword(String oldpassword, String newpassword, String passwordconfirm) {
        if (!oldpassword || !Validators.valPassword(newpassword, passwordconfirm)) {
            msg = message(code: 'msg.failedpasswordrequirements', default: 'Failed password requirements') //user error
        } else { //should be valid
            try {
                currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
                user = User.findByUsername(currentUsername)
                user.password = newpassword
                user.passwordExpired = false
                user.save(flush: true, failOnError: true)
                msg = message(code: 'msg.passwordchanged', default: 'Password was changed')//success
            } catch (Exception e) {
                msg = message(code: 'msg.errorsavingpnewpassword', default: 'Error saving new password. Try again.') //unknown error
                logger.error(e.toString())
            }
        }
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    @Secured(['ROLE_CLIENT']) //POST only
    def updateemail(String email) {
        if (!Validators.valEmail(email)) {
            msg = message(code: 'msg.invalidemail', default: 'Invalid Email. This email may have already been taken.') //user error
        } else {
            try {
                currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
                user = User.findByUsername(currentUsername)
                user.email = email
                user.save(flush: true, failOnError: true)
                msg = message(code: 'msg.emailupdated', default: 'Email Updated') //success
            } catch (Exception e) {
                logger.warn("Could not save settings")
                logger.error(e.toString())
                msg = message(code: 'msg.emailnotupdated', default: 'Email could not be Updated') //unknown error
            }
        }
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    @Secured(['ROLE_CLIENT']) //POST only
    def savesettings(String lang, Character units, String location) {
        try {
            currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
            user = User.findByUsername(currentUsername)
            user.lang = lang
            user.units = units
            user.favoriteLocation = location
            user.save(flush: true, failOnError: true)
            msg = message(code: 'msg.settingssaved', default: 'Settings Saved') //success
        } catch (Exception e) {
            logger.warn("Could not save settings")
            logger.error(e.toString())
            msg = message(code: 'msg.settingsnotsaved', default: 'Settings could not be saved') //unkown error
        }
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    @Secured(['ROLE_CLIENT']) //POST only
    def updatelocation(String location){
        try {
            currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
            user = User.findByUsername(currentUsername)
            user.favoriteLocation = location
            user.save(flush: true, failOnError: true)
            msg = message(code: 'msg.settingssaved', default: 'Settings Saved') //success
        } catch (Exception e) {
            logger.warn("Could not save new Location")
            logger.error(e.toString())
            msg = message(code: 'msg.settingssaved', default: 'Location could not be saved') //unkown error
        }
        render(view: '/account/index', model: [user: user, msg: msg])
    }

    @Secured("permitAll") //POST only
    def register(String username, String email, String password, String passwordconfirm, String favoritelocation) {
        def u, ur
        logger.info("New user, ${username} attempting to register")
        msg = Validators.validateSignup(username, email, password, passwordconfirm)
        logger.info("message: " + msg)
        if (msg) {
            render(view: '/account/signup', model: [msg: msg, username: username, email: email, favoriteLocation: favoritelocation]) //user error
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
                render(view: '/account/login', model: [msg: msg]) //success
            } catch (Exception e) {
                msg = message(code: 'msg.invalidsignup', default: 'Signup not valid.')
                logger.warn("Could not create user: ${username}")
                logger.error(e.toString())
                render(view: '/account/signup', model: [msg: msg]) //unknown error
            }
        }
    }
}
