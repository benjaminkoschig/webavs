package ch.globaz.pegasus.businessimpl.services.models.dessaisissement;

import globaz.globall.util.JAException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneSearch;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.droit.DonneeFinanciereUtils;

public class DessaisissementServiceImpl extends PegasusAbstractServiceImpl implements DessaisissementService {

    @Override
    public void copyDessaisissement(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException {
        try {

            DessaisissementFortuneSearch searchModel = new DessaisissementFortuneSearch();
            searchModel.setWhereKey(DessaisissementFortuneSearch.FOR_DATE_VALABLE_LE);

            searchModel.setForIdDroit(oldDroit.getSimpleDroit().getIdDroit());
            try {
                searchModel.setForDateValable(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(newDroit.getDemande()
                        .getSimpleDemande().getDateDepot()));
            } catch (JAException e) {
                throw new DonneeFinanciereException("Unable to convert Date dépôt to MM.AAAA format.", e);
            }

            searchModel = PegasusServiceLocator.getDroitService().searchDessaisissementFortune(searchModel);

            DessaisissementFortune dessaisissementFortune = null;
            String csType = null;
            DroitMembreFamille droitMembreFamille = null;
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                try {

                    dessaisissementFortune = (DessaisissementFortune) JadePersistenceUtil.clone(model);

                    droitMembreFamille = DonneeFinanciereUtils.findTheDroitMembreFamilleInTheSarchModle(
                            droitMembreFamilleSearch, dessaisissementFortune.getMembreFamilleEtendu()
                                    .getDroitMembreFamille().getSimpleDroitMembreFamille().getIdMembreFamilleSF());
                    // On ne copie pas la donné si le membre de famille n'a pas été trouvée
                    if (droitMembreFamille != null) {
                        dessaisissementFortune.setSimpleDonneeFinanciereHeader(DonneeFinanciereUtils
                                .copySimpleDonneFianciere(dessaisissementFortune.getSimpleDonneeFinanciereHeader()));

                        copyDessaisissementFortune(dessaisissementFortune, newDroit, droitMembreFamille);
                    }
                } catch (JadeCloneModelException e) {
                    throw new DonneeFinanciereException("Unable to clone (habitat) for the copy ", e);
                }
            }

            DessaisissementRevenuSearch search = new DessaisissementRevenuSearch();
            search.setWhereKey(DessaisissementRevenuSearch.FOR_DATE_VALABLE_LE);

            search.setForIdDroit(oldDroit.getSimpleDroit().getIdDroit());

            try {
                searchModel.setForDateValable(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(newDroit.getDemande()
                        .getSimpleDemande().getDateDepot()));
            } catch (JAException e) {
                throw new DonneeFinanciereException("Unable to convert Date dépôt to MM.AAAA format.", e);
            }
            search = PegasusServiceLocator.getDroitService().searchDessaisissementRevenu(search);

            DessaisissementRevenu dessaisissementRevenu = null;

            for (JadeAbstractModel model : search.getSearchResults()) {
                try {

                    dessaisissementRevenu = (DessaisissementRevenu) JadePersistenceUtil.clone(model);

                    droitMembreFamille = DonneeFinanciereUtils.findTheDroitMembreFamilleInTheSarchModle(
                            droitMembreFamilleSearch, dessaisissementRevenu.getMembreFamilleEtendu()
                                    .getDroitMembreFamille().getSimpleDroitMembreFamille().getIdMembreFamilleSF());
                    // On ne copie pas la donné si le membre de famille n'a pas été trouvée
                    if (droitMembreFamille != null) {
                        dessaisissementRevenu.setSimpleDonneeFinanciereHeader(DonneeFinanciereUtils
                                .copySimpleDonneFianciere(dessaisissementRevenu.getSimpleDonneeFinanciereHeader()));
                        copyDessaisissementRevenu(dessaisissementRevenu, newDroit, droitMembreFamille);
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

    private DessaisissementFortune copyDessaisissementFortune(DessaisissementFortune dessaisissementFortune,
            Droit newDroit, DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DessaisissementFortune df = new DessaisissementFortune();
        try {
            df.setSimpleDonneeFinanciereHeader(dessaisissementFortune.getSimpleDonneeFinanciereHeader());
            df.setSimpleDessaisissementFortune(dessaisissementFortune.getSimpleDessaisissementFortune());
            df = createDessaisissementFortune(newDroit.getSimpleVersionDroit(), droitMembreFamille, df);
            return df;
        } catch (DessaisissementFortuneException e) {
            throw new DonneeFinanciereException("Unable to copy the betail", e);
        }
    }

    private DessaisissementRevenu copyDessaisissementRevenu(DessaisissementRevenu dessaisissementRevenu,
            Droit newDroit, DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DessaisissementRevenu dr = new DessaisissementRevenu();
        try {
            dr.setSimpleDonneeFinanciereHeader(dessaisissementRevenu.getSimpleDonneeFinanciereHeader());
            dr.setSimpleDessaisissementRevenu(dessaisissementRevenu.getSimpleDessaisissementRevenu());
            dr = createDessaisissementRevenu(newDroit.getSimpleVersionDroit(), droitMembreFamille, dr);
            return dr;
        } catch (DessaisissementRevenuException e) {
            throw new DonneeFinanciereException("Unable to copy the betail", e);
        }
    }

    @Override
    public DessaisissementFortune createDessaisissementFortune(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, DessaisissementFortune dessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DessaisissementFortuneException(
                    "Unable to create dessaisissement fortune, the droitMembreFamille is null or new");
        }
        if (dessaisissementFortune == null) {
            throw new DessaisissementFortuneException("Unable to create dessaisissement fortune, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new DessaisissementFortuneException(
                    "Unable to create dessaisissement fortune, the simpleVersionDroit is null");
        }

        dessaisissementFortune.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_DESSAISISSEMENT_FORTUNE);

        try {
            dessaisissementFortune.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, dessaisissementFortune.getSimpleDonneeFinanciereHeader()));

            dessaisissementFortune = PegasusImplServiceLocator.getDessaisissementFortuneService().create(
                    dessaisissementFortune);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DessaisissementFortuneException("Service not available - " + e.getMessage());
        }
        return dessaisissementFortune;
    }

    @Override
    public DessaisissementRevenu createDessaisissementRevenu(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, DessaisissementRevenu dessaisissementRevenu)
            throws JadePersistenceException, DessaisissementRevenuException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DessaisissementRevenuException(
                    "Unable to create dessaisissement revenu, the droitMembreFamille is null or new");
        }
        if (dessaisissementRevenu == null) {
            throw new DessaisissementRevenuException("Unable to create dessaisissement revenu, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new DessaisissementRevenuException(
                    "Unable to create dessaisissement revenu, the simpleVersionDroit is null");
        }

        dessaisissementRevenu.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_DESSAISISSEMENT_REVENU);

        try {
            dessaisissementRevenu.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, dessaisissementRevenu.getSimpleDonneeFinanciereHeader()));

            dessaisissementRevenu = PegasusImplServiceLocator.getDessaisissementRevenuService().create(
                    dessaisissementRevenu);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DessaisissementRevenuException("Service not available - " + e.getMessage());
        }
        return dessaisissementRevenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.dessaisissement.
     * DessaisissementService#deleteParListeIdDoFinH(java.util.List, java.lang.String)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_DESSAISISSEMENT_FORTUNE + "-")) {
            PegasusImplServiceLocator.getSimpleDessaisissementFortuneService().deleteParListeIdDoFinH(
                    idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_DESSAISISSEMENT_REVENU + "-")) {
            PegasusImplServiceLocator.getSimpleDessaisissementRevenuService()
                    .deleteParListeIdDoFinH(idsDonneFinanciere);
        }
    }

}
