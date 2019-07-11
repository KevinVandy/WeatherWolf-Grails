package com.weatherwolf

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import org.springframework.context.MessageSource
import org.springframework.web.servlet.support.RequestContextUtils


class LocaleNavbarTagLib implements GrailsConfigurationAware {
    static namespace = 'navbar'

    static defaultEncodeAs = [taglib: 'none']

    MessageSource messageSource

    List<String> languages

    @Override
    void setConfiguration(Config co) {
        languages = co.getProperty('guide.languages', List)
    }

    def localeDropdownListItems = { args ->
        String uri = args.uri
        Map params = args.params
        String encodedParams = ''

        //preserve url parameters
        if (params) {
            encodedParams = params.collect { k, v ->
                if (k != "lang") {
                    "${k}=${v}"
                }
            }.join("&")
            encodedParams = encodedParams.replace('&null', '')
        }

        for (String lang : languages) {
            String languageCode = "language.$lang"
            def locale = RequestContextUtils.getLocale(request)
            def msg = messageSource.getMessage(languageCode, [] as Object[], null, locale)
            if (encodedParams != '' && encodedParams != 'null') {
                out << "<option value='${uri}?${encodedParams}&lang=${lang}'>${msg}</option>"
            } else {
                out << "<option value='${uri}?lang=${lang}'>${msg}</option>"
            }
        }
    }
}
