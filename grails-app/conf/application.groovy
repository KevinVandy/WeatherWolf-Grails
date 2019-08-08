
// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.weatherwolf.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.weatherwolf.security.UserRole'
grails.plugin.springsecurity.authority.className = 'com.weatherwolf.security.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

grails.plugin.springsecurity.rejectIfNoRule = true
grails.plugin.springsecurity.fii.rejectPublicInvocations = false
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/home/success'
grails.plugin.springsecurity.password.algorithm = 'bcrypt'
grails.plugin.springsecurity.password.bcrypt.logrounds = 15

grails {
	mail {
		host = "smtp.gmail.com"
		port = 465
		username = "weatherwolfgrails@gmail.com"
		password = "theweatherwolfapp" //fill in password
		props = ["mail.smtp.auth":"true",
				 "mail.smtp.socketFactory.port":"465",
				 "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
				 "mail.smtp.socketFactory.fallback":"false"]
	}
}

grails.mail.disabled = false
grails.mail.default.from = "weatherwolfgrails@gmail.com"