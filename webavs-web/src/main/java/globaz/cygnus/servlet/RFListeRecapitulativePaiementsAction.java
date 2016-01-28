/*
 * Créé le 09 mai 2012
 */
package globaz.cygnus.servlet;

import globaz.cygnus.vb.process.RFListeRecapitulativePaiementsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author MBO
 */
public class RFListeRecapitulativePaiementsAction extends RFDefaultAction {

    String adrMailGestionnaire = null;
    String datePeriode = null;

    /**
     * @param servlet
     */
    public RFListeRecapitulativePaiementsAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof RFListeRecapitulativePaiementsViewBean) {
            RFListeRecapitulativePaiementsViewBean vb = (RFListeRecapitulativePaiementsViewBean) viewBean;

            adrMailGestionnaire = vb.geteMailAdr();
            datePeriode = vb.getDatePeriode();

            vb.seteMailAdr(adrMailGestionnaire);
            vb.setDatePeriode(datePeriode);

        }
        return super._getDestExecuterSucces(session, request, response, viewBean);
    }

}
