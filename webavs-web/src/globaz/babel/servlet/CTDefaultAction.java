/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * DOCUMENT ME!
 * 
 * @author scr
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class CTDefaultAction extends FWDefaultServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    protected static final String DESACTIVE_VALIDATION = FWServlet.VALID + "=fail&_back=sl";
    protected static final String METHOD_ADD = FWServlet.METHOD + "=" + CTDefaultAction.METHOD_ADD_VALUE;
    protected static final String METHOD_ADD_VALUE = "add";
    protected static final String METHOD_UPD = FWServlet.METHOD + "=" + FWServlet.UPD;
    private static final Class[] PARAMS = new Class[] { HttpSession.class, HttpServletRequest.class,
            HttpServletResponse.class, FWDispatcher.class, FWViewBeanInterface.class };
    protected static final String SELECTED_ID = "selectedId";

    protected static final String VALID_NEW = FWServlet.VALID + "=new";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CTDefaultAction.
     * 
     * @param servlet
     */
    public CTDefaultAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
        String destination = FWDefaultServletAction.ERROR_PAGE;
        FWViewBeanInterface viewBean;

        try {
            viewBean = this.loadViewBean(session);

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
            Method methode = this.getClass().getMethod(getAction().getActionPart(), CTDefaultAction.PARAMS);

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
     * Cette méthode extrait la partie action du paramètre userAction et tente de trouver une méthode PUBLIQUE de notre
     * classe qui porte ce nom et prend comme arguments {HttpSession, HttpServletRequest, HttpServletResponse,
     * FWDispatcher, FWViewBeanInterface} et qui retourne une String, si elle en trouve une, elle l'invoke.
     * 
     * <p>
     * Cette méthode reffectue ensuite un forward vers l'url contenue dans la chaine qui a ete retournee par la méthode
     * invoquée. Si cette url est vide, c'est que par convention la reponse http a deja ete flushee, dans ce cas cette
     * methode ne fait rien (pas de forward).
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
    protected void delegateActionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = FWDefaultServletAction.ERROR_PAGE;
        FWViewBeanInterface viewBean = this.loadViewBean(session);

        if (viewBean != null) {
            try {
                JSPUtils.setBeanProperties(request, viewBean);
            } catch (Exception e) {
                // est-ce que ca a des chances d'arriver ca ?
                e.printStackTrace();
            }
        }

        // selectionner l'action en fonction du parametre transmis
        try {
            Method m = this.getClass().getMethod(getAction().getActionPart(), CTDefaultAction.PARAMS);
            Object[] params = new Object[] { session, request, response, dispatcher, viewBean };

            destination = (String) m.invoke(this, params);
        } catch (Exception e) {
            // impossible de trouver une methode avec ce nom et ces parametres
            // !!!
            e.printStackTrace();
        }

        // desactive le forward pour le cas ou la reponse a deja ete flushee
        if (!JadeStringUtil.isEmpty(destination)) {
            forward(destination, request, response);
        }
    }

    /**
     * Obtient un {@link javax.servlet.RequestDispatcher dispatcher} vers la destination et effectue un forward.
     * 
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
     * retourne la valeur du paramètre _method de cette requete.
     * 
     * @param request
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut method
     */
    protected String getMethod(HttpServletRequest request) {
        return request.getParameter(FWServlet.METHOD);
    }

    /**
     * retourne la valeur du parametre 'selectedId' de la requete 'request'.
     * 
     * @param request
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut selected id
     */
    protected String getSelectedId(HttpServletRequest request) {
        return request.getParameter(CTDefaultAction.SELECTED_ID);
    }

    /**
     * Retourne une chaine de caractere du type 'selectedId=getSelectedId(request)'
     * 
     * @param request
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut selected id param
     */
    protected String getSelectedIdParam(HttpServletRequest request) {
        return CTDefaultAction.SELECTED_ID + "=" + getSelectedId(request);
    }

    /**
     * getter pour l'attribut user action
     * 
     * @param request
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut user action
     */
    protected String getUserAction(HttpServletRequest request) {
        return request.getParameter(FWServlet.USER_ACTION);
    }

    /**
     * Cree une chaine de caractere du type '/nomServlet?userAction=action'
     * 
     * @param request
     *            DOCUMENT ME!
     * @param action
     *            une action COMPLETE (avec actionPart)
     * 
     * @return DOCUMENT ME!
     */
    protected String getUserActionURL(HttpServletRequest request, String action) {
        return request.getServletPath() + "?" + FWServlet.USER_ACTION + "=" + action;
    }

    /**
     * Cree une chaine de caractere du type '/nomServlet?userAction=actionBase.actionPart'
     * 
     * @param request
     *            DOCUMENT ME!
     * @param actionBase
     *            les parties application, package et class de la chaine action
     * @param actionPart
     *            la partie action de la chaine action (exemple 'afficher')
     * 
     * @return DOCUMENT ME!
     */
    protected String getUserActionURL(HttpServletRequest request, String actionBase, String actionPart) {
        return this.getUserActionURL(request, actionBase + "." + actionPart);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param request
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface loadViewBean(HttpServletRequest request) {
        return (FWViewBeanInterface) request.getAttribute(FWServlet.VIEWBEAN);
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
     * @param request
     *            DOCUMENT ME!
     */
    protected void saveViewBean(FWViewBeanInterface viewBean, HttpServletRequest request) {
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);
    }

    /**
     * sauve un viewBean dans la session http sous le nom standard.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     */
    protected void saveViewBean(FWViewBeanInterface viewBean, HttpSession session) {
        session.setAttribute(FWServlet.VIEWBEAN, viewBean);
    }
}
