package ch.globaz.common.ws;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * Le but de cette interface est de pouvoir traiter des exceptions en fonction des APIs REST.
 * Pour d�tecter l'ExceptionHandler � utiliser on se base sur le d�but du path de l'API.
 */
public interface ExceptionHandler {
    /**
     * @param e               l'exception � trait�
     * @param responseBuilder Permet de construire la response que l'on veux envoyer.
     * @param request         La HttpServletRequest qui � g�n�rer l'exception.
     *
     * @return La response qui va �tre renvoy�.
     */
    Response generateResponse(final Exception e, final Response.ResponseBuilder responseBuilder, final HttpServletRequest request);
}
