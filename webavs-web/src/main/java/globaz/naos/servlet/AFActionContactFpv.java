package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AFActionContactFpv extends FWDefaultServletAction {

    public AFActionContactFpv(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".afficher";
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".afficher";
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".afficher";
    }

}
