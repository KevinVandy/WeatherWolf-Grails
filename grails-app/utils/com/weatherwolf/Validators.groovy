package com.weatherwolf

import com.weatherwolf.security.User
import grails.util.Holders
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder as LCH


class Validators {

    private static final Logger logger = LoggerFactory.getLogger(this.getClass())
    static def ms = Holders.getGrailsApplication().getParentContext()

    static String valUsername(String username) {
        logger.debug("Validating Username")
        if (!username || !(username.trim().length() in (3..30))) {
            logger.debug("invalid username length")
            ms.getMessage('msg.usernamelength', null, 'Username must be between 3 and 30 characters long',  LCH.getLocale())
        } else if (User.findByUsername(username)) {
            logger.debug("username: ${username} already used")
            ms.getMessage('msg.usernametaken', null, 'That username is already taken',  LCH.getLocale())
        } else {
            ''
        }
    }

    static String valEmail(String email) {
        logger.debug("Validating Email")
        final String emailPattern = /[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})/
        if (!email || !(email.trim().length() in (5..100))) {
            logger.debug("invalid email length")
            ms.getMessage('msg.emaillength', null, 'Email must be between 5 and 100 characters long',  LCH.getLocale())
        } else if (!(email ==~ emailPattern)) {
            logger.debug("invalid email")
            ms.getMessage('msg.invalidemail', null, 'Invalid Email',  LCH.getLocale())
        } else if (User.findByEmail(email)) {
            logger.debug("email: ${email} already used")
            ms.getMessage('msg.emailtaken', null, 'Another account already uses this email',  LCH.getLocale())
        } else {
            ''
        }
    }

    static String valPassword(String password, String passwordConfirm) {
        logger.debug("Validating Password")
        if (!(password == passwordConfirm)) {
            logger.debug("passwords don't match")
            ms.getMessage('msg.passwordsdonotmatch', null, 'Passwords do not match',  LCH.getLocale())
        } else if (!password || !(password.length() in (6..100))) {
            logger.debug("invalid password length")
            ms.getMessage('msg.passwordlength', null, 'Password must be between 6 and 100 characters',  LCH.getLocale())
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
