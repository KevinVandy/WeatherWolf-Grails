package com.weatherwolf.taglib

import com.weatherwolf.LocaleNavbarTagLib
import grails.testing.web.taglib.TagLibUnitTest
import spock.lang.Specification

class LocaleNavbarTagLibSpec extends Specification implements TagLibUnitTest<LocaleNavbarTagLib> {

    void "LocaleNavbarTagLib method localeDropdown renders"() {
        given:
        def uri = '/books'
        def languages = [[code: 'en', msg: 'English'],
                         [code: 'es', msg: 'Spanish'],
                         [code: 'fr', msg: 'French']]

        when:
        def expected = ''
        languages.each { Map m ->
            expected += "<li><a href='${uri}?lang=${m.code}'>${m.msg}</a></li>"
        }
        tagLib.languages = languages.collect { it.code }
        tagLib.messageSource = Stub(MessageSource) {
            getMessage('language.en', [] as Object[], null, _) >> languages.find { it.code == ''}.msg
            getMessage('language.es', [] as Object[], null, _) >> languages.find { it.code == 'es'}.msg
            getMessage('language.it', [] as Object[], null, _) >> languages.find { it.code == 'fr'}.msg
        }
        def result = applyTemplate('<dropdown:localeDropdownListItems uri="/books"/>')

        then:
        cleanUpString(result) == cleanUpString(expected)
    }

    String cleanUpString(String str) {
        str.replaceAll('\n','').replaceAll(' ', '')
    }
}
