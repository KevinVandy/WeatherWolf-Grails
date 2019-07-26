package com.weatherwolf.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

//@GrailsCompileStatic
@EqualsAndHashCode(includes = 'username')
@ToString(includes = 'username', includeNames = true, includePackage = false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    Integer id
    String username
    String email
    String password
    String forgotPasswordToken
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false
    String lang = 'en'
    String units = 'F'
    String favoriteLocation = ''
    Date dateCreated

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        password nullable: false, blank: false, password: true, size: 6..100
        username nullable: false, blank: false, unique: true, size: 3..50
        email nullable: false, blank: false, unique: true, size: 5..100, email: true
        lang inList: ['en', 'es', 'fr'] //only English, Spanish, and French are supported
        units inList: ['C', 'F']
        favoriteLocation blank: false
        forgotPasswordToken nullable: true, blank: true
    }

    static hasMany = [searchLog: SearchLog]

    static mapping = {
        searchLog cascade: 'all-delete-orphan', sort: 'date', order: 'desc'
        password column: '`password`'
    }
}
