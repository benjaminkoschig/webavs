package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.divers.CPPeriodeFiscaleListViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionPeriodeFiscale extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionPeriodeFiscale(FWServlet servlet) {
        super(servlet);
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        CPPeriodeFiscaleListViewBean vBean = (CPPeriodeFiscaleListViewBean) viewBean;
        // Tri
        vBean.orderByIfd();
        vBean.orderByAnneeDecisionDebut();
        vBean.orderByAnneeRevenuDebut();
        return vBean;
    }
}
