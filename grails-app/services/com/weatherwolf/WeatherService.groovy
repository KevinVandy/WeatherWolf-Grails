package com.weatherwolf

import com.weatherwolf.weather.SearchResult
import com.weatherwolf.weather.CurrentWeather
import com.weatherwolf.weather.DayForecast
import grails.gorm.transactions.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder


@Transactional
class WeatherService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())
    public static final String BASE = 'https://api.apixu.com/v1/forecast.xml'
    public static final String KEY = 'f91adb3427b540b39d7142223190307'
    def root

    Integer numDays = 5

    /**
     * Fills in the full location, the current weather, and a 5 day forecast into the search result object
     *
     * @param sr
     * @return
     */
    def fillWeather(SearchResult sr) {

        String qs
        String lang = LocaleContextHolder.getLocale()

        qs = "q=${URLEncoder.encode(sr.location.toString(), 'UTF-8')}"

        //optional parameters of the URL
        String op = "days=${numDays}&lang=${lang}"

        //assemble the full URL
        String fullURL = "${BASE}?${qs}&${op}&key=${KEY}"
        logger.info("Weather URL: ${fullURL}")

        //Parse the XML response
        try {
            root = new XmlSlurper().parse(fullURL)
        } catch (Exception e) {
            root = null
            logger.warn("Could not parse XML response at all")
        }

        //fill in weather data for the search result
        if (root) {
            try {
                //instantiate
                sr.currentWeather = new CurrentWeather()
                sr.dayForecasts = []

                //fill in full location name
                logger.debug("Filling in full location info")
                sr.location.city = root.location.name
                sr.location.stateProvince = root.location.region
                sr.location.country = root.location.country
                if (sr.location.country == 'United States of America') sr.location.country = 'United States'
                logger.debug("Filled in full location info")

                //fill current weather
                logger.debug("Filling in current weather")
                sr.currentWeather.condition = root.current.condition.text
                sr.currentWeather.icon = root.current.condition.icon
                sr.currentWeather.temperature = (root.current.temp_c).toFloat()
                sr.currentWeather.humidity = (root.current.humidity).toInteger()
                sr.currentWeather.windSpeed = (root.current.wind_mph).toFloat()
                sr.currentWeather.windDirection = root.current.wind_dir
                logger.debug("Filled in current weather")

                //fill day forecasts
                logger.debug("Filling 5 day forecast")
                (0..<numDays).each {
                    def df = new DayForecast()
                    df.date = Date.parse('yyyy-MM-dd', (String) root.forecast.forecastday[it].date)
                    df.condition = root.forecast.forecastday[it].day.condition.text
                    df.iconURL = root.forecast.forecastday[it].day.condition.icon
                    df.minTemp = root.forecast.forecastday[it].day.mintemp_c.toFloat()
                    df.maxTemp = root.forecast.forecastday[it].day.maxtemp_c.toFloat()
                    df.precipitation = root.forecast.forecastday[it].day.totalprecip_in.toFloat()
                    df.windSpeed = root.forecast.forecastday[it].day.maxwind_mph.toFloat()
                    sr.dayForecasts << df
                }
                logger.debug("Filled 5 day forecast")
            } catch (Exception e) {
                sr = null //indicates failed search
                logger.warn("Search failed to parse data")
                logger.error(e.toString())
                try {
                    logger.debug(root.message)
                } catch (Exception ex) {
                    logger.warn("No error message returned either")
                    logger.error(ex.toString())
                }
            }
        }
        return sr
    }

}
