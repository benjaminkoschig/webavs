package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.pavo.db.splitting.CIDomicileSplittingViewBean;
import globaz.pavo.db.splitting.CIDossierSplittingViewBean;

/**
 * Actions concerant les domiciles à l'étranger. Date de création : (22.10.2002 09:49:21)
 * 
 * @author: dgi
 */
public class CIActionDomicileSplitting extends CIActionCIDefault {
    /**
     * Commentaire relatif au constructeur CIActionDomicileSplitting.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionDomicileSplitting(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * Exécute la fonction chercher des mandats de splitting. Date de création : (29.10.2002 13:04:13)
     * 
     * @return globaz.framework.bean.FWViewBean
     */
    private void _actionChercherDomicile(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
        CIDossierSplittingViewBean viewBean = new CIDossierSplittingViewBean();
        // enregister les paramètres de la requête dans le bean
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        FWAction action = getAction();
        // action.setRight(FWSecureConstants.READ);
        // appel du controlleur
        viewBean = (CIDossierSplittingViewBean) mainDispatcher.dispatch(viewBean, action);
        // sauve le bean dans la session en tant que bean foreig key (utilisé
        // plus
        // tard également)
        session.setAttribute("viewBeanDossier", viewBean);
        // redirection vers destination
        String _destination = getRelativeURL(request, session) + "_rc.jsp";
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("chercherDomicile")) {
            // chercher avec chargement des données nécessaire
            _actionChercherDomicile(session, request, response, dispatcher);
        } else {
            actionDefault(session, request, response, dispatcher);
        }
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        // assignation de l'id du compte lors d'un nouveau
        // rassemblement/ouverture
        CIDossierSplittingViewBean _bean = (CIDossierSplittingViewBean) session.getAttribute("viewBeanDossier");
        if (_bean != null) {
            ((CIDomicileSplittingViewBean) viewBean).setIdDossierSplitting(_bean.getIdDossierSplitting());
            ((CIDomicileSplittingViewBean) viewBean).setIdTiersPartenaire(_bean.getIdTiersPartenaire());
        }
        return viewBean;
    }

    @Override
    public FWViewBeanInterface checkViewBean(FWViewBeanInterface viewBean) {
        if (!(viewBean instanceof CIDomicileSplittingViewBean)) {
            return new CIDomicileSplittingViewBean();
        }
        return null;
    }
}
