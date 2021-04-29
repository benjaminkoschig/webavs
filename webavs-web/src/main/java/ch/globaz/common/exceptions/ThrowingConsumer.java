package ch.globaz.common.exceptions;

/**
 * Alternatives for the Functional Interfaces present in JDK to either throw a checked exception by wrapping them in an unchecked exception or to
 * handle them.
 *
 * @param <T> the type of the input to the operation
 * @param <E> the exception type that may be thrown
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

    /**
     * Comsomme le paramètre
     *
     * @param t un objet
     *
     * @throws E l'exception éventuelle
     */
    void accept(T t) throws E;
}
