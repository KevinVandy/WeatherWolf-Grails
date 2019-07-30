package com.weatherwolf.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

//@GrailsCompileStatic
@EqualsAndHashCode(includes = 'authority')
class Role implements Serializable {

    private static final long serialVersionUID = 1

    String authority

    Role(String rolename){
        authority = rolename
    }

    static constraints = {
        authority nullable: false, blank: false, unique: true
    }

    static mapping = {
        cache true
    }

    @Override
    String toString() {
        switch (authority) {
            case 'ROLE_CLIENT':
                'User'
                break
            case 'ROLE_ADMIN':
                'Admin'
                break
            default:
                ''
        }
    }
}
