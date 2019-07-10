package com.weatherwolf

import com.weatherwolf.security.Role
import com.weatherwolf.security.User
import com.weatherwolf.security.UserRole
import grails.plugin.springsecurity.annotation.Secured
import com.weatherwolf.val.Validators


class AccountController {

    String msg = ''

    @Secured(['ROLE_CLIENT'])
    def index() {}

    def login() {

    }

    def signup() {

    }

    def register() {
        String username = params.username
        String email = params.email
        String password = params.password

        if (validateSignup(username, email, password)) {
            def u = new User(username: username, email: email, password: password)
            u.save()
            def ur = new UserRole(user: u, role: Role.findByAuthority('ROLE_CLIENT'))
            ur.save()
            msg = "${username}, your account has been created.<br /> Now just Login."
            render(view: '/account/login', model: [msg: msg])
        } else {
            msg = 'Sign up error'
            render(view: '/account/signup', model: [msg: msg])
        }
    }

    private boolean validateSignup(String username, String email, String password) {
        Validators.valUsername(username) &&
                Validators.valEmail(email) &&
                Validators.valPassword(password)
    }

}
