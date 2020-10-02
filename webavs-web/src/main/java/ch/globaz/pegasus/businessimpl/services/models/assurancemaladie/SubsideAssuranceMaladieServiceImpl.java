package ch.globaz.pegasus.businessimpl.services.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.AssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.SubsideAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.services.models.assurancemaladie.PrimeAssuranceMaladieService;
import ch.globaz.pegasus.business.services.models.assurancemaladie.SubsideAssuranceMaladieService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;


public class SubsideAssuranceMaladieServiceImpl extends PegasusAbstractServiceImpl implements SubsideAssuranceMaladieService {
    private String msgService = "Service not available - ";

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. SubsideAssuranceMaladieService
     * #count(ch.globaz.pegasus.business.models.assurancemaladie .SubsideAssuranceMaladieSearch)
     */
    @Override
    public int count(SubsideAssuranceMaladieSearch search) throws SubsideAssuranceMaladieException, JadePersistenceException {
        if (search == null) {
            throw new SubsideAssuranceMaladieException("Unable to count SubsideAssuranceMaladie, the search model passed is null!");
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
    public SubsideAssuranceMaladie create(SubsideAssuranceMaladie subsideAssuranceMaladie) throws JadePersistenceException, SubsideAssuranceMaladieException,
            DonneeFinanciereException {
        if (subsideAssuranceMaladie == null) {
            throw new SubsideAssuranceMaladieException("Unable to create SubsideAssuranceMaladie, the model passed is null!");
        }

        try {
            subsideAssuranceMaladie.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleSubsideAssuranceMaladieService().create(subsideAssuranceMaladie.getSimpleSubsideAssuranceMaladie());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SubsideAssuranceMaladieException(msgService + e.getMessage());
        }

        return subsideAssuranceMaladie;
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. subsideAssuranceMaladieService
     * #delete(ch.globaz.pegasus.business.models.assurancemaladie .subsideAssuranceMaladie)
     */
    @Override
    public SubsideAssuranceMaladie delete(SubsideAssuranceMaladie subsideAssuranceMaladie) throws SubsideAssuranceMaladieException, JadePersistenceException {
        if (subsideAssuranceMaladie == null) {
            throw new SubsideAssuranceMaladieException("Unable to delete PrimeAssuranceMaladie, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleSubsideAssuranceMaladieService().delete(subsideAssuranceMaladie.getSimpleSubsideAssuranceMaladie());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SubsideAssuranceMaladieException(msgService + e.getMessage());
        }

        return subsideAssuranceMaladie;
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. subsideAssuranceMaladieService#read(java.lang.String)
     */
    @Override
    public SubsideAssuranceMaladie read(String idSubsideAssuranceMaladie) throws JadePersistenceException, SubsideAssuranceMaladieException {
        if (JadeStringUtil.isEmpty(idSubsideAssuranceMaladie)) {
            throw new SubsideAssuranceMaladieException("Unable to read SubsideAssuranceMaladie, the id passed is null!");
        }
        SubsideAssuranceMaladie subsideAssuranceMaladie = new SubsideAssuranceMaladie();
        subsideAssuranceMaladie.setId(idSubsideAssuranceMaladie);
        return (SubsideAssuranceMaladie) JadePersistenceManager.read(subsideAssuranceMaladie);
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
    public SubsideAssuranceMaladie readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws SubsideAssuranceMaladieException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new SubsideAssuranceMaladieException(
                    "Unable to find SubsideAssuranceMaladie the idDonneeFinanciereHeader passed si null!");
        }

        SubsideAssuranceMaladieSearch search = new SubsideAssuranceMaladieSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (SubsideAssuranceMaladieSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new SubsideAssuranceMaladieException("More than one SubsideAssuranceMaladie find, one was exepcted!");
        }

        return (SubsideAssuranceMaladie) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((SubsideAssuranceMaladieSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. SubsideAssuranceMaladieService
     * #search(ch.globaz.pegasus.business.models.assurancemaladie .SubsideAssuranceMaladieSearch)
     */
    @Override
    public SubsideAssuranceMaladieSearch search(SubsideAssuranceMaladieSearch subsideAssuranceMaladieSearch) throws JadePersistenceException,
            SubsideAssuranceMaladieException {
        if (subsideAssuranceMaladieSearch == null) {
            throw new SubsideAssuranceMaladieException("Unable to search SubsideAssuranceMaladie, the search model passed is null!");
        }
        return (SubsideAssuranceMaladieSearch) JadePersistenceManager.search(subsideAssuranceMaladieSearch);
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. subsideAssuranceMaladieService
     * #update(ch.globaz.pegasus.business.models.assurancemaladie .subsideAssuranceMaladie)
     */
    @Override
    public SubsideAssuranceMaladie update(SubsideAssuranceMaladie subsideAssuranceMaladie) throws AssuranceMaladieException, JadePersistenceException, SubsideAssuranceMaladieException,
            DonneeFinanciereException {
        if (subsideAssuranceMaladie == null) {
            throw new SubsideAssuranceMaladieException("Unable to update SubsideAssuranceMaladie, the model passed is null!");
        }

        try {
            subsideAssuranceMaladie.setSimpleSubsideAssuranceMaladie(PegasusImplServiceLocator.getSimpleSubsideAssuranceMaladieService().update(subsideAssuranceMaladie.getSimpleSubsideAssuranceMaladie()));
            subsideAssuranceMaladie.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SubsideAssuranceMaladieException(msgService + e.getMessage());
        }

        return subsideAssuranceMaladie;
    }
}
