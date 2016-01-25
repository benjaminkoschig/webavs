package ch.globaz.pegasus.business.services.process.adaptation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.DemandePcaPrestationSearch;
import ch.globaz.pegasus.business.models.process.adaptation.DonneeFinanciereSearch;
import ch.globaz.pegasus.business.vo.process.adaptation.DonneeFinancierePartiel;

public interface AdaptationService extends JadeApplicationService {

    /**
     * Permet de chercher des certaines données financière selon un modèle de critères. Ce service est utiliser dans le
     * cas ou l'on n'a pas de BLOB pourle calcule des donnnée financières
     * 
     * @param simpleRenteAdaptationSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     * @throws AdaptationException
     */
    public DonneeFinancierePartiel findDonneeFinanciereAncienne(DonneeFinanciereSearch search)
            throws RenteAdapationDemandeException, AdaptationException, JadePersistenceException;

    public DemandePcaPrestationSearch search(DemandePcaPrestationSearch search) throws JadePersistenceException,
            AdaptationException;

    public String findInfosForImpression(String idProcess) throws JadePersistenceException, JadeApplicationException;

}
