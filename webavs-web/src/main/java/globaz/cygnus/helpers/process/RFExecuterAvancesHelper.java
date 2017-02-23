package globaz.cygnus.helpers.process;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.helpers.process.REGenererAvancesProcess;
import globaz.corvus.process.REExecuter1erAcompteAvancesProcess;
import globaz.cygnus.vb.process.RFExecuterAvancesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author sce INFOROM 547, 02.2013 -- SCE
 */
public class RFExecuterAvancesHelper extends PRAbstractHelper {
    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        RFExecuterAvancesViewBean vb = (RFExecuterAvancesViewBean) viewBean;

        // Dispatch sur le process en fonction du choix du traitement
        if (IREAvances.CS_TYPE_ACOMPTES_UNIQUE.equals(vb.getCsTypeAvance())) {
            // Paiment des avances unique,
            REExecuter1erAcompteAvancesProcess process = new REExecuter1erAcompteAvancesProcess();
            process.setSession((BSession) session);
            process.setCsDomaineApplicatif(IREAvances.CS_DOMAINE_AVANCE_RFM);
            process.setEMailAddress(vb.getEMailAddress());
            process.setNoOg(vb.getNoOg());
            process.setIdOrganeExecution(vb.getIdOrganeExecution());
            process.setDateEcheance(vb.getDateEcheance());
            process.setIsoGestionnaire(vb.getIsoGestionnaire());
            process.setIsoHighPriority(vb.getIsoHighPriority());
            process.start();
        } else if (IREAvances.CS_TYPE_LISTES.equals(vb.getCsTypeAvance())) {
            // Génération du fichier excel, liste de toutes les avances
            REGenererAvancesProcess process = new REGenererAvancesProcess();
            process.setSession((BSession) session);
            process.setEMailAddress(vb.getEMailAddress());
            process.start();
        }
    }
}
