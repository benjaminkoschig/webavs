package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.pavo.db.splitting.CIMandatSplittingRCViewBean;
import globaz.pavo.db.splitting.CIMandatSplittingViewBean;

/**
 * Insérez la description du type ici. Date de création : (16.10.2002 09:56:57)
 * 
 * @author: Administrator
 */
public class CIActionMandatSplitting extends CIActionCIDefault {
    /**
     * Commentaire relatif au constructeur CIActionSplitting.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionMandatSplitting(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * Exécute la fonction aperçu des RCI (via Hermes).
     * 
     */
    private void _actionApercuRCI(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "/hermes?userAction=hermes.parametrage.attenteReception.chercher&typeAnnonce=8";
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        try {
            FWViewBeanInterface newBean = checkViewBean(viewBean);
            if (newBean != null) {
                viewBean = newBean;
                // le type du viewBean n'est pas valide, préparer le nouveau
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            }
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                // erreur
                destination = getRelativeURL(request, session) + "_de.jsp";
            } else {
                destination += "&referenceUnique=" + ((CIMandatSplittingViewBean) viewBean).getRefUniqueRCI();
                destination += "&idAnnonce=" + ((CIMandatSplittingViewBean) viewBean).getIdAnnonceRCI();
                destination += "&isArchivage="
                        + String.valueOf(((CIMandatSplittingViewBean) viewBean).getIsArchivage().booleanValue());
            }
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Exécute la fonction chercher des mandats de splitting. Date de création : (29.10.2002 13:04:13)
     * 
     * @return globaz.framework.bean.FWViewBean
     */
    private void _actionChercherMandat(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
        CIMandatSplittingRCViewBean viewBean = new CIMandatSplittingRCViewBean();
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
        viewBean = (CIMandatSplittingRCViewBean) mainDispatcher.dispatch(viewBean, action);
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
        if (getAction().getActionPart().equals("chercherMandat")) {
            // chercher avec chargement des données nécessaire
            _actionChercherMandat(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("apercuRCI")) {
            // aperçu des RCI
            _actionApercuRCI(session, request, response, dispatcher);
        } else {
            actionDefault(session, request, response, dispatcher);
        }
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        // idTiers en session -> assignation seulement possible ici
        // nécessaire pour afficher l'idTiers dans un nouveau mandat
        CIMandatSplittingRCViewBean _bean = (CIMandatSplittingRCViewBean) session.getAttribute("viewBeanDossier");
        if (_bean != null) {
            ((CIMandatSplittingViewBean) viewBean).setIdTiersPartenaire(_bean.getIdTiersPartenaire());
            ((CIMandatSplittingViewBean) viewBean).setIdDossierSplitting(_bean.getIdDossierSplitting());
        }
        return viewBean;
    }

    @Override
    public FWViewBeanInterface checkViewBean(FWViewBeanInterface viewBean) {
        if (!(viewBean instanceof CIMandatSplittingViewBean)) {
            return new CIMandatSplittingViewBean();
        }
        return null;
    }
}
