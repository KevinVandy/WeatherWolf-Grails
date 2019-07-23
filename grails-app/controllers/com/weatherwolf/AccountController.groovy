package com.weatherwolf

import com.weatherwolf.security.User
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder


@Secured(['ROLE_CLIENT'])
class AccountController {

    static allowedMethods = [
            changepassword        : 'POST',
            updateemail           : 'POST',
            savesettings          : 'POST',
            updatelocation        : 'POST'
    ]

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    String msg = ''
    String currentUsername
    def user = new User()

    def index() { //show user's account settings page
        logger.info("User visited account/index")
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        render(view: '/account', model: [user: user])
    }

    //POST only
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

    //POST only
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

    //POST only
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

    //POST only
    def updatelocation(String location) {
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

}
