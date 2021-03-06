package com.weatherwolf

import com.weatherwolf.security.EmailLog
import com.weatherwolf.security.Role
import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import com.weatherwolf.weather.Location
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder


@Secured(['ROLE_ADMIN'])
class AdminController {

    static allowedMethods = [
            changeadminstatus: 'POST',
            changeuserstatus : 'POST',
            disableuser      : 'POST',
            enableuser       : 'POST',
            deleteuser       : 'POST',
            locations        : 'GET', //can use a search parameter from url
            addlocation      : 'POST',
            deletelocation   : 'POST'
    ]

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    private def currentUser = refreshCurrentUser()

    private User refreshCurrentUser() {
        currentUser = User.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
    }

    /**
     * Show the home admin page
     */
    def index() {
        render(view: '/admin/index', model: [user: currentUser])
    }

    /**
     * Show the users page
     *
     * @render /admin/users
     */
    def users() {
        refreshCurrentUser()
        Set<User> userDataSet = null
        try {
            userDataSet = User.findAll(sort: 'username', max: 1000)
            logger.info("Admin [${currentUser.username}] viewed users table")
        } catch (Exception e) {
            flash.error = 'Could load users'
            logger.warn('Could load users')
            logger.error(e.toString())
        }
        render(view: '/admin/users', model: [user: currentUser, userDataSet: userDataSet])
    }

    /** POST only
     *
     * Change the admin status of a user.
     * Creates or removes a user role record of ROLE_ADMIN for a user
     *
     * @param userId
     * @param adminstatus
     * @redirect /admin/users
     */
    def changeadminstatus(Integer userId, String adminstatus) {
        try {
            User u = User.findById(userId)
            Role r = Role.findByAuthority('ROLE_ADMIN')
            UserRole ur = new UserRole()
            if (adminstatus == '1') {
                ur.create(u, r, true)
                flash.success = "${u.username} is now an admin"
            } else {
                ur.remove(u, r)
                flash.success = "${u.username} is no longer an admin"
            }
            logger.info("Admin [${currentUser.username}] changed ${u.username}'s admin status")
        } catch (Exception e) {
            flash.error = 'Could not change role'
            logger.warn('Could not change role')
            logger.error(e.toString())
        }
        redirect(url: '/admin/users')
    }

    /** POST only
     *
     * Change the user status of a user.
     * Creates or removes a user role record of ROLE_CLIENT, though gives a warning is role is removed
     *
     * @param userId
     * @param userstatus
     * @redirect /admin/users
     */
    def changeuserstatus(Integer userId, String userstatus) {
        try {
            User u = User.findById(userId)
            Role r = Role.findByAuthority('ROLE_CLIENT')
            UserRole ur = new UserRole()
            if (userstatus == '1') {
                ur.create(u, r, true)
                flash.success = "${u.username} is now a user"
            } else {
                ur.remove(u, r)
                logger.warn("A user role of ROLE_CLIENT was removed for user ${u.username}")
                flash.warning = "${u.username} is no longer a user. This is not recommended"
            }
            logger.info("Admin [${currentUser.username}] changed ${u.username}'s user status")
        } catch (Exception e) {
            flash.error = 'Could not change role'
            logger.warn('Could not change role')
            logger.error(e.toString())
        }
        redirect(url: '/admin/users')
    }

    /** POST only
     *
     * Disables the account for a user so that they cannot login
     *
     * @param userId
     * @redirect /admin/users
     */
    def disableuser(Integer userId) {
        if (!userId) {
            flash.error = 'Invalid User ID'
        } else {
            try {
                def u = User.findById(userId)
                u.enabled = false
                u.save(flush: true, failOnError: true)
                flash.success = "Disabled ${u.username}"
                logger.info("Admin [${currentUser.username}] disabled ${u.username}")
            } catch (Exception e) {
                flash.error = "Could not disable user"
            }
        }
        redirect(url: '/admin/users')
    }

    /** POST only
     *
     * Enables the account of a user so that they can log in
     *
     * @param userId
     * @redirect /admin/users
     */
    def enableuser(Integer userId) {
        if (!userId) {
            flash.error = 'Invalid User ID'
        } else {
            try {
                def u = User.findById(userId)
                u.enabled = true
                u.save(flush: true, failOnError: true)
                flash.success = "Enabled ${u.username}"
                logger.info("Admin [${currentUser.username}] enabled ${u.username}")
            } catch (Exception e) {
                flash.error = "Could not disable user"
            }
        }
        redirect(url: '/admin/users')
    }

    /** POST only
     *
     * Permanently deletes a user and all of their info.
     * First, all user roles are deleted,
     * then the user is delted
     *
     * @param userId
     * @redirect /admin/users
     */
    def deleteuser(Integer userId) {
        def u
        if (!userId) {
            flash.error = 'Invalid user id'
        } else {
            try {
                u = User.findById(userId)
                if (u) {
                    if (UserRole.where {
                        user == u
                    }.deleteAll()) { // <- delete user's roles first
                        u.delete(flush: true, failOnError: true) //the main delete
                    } else {
                        logger.warn('Could not delete user roles')
                        flash.error = 'Could not delete user roles'
                    }
                }
                flash.success = "Deleted ${u.username}"
                logger.info("Admin [${currentUser.username}] deleted ${u.username}")
            } catch (Exception e) {
                flash.warn = "Failed to delete user"
                logger.warn("Failed to delete user")
                logger.error(e.toString())
            }
        }
        redirect(url: "/admin/users")
    }

