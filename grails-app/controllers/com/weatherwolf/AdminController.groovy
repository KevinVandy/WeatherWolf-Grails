package com.weatherwolf

import com.weatherwolf.security.EmailLog
import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import com.weatherwolf.weather.Location
import grails.plugin.springsecurity.annotation.Secured
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Secured(['ROLE_ADMIN'])
class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())

    def index(){

    }

    def users() {
        Set<User> userDataSet = User.findAll(max: 1000)
        render(view: '/admin/users', model: [userDataSet: userDataSet])
    }

    def deleteuser(){

    }

    def searchlogs() {
        Set<SearchLog> searchLogDataSet = SearchLog.findAll(max: 1000)
        render(view: '/admin/searchlogs', model: [searchLogDataSet: searchLogDataSet])
    }

    def emaillogs() {
        Set<EmailLog> emailLogDataSet = EmailLog.findAll(max: 1000)
        render(view: '/admin/emaillogs', model: [emailLogDataSet: emailLogDataSet])
    }

    def locations(int max, int offset){
        Set<String> countries
        Set<Location> locationDataSet = Location.findAll(offset: offset ?: 0, max: max ?: 1000)
        locationDataSet.sort{it.country}
        render(view: '/admin/locations', model: [locationDataSet: locationDataSet, locationCount: Location.count()])
    }

    def addlocation(){

    }

    def deletelocation() {

    }



}
