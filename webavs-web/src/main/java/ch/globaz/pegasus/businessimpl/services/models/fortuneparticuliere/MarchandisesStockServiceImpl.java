package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.MarchandisesStockException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStock;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStockSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.MarchandisesStockService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class MarchandisesStockServiceImpl extends PegasusAbstractServiceImpl implements MarchandisesStockService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. MarchandisesStockService
     * #count(ch.globaz.pegasus.business.models.fortuneparticuliere .MarchandisesStockSearch)
     */
    @Override
    public int count(MarchandisesStockSearch search) throws MarchandisesStockException, JadePersistenceException {
        if (search == null) {
            throw new MarchandisesStockException("Unable to count marchandisesStock, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. MarchandisesStockService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .MarchandisesStock)
     */
    @Override
    public MarchandisesStock create(MarchandisesStock marchandisesStock) throws JadePersistenceException,
            MarchandisesStockException, DonneeFinanciereException {
        if (marchandisesStock == null) {
            throw new MarchandisesStockException("Unable to create marchandisesStock, the model passed is null!");
        }

        try {
            marchandisesStock.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            marchandisesStock.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleMarchandisesStockService().create(
                    marchandisesStock.getSimpleMarchandisesStock());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new MarchandisesStockException("Service not available - " + e.getMessage());
        }

        return marchandisesStock;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. MarchandisesStockService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .MarchandisesStock)
     */
    @Override
    public MarchandisesStock delete(MarchandisesStock marchandisesStock) throws MarchandisesStockException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * MarchandisesStockService#read(java.lang.String)
     */
    @Override
    public MarchandisesStock read(String idMarchandisesStock) throws JadePersistenceException,
            MarchandisesStockException {
        if (JadeStringUtil.isEmpty(idMarchandisesStock)) {
            throw new MarchandisesStockException("Unable to read marchandisesStock, the id passed is null!");
        }
        MarchandisesStock droit = new MarchandisesStock();
        droit.setId(idMarchandisesStock);
        return (MarchandisesStock) JadePersistenceManager.read(droit);
    }

    /**
     * Chargement d'une MarchandisesStoc via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws MarchandisesStocException
     * @throws JadePersistenceException
     */
    @Override
    public MarchandisesStock readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws MarchandisesStockException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new MarchandisesStockException(
                    "Unable to find MarchandisesStock the idDonneeFinanciereHeader passed si null!");
        }

        MarchandisesStockSearch search = new MarchandisesStockSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (MarchandisesStockSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new MarchandisesStockException("More than one MarchandisesStock find, one was exepcted!");
        }

        return (MarchandisesStock) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((MarchandisesStockSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. MarchandisesStockService
     * #search(ch.globaz.pegasus.business.models.fortuneparticuliere .MarchandisesStockSearch)
     */
    @Override
    public MarchandisesStockSearch search(MarchandisesStockSearch marchandisesStockSearch)
            throws JadePersistenceException, MarchandisesStockException {
        if (marchandisesStockSearch == null) {
            throw new MarchandisesStockException("Unable to search marchandisesStock, the search model passed is null!");
        }
        return (MarchandisesStockSearch) JadePersistenceManager.search(marchandisesStockSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. MarchandisesStockService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .MarchandisesStock)
     */
    @Override
    public MarchandisesStock update(MarchandisesStock marchandisesStock) throws JadePersistenceException,
            MarchandisesStockException, DonneeFinanciereException {
        if (marchandisesStock == null) {
            throw new MarchandisesStockException("Unable to update marchandisesStock, the model passed is null!");
        }

        try {
            marchandisesStock.setSimpleMarchandisesStock(PegasusImplServiceLocator.getSimpleMarchandisesStockService()
                    .update(marchandisesStock.getSimpleMarchandisesStock()));
            marchandisesStock.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            marchandisesStock.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new MarchandisesStockException("Service not available - " + e.getMessage());
        }

        return marchandisesStock;
    }

}
