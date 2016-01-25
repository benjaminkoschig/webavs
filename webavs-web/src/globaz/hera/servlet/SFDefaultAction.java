/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.hera.helpers.SFAbstractHelper;
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
public class SFDefaultAction extends FWDefaultServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     */
    protected static final String ATTRIBUTE_VIEWBEAN = "viewBean";

    /** DOCUMENT ME! */
    protected static final String DESACTIVE_VALIDATION = "_valid=fail&_back=sl";

    /**
     */
    protected static final String ERREUR_NO_AVS_INCOMPLET = "NO_AVS_INCOMPLET";

    /**
     */
    protected static final String ERREUR_NO_AVS_INTROUVABLE = "NO_AVS_INTROUVABLE";

    /**
     */
    protected static final String METHOD_ADD = SFDefaultAction.METHOD_PARAM + "=" + SFDefaultAction.METHOD_ADD_VALUE;

    /**
     */
    protected static final String METHOD_ADD_VALUE = "add";

    /**
     */
    protected static final String METHOD_PARAM = "_method";

    /**
     */
    protected static final String METHOD_UPD = SFDefaultAction.METHOD_PARAM + "=" + SFDefaultAction.METHOD_UPD_VALUE;

    /**
     */
    protected static final String METHOD_UPD_VALUE = "upd";

    private static final Class[] PARAMS = new Class[] { HttpSession.class, HttpServletRequest.class,
            HttpServletResponse.class, FWDispatcher.class, FWViewBeanInterface.class };

    /**
     */
    protected static final String SELECTED_ID = "selectedId";

    /**
     */
    protected static final String USER_ACTION = "userAction";

    /**
     */
    protected static final String VALID_NEW = "_valid=new";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe PRDefaultAction.
     * 
     * @param servlet
     */
    public SFDefaultAction(FWServlet servlet) {
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
            Method methode = this.getClass().getMethod(getAction().getActionPart(), SFDefaultAction.PARAMS);

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
     * change l'action en PRAbstractHelper.SET_SESSION et dispatch.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @return vrai si l'opération a réussi
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    protected boolean actionSetSession(FWViewBeanInterface viewBean, HttpSession session, HttpServletRequest request,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction action = FWAction.newInstance(getUserAction(request));

        try {
            action.changeActionPart(SFAbstractHelper.SET_SESSION);
            this.saveViewBean(mainDispatcher.dispatch(viewBean, action), request);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        return false;
    }

    /**
     * Cette méthode change l'actionPart de l'action en PRAbstractHelper.SET_SESSION, dispatch puis redirigie vers la
     * page RC. Ceci permet de setter la session dans le viewBean en vue d'un affichage de données dans un écran RC.
     * 
     * <p>
     * Note: pour pouvoir appeller cette méthode, il DOIT y avoir un helper qui hérite de PRAbstractHelper !!!
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
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
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    protected void deleguerActionChercher(FWViewBeanInterface viewBean, HttpSession session,
            HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher)
            throws ServletException, IOException {
        if (actionSetSession(viewBean, session, request, mainDispatcher)) {
            forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
        } else {
            forward(FWDefaultServletAction.ERROR_PAGE, request, response);
        }
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
     * retourne la valeur du paramètre _method de cette requete.
     * 
     * @param request
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut method
     */
    protected String getMethod(HttpServletRequest request) {
        return request.getParameter(SFDefaultAction.METHOD_PARAM);
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
        return request.getParameter(SFDefaultAction.SELECTED_ID);
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
        return SFDefaultAction.SELECTED_ID + "=" + getSelectedId(request);
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
        return request.getParameter(SFDefaultAction.USER_ACTION);
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
        return request.getServletPath() + "?" + SFDefaultAction.USER_ACTION + "=" + action;
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
        return (FWViewBeanInterface) request.getAttribute(SFDefaultAction.ATTRIBUTE_VIEWBEAN);
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
        return (FWViewBeanInterface) session.getAttribute(SFDefaultAction.ATTRIBUTE_VIEWBEAN);
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     */
    protected void saveViewBean(FWViewBeanInterface viewBean, HttpServletRequest request) {
        request.setAttribute(SFDefaultAction.ATTRIBUTE_VIEWBEAN, viewBean);
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
        session.setAttribute(SFDefaultAction.ATTRIBUTE_VIEWBEAN, viewBean);
    }
}
