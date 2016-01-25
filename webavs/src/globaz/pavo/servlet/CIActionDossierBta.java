package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.pavo.db.bta.CIDossierBta;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author BJO
 * 
 */
public class CIActionDossierBta extends FWDefaultServletAction {
    /**
     * @param servlet
     */
    public CIActionDossierBta(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return
        // "pavo?userAction=pavo.bta.dossierBta.afficher&selectedId="+((CIDossierBta)viewBean).getIdDossierBta();
        return getActionFullURL() + ".afficher&_method=upd&_back=sl&selectedId="
                + ((CIDossierBta) viewBean).getIdDossierBta();
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return
        // "pavo?userAction=pavo.bta.dossierBta.afficher&selectedId="+((CIDossierBta)viewBean).getIdDossierBta();
        return getActionFullURL() + ".afficher";
    }

}