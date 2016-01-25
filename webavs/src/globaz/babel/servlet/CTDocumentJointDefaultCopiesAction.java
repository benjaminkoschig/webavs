/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.servlet;

import globaz.babel.vb.copies.CTDocumentJointDefaultCopiesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class CTDocumentJointDefaultCopiesAction extends CTDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CTDocumentJointDefaultCopiesAction.
     * 
     * @param servlet
     */
    public CTDocumentJointDefaultCopiesAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Appelle le dispatcher pour l'action chercher afin de charger le document pour lequel des textes doivent être
     * modifiés, sauve ensuite ce document dans la requete afin qu'il puisse etre utilise dans le jsp de recherche.
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
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination;
        CTDocumentJointDefaultCopiesViewBean documentDefaultCopie = new CTDocumentJointDefaultCopiesViewBean();

        documentDefaultCopie.setIdDocument(getSelectedId(request));
        mainDispatcher.dispatch(documentDefaultCopie, getAction());
        saveViewBean(documentDefaultCopie, request);

        destination = getRelativeURL(request, session) + VERS_ECRAN_RC;

        forward(destination, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        CTDocumentJointDefaultCopiesViewBean documentDefaultCopie = (CTDocumentJointDefaultCopiesViewBean) viewBean;
        documentDefaultCopie.setCsDomaineDocument(request.getParameter("csDomaineDocument"));
        documentDefaultCopie.setCsTypeDocument(request.getParameter("csTypeDocument"));
        documentDefaultCopie.setIdDocument(request.getParameter("idDocument"));

        if (!JadeStringUtil.isDecimalEmpty(request.getParameter("selectedId"))) {
            documentDefaultCopie.setIdDefaultCopie(request.getParameter("selectedId"));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
