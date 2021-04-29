package ch.globaz.common.exceptions;

/**
 * Alternatives for the Functional Interfaces present in JDK to either throw a checked exception by wrapping them in an unchecked exception or to
 * handle them.
 *
 * @param <E> the exception type that may be thrown
 */
@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> {

    /**
     * Exécute le code implémenté
     *
     * @throws E l'exception éventuelle
     */
    void run() throws E;
}
