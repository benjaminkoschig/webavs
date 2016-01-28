package ch.globaz.cygnus.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;

public interface ConventionService extends JadeApplicationService {
    /**
     * Methode qui recherche si le fournisseur passé en paramètre, est au bénéfice d'une convention
     * 
     * @param vb
     * @return TRUE si convention trouvée, FALSE si aucune convention trouvée
     * @throws Exception
     */
    public Boolean getfournisseurConventionne(String idTiers, String codeTypeDeSoin, String codeSousTypeDeSoin)
            throws Exception;
}
