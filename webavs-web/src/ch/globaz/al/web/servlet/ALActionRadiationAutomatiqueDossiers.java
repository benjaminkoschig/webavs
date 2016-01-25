package ch.globaz.al.web.servlet;

import globaz.al.vb.radiationauto.ALRadiationAutomatiqueDossiersViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ALActionRadiationAutomatiqueDossiers extends ALAbstractDefaultAction {

    /**
     * Constructeur. Appelle le constructeur par défaut
     * 
     * @param servlet
     */
    public ALActionRadiationAutomatiqueDossiers(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALRadiationAutomatiqueDossiersViewBean) {

            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";
        } else {
            return _getDestAjouterSucces(session, request, response, viewBean);
        }
    }
}
