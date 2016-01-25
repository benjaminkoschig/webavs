/**
 * 
 */
package ch.globaz.amal.business.services.models.formule;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.amal.business.exceptions.models.formule.HistoriqueImportationException;
import ch.globaz.amal.business.exceptions.models.primemoyenne.PrimeMoyenneException;
import ch.globaz.amal.business.models.journalisation.HistoriqueImportationSearch;

/**
 * @author LFO
 * 
 */
public interface HistoriqueImportationService extends JadeApplicationService {

    /**
     * Permet la recherche d'entités
     * 
     * @param primeMoyenneSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public HistoriqueImportationSearch search(HistoriqueImportationSearch historiqueImportationSearch)
            throws JadePersistenceException, HistoriqueImportationException;

}
