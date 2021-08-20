package ch.globaz.common.ws;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Permet de d�finir les exceptionMapper � utiliser pour les api REST (jax-rs).
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionMapper {
    /**
     * Permet d'indiquer le d�but du path sur le quelle il faut appliquer le mapper d'exception.
     *
     * @return le path utilis�.
     */
    String value();
}
