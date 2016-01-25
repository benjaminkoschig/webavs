package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.taxationoffice.PTTaxationofficeViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PTTaxationOfficeAction extends FWDefaultServletAction {

    public PTTaxationOfficeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTTaxationofficeViewBean) {
            PTTaxationofficeViewBean vb = (PTTaxationofficeViewBean) viewBean;
            String idDecompte = request.getParameter(PTConstants.ID_DECOMPTE);
            vb.setIdDecompte(idDecompte);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTTaxationofficeViewBean) {
            PTTaxationofficeViewBean vb = (PTTaxationofficeViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.taxationoffice.taxationoffice.afficher&idDecompte=" + vb.getIdDecompte();
        }
        return super._getDestModifierSucces(session, request, response, viewBean);
    }
}
