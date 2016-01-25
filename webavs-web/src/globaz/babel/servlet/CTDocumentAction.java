package globaz.babel.servlet;

import globaz.babel.vb.cat.CTDocumentViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Créé le 13 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTDocumentAction extends CTDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CTDocumentAction.
     * 
     * @param servlet
     */
    public CTDocumentAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
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
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        CTDocumentViewBean viewBean = new CTDocumentViewBean();

        beforeNouveau(session, request, response, viewBean);
        viewBean.setCsDomaine(request.getParameter("forCsDomaine"));
        viewBean.setCsTypeDocument(request.getParameter("forCsTypeDocument"));

        // charger la liste des types de documents pour le domaine en question
        saveViewBean(viewBean, request);
        // Si non null, les taglib dans la jsp de recherche vont récupéré le
        // viewBean en session
        // et appeler les méthodes setFor... par introspection.
        // Comme elle n'existe pas dans notre viewBean....
        saveViewBean(null, session);
        mainDispatcher.dispatch(viewBean, getAction());
        forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String actionCopierDocument(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        CTDocumentViewBean document = new CTDocumentViewBean();

        document.setIdDocument(getSelectedId(request));
        dispatcher.dispatch(document, getAction());

        return getUserActionURL(request, CTMainServletAction.ACTION_DOCUMENTS, FWAction.ACTION_CHERCHER
                + "&csGroupeDomaines=" + request.getParameter("csGroupeDomaines") + "&csGroupeTypesDocuments="
                + request.getParameter("csGroupeTypesDocuments"));
    }

    /**
     * Utiliser l'introspection pour determiner l'action custom a executer.
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
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        delegateActionCustom(session, request, response, dispatcher);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // recuperer les messages d'erreurs si jamais
        FWViewBeanInterface oldViewBean = loadViewBean(request);

        if ((oldViewBean != null) && FWViewBeanInterface.ERROR.equals(oldViewBean.getMsgType())) {
            viewBean.setMessage(oldViewBean.getMessage());
            viewBean.setMsgType(oldViewBean.getMsgType());
        }

        // setter les parametres de la requete
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }
}
