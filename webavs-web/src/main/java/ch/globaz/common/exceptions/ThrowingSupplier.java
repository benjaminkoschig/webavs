package ch.globaz.common.exceptions;

/**
 * Alternatives for the Functional Interfaces present in JDK to either throw a checked exception by wrapping them in an unchecked exception or to
 * handle them.
 *
 * @param <T> the type of the output of the operation
 * @param <E> the exception type that may be thrown
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {

    /**
     * Renvoie un objet
     *
     * @return un objet
     *
     * @throws E l'exception éventuelle
     */
    T get() throws E;
}
