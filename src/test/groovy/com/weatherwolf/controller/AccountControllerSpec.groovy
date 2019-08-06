package com.weatherwolf.controller

import com.weatherwolf.AccountController
import com.weatherwolf.security.EmailLog
import com.weatherwolf.security.Role
import com.weatherwolf.security.SearchLog
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import com.weatherwolf.weather.CurrentWeather
import com.weatherwolf.weather.DayForecast
import com.weatherwolf.weather.Location
import com.weatherwolf.weather.SearchResult
import grails.gorm.multitenancy.CurrentTenant
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

@CurrentTenant
class AccountControllerSpec extends Specification implements ControllerUnitTest<AccountController> {



}
