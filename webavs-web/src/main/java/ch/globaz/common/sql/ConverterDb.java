package ch.globaz.common.sql;

public interface ConverterDb<T> {
    T convert(Object value, String fieldName, String alias);

    Class<T> forType();
}
