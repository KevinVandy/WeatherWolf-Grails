package com.weatherwolf

import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder


@Secured(['ROLE_CLIENT', 'ROLE_ADMIN'])
class AccountController {

    static allowedMethods = [
            changepassword: 'POST',
            updateemail   : 'POST',
            savesettings  : 'POST',
            updatelocation: 'POST',
            deletesearchhistory: 'POST',
            deleteaccount : 'POST'
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
        if(email && email == user.email){
            flash.warning = 'Email not changed because that is your same email'
        } else {
            flash.error = Validators.valEmail(email)
            if (!flash.error) { //not user error
                try {
                    user.email = email
                    user.save(flush: true, failOnError: true)
                    flash.success = message(code: 'msg.emailupdated', default: 'Email Updated') //success
                } catch (Exception e) {
                    logger.warn("Could not save settings")
                    logger.error(e.toString())
                    flash.error = message(code: 'msg.emailnotupdated', default: 'Email could not be Updated') //unknown error
                }
            }
        }
        render(view: '/account/index', model: [user: user])
    }

    //POST only
    def savesettings(String lang, Character units, String location) {
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        if (user.lang == lang && user.units == units && user.favoriteLocation == location) {
            flash.warning = message(code: 'msg.nosettingschanged', default: 'No settings were changed')
        } else {
            if (!lang) {
                flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required')
            } else {
                user.lang = lang
            }
            if (!units) {
                flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required')
            } else {
                user.units = units
            }
            if (!location) {
                flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required')
            } else {
                user.favoriteLocation = location
            }
            try {
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
            user.favoriteLocation = favoritelocation
            user.save(flush: true, failOnError: true)
            flash.success = message(code: 'msg.settingssaved', default: 'Settings Saved') //success
        } catch (Exception e) {
            logger.warn("Could not save new Location")
            logger.error(e.toString())
            flash.error = message(code: 'msg.settingsnotsaved', default: 'Location could not be saved') //unkown error
        }
        render(view: '/account/index', model: [user: user])
    }

    def deletesearchhistory() {
        try {
            user.searchLog.each {
                it.delete(flush: true, failOnError: true)
            }
            flash.success = message(code: 'msg.searchhistorydeleted', default: 'Search History Deleted')
        } catch (Exception e) {
            flash.error = message(code: 'msg.couldnotdeletesearchhistory', default: 'Could not delete search history')
        }
        currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
        user = User.findByUsername(currentUsername)
        render(view: '/account/index', model: [user: user])
    }

    //POST only, username only from session
    def deleteaccount() {
        try {
            currentUsername = SecurityContextHolder.getContext().getAuthentication().getName()
            user = User.findByUsername(currentUsername)
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
