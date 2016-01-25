package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.pavo.db.bta.CIRequerantBta;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author BJO
 * 
 */
public class CIActionRequerantBta extends FWDefaultServletAction {
    /**
     * @param servlet
     */
    public CIActionRequerantBta(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return
        // getActionFullURL()+".afficher&_method=upd&_back=sl&selectedId="+((CIRequerantBta)viewBean).getIdRequerant();
        // "/"+_action.getApplicationPart()+"?userAction="+
        return "pavo?userAction=pavo.bta.dossierBta.afficher&selectedId="
                + ((CIRequerantBta) viewBean).getIdDossierBta();
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getActionFullURL()+".afficher";
        return "pavo?userAction=pavo.bta.dossierBta.afficher&selectedId="
                + ((CIRequerantBta) viewBean).getIdDossierBta();
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "pavo?userAction=pavo.bta.dossierBta.afficher&selectedId="
                + ((CIRequerantBta) viewBean).getIdDossierBta();
    }

}