package ch.globaz.cygnus.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;

public interface ConventionService extends JadeApplicationService {
    /**
     * Methode qui recherche si le fournisseur pass� en param�tre, est au b�n�fice d'une convention
     * 
     * @param vb
     * @return TRUE si convention trouv�e, FALSE si aucune convention trouv�e
     * @throws Exception
     */
    public Boolean getfournisseurConventionne(String idTiers, String codeTypeDeSoin, String codeSousTypeDeSoin)
            throws Exception;
}
