package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.MarchandisesStockException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleMarchandisesStock;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleMarchandisesStockSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleMarchandisesStockService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere.SimpleMarchandisesStockChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleMarchandisesStockServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleMarchandisesStockService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleMarchandisesStockService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleMarchandisesStock)
     */
    @Override
    public SimpleMarchandisesStock create(SimpleMarchandisesStock simpleMarchandisesStock)
            throws JadePersistenceException, MarchandisesStockException {
        if (simpleMarchandisesStock == null) {
            throw new MarchandisesStockException("Unable to create simpleMarchandisesStock, the model passed is null!");
        }
        SimpleMarchandisesStockChecker.checkForCreate(simpleMarchandisesStock);
        return (SimpleMarchandisesStock) JadePersistenceManager.add(simpleMarchandisesStock);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleMarchandisesStockService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleMarchandisesStock)
     */
    @Override
    public SimpleMarchandisesStock delete(SimpleMarchandisesStock simpleMarchandisesStock)
            throws MarchandisesStockException, JadePersistenceException {
        if (simpleMarchandisesStock == null) {
            throw new MarchandisesStockException("Unable to delete simpleMarchandisesStock, the model passed is null!");
        }
        if (simpleMarchandisesStock.isNew()) {
            throw new MarchandisesStockException("Unable to delete simpleMarchandisesStock, the model passed is new!");
        }
        SimpleMarchandisesStockChecker.checkForDelete(simpleMarchandisesStock);
        return (SimpleMarchandisesStock) JadePersistenceManager.delete(simpleMarchandisesStock);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleMarchandisesStockService#deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleMarchandisesStockSearch search = new SimpleMarchandisesStockSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleMarchandisesStockService#read(java.lang.String)
     */
    @Override
    public SimpleMarchandisesStock read(String idMarchandisesStock) throws JadePersistenceException,
            MarchandisesStockException {
        if (JadeStringUtil.isEmpty(idMarchandisesStock)) {
            throw new MarchandisesStockException(
                    "Unable to read simpleMarchandisesStock, the id passed is not defined!");
        }
        SimpleMarchandisesStock simpleMarchandisesStock = new SimpleMarchandisesStock();
        simpleMarchandisesStock.setId(idMarchandisesStock);
        return (SimpleMarchandisesStock) JadePersistenceManager.read(simpleMarchandisesStock);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleMarchandisesStockService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleMarchandisesStock)
     */
    @Override
    public SimpleMarchandisesStock update(SimpleMarchandisesStock simpleMarchandisesStock)
            throws JadePersistenceException, MarchandisesStockException {
        if (simpleMarchandisesStock == null) {
            throw new MarchandisesStockException("Unable to update simpleMarchandisesStock, the model passed is null!");
        }
        if (simpleMarchandisesStock.isNew()) {
            throw new MarchandisesStockException("Unable to update simpleMarchandisesStock, the model passed is new!");
        }
        SimpleMarchandisesStockChecker.checkForUpdate(simpleMarchandisesStock);
        return (SimpleMarchandisesStock) JadePersistenceManager.update(simpleMarchandisesStock);
    }

}
