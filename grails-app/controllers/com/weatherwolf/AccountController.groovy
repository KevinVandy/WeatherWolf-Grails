package com.weatherwolf

import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
        if (oldpassword && !flash.error) {
            try {
                refreshCurrentUser()
                currentUser.password = newpassword
                currentUser.passwordExpired = false
                currentUser.save(flush: true, failOnError: true)
                flash.success = message(code: 'msg.passwordchanged', default: 'Password was changed') //success
            } catch (Exception e) {
                flash.error = message(code: 'msg.errorsavingpnewpassword', default: 'Error saving new password. Try again.') //unknown error
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
            flash.warning = 'Email not changed because that is your same email'
        } else {
            flash.error = Validators.valEmail(email)
            if (!flash.error) { //not user error
                try {
                    currentUser.email = email
                    currentUser.save(flush: true, failOnError: true)
                    flash.success = message(code: 'msg.emailupdated', default: 'Email Updated') //success
                } catch (Exception e) {
                    logger.warn("Could not save settings")
                    logger.error(e.toString())
                    flash.error = message(code: 'msg.emailnotupdated', default: 'Email could not be Updated') //unknown error
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
            flash.warning = message(code: 'msg.nosettingschanged', default: 'No settings were changed')
        } else {
            if (!lang) {
                flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required')
            } else {
                currentUser.lang = lang
            }
            if (!units) {
                flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required')
            } else {
                currentUser.units = units
            }
            if (!location) {
                flash.error = message(code: 'msg.allfieldsrequired', default: 'All fields are required')
            } else {
                currentUser.favoriteLocation = location
            }
            try {
                currentUser.save(flush: true, failOnError: true)
                flash.success = message(code: 'msg.settingssaved', default: 'Settings Saved') //success
            } catch (Exception e) {
                currentUser = User.findByUsername(currentUsername) //refresh from db
                logger.warn("Could not save settings")
                logger.error(e.toString())
                flash.error = message(code: 'msg.settingsnotsaved', default: 'Settings could not be saved') //unkown error
            }
        }
        redirect(url: '/account/index')
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
            currentUser.favoriteLocation = favoritelocation
            currentUser.save(flush: true, failOnError: true)
            flash.success = message(code: 'msg.settingssaved', default: 'Settings Saved') //success
        } catch (Exception e) {
            logger.warn("Could not save new Location")
            logger.error(e.toString())
            flash.error = message(code: 'msg.settingsnotsaved', default: 'Location could not be saved') //unkown error
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
        try {
            refreshCurrentUser()
            SearchLog.where {
                user == currentUser
            }.deleteAll(flush: true, failOnError: true)
            flash.success = message(code: 'msg.searchhistorydeleted', default: 'Search History Deleted')
        } catch (Exception e) {
            flash.error = message(code: 'msg.couldnotdeletesearchhistory', default: 'Could not delete search history')
            logger.warn("Could not delete search history for ${currentUser.username}")
            logger.error(e.toString())
        }
        redirect(url: '/account/index')
    }

    /** POST only
     *
     * Delete's a user's account. Only delete's a user by the session id of the logged in user.
     * first delete all user roles for the user,
     * then delete the user. All other info associated should cascade delete.
     *
     * @redirect /logout on success, /account/index on fail
     */
    def deleteaccount() {
        try {
            refreshCurrentUser()
            if (!UserRole.where {
                user == currentUser
            }.deleteAll()){
                logger.warn('Could not delete user roles')
            } else {
                currentUser.delete(flush: true, failOnError: true) //the main delete
            }
            flash.success = message(code: 'msg.accountdeleted', default: 'Account Deleted')
            session.invalidate()
            redirect(url: '/home')
        } catch (Exception e) {
            flash.error = message(code: 'msg.couldnotdeleteaccount', default: 'Could not delete your account')
            logger.warn("Could not delete search account for ${currentUser.username}")
            logger.error(e.toString())
            redirect(url: '/account/index')
        }
    }

}
