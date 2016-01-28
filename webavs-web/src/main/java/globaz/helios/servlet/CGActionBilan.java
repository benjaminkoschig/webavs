package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.helios.db.comptes.CGBilanListViewBean;
import globaz.helios.tools.CGSessionDataContainerHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage de l'écran du Bilan.
 * 
 * @author DDA
 */
public class CGActionBilan extends globaz.framework.controller.FWDefaultServletAction {

    public CGActionBilan(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((BManager) viewBean).changeManagerSize(BManager.SIZE_NOLIMIT);

        String idPeriodeComptable = ((CGBilanListViewBean) viewBean).getReqForListPeriodesComptable();

        CGSessionDataContainerHelper sessionDataContainer = new CGSessionDataContainerHelper();
        sessionDataContainer.setData(session, CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_PERIODE,
                idPeriodeComptable.trim());

        String idTypeCompta = ((CGBilanListViewBean) viewBean).getReqComptabilite();
        sessionDataContainer.setData(session, CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_TYPE_COMPTA,
                idTypeCompta.trim());

        return viewBean;
    }

}
