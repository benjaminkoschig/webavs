package ch.globaz.common.exceptions;

import java.util.Locale;

public interface ExceptionFormatter<E extends Throwable> {

    public String formatte(E exception, Locale locale);

    public Class<E> getType();
}
