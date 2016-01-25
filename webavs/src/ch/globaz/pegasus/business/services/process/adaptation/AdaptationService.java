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
     * Permet de chercher des certaines donn�es financi�re selon un mod�le de crit�res. Ce service est utiliser dans le
     * cas ou l'on n'a pas de BLOB pourle calcule des donnn�e financi�res
     * 
     * @param simpleRenteAdaptationSearch Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws AdaptationException
     */
    public DonneeFinancierePartiel findDonneeFinanciereAncienne(DonneeFinanciereSearch search)
            throws RenteAdapationDemandeException, AdaptationException, JadePersistenceException;

    public DemandePcaPrestationSearch search(DemandePcaPrestationSearch search) throws JadePersistenceException,
            AdaptationException;

    public String findInfosForImpression(String idProcess) throws JadePersistenceException, JadeApplicationException;

}
