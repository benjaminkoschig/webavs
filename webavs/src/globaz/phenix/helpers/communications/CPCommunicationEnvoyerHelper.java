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
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.communications.CPCommunicationEnvoyerViewBean;
import globaz.phenix.process.communications.CPProcessAsciiWritter;
import globaz.phenix.process.communications.CPProcessXMLSedexWriter;

/**
 * @author mar
 */
public class CPCommunicationEnvoyerHelper extends FWHelper {

    public CPCommunicationEnvoyerHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start (globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CPCommunicationEnvoyerViewBean vb = (CPCommunicationEnvoyerViewBean) viewBean;
        try {
            if (!JadeStringUtil.isNull(vb.getThroughSedex()) && vb.getThroughSedex().equalsIgnoreCase("on")) {
                CPProcessXMLSedexWriter process = new CPProcessXMLSedexWriter();
                process.setEMailAddress(vb.getEMailAddress());
                process.setForGenreAffilie(vb.getGenreAffilie());
                process.setIdCommunication(vb.getIdCommunication());
                process.setDateEnvoiVide(vb.getDateEnvoiVide());
                process.setDemandeAnnulee(vb.getDemandeAnnulee());
                process.setDateEnvoi(vb.getDateEnvoi());
                process.setAnneeDecision(vb.getAnneeDecision());
                process.setForCanton(vb.getCanton());
                process.setWithAnneeEnCours(vb.getWithAnneeEnCours());
                process.setSession((BSession) session);
                process.setIdIfd(vb.getIdIfd());
                process.setDonneesCommerciales(vb.getDonneesCommerciales());
                process.setDonneesPrivees(vb.getDonneesPrivees());
                BProcessLauncher.start(process);
            } else {
                CPProcessAsciiWritter process = new CPProcessAsciiWritter();
                process.setEMailAddress(vb.getEMailAddress());
                process.setForGenreAffilie(vb.getGenreAffilie());
                process.setIdCommunication(vb.getIdCommunication());
                process.setDateEnvoiVide(vb.getDateEnvoiVide());
                process.setDemandeAnnulee(vb.getDemandeAnnulee());
                process.setDateEnvoi(vb.getDateEnvoi());
                process.setAnneeDecision(vb.getAnneeDecision());
                process.setForCanton(vb.getCanton());
                process.setWithAnneeEnCours(vb.getWithAnneeEnCours());
                process.setSession((BSession) session);
                process.setIdIfd(vb.getIdIfd());
                BProcessLauncher.start(process);
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
