package com.weatherwolf

import com.weatherwolf.security.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class Validators {

    private static final Logger logger = LoggerFactory.getLogger(this.getClass())

    static String valUsername(String username) {
        logger.debug("Validating Username")
        if (!(username.trim().length() in (3..30))) {
            logger.debug("invalid username length")
            'Username must be between 3 and 30 characters long'
        } else if (User.findByUsername(username)) {
            logger.debug("username: ${username} already used")
            'That username is already taken'
        } else {
            ''
        }
    }

    static String valEmail(String email) {
        logger.debug("Validating Email")
        final String emailPattern = /[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})/
        if (!(email.trim().length() in (5..100))) {
            logger.debug("invalid email length")
            'Email must be between 5 and 100 characters long'
        } else if (!(email ==~ emailPattern)) {
            logger.debug("invalid email")
            'Not a valid email address'
        } else if (User.findByEmail(email)) {
            logger.debug("email: ${email} already used")
            'Another account already uses this email'
        } else {
            ''
        }
    }

    static String valPassword(String password, String passwordConfirm) {
        logger.debug("Validating Password")
        if (!(password == passwordConfirm)) {
            logger.debug("passwords don't match")
            'Passwords do not match'
        } else if (!(password.length() in (6..100))) {
            logger.debug("invalid password length")
            'Password must be between 6 and 100 characters'
        } else {
            ''
        }
    }

    static String validateSignup(String username, String email, String password, String passwordconfirm) {
        logger.debug("Validating Signup")
        String errmsg = ''
        if (!errmsg) errmsg = valUsername(username)
        if (!errmsg) errmsg = valEmail(email)
        if (!errmsg) errmsg = valPassword(password, passwordconfirm)
        errmsg
    }
}
