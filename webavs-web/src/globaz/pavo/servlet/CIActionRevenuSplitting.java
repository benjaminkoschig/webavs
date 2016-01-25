package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.pavo.db.splitting.CIRevenuSplittingRCViewBean;
import globaz.pavo.db.splitting.CIRevenuSplittingViewBean;

/**
 * Actions concerant les domiciles � l'�tranger. Date de cr�ation : (22.10.2002 09:49:21)
 * 
 * @author: dgi
 */
public class CIActionRevenuSplitting extends FWDefaultServletAction {

    /**
     * Commentaire relatif au constructeur CIActionDomicileSplitting.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionRevenuSplitting(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * Ex�cute la fonction chercher des revenus de splitting. Date de cr�ation : (29.10.2002 13:04:13)
     * 
     * @exception si
     *                l'ex�cution a �chou�e.
     */
    private void _actionChercherRevenu(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination;
        // nouvelle instance du bean utilis� dans l'en-t�te de la recherche.
        CIRevenuSplittingRCViewBean viewBean = new CIRevenuSplittingRCViewBean();
        // enregister les param�tres de la requ�te dans le bean
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        // en mode nouveau du mandat, il ne faut pas affichier les revenus!
        if (viewBean.getIdMandatSplitting().length() != 0) {
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur
            viewBean = (CIRevenuSplittingRCViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session en tant que bean foreig key
            // (utilis� plus
            // tard �galement)
            // tocheck: nom viewBeanFK ok avec bean du mandat?
            session.setAttribute("viewBeanMandat", viewBean);
            // redirection vers destination
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        } else {
            _destination = "splitting/mandatSplitting_de.jsp";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("chercherRevenu")) {
            // chercher avec chargement des donn�es n�cessaire
            _actionChercherRevenu(session, request, response, dispatcher);
        }
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        // idMandat en session -> assignation seulement possible ici
        // n�cessaire pour afficher l'idMandatSplitting dans un nouveau revenu
        CIRevenuSplittingRCViewBean _bean = (CIRevenuSplittingRCViewBean) session.getAttribute("viewBeanMandat");
        if (_bean != null) {
            ((CIRevenuSplittingViewBean) viewBean).setIdMandatSplitting(_bean.getIdMandatSplitting());
        }
        return viewBean;
    }
}
