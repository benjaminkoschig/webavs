package globaz.aquila.ts.opge;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.tiers.COTiersService;
import globaz.aquila.ts.COTSPredicate;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

/**
 * Permet de pr�dire si un contentieux et une transition sont des candidats pour le traitement sp�cifiques de cr�ation
 * du fichier pour l'OP de Gen�ve.
 * 
 * @author vre
 */
public class COTSOPGEPredicate implements COTSPredicate {

    private static final String CODE_CANTON_GENEVE = "GE";

    private COTiersService tiersService = COServiceLocator.getTiersService();

    /**
     * @see COTSPredicate#evaluate(COContentieux, COTransition, String)
     */
    @Override
    public boolean evaluate(COContentieux contentieux, COTransition transition, String dateExecution) throws Exception {
        return isTransitionRDP(transition) && isDependOPGE(contentieux, dateExecution)
                && isFirstRequisitionPoursuite(contentieux);
    }

    /**
     * Le contentieux est-il dans le canton de gen�ve ?
     * 
     * @param contentieux
     * @param dateExecution
     * @return
     * @throws Exception
     */
    private boolean isDependOPGE(COContentieux contentieux, String dateExecution) throws Exception {
        IntTiers officePoursuite = COServiceLocator.getTiersService().getOfficePoursuite(contentieux.getSession(),
                contentieux.getCompteAnnexe().getTiers(), contentieux.getCompteAnnexe().getIdExterneRole());

        if (officePoursuite == null) {
            return false;
        }

        TIAdresseDataSource adresseDataSource = tiersService.getAdresseDataSourceForOfficePoursuite(
                contentieux.getSession(), officePoursuite, dateExecution);
        return COTSOPGEPredicate.CODE_CANTON_GENEVE.equalsIgnoreCase(adresseDataSource.canton_court.toUpperCase());
    }

    /**
     * Afin d'�tre candidate � la r�quisition de poursuite par bande, la r�quisition doit �tre la premi�re. <br/>
     * Si une r�quisition � d�j� �t� effectu� alors l'�tape r�quisition sera effectu� manuellement.
     * 
     * @param contentieux
     * @return
     * @throws Exception
     */
    private boolean isFirstRequisitionPoursuite(COContentieux contentieux) throws Exception {
        return null == COServiceLocator.getHistoriqueService().getHistoriqueForLibEtape(contentieux.getSession(),
                contentieux, ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE);
    }

    /**
     * La transition est-elle une RDP ?
     * 
     * @param transition
     * @return
     */
    private boolean isTransitionRDP(COTransition transition) {
        return transition.getEtapeSuivante().getLibEtape().equals(ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE);
    }
}
