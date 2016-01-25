/**
 * 
 */
package ch.globaz.al.business.services.attestationVersement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexSearchModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;

/**
 * @author pta
 * 
 */
public interface AttestationsVersementLoadCsvDetailleService extends JadeApplicationService {
    /**
     * Méthode qui récupére les données pour le le document détaillé des déclaration de versement
     * 
     * @param listPrestAtte
     * @param dossierSearch
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String buildCsvAttestationDetaille(
            ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listPrestAtte,
            DossierAttestationVersementComplexSearchModel dossierSearch, String periodeDe, String periodeA,
            String typePrestation) throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode qui récupére les données pour le le document non détaillé des déclaration de versement
     * 
     * @param listPrestAtte
     * @param dossierSearch
     * @param periodeDe
     * @param periodeA
     * @param typePrestation
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String buildCsvAttestationNonDetaille(
            ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listPrestAtte,
            DossierAttestationVersementComplexSearchModel dossierSearch, String periodeDe, String periodeA,
            String typePrestation) throws JadeApplicationException, JadePersistenceException;

}
