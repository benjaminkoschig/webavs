/*
 * Créé le 30 novembre 2009
 */
package globaz.cygnus.servlet;

import globaz.cygnus.vb.process.RFStatistiquesParMontantsSashViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author MBO
 */
public class RFStatistiquesParMontantsSashAction extends RFDefaultAction {

    String adrMailGestionnaire = null;
    String dateDebut = null;
    String dateFin = null;
    String nomGestionnaire = null;

    /**
     * @param servlet
     */
    public RFStatistiquesParMontantsSashAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof RFStatistiquesParMontantsSashViewBean) {
            RFStatistiquesParMontantsSashViewBean vb = (RFStatistiquesParMontantsSashViewBean) viewBean;

            nomGestionnaire = vb.getGestionnaire();
            adrMailGestionnaire = vb.geteMailAdr();
            dateDebut = vb.getDateDebutStat();
            dateFin = vb.getDateFinStat();

            vb.setGestionnaire(nomGestionnaire);
            vb.seteMailAdr(adrMailGestionnaire);
            vb.setDateDebutStat(dateDebut);
            vb.setDateFinStat(dateFin);

        }

        return super._getDestExecuterSucces(session, request, response, viewBean);
    }
}
