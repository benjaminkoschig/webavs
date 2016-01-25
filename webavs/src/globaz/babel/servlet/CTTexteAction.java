/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.servlet;

import globaz.babel.vb.cat.CTDocumentViewBean;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTTexteAction extends CTDefaultAction {

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
    public CTTexteAction(FWServlet servlet) {
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
        CTDocumentViewBean document = new CTDocumentViewBean();
        String destination;

        document.setIdDocument(getSelectedId(request));
        mainDispatcher.dispatch(document, getAction());
        saveViewBean(document, request);

        if (document.isTextesEditables()) {
            destination = getRelativeURL(request, session) + VERS_ECRAN_RC;
        } else {
            document.getSession().addError(document.getSession().getLabel("TEXTES_NON_EDITABLES"));
            destination = getUserActionURL(request, FWServlet.BACK);
        }

        forward(destination, request, response);
    }
}
