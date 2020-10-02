package ch.globaz.pegasus.businessimpl.services.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.AssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.services.models.assurancemaladie.PrimeAssuranceMaladieService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;


public class PrimeAssuranceMaladieServiceImpl extends PegasusAbstractServiceImpl implements PrimeAssuranceMaladieService {
    private String msgService = "Service not available - ";

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. PrimeAssuranceMaladieService
     * #count(ch.globaz.pegasus.business.models.assurancemaladie .PrimeAssuranceMaladieSearch)
     */
    @Override
    public int count(PrimeAssuranceMaladieSearch search) throws PrimeAssuranceMaladieException, JadePersistenceException {
        if (search == null) {
            throw new PrimeAssuranceMaladieException("Unable to count primeAssuranceMaladie, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. FraisGardeService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .FraisGarde)
     */
    @Override
    public PrimeAssuranceMaladie create(PrimeAssuranceMaladie primeAssuranceMaladie) throws JadePersistenceException, PrimeAssuranceMaladieException,
            DonneeFinanciereException {
        if (primeAssuranceMaladie == null) {
            throw new PrimeAssuranceMaladieException("Unable to create PrimeAssuranceMaladie, the model passed is null!");
        }

        try {
            primeAssuranceMaladie.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(primeAssuranceMaladie.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimplePrimeAssuranceMaladieService().create(primeAssuranceMaladie.getSimplePrimeAssuranceMaladie());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrimeAssuranceMaladieException(msgService + e.getMessage());
        }

        return primeAssuranceMaladie;
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. primeAssuranceMaladieService
     * #delete(ch.globaz.pegasus.business.models.assurancemaladie .primeAssuranceMaladie)
     */
    @Override
    public PrimeAssuranceMaladie delete(PrimeAssuranceMaladie primeAssuranceMaladie) throws PrimeAssuranceMaladieException, JadePersistenceException {
        if (primeAssuranceMaladie == null) {
            throw new PrimeAssuranceMaladieException("Unable to delete PrimeAssuranceMaladie, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimplePrimeAssuranceMaladieService().delete(primeAssuranceMaladie.getSimplePrimeAssuranceMaladie());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrimeAssuranceMaladieException(msgService + e.getMessage());
        }

        return primeAssuranceMaladie;
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. primeAssuranceMaladieService#read(java.lang.String)
     */
    @Override
    public PrimeAssuranceMaladie read(String idPrimeAssuranceMaladie) throws JadePersistenceException, PrimeAssuranceMaladieException {
        if (JadeStringUtil.isEmpty(idPrimeAssuranceMaladie)) {
            throw new PrimeAssuranceMaladieException("Unable to read PrimeAssuranceMaladie, the id passed is null!");
        }
        PrimeAssuranceMaladie primeAssuranceMaladie = new PrimeAssuranceMaladie();
        primeAssuranceMaladie.setId(idPrimeAssuranceMaladie);
        return (PrimeAssuranceMaladie) JadePersistenceManager.read(primeAssuranceMaladie);
    }

    /**
     * Chargement d'un PrimeAssuranceMaladie via l'id donnee financiere header
     *
     * @param idDonneeFinanciereHeader
     * @return
     * @throws PrimeAssuranceMaladieException
     * @throws JadePersistenceException
     */
    @Override
    public PrimeAssuranceMaladie readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws PrimeAssuranceMaladieException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new PrimeAssuranceMaladieException(
                    "Unable to find PrimeAssuranceMaladie the idDonneeFinanciereHeader passed si null!");
        }

        PrimeAssuranceMaladieSearch search = new PrimeAssuranceMaladieSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (PrimeAssuranceMaladieSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new PrimeAssuranceMaladieException("More than one PrimeAssuranceMaladie find, one was exepcted!");
        }

        return (PrimeAssuranceMaladie) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((PrimeAssuranceMaladieSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. primeAssuranceMaladieService
     * #search(ch.globaz.pegasus.business.models.assurancemaladie .primeAssuranceMaladieSearch)
     */
    @Override
    public PrimeAssuranceMaladieSearch search(PrimeAssuranceMaladieSearch primeAssuranceMaladieSearch) throws JadePersistenceException,
            PrimeAssuranceMaladieException {
        if (primeAssuranceMaladieSearch == null) {
            throw new PrimeAssuranceMaladieException("Unable to search PrimeAssuranceMaladie, the search model passed is null!");
        }
        return (PrimeAssuranceMaladieSearch) JadePersistenceManager.search(primeAssuranceMaladieSearch);
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. primeAssuranceMaladieService
     * #update(ch.globaz.pegasus.business.models.assurancemaladie .primeAssuranceMaladie)
     */
    @Override
    public PrimeAssuranceMaladie update(PrimeAssuranceMaladie primeAssuranceMaladie) throws AssuranceMaladieException, JadePersistenceException, PrimeAssuranceMaladieException,
            DonneeFinanciereException {
        if (primeAssuranceMaladie == null) {
            throw new PrimeAssuranceMaladieException("Unable to update PrimeAssuranceMaladie, the model passed is null!");
        }

        try {
            primeAssuranceMaladie.setSimplePrimeAssuranceMaladie(PegasusImplServiceLocator.getSimplePrimeAssuranceMaladieService().update(primeAssuranceMaladie.getSimplePrimeAssuranceMaladie()));
            primeAssuranceMaladie.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(primeAssuranceMaladie.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrimeAssuranceMaladieException(msgService + e.getMessage());
        }

        return primeAssuranceMaladie;
    }
}
