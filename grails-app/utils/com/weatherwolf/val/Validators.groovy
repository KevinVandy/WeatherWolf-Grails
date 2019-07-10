package com.weatherwolf.val

import com.weatherwolf.security.User


class Validators {
    static boolean valUsername(String username) {
        username.trim().length() in (3..30) &&
                !User.findByUsername(username: username)
    }

    static boolean valEmail(String email) {
        final String emailPattern = /[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})/

        email.trim().length() in (5..50) &&
                email ==~ emailPattern &&
                !User.findByEmail(email: email)
    }

    static boolean valPassword(String password) {
        password.length() in (6..100)
    }
}
