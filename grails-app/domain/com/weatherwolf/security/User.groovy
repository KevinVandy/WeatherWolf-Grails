package com.weatherwolf.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

//@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    Integer id
    String username
    String email
    String password
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false
    String lang = 'en'
    String units = 'F'
    String favoriteLocation = 'Lincoln, Nebraska'

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        email nullable: false, blank: false, unique: true
    }

    static mapping = {
	    password column: '`password`'
    }
}
