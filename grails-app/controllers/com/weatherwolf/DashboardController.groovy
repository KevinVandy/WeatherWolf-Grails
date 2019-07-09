package com.weatherwolf

import grails.plugin.springsecurity.annotation.Secured


class DashboardController {

    @Secured(['ROLE_CLIENT'])
    def index() {

    }

    @Secured(['ROLE_CLIENT'])
    def profile() {

    }

    @Secured(['ROLE_CLIENT'])
    def result() {

    }

}
