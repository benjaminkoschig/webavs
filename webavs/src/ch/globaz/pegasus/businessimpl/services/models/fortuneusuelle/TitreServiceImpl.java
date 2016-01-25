package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.Titre;
import ch.globaz.pegasus.business.models.fortuneusuelle.TitreSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.TitreService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class TitreServiceImpl extends PegasusAbstractServiceImpl implements TitreService {

    private static void check(Titre titre) {
        if (titre.getSimpleTitre().getCsGenreTitre().equals("64021002")
                && JadeStringUtil.isEmpty(titre.getSimpleDonneeFinanciereHeader().getDateFin())) {
            JadeThread.logError(titre.getClass().getName(), "pegasus.titre.dateFin.mandatory");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TitreService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .TitreSearch)
     */
    @Override
    public int count(TitreSearch search) throws TitreException, JadePersistenceException {
        if (search == null) {
            throw new TitreException("Unable to count Titre, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TitreService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .Titre)
     */
    @Override
    public Titre create(Titre titre) throws JadePersistenceException, TitreException, DonneeFinanciereException {
        if (titre == null) {
            throw new TitreException("Unable to create Titre, the model passed is null!");
        }

        try {
            // vérifie que si "genre titres" vaut "obligations" le champ
            // "date fin" est obligatoire
            TitreServiceImpl.check(titre);

            titre.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .create(titre.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleTitreService().create(titre.getSimpleTitre());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TitreException("Service not available - " + e.getMessage());
        }

        return titre;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TitreService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .Titre)
     */
    @Override
    public Titre delete(Titre titre) throws TitreException, JadePersistenceException {
        if (titre == null) {
            throw new TitreException("Unable to delete Titre, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleTitreService().delete(titre.getSimpleTitre());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TitreException("Service not available - " + e.getMessage());
        }

        return titre;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TitreService#read(java.lang.String)
     */
    @Override
    public Titre read(String idTitre) throws JadePersistenceException, TitreException {
        if (JadeStringUtil.isEmpty(idTitre)) {
            throw new TitreException("Unable to read Titre, the id passed is null!");
        }
        Titre Titre = new Titre();
        Titre.setId(idTitre);
        return (Titre) JadePersistenceManager.read(Titre);
    }

    /**
     * Chargement d'un Titre via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws TitreException
     * @throws JadePersistenceException
     */
    @Override
    public Titre readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws TitreException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new TitreException("Unable to find Titre the idDonneeFinanciereHeader passed si null!");
        }

        TitreSearch search = new TitreSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (TitreSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new TitreException("More than one Titre find, one was exepcted!");
        }

        return (Titre) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((TitreSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TitreService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .TitreSearch)
     */
    @Override
    public TitreSearch search(TitreSearch titreSearch) throws JadePersistenceException, TitreException {
        if (titreSearch == null) {
            throw new TitreException("Unable to search Titre, the search model passed is null!");
        }
        return (TitreSearch) JadePersistenceManager.search(titreSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TitreService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .Titre)
     */
    @Override
    public Titre update(Titre titre) throws JadePersistenceException, TitreException, DonneeFinanciereException {
        if (titre == null) {
            throw new TitreException("Unable to update Titre, the model passed is null!");
        }

        try {
            titre.setSimpleTitre(PegasusImplServiceLocator.getSimpleTitreService().update(titre.getSimpleTitre()));
            titre.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .update(titre.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TitreException("Service not available - " + e.getMessage());
        }

        return titre;
    }

}
