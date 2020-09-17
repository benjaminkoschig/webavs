package globaz.corvus.helpers.adaptation;

import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.properties.PropertiesException;
import globaz.corvus.process.REEnvoiAnnoncesSubsequentesProcess;
import globaz.corvus.process.REEnvoyerAnnoncesXMLProcess;
import globaz.corvus.properties.REProperties;
import globaz.corvus.vb.adaptation.REEnvoiAnnoncesSubsequentesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 *
 * @author HPE
 *
 */
public class REEnvoiAnnoncesSubsequentesHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REEnvoiAnnoncesSubsequentesViewBean vb = (REEnvoiAnnoncesSubsequentesViewBean) viewBean;

        // D0215 si la propriété est activée on passe en mode xml
        try {
            if (REProperties.ACTIVER_ANNONCES_XML.getBooleanValue()) {
                REEnvoyerAnnoncesXMLProcess process = new REEnvoyerAnnoncesXMLProcess((BSession) session);
                process.setEMailAddress(vb.getEMailAddress());
                process.setForDateEnvoi(JACalendar.todayJJsMMsAAAA());
                process.setForMoisAnneeComptable(vb.getMoisAnnee());
                process.setIsForAnnoncesSubsequentes(true);
                process.start();

            } else {

                REEnvoiAnnoncesSubsequentesProcess process = new REEnvoiAnnoncesSubsequentesProcess();
                process.setSession(vb.getSession());
                process.setEMailAddress(vb.getEMailAddress());
                process.setMoisAnnee(vb.getMoisAnnee());
                process.start();
            }
        } catch (PropertiesException e) {
            throw new CommonTechnicalException("Impossible de récupérer la propriété corvus.isAnnoncesXML", e);
        }

    }

}
