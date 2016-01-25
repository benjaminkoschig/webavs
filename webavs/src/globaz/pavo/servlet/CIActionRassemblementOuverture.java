package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.pavo.db.compte.CICompteIndividuelViewBean;
import globaz.pavo.db.compte.CIRassemblementOuvertureEcrituresListViewBean;
import globaz.pavo.db.compte.CIRassemblementOuvertureViewBean;
import java.io.IOException;
import javax.servlet.ServletException;

/**
 * Action supplémentaire des rassemblements et ouvertures. Date de création : (16.10.2002 09:56:57)
 * 
 * @author: Administrator
 */
public class CIActionRassemblementOuverture extends CIActionCIDefault {
    /**
     * Commentaire relatif au constructeur CIActionSplitting.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionRassemblementOuverture(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    protected void _actionChercherEcrituresLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) {
        CIRassemblementOuvertureEcrituresListViewBean viewBean = new CIRassemblementOuvertureEcrituresListViewBean();

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // String rao = request.getParameter("selectedId");
        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        FWAction action = getAction();
        action.setRight(FWSecureConstants.READ);
        // appel du controlleur
        /*
         * try { viewBean.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
         * viewBean.find(); } catch (Exception e) { }
         */
        viewBean = (CIRassemblementOuvertureEcrituresListViewBean) mainDispatcher.dispatch(viewBean, action);
        session.setAttribute("viewBean", viewBean);
        // session.removeAttribute("listViewBean");
        // session.setAttribute("listViewBean",viewBean);
        String _destination = getRelativeURL(request, session) + "Ecritures_rcListe.jsp";
        try {
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } catch (ServletException e) {
        } catch (IOException e) {
        }

    }

    private void _actionChercherEcrituresRassemblement(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
        CIRassemblementOuvertureViewBean viewBean = new CIRassemblementOuvertureViewBean();
        // enregister les paramètres de la requête dans le bean
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // String rao = request.getParameter("selectedId");
        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        FWAction action = getAction();
        // action.setRight(FWSecureConstants.READ);
        // appel du controlleur
        viewBean = (CIRassemblementOuvertureViewBean) mainDispatcher.dispatch(viewBean, action);
        // sauve le bean dans la session en tant que bean foreig key (utilisé
        // plus
        // tard également)
        session.setAttribute("viewBean", viewBean);
        // redirection vers destination
        String _destination = getRelativeURL(request, session) + "Ecritures_rc.jsp";
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Exécute la fonction chercher des rassemblements et/ou ouvertures. Date de création : (29.10.2002 13:04:13)
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
        // action.setRight(FWSecureConstants.READ);
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
    public void actionAfficher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            FWAction action = getAction();
            action.setRight(FWSecureConstants.READ);
            // action.changeActionPart(FWAction.ACTION_AFFICHER);

            String idRass = request.getParameter("selectedId");
            CICompteIndividuelViewBean viewBean2 = new CICompteIndividuelViewBean();
            viewBean2.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            CIRassemblementOuvertureViewBean viewBean = new CIRassemblementOuvertureViewBean();
            viewBean.setRassemblementOuvertureId(idRass);
            viewBean.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            viewBean.retrieve();
            viewBean = (CIRassemblementOuvertureViewBean) mainDispatcher.dispatch(viewBean, action);
            viewBean2.setCompteIndividuelId(viewBean.getCompteIndividuelId());
            viewBean2.retrieve();
            viewBean2 = (CICompteIndividuelViewBean) mainDispatcher.dispatch(viewBean2, action);
            session.setAttribute("viewBean", viewBean);
            session.setAttribute("viewBeanFK", viewBean2);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("chercherRassemblementOuverture")) {
            // chercher avec chargement des données nécessaire
            _actionChercherPeriodeSplitting(session, request, response, dispatcher);
            /*
             * } else if (getAction().getActionPart().equals("envoiCI")) { // afficher le dossier depuis l'écran du
             * mandat _actionEnvoiCI(session, request, response, dispatcher);
             */

        } else if (getAction().getActionPart().equals("chercherEcrituresRassemblement")) {
            _actionChercherEcrituresRassemblement(session, request, response, dispatcher);

        } else if (getAction().getActionPart().equals("listerEcritures")) {
            _actionChercherEcrituresLister(session, request, response, dispatcher);
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
        CICompteIndividuelViewBean _bean = (CICompteIndividuelViewBean) session.getAttribute("viewBeanFK");
        if (_bean != null) {
            ((CIRassemblementOuvertureViewBean) viewBean).setCompteIndividuelId(_bean.getCompteIndividuelId());
        }
        return viewBean;
    }

    @Override
    public FWViewBeanInterface checkViewBean(FWViewBeanInterface viewBean) {
        if (!(viewBean instanceof CIRassemblementOuvertureViewBean)) {
            return new CIRassemblementOuvertureViewBean();
        }
        return null;
    }
}
