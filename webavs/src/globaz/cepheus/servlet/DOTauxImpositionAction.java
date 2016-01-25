/*
 * Créé le 10 oct. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.servlet;

import globaz.cepheus.vb.tauxImposition.DOTauxImpositionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DOTauxImpositionAction extends PRDefaultAction {

    /**
     * @param servlet
     */
    public DOTauxImpositionAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        DOTauxImpositionViewBean viewBean = (DOTauxImpositionViewBean) request.getAttribute("viewBean");

        if (viewBean == null) {
            viewBean = new DOTauxImpositionViewBean();
            viewBean.setSession((BSession) mainDispatcher.getSession());
            request.setAttribute("viewBean", viewBean);
        }

        super.actionChercher(session, request, response, mainDispatcher);
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

        DOTauxImpositionViewBean tiViewBean = (DOTauxImpositionViewBean) viewBean;

        String method = request.getParameter("_method");
        if ((method != null) && (method.equalsIgnoreCase("ADD"))) {

            String csCanton = request.getParameter("csCanton");

            if (!JadeStringUtil.isIntegerEmpty(csCanton)) {
                tiViewBean.setCsCanton(csCanton);
            }
        }

        return viewBean;
    }

}
