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


@Secured(['ROLE_ADMIN'])
class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())

    def index() {

    }

    def users() {
        Set<User> userDataSet = User.findAll(sort: 'username', max: 1000)
        render(view: '/admin/users', model: [userDataSet: userDataSet])
    }

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
        } catch (Exception e) {
            flash.error = 'Could not change role'
            logger.warn('Could not change role')
            logger.error(e.toString())
        }
        redirect(url: '/admin/users')
    }

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
                flash.success = "${u.username} is no longer a user"
            }
        } catch (Exception e) {
            flash.error = 'Could not change role'
            logger.warn('Could not change role')
            logger.error(e.toString())
        }
        redirect(url: '/admin/users')
    }

    def disableuser(Integer userId) {
        if (!userId) {
            flash.error = 'Invalid User ID'
        } else {
            try {
                def u = User.findById(userId)
                u.enabled = false
                u.save(flush: true, failOnError: true)
                flash.success = "Disabled ${u.username}"
            } catch (Exception e) {
                flash.error = "Could not disable user"
            }
        }
        redirect(url: '/admin/users')
    }

    def enableuser(Integer userId) {
        if (!userId) {
            flash.error = 'Invalid User ID'
        } else {
            try {
                def u = User.findById(userId)
                u.enabled = true
                u.save(flush: true, failOnError: true)
                flash.success = "Enabled ${u.username}"
            } catch (Exception e) {
                flash.error = "Could not disable user"
            }
        }
        redirect(url: '/admin/users')
    }

    def deleteuser(Integer userId) {
        def u, ur
        if (!userId) {
            flash.error = 'Invalid user id'
        } else {
            try {
                u = User.findById(userId)
                if (u) {
                    ur = UserRole.findByUser(u)
                    if (ur) {
                        ur.delete(flush: true, failOnError: true)
                    }
                    u.delete(flush: true, failOnError: true)
                }
                flash.success = "Deleted ${u.username}"
            } catch (Exception e) {
                flash.error = "Failed to delete user"
                logger.error(e.toString())
            }
        }
        redirect(url: "/admin/users")
    }

    def searchlogs() {
        Set<SearchLog> searchLogDataSet = SearchLog.findAll(max: 1000)
        render(view: '/admin/searchlogs', model: [searchLogDataSet: searchLogDataSet])
    }

    def emaillogs() {
        Set<EmailLog> emailLogDataSet = EmailLog.findAll(max: 1000)
        render(view: '/admin/emaillogs', model: [emailLogDataSet: emailLogDataSet])
    }

    def locations(String q) {
        def query
        Set<Location> locationDataSet

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
        render(view: '/admin/locations', model: [locationDataSet: locationDataSet, locationCount: Location.count(), pages: ('A'..'Z')])
    }

    def addlocation(String q, String city, String stateProvince, String country) {
        if (!city || !stateProvince || !country) {
            flash.error = 'All fields are required'
        } else if (Location.findByCityAndStateProvinceAndCountry(city, stateProvince, country)) {
            flash.warning = "${city}, ${stateProvince}, ${country} already exists in the database. See the table below"
            q = "${city},${stateProvince},${country}"
        } else {
            try {
                def l = new Location(city: city, stateProvince: stateProvince, country: country)
                l.save(flush: true, failOnError: true)
                flash.success = "${city}, ${stateProvince}, ${country} was added"
            } catch (Exception e) {
                flash.error = "Could not add ${city}, ${stateProvince}, ${country}"
                logger.error(e.toString())
            }
        }
        redirect(url: "/admin/locations?q=${q ?: 'A'}")
    }

    def deletelocation(String q, Integer locationId) {
        if (!locationId) {
            flash.error = 'Invalid location id'
        } else {
            try {
                def l = Location.findById(locationId)
                l.delete(flush: true, failOnError: true)
                flash.success = "Deleted ${l.city}, ${l.stateProvince}, ${l.country}"
            } catch (Exception e) {
                flash.error = 'Failed to delete Location'
                logger.error(e.toString())
            }
        }
        redirect(url: "/admin/locations?q=${q ?: 'A'}")
    }

}
