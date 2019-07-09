package com.weatherwolf

import grails.plugin.springsecurity.annotation.Secured


class AccountController {

    @Secured(['ROLE_CLIENT'])
    def index() { }
}
