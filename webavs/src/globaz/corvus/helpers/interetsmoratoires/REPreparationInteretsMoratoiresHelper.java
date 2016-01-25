/*
 * Créé le 3 août 07
 */
package globaz.corvus.helpers.interetsmoratoires;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.vb.interetsmoratoires.REPreparationInteretMoratoireViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author BSC
 * 
 */
public class REPreparationInteretsMoratoiresHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface arg0, FWAction arg1, BISession arg2) throws Exception {

        REPreparationInteretMoratoireViewBean pimViewBean = (REPreparationInteretMoratoireViewBean) arg0;

        // on cherche la demande de rente pour recuperer les dates
        REDemandeRente dr = new REDemandeRente();
        dr.setSession((BSession) arg2);
        dr.setIdDemandeRente(pimViewBean.getIdDemandeRente());
        dr.retrieve();

        pimViewBean.setDateDepotDemande(dr.getDateDepot());
        pimViewBean.setDateDebutDroit(dr.getDateDebut());
        if (JadeStringUtil.isIntegerEmpty(pimViewBean.getDateDecision())) {
            pimViewBean.setDateDecision(JACalendar.todayJJsMMsAAAA());
        }
    }

}
