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
     * M�thode qui r�cup�re les donn�es pour le le document d�taill� des d�claration de versement
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
     * M�thode qui r�cup�re les donn�es pour le le document non d�taill� des d�claration de versement
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
