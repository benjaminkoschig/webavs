package ch.globaz.common.exceptions;

import com.google.common.annotations.VisibleForTesting;

import java.util.function.Consumer;

/**
 * Classe utilitaire pour faciliter la gestion des exceptions.
 */
public final class Exceptions {

    private Exceptions() {
        // Pas de constructeur exposé pour les classes utilitaire
    }

    /**
     * Wrapper d'appel consumer de lambda pour transformer une Checked Exception en une Unchecked exception.
     *
     * @param throwingConsumer alternative de Functional Interfaces de la JDK {@see ThrowingConsumer}
     */
    public static <T> Consumer<T> checkedToUnChecked(final ThrowingConsumer<T, ? extends Exception> throwingConsumer) {
        return param -> {
            try {
                throwingConsumer.accept(param);
            } catch (@SuppressWarnings("Illegal catches"/* But de la fonction passer d'exception check à unCheck*/) final Exception ex) {
                throw new CommonTechnicalException(ex);
            }
        };
    }

    /**
     * Wrapper d'appel consumer de lambda pour transformer une Checked Exception en une Unchecked exception.
     *
     * @param throwingConsumer alternative de Functional Interfaces de la JDK {@see ThrowingConsumer}
     * @param message          permet d'ajouter un message dans l'exception.
     */
    public static <T> Consumer<T> checkedToUnChecked(final ThrowingConsumer<T, ? extends Exception> throwingConsumer,
                                                     final String message) {
        return param -> {
            try {
                throwingConsumer.accept(param);
            } catch (@SuppressWarnings("Illegal catches"/* But de la fonction passer d'exception check à unCheck*/) final Exception ex) {
                throw new CommonTechnicalException(message, ex);
            }
        };
    }

    /**
     * Encapsule l'exception éventuellement levé par le {@link ThrowingRunnable} en paramètre dans une {@link RuntimeException}
     *
     * @param throwingRunnable le code à exécuter
     * @param errorType        le type d'erreur à transformer en {@link RuntimeException}
     * @param <E>              le type d'{@link Exception}
     */
    public static <E extends Exception> void checkedToUnChecked(final ThrowingRunnable<E> throwingRunnable, final Class<E> errorType) {
        try {
            throwingRunnable.run();
        } catch (@SuppressWarnings("Illegal catches"/* But de la fonction passer d'exception check à unCheck*/) final Exception ex) {
            throw generateException(errorType, ex);
        }
    }

    /**
     * Encapsule l'exception éventuellement levé par le {@link ThrowingRunnable} en paramètre dans une {@link RuntimeException}
     *
     * @param throwingRunnable le code à exécuter
     * @param <E>              le type d'{@link Exception}
     */
    public static <E extends Exception> void checkedToUnChecked(final ThrowingRunnable<E> throwingRunnable) {
        try {
            throwingRunnable.run();
        } catch (@SuppressWarnings("Illegal catches"/* But de la fonction passer d'exception check à unCheck*/) final Exception ex) {
            throw new CommonTechnicalException(ex);
        }
    }

    /**
     * Encapsule l'exception éventuellement levé par le {@link ThrowingRunnable} en paramètre dans une {@link RuntimeException}
     *
     * @param throwingRunnable le code à exécuter
     * @param <E>              le type d'{@link Exception}
     * @param message          permet d'ajouter un message dans l'exception.
     */
    public static <E extends Exception> void checkedToUnChecked(final ThrowingRunnable<E> throwingRunnable, final String message) {
        try {
            throwingRunnable.run();
        } catch (@SuppressWarnings("Illegal catches"/* But de la fonction passer d'exception check à unCheck*/) final Exception ex) {
            throw new CommonTechnicalException(message, ex);
        }
    }

    /**
     * Encapsule l'exception éventuellement levé par le {@link ThrowingSupplier} en paramètre dans une {@link CommonTechnicalException}
     *
     * @param throwingConsumer le code à exécuter
     * @param errorType        le type d'erreur à transformer en {@link RuntimeException}
     * @param <T>              le type de retour du {@link ThrowingSupplier}
     * @param <E>              le type d'{@link Exception}
     *
     * @return la valeur du {@link ThrowingSupplier}
     */
    public static <T, E extends Exception> T checkedToUnChecked(final ThrowingSupplier<T, E> throwingConsumer, final Class<E> errorType) {
        try {
            return throwingConsumer.get();
        } catch (@SuppressWarnings("Illegal catches"/* But de la fonction passer d'exception check à unCheck*/) final Exception ex) {
            throw generateException(errorType, ex);
        }
    }

    /**
     * Encapsule l'exception éventuellement levé par le {@link ThrowingSupplier} en paramètre dans une {@link CommonTechnicalException}
     *
     * @param throwingConsumer le code à exécuter
     * @param <T>              le type de retour du {@link ThrowingSupplier}
     * @param <E>              le type d'{@link Exception}
     * @param message          permet d'ajouter un message dans l'exception.
     *
     * @return la valeur du {@link ThrowingSupplier}
     */
    public static <T, E extends Exception> T checkedToUnChecked(final ThrowingSupplier<T, E> throwingConsumer, final String message) {
        try {
            return throwingConsumer.get();
        } catch (@SuppressWarnings("Illegal catches"/* But de la fonction passer d'exception check à unCheck*/) final Exception ex) {
            throw new CommonTechnicalException(message, ex);
        }
    }

    @VisibleForTesting
    static <E extends Exception> RuntimeException generateException(final Class<E> errorType, final Exception ex) {
        if (errorType.isInstance(ex)) {
            return new CommonTechnicalException(ex);
        } else {
            return (RuntimeException) ex;
        }
    }

}
