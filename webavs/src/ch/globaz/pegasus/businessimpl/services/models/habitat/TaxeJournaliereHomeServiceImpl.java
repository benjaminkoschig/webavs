package ch.globaz.pegasus.businessimpl.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.habitat.Habitat;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeSearch;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.PrixChambreSearch;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.habitat.TaxeJournaliereHomeService;
import ch.globaz.pegasus.businessimpl.checkers.habitat.TaxeJournaliereHomeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class TaxeJournaliereHomeServiceImpl extends PegasusServiceLocator implements TaxeJournaliereHomeService {

    private Map<String, PrixChambre> prixChambre = new HashMap<String, PrixChambre>();

    @Override
    public int count(TaxeJournaliereHomeSearch search) throws JadePersistenceException, TaxeJournaliereHomeException {
        if (search == null) {
            throw new TaxeJournaliereHomeException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public TaxeJournaliereHome create(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException, DonneeFinanciereException {

        TaxeJournaliereHomeChecker.checkForCreate(taxeJournaliereHome);

        if (taxeJournaliereHome == null) {
            throw new TaxeJournaliereHomeException("Unable to create taxeJournaliereHome, the model passed is null!");
        }
        try {
            // creation du donneeFinanciereHeader
            taxeJournaliereHome.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            taxeJournaliereHome.getSimpleDonneeFinanciereHeader()));

            // creation du simpleTaxeJournaliereHome
            taxeJournaliereHome.setSimpleTaxeJournaliereHome((PegasusImplServiceLocator
                    .getSimpleTaxeJournaliereHomeService().create(taxeJournaliereHome.getSimpleTaxeJournaliereHome())));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TaxeJournaliereHomeException("Service not available - " + e.getMessage());
        }
        return taxeJournaliereHome;
    }

    @Override
    public TaxeJournaliereHome delete(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException, DonneeFinanciereException {
        try {
            // effacement du donneeFinanciereHeader
            taxeJournaliereHome.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().delete(
                            taxeJournaliereHome.getSimpleDonneeFinanciereHeader()));

            // effacement du simpleTaxeJournaliereHome
            taxeJournaliereHome.setSimpleTaxeJournaliereHome(PegasusImplServiceLocator
                    .getSimpleTaxeJournaliereHomeService().delete(taxeJournaliereHome.getSimpleTaxeJournaliereHome()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TaxeJournaliereHomeException("Service not available - " + e.getMessage());
        }
        return taxeJournaliereHome;
    }

    /**
     * @return the prix
     */
    public Map<String, PrixChambre> getPrix() {
        return prixChambre;
    }

    @Override
    public PrixChambre getPrixTypeChambre(SimpleDonneeFinanciereHeader donneeFinanciereHeader, TypeChambre typeChambre)
            throws PrixChambreException, HomeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        PrixChambreSearch prixChambreSearch = new PrixChambreSearch();
        prixChambreSearch.setWhereKey("withPrixInPeriode");
        PrixChambre prixChambre = null;
        prixChambreSearch.setForIdTypeChambre(typeChambre.getId());
        prixChambreSearch.setForDateDebut(donneeFinanciereHeader.getDateDebut());
        prixChambreSearch.setForDateFin(donneeFinanciereHeader.getDateFin());
        prixChambreSearch.setForIdHome(typeChambre.getSimpleTypeChambre().getIdHome());

        PegasusServiceLocator.getHomeService().findPrixChambre(prixChambreSearch);
        if (prixChambreSearch.getSearchResults().length > 0) {
            prixChambre = (PrixChambre) prixChambreSearch.getSearchResults()[0];
        }

        return prixChambre;
    }

    @Override
    public String getPrixTypeChambreInMap(TaxeJournaliereHome taxeJournaliereHome) {
        PrixChambre prixChambre = null;
        String prix = null;

        prixChambre = this.prixChambre.get(taxeJournaliereHome.getId());
        if (prixChambre != null) {
            prix = prixChambre.getSimplePrixChambre().getPrixJournalier();
        }

        return prix;
    }

    @Override
    public void putPrixTypeChambreInMap(Habitat habitat) throws PrixChambreException, HomeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        TaxeJournaliereHome taxeJournaliereHome = (TaxeJournaliereHome) habitat.getDonneeFinanciere();
        TypeChambre tyChambre = taxeJournaliereHome.getTypeChambre();
        prixChambre.put(taxeJournaliereHome.getId(),
                getPrixTypeChambre(habitat.getSimpleDonneeFinanciereHeader(), tyChambre));
    }

    @Override
    public TaxeJournaliereHome read(String idTaxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException {
        if (idTaxeJournaliereHome == null) {
            throw new TaxeJournaliereHomeException("Unable to read idTaxeJournaliereHome, the model passed is null!");
        }
        TaxeJournaliereHome taxeJournaliereHome = new TaxeJournaliereHome();
        taxeJournaliereHome.setId(idTaxeJournaliereHome);
        return (TaxeJournaliereHome) JadePersistenceManager.read(taxeJournaliereHome);
    }

    /**
     * Chargement d'une TaxeJournaliereHome via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws TaxeJournaliereHomeException
     * @throws JadePersistenceException
     */
    @Override
    public TaxeJournaliereHome readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws TaxeJournaliereHomeException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to find TaxeJournaliereHome the idDonneeFinanciereHeader passed si null!");
        }

        TaxeJournaliereHomeSearch search = new TaxeJournaliereHomeSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (TaxeJournaliereHomeSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new TaxeJournaliereHomeException("More than one TaxeJournaliereHome find, one was exepcted!");
        }

        return (TaxeJournaliereHome) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((TaxeJournaliereHomeSearch) donneeFinanciereSearch);
    }

    @Override
    public TaxeJournaliereHomeSearch search(TaxeJournaliereHomeSearch taxeJournaliereHomeSearch)
            throws JadePersistenceException, TaxeJournaliereHomeException {
        if (taxeJournaliereHomeSearch == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to search taxeJournaliereHomeSearch, the model passed is null!");
        }
        return (TaxeJournaliereHomeSearch) JadePersistenceManager.search(taxeJournaliereHomeSearch);
    }

    @Override
    public TaxeJournaliereHome update(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException, DonneeFinanciereException {
        TaxeJournaliereHomeChecker.checkForCreate(taxeJournaliereHome);

        if (taxeJournaliereHome == null) {
            throw new TaxeJournaliereHomeException("Unable to update taxeJournaliereHome, the model passed is null!");
        }

        try {
            // mise a jour du donneeFinanciereHeader
            taxeJournaliereHome.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            taxeJournaliereHome.getSimpleDonneeFinanciereHeader()));

            // mise a jour du simpleTaxeJournaliereHome
            taxeJournaliereHome.setSimpleTaxeJournaliereHome((PegasusImplServiceLocator
                    .getSimpleTaxeJournaliereHomeService().update(taxeJournaliereHome.getSimpleTaxeJournaliereHome())));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TaxeJournaliereHomeException("Service not available - " + e.getMessage());
        }
        return taxeJournaliereHome;
    }

}
