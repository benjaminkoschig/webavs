package ch.globaz.common.ws;

import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.framework.controller.FWController;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Cette interface à pour but de faciliter la gestion des filters pour les API Rest.
 * Il suffit d'implémenter cette interface pour que le filtre soit utilisé.
 */
public interface FilterMapper extends Filter {

    /**
     * Permet d'iniquer s'il faut appliquer le filtre.
     */
    boolean isFilterable(HttpServletRequest request);

    /**
     * L'identifiant de l'application qui est principalement utilisé pour les session.
     *
     * @return l'id de l'application.
     */
    String getApplicationId();

    /**
     * Doit retourner le path de base qui est utilisé par l'application. Ex. ij, ou corvus.
     *
     * @return la path de de bas.
     */
    String getBasePath();

    /**
     * Applique le filtre. {@link Filter}.
     *
     * @param request  {@link Filter}
     * @param response {@link Filter}
     * @param chain    {@link Filter}
     */
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;

    @Override
    default void init(final FilterConfig filterConfig) throws ServletException {}

    @Override
    default void destroy() {}

    default void initSessionAndDoAction(final ServletRequest request,
                                        final ServletResponse servletResponse,
                                        final FilterChain chain,
                                        final String uderId) {
        Object object = null;
        try {
            if (BSessionUtil.getSessionFromThreadContext() == null) {
                BSession bsession = resolveSession((HttpServletRequest) request).orElse(null);
                if(bsession == null){
                    bsession = BSessionUtil.createSession(this.getApplicationId(), uderId);
                }

                object = new Object();
                BSessionUtil.initContext(bsession, object);
                JadeThread.storeTemporaryObject("bsession", bsession);
                //the chain.doFilter do the action
                chain.doFilter(request, servletResponse);
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        } finally {
            if (object != null) {
                JadeThreadActivator.stopUsingContext(object);
            }
        }
    }

    default Optional<BSession> resolveSession(final HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            FWController controller = (FWController) httpSession.getAttribute("objController");
            return Optional.of((BSession) controller.getSession());
        }
        return Optional.empty();
    }
}
