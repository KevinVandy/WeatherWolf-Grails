package com.weatherwolf

import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.core.context.SecurityContextHolder


@Secured(['ROLE_CLIENT'])
class AccountController {

    static allowedMethods = [
            changepassword     : 'POST',
            updateemail        : 'POST',
            savesettings       : 'POST',
            updatelocation     : 'POST',
            deletesearchhistory: 'POST',
            deleteaccount      : 'POST'
    ]

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    private def currentUser = new User()

    private User refreshCurrentUser() {
        currentUser = User.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
    }

    /**
     * Show the User's Account Settings page with their own personal info
     *
     * @render /account/idex
     */
    def index() { //show user's account settings page
        logger.info("User visited account/index")
        refreshCurrentUser()
        render(view: '/account/index', model: [user: currentUser])
    }

    /** POST only
     *
     * A user changes their password from the account settings page.
     * User must verify old password and confirm new password and pass password requirements.
     *
     * @param oldpassword
     * @param newpassword
     * @param newpasswordconfirm
     * @redirect /account/index
     */
    def changepassword(String oldpassword, String newpassword, String newpasswordconfirm) {
        flash.error = Validators.valPassword(newpassword, newpasswordconfirm)
        if (oldpassword && !flash.error) { //TODO - verify old password if possible
            try {
                refreshCurrentUser()
                currentUser.password = newpassword
                currentUser.passwordExpired = false
                currentUser.save(flush: true, failOnError: true)
                flash.success = message(code: 'msg.passwordchanged', default: 'Password was changed') as String //success
                logger.info("User [${currentUser.username}] changed their password")
            } catch (Exception e) {
                flash.error = message(code: 'msg.errorsavingpnewpassword', default: 'Error saving new password. Try again.') as String //unknown error
                logger.warn('Could not change password')
                logger.error(e.toString())
            }
        }
        redirect(url: '/account/index')
    }

    /** POST only
     *
     * Update the user's email address from the account settings page.
     * Will only update the database after verifying the email is new and valid
     *
     * @param email
     * @redirect /account/index
     */
    def updateemail(String email) {
        refreshCurrentUser()
        if (email && email == currentUser.email) {
            flash.warning = message(code: 'msg.emailnotchangedsame', default: 'Email not changed because that is your same email') as String
        } else {
            flash.error = Validators.valEmail(email)
            if (!flash.error) { //if no user error
                try {
                    currentUser.email = email.trim()
                    currentUser.save(flush: true, failOnError: true)
                    flash.success = message(code: 'msg.emailupdated', default: 'Email Updated') as String //success
                    logger.info("User [${currentUser.username}] changed their email address")
                } catch (Exception e) {
                    logger.warn("Could not save settings")
                    logger.error(e.toString())
                    flash.error = message(code: 'msg.emailnotupdated', default: 'Email could not be Updated') as String //unknown error
                }
            }
        }
        redirect(url: '/account/index')
    }

    /** POST only
     *
     * Save the user's settings from the user's settings page.
     * Only valid changes will be applied.
     *
     * @param lang
     * @param units
     * @param location
     * @redirect /account/index
     */
    def savesettings(String lang, Character units, String location) {
        refreshCurrentUser()
        if (currentUser.lang == lang && currentUser.units == units && currentUser.favoriteLocation == location) {
            flash.warning = message(code: 'msg.nosettingschanged', default: 'No settings were changed') as String
        } else {
            if (!lang) {
                flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required') as String
            } else {
                currentUser.lang = lang
            }
            if (!units) {
                flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required') as String
            } else {
                currentUser.units = units
            }
            if (!location) {
                flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required') as String
            } else {
                currentUser.favoriteLocation = location.trim()
            }
            try {
                currentUser.save(flush: true, failOnError: true)
                flash.success = message(code: 'msg.settingssaved', default: 'Settings Saved') as String //success
                logger.info("User [${currentUser.username}] changed their settings")
            } catch (Exception e) {
                refreshCurrentUser()
                logger.warn("Could not save settings")
                logger.error(e.toString())
                flash.error = message(code: 'msg.settingsnotsaved', default: 'Settings could not be saved') as String //unkown error
            }
        }
        redirect(url: '/account/index')
    }

