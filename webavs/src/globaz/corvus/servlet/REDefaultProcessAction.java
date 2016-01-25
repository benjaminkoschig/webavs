/*
 * Créé le 21 sept. 05
 */
package globaz.corvus.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author SCR
 */
public class REDefaultProcessAction extends FWDefaultServletAction {

    private static final Class[] PARAMS = new Class[] { HttpSession.class, HttpServletRequest.class,
            HttpServletResponse.class, FWDispatcher.class, FWViewBeanInterface.class };

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APDefaultProcessAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public REDefaultProcessAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redefinie car l'action afficher par défaut cherche à faire instancier un viewbean et a faire un setId dessus,
     * chose que n'ont pas les process
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setISession(mainDispatcher.getSession());
            saveViewBean(viewBean, session);
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Implémentation de actionCustom qui extrait la partie action du paramètre userAction et tente de trouver une
     * méthode PUBLIQUE de notre classe qui porte ce nom et prend comme arguments {HttpSession, HttpServletRequest,
     * HttpServletResponse, FWDispatcher, FWViewBeanInterface}, si elle en trouve une, elle l'invoke et redirige vers la
     * destination que cette méthode retourne en paramètre, sinon elle redirige vers la page d'erreur.
     * 
     * <p>
     * Cette méthode extrait le viewBean de la session, appelle JSPUtils.setBeanProperties et appelle la méthode trouvée
     * en lui transmettant ce viewBean.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = ERROR_PAGE;
        FWViewBeanInterface viewBean;

        try {
            viewBean = loadViewBean(session);

            if (viewBean != null) {
                JSPUtils.setBeanProperties(request, viewBean);
            }
        } catch (Exception e) {
            // impossible de trouver le viewBean dans la session. arreter le
            // processus et rediriger vers les erreurs
            goSendRedirect(destination, request, response);

            return;
        }

        // selectionner l'action en fonction du parametre transmis
        try {
            Method methode = getClass().getMethod(getAction().getActionPart(), PARAMS);

            destination = (String) methode.invoke(this,
                    new Object[] { session, request, response, dispatcher, viewBean });
        } catch (Exception e) {
            // impossible de trouver une methode avec ce nom et ces parametres
            // !!!
            e.printStackTrace();
        }

        // desactive le forward pour le cas ou la reponse a deja ete flushee
        if (!JadeStringUtil.isBlank(destination)) {
            forward(destination, request, response);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = loadViewBean(session);

        if (viewBean instanceof PRAbstractViewBeanSupport) {
            try {
                JSPUtils.setBeanProperties(request, viewBean);

                if (!((PRAbstractViewBeanSupport) viewBean).validate()) {
                    servlet.getServletContext()
                            .getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp?_method=add")
                            .forward(request, response);

                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                servlet.getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
            }
        }

        super.actionExecuter(session, request, response, mainDispatcher);
    }

    /**
     * @param destination
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    protected void forward(String destination, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * charge le viewBean sauve dans le session sous le nom standard.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface loadViewBean(HttpSession session) {
        return (FWViewBeanInterface) session.getAttribute(FWServlet.VIEWBEAN);
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     */
    protected void saveViewBean(FWViewBeanInterface viewBean, HttpSession session) {
        session.setAttribute(FWServlet.VIEWBEAN, viewBean);
    }
}
