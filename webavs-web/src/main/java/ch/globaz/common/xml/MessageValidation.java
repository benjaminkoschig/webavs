package ch.globaz.common.xml;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.ValidationEvent;

/**
 * Le but de cette class est de facilité l'affichage de l'erreur qui est produite lors de la validation du xml.
 */
@Slf4j
@Value
public class MessageValidation {
    private final ValidationEvent validationEvent;
    private final SeverityLevel severityLevel;

    public MessageValidation(final ValidationEvent validationEvent) {
        this.validationEvent = validationEvent;
        severityLevel = resolveEnum(validationEvent);
    }

    public static MessageValidation of(final ValidationEvent event) {
        return new MessageValidation(event);
    }

    public String message() {
        return "severityLevel:" + this.severityLevel + ", " + this.validationEvent.getMessage() + " " + this.validationEvent.getLocator();
    }

    private SeverityLevel resolveEnum(final ValidationEvent validationEvent) {
        final SeverityLevel severityLevel;
        if (validationEvent.getSeverity() == ValidationEvent.FATAL_ERROR) {
            severityLevel = SeverityLevel.FATAL_ERROR;
        } else if (validationEvent.getSeverity() == ValidationEvent.ERROR) {
            severityLevel = SeverityLevel.ERROR;
        } else if (validationEvent.getSeverity() == ValidationEvent.WARNING) {
            severityLevel = SeverityLevel.WARNING;
        } else {
            severityLevel = SeverityLevel.UNDEFINED;
        }
        return severityLevel;
    }

    enum SeverityLevel {
        ERROR,
        WARNING,
        FATAL_ERROR,
        UNDEFINED
    }
}
