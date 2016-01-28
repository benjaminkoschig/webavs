package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.NumeraireException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire;
import ch.globaz.pegasus.business.models.fortuneparticuliere.NumeraireSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.NumeraireService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class NumeraireServiceImpl extends PegasusAbstractServiceImpl implements NumeraireService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. NumeraireService
     * #count(ch.globaz.pegasus.business.models.fortuneparticuliere .NumeraireSearch)
     */
    @Override
    public int count(NumeraireSearch search) throws NumeraireException, JadePersistenceException {
        if (search == null) {
            throw new NumeraireException("Unable to count numeraire, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. NumeraireService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire)
     */
    @Override
    public Numeraire create(Numeraire numeraire) throws JadePersistenceException, NumeraireException,
            DonneeFinanciereException {
        if (numeraire == null) {
            throw new NumeraireException("Unable to create numeraire, the model passed is null!");
        }

        try {
            numeraire.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(numeraire.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleNumeraireService().create(numeraire.getSimpleNumeraire());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new NumeraireException("Service not available - " + e.getMessage());
        }

        return numeraire;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. NumeraireService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire)
     */
    @Override
    public Numeraire delete(Numeraire numeraire) throws NumeraireException, JadePersistenceException {
        if (numeraire == null) {
            throw new NumeraireException("Unable to delete numeraire, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleNumeraireService().delete(numeraire.getSimpleNumeraire());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new NumeraireException("Service not available - " + e.getMessage());
        }

        return numeraire;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. NumeraireService#read(java.lang.String)
     */
    @Override
    public Numeraire read(String idNumeraire) throws JadePersistenceException, NumeraireException {
        if (JadeStringUtil.isEmpty(idNumeraire)) {
            throw new NumeraireException("Unable to read numeraire, the id passed is null!");
        }
        Numeraire numeraire = new Numeraire();
        numeraire.setId(idNumeraire);
        return (Numeraire) JadePersistenceManager.read(numeraire);
    }

    /**
     * Chargement d'un Numeraire via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws NumeraireException
     * @throws JadePersistenceException
     */
    @Override
    public Numeraire readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws NumeraireException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new NumeraireException("Unable to find Numeraire the idDonneeFinanciereHeader passed si null!");
        }

        NumeraireSearch search = new NumeraireSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (NumeraireSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new NumeraireException("More than one Numeraire find, one was exepcted!");
        }

        return (Numeraire) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((NumeraireSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. NumeraireService
     * #search(ch.globaz.pegasus.business.models.fortuneparticuliere .NumeraireSearch)
     */
    @Override
    public NumeraireSearch search(NumeraireSearch numeraireSearch) throws JadePersistenceException, NumeraireException {
        if (numeraireSearch == null) {
            throw new NumeraireException("Unable to search numeraire, the search model passed is null!");
        }
        return (NumeraireSearch) JadePersistenceManager.search(numeraireSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. NumeraireService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire)
     */
    @Override
    public Numeraire update(Numeraire numeraire) throws JadePersistenceException, NumeraireException,
            DonneeFinanciereException {
        if (numeraire == null) {
            throw new NumeraireException("Unable to update numeraire, the model passed is null!");
        }

        try {
            numeraire.setSimpleNumeraire(PegasusImplServiceLocator.getSimpleNumeraireService().update(
                    numeraire.getSimpleNumeraire()));
            numeraire.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(numeraire.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new NumeraireException("Service not available - " + e.getMessage());
        }

        return numeraire;
    }

}
