package ch.globaz.common.ws;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Path("/loggers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogApiRest {

    @PUT
    @Path("/change-level")
    public String changeLogLevel(@QueryParam("loggerName") String loggerName,
                                 @QueryParam("level") String level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        Logger logger = loggerName.equalsIgnoreCase(Logger.ROOT_LOGGER_NAME) ?
                loggerContext.getLogger(loggerName) : loggerContext.exists(loggerName);

        if (logger != null) {
            logger.setLevel(Level.toLevel(level));
            return "Changed logger: " + loggerName + " to level : " + level;
        } else {
            return "Logger Not Found Make Sure that logger name is correct";
        }
    }

    @GET
    public List<LoggerDto> getLoggers() {
        return getRootLogger().getLoggerContext().getLoggerList().stream().map(LoggerDto::new).collect(Collectors.toList());
    }

    private static Logger getRootLogger() {
        return (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

    @Data
    class LoggerDto {
        private String name;
        private String level;
        private String effectiveLevel;

        LoggerDto(Logger logger) {
            this.name = logger.getName();
            this.effectiveLevel = logger.getEffectiveLevel().toString();
            this.level = logger.getLevel() != null ? logger.getLevel().toString() : null;
        }
    }
}
