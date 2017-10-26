package ch.globaz.common.process.byitem;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.common.exceptions.ExceptionFormatter;

public class ExceptionFormatterResolver {
    private Map<Class<? extends Throwable>, ExceptionFormatter<? extends Throwable>> exceptionFormatters = new HashMap<Class<? extends Throwable>, ExceptionFormatter<? extends Throwable>>();

    public void addFormatter(ExceptionFormatter<?>... formatters) {
        for (ExceptionFormatter<? extends Throwable> exceptionFormatter : formatters) {
            exceptionFormatters.put(exceptionFormatter.getType(), exceptionFormatter);
        }
    }

    ExceptionFormatter<? extends Throwable> resolve(Class<?> clazz) {
        return exceptionFormatters.get(clazz);
    }
}
