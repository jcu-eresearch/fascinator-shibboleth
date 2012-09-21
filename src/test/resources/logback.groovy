import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.sift.GSiftingAppender
import ch.qos.logback.classic.sift.MDCBasedDiscriminator
import ch.qos.logback.core.ConsoleAppender


import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.OFF
import static ch.qos.logback.classic.Level.ALL
import static ch.qos.logback.classic.Level.WARN
import static ch.qos.logback.classic.Level.TRACE

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d %-8X{name} %-6p %-20.20c{0} %m%n"
    }
}


root(ALL)
//logger("au.edu.jcu", TRACE, ["CONSOLE"])
