package ch.globaz.common.ws;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
}
