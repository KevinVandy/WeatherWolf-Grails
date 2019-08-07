package com.weatherwolf

import com.weatherwolf.security.Role
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole


class BootStrap {

    def init = { servletContext ->
//        def authorities = ['ROLE_CLIENT', 'ROLE_ADMIN']
//        authorities.each {
//            if (!Role.findByAuthority(it)) {
//                new Role(authority: it).save(flush: true, failonerror: true)
//            }
//        }
//        if (!User.findByUsername('kevinvandy')) {
//            def u = new User(username: 'kevinvandy', email: 'kvancott@talentplus.com', password: 'hellothere', favoriteLocation: 'Lincoln, NE')
//            u.save(flush: true, failonerror: true)
//            def ur = new UserRole(user: u, role: Role.findByAuthority('ROLE_CLIENT'))
//            ur.save(flush: true, failonerror: true)
//        }
//        if (!User.findByUsername('admin')) {
//            def u = new User(username: 'admin1', email: 'weatherwolfgrails@gmail.com', password: 'hellothere', favoriteLocation: 'Lincoln, NE')
//            u.save(flush: true, failonerror: true)
//            def ur = new UserRole(user: u, role: Role.findByAuthority('ROLE_ADMIN'))
//            ur.save(flush: true, failonerror: true)
//        }


    }
    def destroy = {
    }
}
