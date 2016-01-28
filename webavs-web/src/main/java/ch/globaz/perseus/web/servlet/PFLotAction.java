package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.perseus.vb.lot.PFGenererListeOrdresVersementViewBean;
import globaz.perseus.vb.lot.PFImprimerDecisionFactureViewBean;
import globaz.perseus.vb.lot.PFPrestationViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PFLotAction extends PFAbstractDefaultServletAction {

    public PFLotAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFPrestationViewBean) {

            return getActionFullURL() + ".chercher&selectedId=" + request.getParameter("idLot") + "&csTypeLot="
                    + request.getParameter("csTypeLot") + "&csEtatLot=" + request.getParameter("csEtatLot")
                    + "&descriptionLot=" + request.getParameter("descriptionLot");
        }

        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFGenererListeOrdresVersementViewBean) {
            PFGenererListeOrdresVersementViewBean vb = (PFGenererListeOrdresVersementViewBean) viewBean;
            vb.setIdLot(request.getParameter("idLot"));

        }

        if (viewBean instanceof PFImprimerDecisionFactureViewBean) {
            PFImprimerDecisionFactureViewBean vb = (PFImprimerDecisionFactureViewBean) viewBean;
            vb.setIdLot(request.getParameter("idLot"));

        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
