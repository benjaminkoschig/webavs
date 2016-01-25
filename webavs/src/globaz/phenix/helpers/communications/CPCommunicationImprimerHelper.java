/*
 * Created on 29 nov. 05
 */
package globaz.phenix.helpers.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.communications.CPCommunicationImprimerViewBean;
import globaz.phenix.documentsItext.CPImpressionCommunication_Doc;

/**
 * @author mar
 */
public class CPCommunicationImprimerHelper extends FWHelper {

    /**
	 * 
	 */
    public CPCommunicationImprimerHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start (globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CPCommunicationImprimerViewBean vb = (CPCommunicationImprimerViewBean) viewBean;
        try {
            CPImpressionCommunication_Doc process = new CPImpressionCommunication_Doc();
            process.setEMailAddress(vb.getEMailAddress());
            process.setForGenreAffilie(vb.getGenreAffilie());
            process.setDateEdition(vb.getDateEdition());
            process.setDateEnvoiVide(vb.getDateEnvoiVide());
            process.setIdCommunication(vb.getIdCommunication());
            process.setIdAffiliation(vb.getIdAffiliation());
            process.setDateEnvoi(vb.getDateEnvoi());
            process.setAnneeDecision(vb.getAnneeDecision());
            process.setForCanton(vb.getCanton());
            process.setSession((BSession) session);
            process.setDebutActivite(vb.getDebutActivite());
            process.setIdIfd(vb.getIdIfd());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
