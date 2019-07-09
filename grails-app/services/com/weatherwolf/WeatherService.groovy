package com.weatherwolf

import com.weatherwolf.search.SearchResult
import com.weatherwolf.weather.CurrentWeather
import com.weatherwolf.weather.DayForecast
import grails.gorm.transactions.Transactional

@Transactional
class WeatherService {

    public static final String BASE = 'https://api.apixu.com/v1/forecast.xml'
    public static final String KEY = 'f91adb3427b540b39d7142223190307' //daily

    Integer numDays = 5

    def fillWeather(SearchResult sr) {

        //if location lat and long have not been filled in yet
        if (sr.location.latitude > -0.1 && sr.location.latitude < 0.1 && sr.location.longitude > -0.1 && sr.location.longitude < 0.1) {
            def geocodeService = new GeocodeService()
            geocodeService.fillLatLng(sr.location)
        }

        //main query part of URL
        String qs = "q=${sr.location.latitude},${sr.location.longitude}"

        //optional parameters of the URL
        String op = "days=${numDays}"

        //assemble the full URL
        String fullURL = "${BASE}?${qs}&${op}&key=${KEY}"
        println(fullURL) //debug log

        //Parse the XML response
        def root = new XmlSlurper().parse(fullURL)

        //instantiate
        sr.currentWeather = new CurrentWeather()
        sr.dayForecasts = []

        //fill in weather data for the search result
        try {
            //fill in full location name
            sr.location.city = root.location.name
            sr.location.stateProvince = root.location.region
            sr.location.country = root.location.country

            //fill current weather
            sr.currentWeather.condition = root.current.condition.text
            sr.currentWeather.icon = root.current.condition.icon
            sr.currentWeather.temperature = (root.current.temp_c).toFloat()
            sr.currentWeather.humidity = (root.current.humidity).toInteger()
            sr.currentWeather.windSpeed = (root.current.wind_mph).toFloat()
            sr.currentWeather.windDirection = root.current.wind_dir

            //fill day forecasts
            (0..<numDays).each {
                def df = new DayForecast()
                df.date = Date.parse('yyyy-mm-dd', (String) root.forecast.forecastday[it].date)
                df.condition = root.forecast.forecastday[it].day.condition.text
                df.iconURL = root.forecast.forecastday[it].day.condition.icon
                df.minTemp = root.forecast.forecastday[it].day.mintemp_c.toFloat()
                df.maxTemp = root.forecast.forecastday[it].day.maxtemp_c.toFloat()
                df.precipitation = root.forecast.forecastday[it].day.totalprecip_in.toFloat()
                df.windSpeed = root.forecast.forecastday[it].day.maxwind_mph.toFloat()
                sr.dayForecasts.push(df)
            }
        } catch (Exception ex) {
            println("exception\n${ex}")
        }
        return sr
    }


}
