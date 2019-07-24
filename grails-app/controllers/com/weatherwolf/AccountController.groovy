package com.weatherwolf

import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder


@Secured(['ROLE_CLIENT'])
class AccountController {

    static allowedMethods = [
            changepassword: 'POST',
            updateemail   : 'POST',
            savesettings  : 'POST',
            updatelocation: 'POST',
            deleteaccount: 'POST'
    ]

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    def currentUsername
    def user = new User()

    def index() { //show user's account settings page
        logger.info("User visited account/index")
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        render(view: '/account/index', model: [user: user])
    }

    //POST only
    def changepassword(String oldpassword, String newpassword, String newpasswordconfirm) {
        flash.error = Validators.valPassword(newpassword, newpasswordconfirm)
        if (oldpassword && !flash.error) {
            try {
                currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
                user = User.findByUsername(currentUsername)
                user.password = newpassword
                user.passwordExpired = false
                user.save(flush: true, failOnError: true)
                flash.success = message(code: 'msg.passwordchanged', default: 'Password was changed') //success
            } catch (Exception e) {
                flash.error = message(code: 'msg.errorsavingpnewpassword', default: 'Error saving new password. Try again.') //unknown error
                logger.error(e.toString())
            }
        }
        render(view: '/account/index', model: [user: user])
    }

    //POST only
    def updateemail(String email) {
        flash.error = Validators.valEmail(email)
        if (!flash.error) { //not user error
            try {
                currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
                user = User.findByUsername(currentUsername)
                user.email = email
                user.save(flush: true, failOnError: true)
                flash.success = message(code: 'msg.emailupdated', default: 'Email Updated') //success
            } catch (Exception e) {
                logger.warn("Could not save settings")
                logger.error(e.toString())
                flash.error = message(code: 'msg.emailnotupdated', default: 'Email could not be Updated') //unknown error
            }
        }
        render(view: '/account/index', model: [user: user])
    }

    //POST only
    def savesettings(String lang, Character units, String location) {
        if (!lang || !units || !location) {
            flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required') //unkown error
        } else {
            try {
                currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
                user = User.findByUsername(currentUsername)
                user.lang = lang
                user.units = units
                user.favoriteLocation = location
                user.save(flush: true, failOnError: true)
                flash.success = message(code: 'msg.settingssaved', default: 'Settings Saved') //success
            } catch (Exception e) {
                user = User.findByUsername(currentUsername) //refresh from db
                logger.warn("Could not save settings")
                logger.error(e.toString())
                flash.error = message(code: 'msg.settingsnotsaved', default: 'Settings could not be saved') //unkown error
            }
        }
        render(view: '/account/index', model: [user: user])
    }

    //POST only
    def updatelocation(String favoritelocation) {
        try {
            currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
            user = User.findByUsername(currentUsername)
            user.favoriteLocation = favoritelocation
            user.save(flush: true, failOnError: true)
            msg = message(code: 'msg.settingssaved', default: 'Settings Saved') //success
            flash.success = msg
        } catch (Exception e) {
            logger.warn("Could not save new Location")
            logger.error(e.toString())
            flash.error = message(code: 'msg.settingssaved', default: 'Location could not be saved') //unkown error
        }
        render(view: '/account/index', model: [user: user])
    }

    //POST only, username only from session
    def deleteaccount() {
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        try {
            def userRole = UserRole.findByUser(user)
            userRole.delete(flush: true, failOnError: true) //TODO Rollback on error?
            user.delete(flush: true, failOnError: true) //TODO Rollback on error?
            flash.success = message(code: 'msg.accountdeleted', default: 'Account Deleted')
            redirect(url: '/logout')
        } catch (Exception e) {
            flash.error = message(code: 'msg.couldnotdeleteaccount', default: 'Could not delete your account')
            render(view: '/account/index', model: [user: user])
        }
    }

}