    def changelang(String lang) {
        refreshCurrentUser()
        if (!lang) {
            flash.error = message(code: 'msg.couldnotchangelanguage', default: 'Could not change language') as String
        } else if (lang == currentUser.lang) {
            flash.warning = message(code: 'msg.samelanguage', default: 'Language stayed the same') as String
        } else {
            currentUser.lang = lang
            try {
                currentUser.save(flush: true, failOnError: true)
                flash.success = message(code: 'msg.languagechanged', default: 'Language Changed') as String //success
                logger.info("User [${currentUser.username}] changed Locale to ${lang}")
            } catch (Exception e) {
                refreshCurrentUser()
                logger.warn("Could not change language")
                logger.error(e.toString())
                flash.error = message(code: 'msg.couldnotchangelanguage', default: 'Language could not be changed') as String //unkown error
            }
        }
        LocaleContextHolder.setLocale(new Locale(currentUser.lang)) //load language from user's settings
        redirect(url: "/account/index?lang=${lang}")
    }

    /** POST only
     *
     * Update the user's favorite location from either the search result page or the settings page.
     *
     * @param favoritelocation
     * @redirect /account/index
     */
    def updatelocation(String favoritelocation) {
        refreshCurrentUser()
        try {
            currentUser.favoriteLocation = favoritelocation.trim()
            currentUser.save(flush: true, failOnError: true)
            flash.success = message(code: 'msg.settingssaved', default: 'Settings Saved') as String //success
            logger.info("User [${currentUser.username}] changed their favorite location to ${favoritelocation}")
        } catch (Exception e) {
            logger.warn("Could not save new Location")
            logger.error(e.toString())
            flash.error = message(code: 'msg.settingsnotsaved', default: 'Location could not be saved') as String //unknown error
        }
        redirect(url: '/account/index')
    }

    /** POST only
     *
     * Delete the user's search history from the user's settings page
     * Should delete with one db ping now
     *
     * @redirect /account/index
     */
    def deletesearchhistory() {
        refreshCurrentUser()
        try {
            refreshCurrentUser()
            SearchLog.where {
                user == currentUser
            }.deleteAll()
            flash.success = message(code: 'msg.searchhistorydeleted', default: 'Search History Deleted') as String
            logger.info("User [${currentUser.username}] deleted all of their search history")
        } catch (Exception e) {
            flash.error = message(code: 'msg.couldnotdeletesearchhistory', default: 'Could not delete search history') as String
            logger.warn("Could not delete search history for ${currentUser.username}")
            logger.error(e.toString())
        }
        redirect(url: '/account/index')
    }

    /** POST only
     *
     * Delete's a user's account. Only delete's a user by the session id of the logged in user.
     * First delete all user roles for the user,
     * then delete the user. All other info associated should cascade delete.
     *
     * @redirect /logout on success, /account/index on fail
     */
    def deleteaccount(/*no params*/) {
        try {
            refreshCurrentUser()
            if (UserRole.where {
                user == currentUser
            }.deleteAll()) {
                currentUser.delete(flush: true, failOnError: true) //the main delete
            } else {
                logger.warn('Could not delete user roles')
            }
            flash.success = message(code: 'msg.accountdeleted', default: 'Account Deleted') as String
            logger.info("User [${currentUser.username}] deleted their account")
            session.invalidate()
            redirect(url: '/home')
        } catch (Exception e) {
            flash.error = message(code: 'msg.couldnotdeleteaccount', default: 'Could not delete your account') as String
            logger.warn("Could not delete search account for ${currentUser.username}")
            logger.error(e.toString())
            redirect(url: '/account/index')
        }
    }

}
