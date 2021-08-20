package ch.globaz.common.ws;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Permet de définir les exceptionMapper à utiliser pour les api REST (jax-rs).
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionMapper {
    /**
     * Permet d'indiquer le début du path sur le quelle il faut appliquer le mapper d'exception.
     *
     * @return le path utilisé.
     */
    String value();
}
