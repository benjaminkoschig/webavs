package ch.globaz.pegasus.businessimpl.services.models.habitat;

import globaz.globall.util.JAException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCLoyer;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.habitat.Habitat;
import ch.globaz.pegasus.business.models.habitat.HabitatSearch;
import ch.globaz.pegasus.business.models.habitat.Loyer;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.habitat.HabitatService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.droit.DonneeFinanciereUtils;

public class HabitatServiceImpl extends PegasusAbstractServiceImpl implements HabitatService {

    @Override
    public void copyHabitat(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException {
        try {
            List<String> lisCsType = new ArrayList<String>();

            lisCsType.add(IPCLoyer.CS_TYPE_DONNEE_FINANCIERE);
            lisCsType.add(IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE);

            HabitatSearch searchModel = new HabitatSearch();
            searchModel.setWhereKey(HabitatSearch.FOR_ALL_VALABLE_LE);
            searchModel.setInCsTypeDonneeFinancierer(lisCsType);
            searchModel.setForIdDroit(oldDroit.getSimpleDroit().getIdDroit());
            try {
                searchModel.setForDateValable(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(newDroit.getDemande()
                        .getSimpleDemande().getDateDepot()));
            } catch (JAException e) {
                throw new DonneeFinanciereException("Unable to convert Date dépôt to MM.AAAA format.", e);
            }
            searchModel = PegasusServiceLocator.getDroitService().searchHabitat(searchModel);

            // RenteIjApi renteIjApiOld = null;
            Habitat habitat = null;
            String csType = null;
            DroitMembreFamille droitMembreFamille = null;
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                try {

                    habitat = (Habitat) JadePersistenceUtil.clone(model);

                    droitMembreFamille = DonneeFinanciereUtils.findTheDroitMembreFamilleInTheSarchModle(
                            droitMembreFamilleSearch, habitat.getMembreFamilleEtendu().getDroitMembreFamille()
                                    .getSimpleDroitMembreFamille().getIdMembreFamilleSF());

                    // On ne copie pas la donné si le membre de famille n'a pas été trouvée
                    if (droitMembreFamille != null) {
                        csType = habitat.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere();

                        habitat.setSimpleDonneeFinanciereHeader(DonneeFinanciereUtils.copySimpleDonneFianciere(habitat
                                .getSimpleDonneeFinanciereHeader()));

                        if ((habitat.getSimpleLoyer() != null) && IPCLoyer.CS_TYPE_DONNEE_FINANCIERE.equals(csType)) {
                            copyLoyer(habitat, newDroit, droitMembreFamille);
                        }
                        if ((habitat.getTaxeJournaliereHome() != null)
                                && IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE.equals(csType)) {
                            copyTaxeJournaliereHome(habitat, newDroit, droitMembreFamille);
                        }
                    }
                } catch (JadeCloneModelException e) {
                    throw new DonneeFinanciereException("Unable to clone (habitat) for the copy ", e);
                }
            }
        } catch (DroitException e) {
            throw new DonneeFinanciereException("Unable to search the renteIjApi for the copy", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        } catch (JadePersistenceException e) {
            throw new DonneeFinanciereException(
                    "Unable to copy the donneeFinanciere (habitat) probleme with the persistence" + e);
        }
    }

    private Loyer copyLoyer(Habitat habitat, Droit newDroit, DroitMembreFamille droitMembreFamille)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Loyer loyer = new Loyer();
        try {
            loyer.setSimpleDonneeFinanciereHeader(habitat.getSimpleDonneeFinanciereHeader());
            loyer.setSimpleLoyer(habitat.getSimpleLoyer());
            loyer = createLoyer(newDroit.getSimpleVersionDroit(), droitMembreFamille, loyer);
            return loyer;
        } catch (LoyerException e) {
            throw new DonneeFinanciereException("Unable to copy the betail", e);
        }
    }

    private TaxeJournaliereHome copyTaxeJournaliereHome(Habitat habitat, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        TaxeJournaliereHome taxeJournaliereHome = new TaxeJournaliereHome();
        try {
            taxeJournaliereHome.setSimpleDonneeFinanciereHeader(habitat.getSimpleDonneeFinanciereHeader());
            taxeJournaliereHome.setSimpleTaxeJournaliereHome(habitat.getTaxeJournaliereHome()
                    .getSimpleTaxeJournaliereHome());
            taxeJournaliereHome = createTaxeJournaliereHome(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    taxeJournaliereHome);
            return taxeJournaliereHome;
        } catch (TaxeJournaliereHomeException e) {
            throw new DonneeFinanciereException("Unable to copy the betail", e);
        }
    }

    @Override
    public Loyer createLoyer(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille, Loyer loyer)
            throws LoyerException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new LoyerException("Unable to create loyer, the droitMembreFamille is null or new");
        }
        if (loyer == null) {
            throw new LoyerException("Unable to create loyer, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new LoyerException("Unable to create loyer, the simpleVersionDroit is null");
        }

        loyer.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCLoyer.CS_TYPE_DONNEE_FINANCIERE);

        try {
            loyer.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            loyer.getSimpleDonneeFinanciereHeader()));
            loyer = PegasusImplServiceLocator.getLoyerService().create(loyer);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LoyerException("Service not available - " + e.getMessage(), e);
        }
        return loyer;
    }

    @Override
    public TaxeJournaliereHome createTaxeJournaliereHome(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, TaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new TaxeJournaliereHomeException(
                    "Unable to create taxeJournaliereHome, the droitMembreFamille is null or new");
        }
        if (taxeJournaliereHome == null) {
            throw new TaxeJournaliereHomeException("Unable to create taxeJournaliereHome, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to create taxeJournaliereHome, the simpleVersionDroit is null ");
        }

        taxeJournaliereHome.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE);
        try {
            taxeJournaliereHome.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, taxeJournaliereHome.getSimpleDonneeFinanciereHeader()));

            taxeJournaliereHome = PegasusImplServiceLocator.getTaxeJournaliereHomeService().create(taxeJournaliereHome);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TaxeJournaliereHomeException("Service not available - " + e.getMessage(), e);
        }
        return taxeJournaliereHome;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat.HabitatService# deleteParListeIdDoFinH(java.util.List,
     * java.lang.String)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (typeDonneFinianciere.contains("-" + IPCLoyer.CS_TYPE_DONNEE_FINANCIERE + "-")) {
            PegasusImplServiceLocator.getSimpleLoyerService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE + "-")) {
            PegasusImplServiceLocator.getSimpleTaxeJournaliereHomeService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
    }

}
