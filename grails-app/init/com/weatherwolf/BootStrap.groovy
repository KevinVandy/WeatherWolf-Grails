package com.weatherwolf

import com.weatherwolf.security.Role
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole


class BootStrap {

    def init = { servletContext ->
        def authorities = ['ROLE_CLIENT']
        authorities.each {
            if (!Role.findByAuthority(it)) {
                new Role(authority: it).save()
            }
        }
        if (!User.findByUsername('kevinvandy')) {
            def u = new User(username: 'kevinvandy', email: 'kvancott@talentplus.com', password: 'hellothere')
            u.save()
            def ur = new UserRole(user: u, role: Role.findByAuthority('ROLE_CLIENT'))
            ur.save()
        }
    }
    def destroy = {
    }
}
