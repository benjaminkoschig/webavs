package globaz.osiris.api;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;

public interface APIUtil {

    /**
     * Création d'un numéro de section unique utilisé par le module sunanda.
     * 
     * @param session
     * @param trans
     * @param idRole
     * @param idExterneRole
     * @param idTypeSection
     * @param annee
     * @param categorieSection
     * @return
     * @throws Exception
     */
    public String creerNumeroSectionUniquePourDossierAf(BISession session, BITransaction trans, String idRole,
            String idExterneRole, String idTypeSection, String annee, String categorieSection) throws Exception;

}