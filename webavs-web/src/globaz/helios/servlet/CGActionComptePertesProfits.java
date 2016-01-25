package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.helios.db.comptes.CGComptePertesProfitsListViewBean;
import globaz.helios.tools.CGSessionDataContainerHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion de l'écran de pertes et profits.
 * 
 * @author DDA
 * 
 */
public class CGActionComptePertesProfits extends globaz.framework.controller.FWDefaultServletAction {

    public CGActionComptePertesProfits(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeLister(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWViewBeanInterface) Affiche tous (le maximum) les mouvements du rcListe.
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((BManager) viewBean).changeManagerSize(BManager.SIZE_NOLIMIT);

        String idPeriodeComptable = null;
        String idTypeCompta = null;
        if (viewBean instanceof CGComptePertesProfitsListViewBean) {
            idPeriodeComptable = ((CGComptePertesProfitsListViewBean) viewBean).getReqForListPeriodesComptable();
            idTypeCompta = ((CGComptePertesProfitsListViewBean) viewBean).getReqComptabilite();
        }

        CGSessionDataContainerHelper sessionDataContainer = new CGSessionDataContainerHelper();
        sessionDataContainer.setData(session, CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_PERIODE,
                idPeriodeComptable.trim());

        sessionDataContainer.setData(session, CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_TYPE_COMPTA,
                idTypeCompta.trim());

        return viewBean;
    }

}