    /**
     * Shows the admin page for Search Logs
     *
     * @render /admin/searchlogs
     */
    def searchlogs() {
        Set<SearchLog> searchLogDataSet = null
        try {
            searchLogDataSet = SearchLog.findAll(max: 1000)
            logger.info("Admin [${currentUser.username}] viewed search logs table")
        } catch (Exception e) {
            flash.warn = "Failed to load search logs"
            logger.warn("Failed to load search logs")
            logger.error(e.toString())
        }
        render(view: '/admin/searchlogs', model: [user: currentUser, searchLogDataSet: searchLogDataSet])
    }

    /**
     * Shows the admin page for Email Logs
     *
     * @return
     */
    def emaillogs() {
        Set<EmailLog> emailLogDataSet = null
        try {
            emailLogDataSet = EmailLog.findAll(max: 1000)
            logger.info("Admin [${currentUser.username}] viewed email logs table")
        } catch (Exception e) {
            flash.warn = "Failed to load email logs"
            logger.warn("Failed to load email logs")
            logger.error(e.toString())
        }
        render(view: '/admin/emaillogs', model: [user: currentUser, emailLogDataSet: emailLogDataSet])
    }

    /** //GET optional
     *
     * Shows the admin page for Locations. Locations are used for Typeahead suggestions.
     * If a param q is not supplied, then no locations load to the page
     * If a param q is supplied and it contains a comma, it will run a search just like the searchbar would.
     * If it does not contain a comma, it will search by city only
     *
     * @param q
     * @return
     */
    def locations(String q) {
        refreshCurrentUser()
        def query
        Set<Location> locationDataSet
        try{
            if (!q) {
                locationDataSet = null
            } else if (q && q.contains(',')) {
                def l = new Location()
                l = WeatherUtils.assignCityStateProvinceCountry(q, l)
                if (l.city && !l.country && !l.stateProvince) {
                    query = Location.where {
                        (city ==~ "%${q}%")
                    }
                } else if (l.city && l.country && !l.stateProvince) {
                    query = Location.where {
                        (city ==~ "%${l.city}%") && ((country ==~ "%${l.country}%") || (stateProvince ==~ "%${l.country}%"))
                    }
                } else if (l.city && l.country && l.stateProvince) {
                    query = Location.where {
                        (city ==~ "%${l.city}%") && (country ==~ "%${l.country}%") && (stateProvince ==~ "%${l.stateProvince}%")
                    }
                } else {
                    query = Location.where {
                        (city ==~ "%${q}%")
                    }
                }
                locationDataSet = query.list(max: 6000)
            } else {
                query = Location.where {
                    city =~ "${q ?: 'A'}%"
                }
                locationDataSet = query.list(max: 6000)
            }
            logger.info("Admin [${currentUser.username}] viewed location table")
        } catch(Exception e){
            flash.warn = "Failed to load email logs"
            logger.warn("Failed to load locations with query [${q ?: ''}]")
            logger.error(e.toString())
        }
        render(view: '/admin/locations', model: [user: currentUser, locationDataSet: locationDataSet, locationCount: Location.count(), pages: ('A'..'Z')])
    }

    /** POST only
     *
     * Adds a location to the location table.
     * It will not add a duplicate location if the city, stateProvince, and country combination already exist
     *
     * @param q
     * @param city
     * @param stateProvince
     * @param country
     * @param lat
     * @param lng
     * @return
     */
    def addlocation(String q, String city, String stateProvince, String country, Float lat, Float lng) {
        if (!city || !stateProvince || !country) {
            flash.error = 'City, State / Province, and Country are required'
        } else if (Location.findByCityAndStateProvinceAndCountry(city, stateProvince, country)) {
            flash.warning = "${city}, ${stateProvince}, ${country} already exists in the database. See the table below"
            q = "${city},${stateProvince},${country}" //show the already existing city in db
        } else {
            try {
                def l = new Location(city: city, stateProvince: stateProvince, country: country, latitude: lat ?: 0.0F, longitude: lng ?: 0.0F)
                l.save(flush: true, failOnError: true)
                flash.success = "${city}, ${stateProvince}, ${country} was added"
                logger.info("Admin [${currentUser.username}] added location [${l.toString()}]")
            } catch (Exception e) {
                flash.error = "Could not add ${city}, ${stateProvince}, ${country}"
                logger.error(e.toString())
            }
        }
        redirect(url: "/admin/locations?q=${q ?: ''}")
    }

    /** POST only
     *
     * Deletes a location, preserves the search query so that the user can see the table after the deletion
     *
     * @param q
     * @param locationId
     * @return
     */
    def deletelocation(String q, Integer locationId) {
        if (!locationId) {
            flash.error = 'Invalid location id'
        } else {
            try {
                def l = Location.findById(locationId)
                l.delete(flush: true, failOnError: true)
                flash.success = "Deleted ${l.city}, ${l.stateProvince}, ${l.country}"
                logger.info("Admin [${currentUser.username}] deleted location [${l.toString()}]")
            } catch (Exception e) {
                flash.error = 'Failed to delete Location'
                logger.warn("Could not delete location")
                logger.error(e.toString())
            }
        }
        redirect(url: "/admin/locations?q=${q ?: 'A'}")
    }
}
