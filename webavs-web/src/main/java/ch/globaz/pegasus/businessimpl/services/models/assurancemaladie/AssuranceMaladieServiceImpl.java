package ch.globaz.pegasus.businessimpl.services.models.assurancemaladie;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.AssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.SubsideAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.assurancemaladie.*;
import ch.globaz.pegasus.business.models.droit.*;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.assurancemaladie.AssuranceMaladieService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.droit.DonneeFinanciereUtils;
import globaz.globall.util.JAException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;

import java.util.ArrayList;
import java.util.List;


public class AssuranceMaladieServiceImpl implements AssuranceMaladieService {


    @Override
    public void copyAssuranceMaladie(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException {
        try {
            List<String> lisCsType = new ArrayList<String>();

            lisCsType.add(IPCDroits.CS_AUTRES_REVENUS);
            lisCsType.add(IPCDroits.CS_CONTRAT_ENTRETIEN_VIAGER);
            lisCsType.add(IPCDroits.CS_REVENU_HYPOTHETIQUE);
            lisCsType.add(IPCDroits.CS_ALLOCATIONS_FAMILIALLES);
            lisCsType.add(IPCDroits.CS_COTISATIONS_PSAL);
            lisCsType.add(IPCDroits.CS_PENSIONS_ALIMENTAIRES);
            lisCsType.add(IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE);
            lisCsType.add(IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE);

            AssuranceMaladieSearch searchModel = new AssuranceMaladieSearch();
            searchModel.setWhereKey(AssuranceMaladieSearch.FOR_ALL_VALABLE_LE);
            searchModel.setInCsTypeDonneeFinancierer(lisCsType);
            searchModel.setForIdDroit(oldDroit.getSimpleDroit().getIdDroit());
            try {
                searchModel.setForDateValable(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(newDroit.getDemande()
                        .getSimpleDemande().getDateDepot()));
            } catch (JAException e) {
                throw new DonneeFinanciereException("Unable to convert Date dépôt to MM.AAAA format.", e);
            }

            searchModel = PegasusServiceLocator.getDroitService().searchAssuranceMaladie(searchModel);

            AssuranceMaladie assuranceMaladie = null;
            String csType = null;
            DroitMembreFamille droitMembreFamille = null;
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                try {

                    assuranceMaladie = (AssuranceMaladie) JadePersistenceUtil.clone(model);

                    droitMembreFamille = DonneeFinanciereUtils.findTheDroitMembreFamilleInTheSarchModle(
                            droitMembreFamilleSearch, assuranceMaladie.getMembreFamilleEtendu().getDroitMembreFamille()
                                    .getSimpleDroitMembreFamille().getIdMembreFamilleSF());

                    // On ne copie pas la donné si le membre de famille n'a pas été trouvée
                    if (droitMembreFamille != null) {
                        csType = assuranceMaladie.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere();

                        assuranceMaladie.setSimpleDonneeFinanciereHeader(DonneeFinanciereUtils
                                .copySimpleDonneFianciere(assuranceMaladie.getSimpleDonneeFinanciereHeader()));

                        // TODO Executer la copie
                    }
                } catch (JadeCloneModelException e) {
                    throw new DonneeFinanciereException("Unable to clone (assuranceMaladie) for the copy ", e);
                }
            }
        } catch (DroitException e) {
            throw new DonneeFinanciereException("Unable to search the assuranceMaladie for the copy", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        } catch (JadePersistenceException e) {
            throw new DonneeFinanciereException(
                    "Unable to copy the donneeFinanciere (assuranceMaladie) probleme with the persistence" + e);
        }
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException{

        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_PRIME_ASSURANCE_MALADIE + "-")) {
            PegasusImplServiceLocator.getSimplePrimeAssuranceMaladieService().deleteParListeIdDoFinH(idsDonneFinanciere);

        }
    }
    //REFORME PC
    @Override
    public PrimeAssuranceMaladie createPrimeAssuranceMaladie(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille, PrimeAssuranceMaladie primeAssuranceMaladie)
            throws PrimeAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DonneeFinanciereException(
                    "Unable to create primeAssuranceMaladie, the droitMembreFamille is null or new");
        }
        if (primeAssuranceMaladie == null) {
            throw new DonneeFinanciereException("Unable to create primeAssuranceMaladie, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new DonneeFinanciereException("Unable to create primeAssuranceMaladie, the simpleVersionDroit is null");
        }

        primeAssuranceMaladie.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_PRIME_ASSURANCE_MALADIE);

        try {
            primeAssuranceMaladie.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, primeAssuranceMaladie.getSimpleDonneeFinanciereHeader()));

            primeAssuranceMaladie = PegasusImplServiceLocator.getPrimeAssuranceMaladieService().create(primeAssuranceMaladie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        }

        return primeAssuranceMaladie;
    }

    @Override
    public PrimeAssuranceMaladie createPrimeAssuranceMaladie(PrimeAssuranceMaladie primeAssuranceMaladie)
            throws PrimeAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException {

        if (primeAssuranceMaladie == null) {
            throw new DonneeFinanciereException("Unable to create primeAssuranceMaladie, the model is null");
        }

        primeAssuranceMaladie.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_PRIME_ASSURANCE_MALADIE);

        try {
            primeAssuranceMaladie = PegasusImplServiceLocator.getPrimeAssuranceMaladieService().create(primeAssuranceMaladie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        }

        return primeAssuranceMaladie;
    }

    //REFORME PC
    private PrimeAssuranceMaladie copyPrimeAssuranceMaladie(AssuranceMaladie assuranceMaladie, Droit newDroit,
                                      DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PrimeAssuranceMaladie primeAssuranceMaladie = new PrimeAssuranceMaladie();
        try {
            primeAssuranceMaladie.setSimpleDonneeFinanciereHeader(assuranceMaladie.getSimpleDonneeFinanciereHeader());
            primeAssuranceMaladie.setSimplePrimeAssuranceMaladie(assuranceMaladie.getSimplePrimeAssuranceMaladie());
            primeAssuranceMaladie = createPrimeAssuranceMaladie(newDroit.getSimpleVersionDroit(), droitMembreFamille, primeAssuranceMaladie);
            return primeAssuranceMaladie;
        } catch (PrimeAssuranceMaladieException e) {
            throw new DonneeFinanciereException("Unable to copy the primeAssuranceMaladie", e);
        }
    }

    @Override
    public PrimeAssuranceMaladie readPrimeAssuranceMaladie(String id) throws JadePersistenceException, AssuranceMaladieException {
        PrimeAssuranceMaladie assuranceMaladie = new PrimeAssuranceMaladie();
        assuranceMaladie.setId(id);

        return (PrimeAssuranceMaladie) JadePersistenceManager.read(assuranceMaladie);
    }

    @Override
    public PrimeAssuranceMaladieSearch searchPrimeAssuranceMaladie(PrimeAssuranceMaladieSearch searchModel) throws JadePersistenceException,
            AssuranceMaladieException {
        if (searchModel == null) {
            throw new AssuranceMaladieException("Unable to search PrimeAssuranceMaladie, the search model passed is null!");
        }
        return (PrimeAssuranceMaladieSearch) JadePersistenceManager.search(searchModel);
    }


    @Override
    public SubsideAssuranceMaladie createSubsideAssuranceMaladie(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille, SubsideAssuranceMaladie subsideAssuranceMaladie)
            throws SubsideAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DonneeFinanciereException(
                    "Unable to create SubsideAssuranceMaladie, the droitMembreFamille is null or new");
        }
        if (subsideAssuranceMaladie == null) {
            throw new DonneeFinanciereException("Unable to create SubsideAssuranceMaladie, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new DonneeFinanciereException("Unable to create SubsideAssuranceMaladie, the simpleVersionDroit is null");
        }

        subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_SUBSIDE_ASSURANCE_MALADIE);

        try {
            subsideAssuranceMaladie.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader()));

            subsideAssuranceMaladie = PegasusImplServiceLocator.getSubsideAssuranceMaladieService().create(subsideAssuranceMaladie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        }

        return subsideAssuranceMaladie;
    }

    @Override
    public SubsideAssuranceMaladie createSubsideAssuranceMaladie(SubsideAssuranceMaladie subsideAssuranceMaladie)
            throws SubsideAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException {

        if (subsideAssuranceMaladie == null) {
            throw new DonneeFinanciereException("Unable to create SubsideAssuranceMaladie, the model is null");
        }

        subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_SUBSIDE_ASSURANCE_MALADIE);

        try {
            subsideAssuranceMaladie = PegasusImplServiceLocator.getSubsideAssuranceMaladieService().create(subsideAssuranceMaladie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        }

        return subsideAssuranceMaladie;
    }

    //REFORME PC
    private SubsideAssuranceMaladie copySubsideAssuranceMaladie(AssuranceMaladie assuranceMaladie, Droit newDroit,
                                                                DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SubsideAssuranceMaladie subsideAssuranceMaladie = new SubsideAssuranceMaladie();
        try {
            subsideAssuranceMaladie.setSimpleDonneeFinanciereHeader(assuranceMaladie.getSimpleDonneeFinanciereHeader());
            subsideAssuranceMaladie.setSimpleSubsideAssuranceMaladie(assuranceMaladie.getSimpleSubsideAssuranceMaladie());
            subsideAssuranceMaladie = createSubsideAssuranceMaladie(newDroit.getSimpleVersionDroit(), droitMembreFamille, subsideAssuranceMaladie);
            return subsideAssuranceMaladie;
        } catch (SubsideAssuranceMaladieException e) {
            throw new DonneeFinanciereException("Unable to copy the subsideAssuranceMaladie", e);
        }
    }

    @Override
    public SubsideAssuranceMaladie readSubsideAssuranceMaladie(String id) throws JadePersistenceException, AssuranceMaladieException {
        SubsideAssuranceMaladie assuranceMaladie = new SubsideAssuranceMaladie();
        assuranceMaladie.setId(id);

        return (SubsideAssuranceMaladie) JadePersistenceManager.read(assuranceMaladie);
    }

    @Override
    public SubsideAssuranceMaladieSearch searchSubsideAssuranceMaladie(SubsideAssuranceMaladieSearch searchModel) throws JadePersistenceException,
            AssuranceMaladieException {
        if (searchModel == null) {
            throw new AssuranceMaladieException("Unable to search SubsideAssuranceMaladie, the search model passed is null!");
        }
        return (SubsideAssuranceMaladieSearch) JadePersistenceManager.search(searchModel);
    }
}
