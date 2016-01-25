/*
 * Créé le 2 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.servlet;

import globaz.aquila.vb.process.COProcessContentieuxViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
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
public class COActionProcessContentieux extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COActionProcessContentieux.
     * 
     * @param servlet
     */
    public COActionProcessContentieux(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * redefinit pour renseigner les options de selection des sequences qui sont passees de maniere originales...
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // valider les données du viewBean.
        COProcessContentieuxViewBean viewBean = (COProcessContentieuxViewBean) session.getAttribute("viewBean");

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        // retrouve les options de selection des etapes dans la requete.
        viewBean.clearSelections();
        String[] etapes = request.getParameterValues("etapes_" + viewBean.getForIdSequence());

        if (((etapes != null) && (etapes.length > 0))) {
            viewBean.addSelection(viewBean.getForIdSequence(), etapes);
        }

        // valider le viewbean
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType()) || !viewBean.validate()) {
            servlet.getServletContext()
                    .getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl")
                    .forward(request, response);

            return;
        }

        super.actionExecuter(session, request, response, mainDispatcher);
    }
}
