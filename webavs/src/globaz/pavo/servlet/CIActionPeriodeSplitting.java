package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.pavo.db.compte.CICompteIndividuelViewBean;
import globaz.pavo.db.compte.CIPeriodeSplittingViewBean;

/**
 * Action supplémentaire des périodes de splitting. Date de création : (16.10.2002 09:56:57)
 * 
 * @author: Administrator
 */
public class CIActionPeriodeSplitting extends CIActionCIDefault {
    /**
     * Commentaire relatif au constructeur CIActionSplitting.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionPeriodeSplitting(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * Exécute la fonction chercher des mandats de splitting. Date de création : (29.10.2002 13:04:13)
     * 
     * @return globaz.framework.bean.FWViewBean
     */
    private void _actionChercherPeriodeSplitting(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
        CICompteIndividuelViewBean viewBean = new CICompteIndividuelViewBean();
        // enregister les paramètres de la requête dans le bean
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        FWAction action = getAction();
        action.setRight(FWSecureConstants.READ);
        // appel du controlleur
        viewBean = (CICompteIndividuelViewBean) mainDispatcher.dispatch(viewBean, action);
        // sauve le bean dans la session en tant que bean foreig key (utilisé
        // plus
        // tard également)
        session.setAttribute("viewBeanFK", viewBean);
        // redirection vers destination
        String _destination = getRelativeURL(request, session) + "_rc.jsp";
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("chercherPeriodeSplitting")) {
            // chercher avec chargement des données nécessaire
            _actionChercherPeriodeSplitting(session, request, response, dispatcher);
        } else {
            actionDefault(session, request, response, dispatcher);
        }
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        // assignation de l'id du compte lors d'une nouvelle période de
        // splitting
        CICompteIndividuelViewBean _bean = (CICompteIndividuelViewBean) session.getAttribute("viewBeanFK");
        if (_bean != null) {
            ((CIPeriodeSplittingViewBean) viewBean).setCompteIndividuelId(_bean.getCompteIndividuelId());
        }
        return viewBean;
    }

    @Override
    public FWViewBeanInterface checkViewBean(FWViewBeanInterface viewBean) {
        if (!(viewBean instanceof CIPeriodeSplittingViewBean)) {
            return new CIPeriodeSplittingViewBean();
        }
        return null;
    }
}
