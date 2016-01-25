package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.NumeraireException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleNumeraire;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleNumeraireSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleNumeraireService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere.SimpleNumeraireChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleNumeraireServiceImpl extends PegasusAbstractServiceImpl implements SimpleNumeraireService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleNumeraireService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleNumeraire)
     */
    @Override
    public SimpleNumeraire create(SimpleNumeraire simpleNumeraire) throws JadePersistenceException, NumeraireException {
        if (simpleNumeraire == null) {
            throw new NumeraireException("Unable to create simpleNumeraire, the model passed is null!");
        }
        SimpleNumeraireChecker.checkForCreate(simpleNumeraire);
        return (SimpleNumeraire) JadePersistenceManager.add(simpleNumeraire);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleNumeraireService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleNumeraire)
     */
    @Override
    public SimpleNumeraire delete(SimpleNumeraire simpleNumeraire) throws NumeraireException, JadePersistenceException {
        if (simpleNumeraire == null) {
            throw new NumeraireException("Unable to delete simpleNumeraire, the model passed is null!");
        }
        if (simpleNumeraire.isNew()) {
            throw new NumeraireException("Unable to delete simpleNumeraire, the model passed is new!");
        }
        SimpleNumeraireChecker.checkForDelete(simpleNumeraire);
        return (SimpleNumeraire) JadePersistenceManager.delete(simpleNumeraire);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleNumeraireService#deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleNumeraireSearch search = new SimpleNumeraireSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleNumeraireService#read(java.lang.String)
     */
    @Override
    public SimpleNumeraire read(String idNumeraire) throws JadePersistenceException, NumeraireException {
        if (JadeStringUtil.isEmpty(idNumeraire)) {
            throw new NumeraireException("Unable to read simpleNumeraire, the id passed is not defined!");
        }
        SimpleNumeraire simpleNumeraire = new SimpleNumeraire();
        simpleNumeraire.setId(idNumeraire);
        return (SimpleNumeraire) JadePersistenceManager.read(simpleNumeraire);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleNumeraireService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleNumeraire)
     */
    @Override
    public SimpleNumeraire update(SimpleNumeraire simpleNumeraire) throws JadePersistenceException, NumeraireException {
        if (simpleNumeraire == null) {
            throw new NumeraireException("Unable to update simpleNumeraire, the model passed is null!");
        }
        if (simpleNumeraire.isNew()) {
            throw new NumeraireException("Unable to update simpleNumeraire, the model passed is new!");
        }
        SimpleNumeraireChecker.checkForUpdate(simpleNumeraire);
        return (SimpleNumeraire) JadePersistenceManager.update(simpleNumeraire);
    }

}
