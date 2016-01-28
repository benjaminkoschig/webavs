package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.VehiculeException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule;
import ch.globaz.pegasus.business.models.fortuneparticuliere.VehiculeSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.VehiculeService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class VehiculeServiceImpl extends PegasusAbstractServiceImpl implements VehiculeService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. VehiculeService
     * #count(ch.globaz.pegasus.business.models.fortuneparticuliere .VehiculeSearch)
     */
    @Override
    public int count(VehiculeSearch search) throws VehiculeException, JadePersistenceException {
        if (search == null) {
            throw new VehiculeException("Unable to count vehicule, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. VehiculeService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule)
     */
    @Override
    public Vehicule create(Vehicule vehicule) throws JadePersistenceException, VehiculeException,
            DonneeFinanciereException {

        if (vehicule == null) {
            throw new VehiculeException("Unable to create vehicule, the given model is null!");
        }

        try {
            // creation du donneeFinanciereHeader
            vehicule.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .create(vehicule.getSimpleDonneeFinanciereHeader()));

            // creation du simpleVehicule
            vehicule.setSimpleVehicule(PegasusImplServiceLocator.getSimpleVehiculeService().create(
                    vehicule.getSimpleVehicule()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new VehiculeException("Service not available - " + e.getMessage());
        }

        return vehicule;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. VehiculeService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule)
     */
    @Override
    public Vehicule delete(Vehicule vehicule) throws VehiculeException, DonneeFinanciereException,
            JadePersistenceException {
        if (vehicule == null) {
            throw new VehiculeException("Unable to delete vehicule, the given model is null!");
        }

        try {
            // effacement du donneeFinanciereHeader
            vehicule.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .delete(vehicule.getSimpleDonneeFinanciereHeader()));

            // effacement du simpleVehicule
            vehicule.setSimpleVehicule(PegasusImplServiceLocator.getSimpleVehiculeService().delete(
                    vehicule.getSimpleVehicule()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new VehiculeException("Service not available - " + e.getMessage());
        }

        return vehicule;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. VehiculeService#read(java.lang.String)
     */
    @Override
    public Vehicule read(String idVehicule) throws JadePersistenceException, VehiculeException {
        if (JadeStringUtil.isEmpty(idVehicule)) {
            throw new VehiculeException("Unable to read vehicule, the id passed is null!");
        }
        Vehicule vehicule = new Vehicule();
        vehicule.setId(idVehicule);
        return (Vehicule) JadePersistenceManager.read(vehicule);
    }

    /**
     * Chargement d'une Vehicule via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws VehiculeException
     * @throws JadePersistenceException
     */
    @Override
    public Vehicule readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws VehiculeException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new VehiculeException("Unable to find Vehicule the idDonneeFinanciereHeader passed si null!");
        }

        VehiculeSearch search = new VehiculeSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (VehiculeSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new VehiculeException("More than one Vehiculefind, one was exepcted!");
        }

        return (Vehicule) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((VehiculeSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. VehiculeService
     * #search(ch.globaz.pegasus.business.models.fortuneparticuliere .VehiculeSearch)
     */
    @Override
    public VehiculeSearch search(VehiculeSearch vehiculeSearch) throws JadePersistenceException, VehiculeException {
        if (vehiculeSearch == null) {
            throw new VehiculeException("Unable to search vehicule, the search model passed is null!");
        }
        return (VehiculeSearch) JadePersistenceManager.search(vehiculeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. VehiculeService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule)
     */
    @Override
    public Vehicule update(Vehicule vehicule) throws JadePersistenceException, VehiculeException,
            DonneeFinanciereException {
        if (vehicule == null) {
            throw new VehiculeException("Unable to update vehicule, the given model is null!");
        }

        try {
            // mise a jour du donneeFinanciereHeader
            vehicule.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .update(vehicule.getSimpleDonneeFinanciereHeader()));

            // mise a jour du simpleVehicule
            vehicule.setSimpleVehicule(PegasusImplServiceLocator.getSimpleVehiculeService().update(
                    vehicule.getSimpleVehicule()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new VehiculeException("Service not available - " + e.getMessage());
        }

        return vehicule;

    }

}
