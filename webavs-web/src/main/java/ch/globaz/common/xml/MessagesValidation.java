package ch.globaz.common.xml;

import lombok.Value;
import org.slf4j.Logger;

import java.util.List;

@Value(staticConstructor = "of")
public class MessagesValidation {
    private final List<MessageValidation> list;
    private final Class<?> clazz;

    public void logMessagesInWarn(final Logger log) {
        list.forEach(validationEvent -> log.warn("Object {} is not valid: {}", clazz.getSimpleName(), validationEvent.message()));
    }
}
