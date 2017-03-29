/*
 * Créé le 17 juillet 2008
 */
package globaz.corvus.helpers.process;

import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.REEnvoyerAnnoncesProcess;
import globaz.corvus.process.REEnvoyerAnnoncesXMLProcess;
import globaz.corvus.properties.REProperties;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.process.REEnvoyerAnnoncesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import ch.globaz.common.business.exceptions.CommonTechnicalException;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REEnvoyerAnnoncesHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        REEnvoyerAnnoncesViewBean eaViewBean = (REEnvoyerAnnoncesViewBean) viewBean;

        if (valider((BSession) session, viewBean)) {
            try {
                // D0215 si la propriété est activée on passe en mode xml
                if (REProperties.ACTIVER_ANNONCES_XML.getBooleanValue()) {
                    REEnvoyerAnnoncesXMLProcess process = new REEnvoyerAnnoncesXMLProcess((BSession) session);
                    process.setEMailAddress(eaViewBean.getEMailAddress());
                    process.setForDateEnvoi(eaViewBean.getForDateEnvoi());
                    process.setForMoisAnneeComptable(eaViewBean.getForMoisAnneeComptable());
                    process.setIsForAnnoncesSubsequentes(false);
                    process.start();

                } else {

                    REEnvoyerAnnoncesProcess process = new REEnvoyerAnnoncesProcess((BSession) session);
                    process.setEMailAddress(eaViewBean.getEMailAddress());
                    process.setForDateEnvoi(eaViewBean.getForDateEnvoi());
                    process.setForMoisAnneeComptable(eaViewBean.getForMoisAnneeComptable());
                    process.setIsForAnnoncesSubsequentes(false);
                    process.start();
                }

            } catch (Exception e) {
                throw new CommonTechnicalException("Problem in REEnvoyerAnnoncesHelper", e);
            }
        }

    }

    /**
     * Valider les données du viewBean pour empêcher les process foireux
     * 
     * @param session
     * @param viewBean référence vers le viewBean (doit être un REEnvoyerAnnoncesViewBean)
     * @return false si non validé, les messages d'erreurs sont remontés dans le viewBean
     */
    private boolean valider(BSession session, FWViewBeanInterface viewBean) {
        REEnvoyerAnnoncesViewBean eaViewBean = (REEnvoyerAnnoncesViewBean) viewBean;
        if (!eaViewBean.getForMoisAnneeComptable().matches("^(0[1-9]{1}|1[0-2]{1}).\\d{4}$")) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(session.getLabel("VALID_MOIS_ANNEE_COMPTABLE"));
            return false;
        } else {
            String dateDernierPaiement = REPmtMensuel.getDateDernierPmt(session);
            if (REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT.equals(dateDernierPaiement)) {
                String message = session.getLabel("ERREUR_IMPOSSIBLE_RETROUVER_DATE_DERNIER_PAIEMENT");
                throw new RETechnicalException(message);
            }
            try {
                JADate moisEncours = new JADate(dateDernierPaiement);
                int encours = moisEncours.getYear() * 12 + moisEncours.getMonth();
                JADate dateProcess = new JADate(eaViewBean.getForMoisAnneeComptable());
                int moisProcess = dateProcess.getYear() * 12 + dateProcess.getMonth();
                if (encours - moisProcess > 1 || encours - moisProcess < 0) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(session.getLabel("VALID_MOIS_ANNEE_COMPTABLE"));
                    return false;
                }
            } catch (JAException e) {
                throw new RETechnicalException(e);
            }
        }
        return true;
    }

}
