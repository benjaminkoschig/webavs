/*
 * Créé le 17 janv. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jmc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIActionEcrituresSuspens extends FWDefaultServletAction {
    CIActionEcrituresSuspens(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /*
     * protected FWViewBeanInterface beforeLister( HttpSession session, HttpServletRequest request, HttpServletResponse
     * response, FWViewBeanInterface viewBean) { ((BManager) viewBean).changeManagerSize(20); return viewBean; }
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);
            String idEcritureAAff = request.getParameter("selectedId");
            CIEcritureViewBean viewBean = new CIEcritureViewBean();
            viewBean.setEcritureId(idEcritureAAff);
            viewBean.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            viewBean.retrieve();
            viewBean = (CIEcritureViewBean) mainDispatcher.dispatch(viewBean, _action);
            viewBean.setEcranInscriptionsSuspens("True");
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "ecriture_de.jsp";

            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            // Pour mettre à jour le type de compte
            // viewBean = beforeSupprimerSuspens(session, request, response,
            // viewBean);
            FWAction action = getAction();
            action.setRight(FWSecureConstants.READ);
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = "/pavo?userAction=pavo.compte.ecriture.reAfficher";
            } else {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        try {
            getAction().changeActionPart(FWAction.ACTION_MODIFIER);
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            // Pour mettre à jour le type de compte
            // viewBean = beforeSupprimerSuspens(session, request, response,
            // viewBean);

            FWAction action = getAction();
            action.setRight(FWSecureConstants.READ);
            ((CIEcritureViewBean) viewBean).setModeAjout(CIEcriture.MODE_EXTOURNE);
            ((CIEcritureViewBean) viewBean).setIdTypeCompte(CIEcriture.CS_CI_SUSPENS_SUPPRIMES);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = "/pavo?userAction=pavo.compte.ecriture.reAfficher";
            } else {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

}
