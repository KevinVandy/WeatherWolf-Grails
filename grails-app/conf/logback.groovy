import ch.qos.logback.ext.loggly.LogglyAppender
import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')

        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

appender('loggly', LogglyAppender) {
    endpointUrl = 'https://logs-01.loggly.com/inputs/5fac6954-ce72-4d62-920c-c5d467dfeca3/tag/logback'
    pattern = '%d{"ISO8601", UTC}  %p %t %c{0}.%M - %m%n'
}

if (Environment.isDevelopmentMode() && BuildSettings.TARGET_DIR != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${BuildSettings.TARGET_DIR}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", INFO, ['FULL_STACKTRACE'], false)
}

root(INFO, ['STDOUT', 'loggly'])
