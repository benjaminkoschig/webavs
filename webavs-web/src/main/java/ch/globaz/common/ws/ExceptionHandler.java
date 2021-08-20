package ch.globaz.common.ws;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * Le but de cette interface est de pouvoir traiter des exceptions en fonction des APIs REST.
 * Pour détecter l'ExceptionHandler à utiliser on se base sur le début du path de l'API.
 */
public interface ExceptionHandler {
    /**
     * @param e               l'exception à traité
     * @param responseBuilder Permet de construire la response que l'on veux envoyer.
     * @param request         La HttpServletRequest qui à générer l'exception.
     *
     * @return La response qui va être renvoyé.
     */
    Response generateResponse(final Exception e, final Response.ResponseBuilder responseBuilder, final HttpServletRequest request);
}
