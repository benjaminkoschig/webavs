/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.formule;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.amal.business.exceptions.models.formule.HistoriqueImportationException;
import ch.globaz.amal.business.models.journalisation.HistoriqueImportationSearch;
import ch.globaz.amal.business.services.models.formule.HistoriqueImportationService;

/**
 * @author LFO
 * 
 */
public class HistoriqueImportationServiceImpl implements HistoriqueImportationService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primemoyenne.SimplePrimeMoyenneService#search(ch.globaz.amal.business
     * .models.primemoyenne.SimplePrimeMoyenneSearch)
     */
    @Override
    public HistoriqueImportationSearch search(HistoriqueImportationSearch historiqueImportationSearch)
            throws JadePersistenceException, HistoriqueImportationException {
        if (historiqueImportationSearch == null) {
            throw new HistoriqueImportationException(
                    "Unable to search historique importation, the search model passed is null!");
        }
        return (HistoriqueImportationSearch) JadePersistenceManager.search(historiqueImportationSearch);
    }

}
