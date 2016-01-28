package ch.globaz.al.web.servlet;

import globaz.al.vb.radiationaffilie.ALRadiationAffilieViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ALActionRadiationAffilie extends ALAbstractDefaultAction {

    /**
     * Constructeur. Appelle le constructeur par défaut
     * 
     * @param servlet
     */
    public ALActionRadiationAffilie(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALRadiationAffilieViewBean) {

            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";
        } else {
            return _getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".reAfficher&_method=add";
    }

}
