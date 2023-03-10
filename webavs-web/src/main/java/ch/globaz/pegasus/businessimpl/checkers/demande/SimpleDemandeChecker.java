package ch.globaz.pegasus.businessimpl.checkers.demande;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.process.adaptation.PCAdaptationUtils;
import globaz.corvus.api.lots.IRELot;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.demande.SimpleDemandeSearch;
import ch.globaz.pegasus.business.models.dossier.DossierSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.SimpleDroitSearch;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.lot.SimplePrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SimpleDemandeChecker extends PegasusAbstractChecker {

    public static void checkForCreate(SimpleDemande demande) throws DemandeException, DossierException,
            JadePersistenceException {
        SimpleDemandeChecker.checkMandatory(demande);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDemandeChecker.checkIntegrity(demande);
            if (!demande.getIsPurRetro()) {
                if (SimpleDemandeChecker.existeDemandeInVlalidEtat(demande)) {
                    JadeThread.logError(demande.getClass().getName(), "pegasus.demande.valideDemande.integrity");
                }
            }
        }

    }

    public static void checkForDelete(SimpleDemande demande) throws DemandeException {
        if (SimpleDemandeChecker.existDroitForDemande(demande)) {
            JadeThread.logError(demande.getClass().getName(), "pegasus.demande.delete.integrity");
        }

    }

    public static void checkForUpdate(SimpleDemande demande) throws DemandeException, DossierException,
            JadePersistenceException {
        SimpleDemandeChecker.checkMandatory(demande);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDemandeChecker.checkIntegrity(demande);
        }

    }

    private static void checkIntegrity(SimpleDemande demande) throws DemandeException, DossierException,
            JadePersistenceException {

        DossierSearch search = new DossierSearch();
        search.setForIdDossier(demande.getIdDossier());

        try {
            if (PegasusServiceLocator.getDossierService().count(search) != 1) {
                JadeThread.logError(demande.getClass().getName(), "pegasus.demande.iddossier.integrity");
            }

            if (!JadeStringUtil.isBlankOrZero(demande.getDateDebut())
                    && !JadeStringUtil.isBlankOrZero(demande.getDateFin())) {
                SimpleDemandeChecker.checkSuperpositionPeriode(demande);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Unable to check demande", e);
        }

    }

    private static void checkMandatory(SimpleDemande demande) {

        if (demande.getIsPurRetro()) {
            if (JadeStringUtil.isEmpty(demande.getDateDebut())) {
                JadeThread.logError(demande.getClass().getName(), "pegasus.demande.dateDebutRetro.mandatory");
            }
            if (JadeStringUtil.isEmpty(demande.getDateFin())) {
                JadeThread.logError(demande.getClass().getName(), "pegasus.demande.dateFinRetro.mandatory");
            }
        }
        if (JadeStringUtil.isEmpty(demande.getIdDossier())) {
            JadeThread.logError(demande.getClass().getName(), "pegasus.demande.iddossier.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getTypeDemande())) {
            JadeThread.logError(demande.getClass().getName(), "pegasus.demande.typeDemande.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getDateDepot())) {
            JadeThread.logError(demande.getClass().getName(), "pegasus.demande.datedepot.mandatory");
        }

    }

    private static void checkSuperpositionPeriode(SimpleDemande demande) throws DemandeException {
        String[] param = new String[2];
        param[0] = demande.getDateDebut();
        param[1] = demande.getDateFin();
        if (SimpleDemandeChecker.hasSuperpositionPeriode(demande)) {
            JadeThread.logError(demande.getClass().getName(), "pegasus.demande.superPositionPeriode.integrity", param);
        }
    }

    private static boolean existDroitForDemande(SimpleDemande demande) throws DemandeException {
        SimpleDroitSearch search = new SimpleDroitSearch();
        search.setForIdDemandePC(demande.getId());
        boolean exist = false;

        try {
            int nb = PegasusImplServiceLocator.getSimpleDroitService().count(search);
            if (nb > 0) {
                exist = true;
            }
        } catch (DroitException e) {
            throw new DemandeException("Unable to check demande", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Unable to check demande", e);
        } catch (JadePersistenceException e) {
            throw new DemandeException("Unable to check demande", e);
        }
        return exist;
    }

    public static boolean existDroitForDemandeWithOutException(SimpleDemande demande) {
        boolean exit = false;
        try {
            exit = SimpleDemandeChecker.existDroitForDemande(demande);
        } catch (DemandeException e) {
            exit = true;
        }
        return exit;
    }

    public static boolean isLotAnnuleComptabilise(SimpleDemande demande) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PrestationException, LotException {
        if (IPCDemandes.CS_ANNULE.equals(demande.getCsEtatDemande())) {
            List<Droit> droits = PegasusServiceLocator.getDroitService().findCurrentVersionDroitByIdsDemande(
                    Arrays.asList(demande.getId()));

            if (droits != null && !droits.isEmpty()) {
                SimplePrestationSearch simplePrestationSearch = new SimplePrestationSearch();
                simplePrestationSearch.setForIdVersionDroit(droits.get(0).getSimpleVersionDroit().getId());
                simplePrestationSearch = PegasusImplServiceLocator.getSimplePrestationService().search(
                        simplePrestationSearch);
                if (simplePrestationSearch.getSize() > 0) {
                    SimplePrestation simplePrestation = (SimplePrestation) simplePrestationSearch.getSearchResults()[0];
                    SimpleLot lot = CorvusServiceLocator.getLotService().read(simplePrestation.getIdLot());
                    return !IRELot.CS_ETAT_LOT_OUVERT.equals(lot.getCsEtat());
                }
            }
        }
        return false;
    }

    public static boolean isLotDateReducComptabilise(SimpleDemande demande) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PrestationException, LotException {
        if (IPCDemandes.CS_SUPPRIME.equals(demande.getCsEtatDemande())

        && !JadeStringUtil.isEmpty(demande.getDateFinInitial())) {
            List<Droit> droits = PegasusServiceLocator.getDroitService().findCurrentVersionDroitByIdsDemande(
                    Arrays.asList(demande.getId()));

            if (droits != null && !droits.isEmpty()) {
                SimplePrestationSearch simplePrestationSearch = new SimplePrestationSearch();
                simplePrestationSearch.setForIdVersionDroit(droits.get(0).getSimpleVersionDroit().getId());
                simplePrestationSearch = PegasusImplServiceLocator.getSimplePrestationService().search(
                        simplePrestationSearch);
                if (simplePrestationSearch.getSize() > 0) {
                    SimplePrestation simplePrestation = (SimplePrestation) simplePrestationSearch.getSearchResults()[0];
                    SimpleLot lot = CorvusServiceLocator.getLotService().read(simplePrestation.getIdLot());
                    return !IRELot.CS_ETAT_LOT_OUVERT.equals(lot.getCsEtat());
                }
            }
        }
        return false;
    }

    private static boolean existeDemandeInVlalidEtat(SimpleDemande demande) throws DemandeException {
        SimpleDemandeSearch search = new SimpleDemandeSearch();
        search.setWhereKey("demandeInCsEtat");
        List<String> inCsEtat = new ArrayList<String>();
        inCsEtat.add(IPCDemandes.CS_OCTROYE);
        inCsEtat.add(IPCDemandes.CS_EN_ATTENTE_CALCUL);
        inCsEtat.add(IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS);
        search.setForCsEtatDemandeIN(inCsEtat);
        search.setForIdDossier(demande.getIdDossier());
        boolean exist = true;
        try {
            int nb = PegasusImplServiceLocator.getSimpleDemandeService().count(search);
            if (nb > 0) {
                exist = true;
            } else {
                exist = false;
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Unable to check simpleDemande for existeDemandeInVlalidEtat", e);
        } catch (JadePersistenceException e) {
            throw new DemandeException("Unable to check simpleDemande for existeDemandeInVlalidEtat", e);
        }
        return exist;

    }

    private static boolean existeDemandeWithDateDeFinVide(SimpleDemande demande) throws DemandeException {
        SimpleDemandeSearch search = new SimpleDemandeSearch();
        search.setWhereKey("WithDateFinNull");
        search.setForIdDossier(demande.getIdDossier());
        search.setForNotCsEtatDemande(IPCDemandes.CS_ANNULE);
        boolean exist = true;
        try {
            int nb = PegasusImplServiceLocator.getSimpleDemandeService().count(search);
            if (nb > 0) {
                exist = true;
            } else {
                exist = false;
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Unable to check simpleDemande for existeDemandeInVlalidEtat", e);
        } catch (JadePersistenceException e) {
            throw new DemandeException("Unable to check simpleDemande for existeDemandeInVlalidEtat", e);
        }
        return exist;

    }

    public static boolean isPossibleToCreateNewDeamande(String idDossier) {
        boolean canCreate = false;
        if (!JadeStringUtil.isEmpty(idDossier)) {
            SimpleDemande demande = new SimpleDemande();
            demande.setIdDossier(idDossier);
            try {
                canCreate = !SimpleDemandeChecker.existeDemandeWithDateDeFinVide(demande);
            } catch (DemandeException e) {

            }
        }
        return canCreate;
    }

    public static boolean hasSuperpositionPeriode(SimpleDemande simpleDemande) throws DemandeException {
        SimpleDemandeSearch search = new SimpleDemandeSearch();
        search.setWhereKey(SimpleDemandeSearch.CHECK_SUPERPOSIZION_DATES);
        search.setForDateDebut(simpleDemande.getDateDebut());
        search.setForDateFin(simpleDemande.getDateFin());
        search.setForIdDossier(simpleDemande.getIdDossier());
        search.setForNotIdDemande(simpleDemande.getIdDemande());
        search.setForNotCsEtatDemande(IPCDemandes.CS_ANNULE);
        try {
            return PegasusImplServiceLocator.getSimpleDemandeService().count(search) > 0;
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Unable to check simpleDemande for checkSuperpositionPeriode", e);
        } catch (JadePersistenceException e) {
            throw new DemandeException("Unable to check simpleDemande for checkSuperpositionPeriode", e);
        }
    }

    /**
     *
     * @param demande
     * @return
     * @throws DemandeException
     */
    public static boolean isDemandePurRetro(Demande demande) throws DemandeException {
        boolean isPurRetro = false;

        Droit currentDroit = getDroitByIdDemande(demande.getId());

        if (currentDroit!=null && isVersionDroitUn(currentDroit) && droitIsNonValide(currentDroit) && demandeIsEnAttenteCalculOrEnAttenteJustificatifs(demande.getDossier().getId())){
            isPurRetro = true;
        }

        return isPurRetro;
    }

    private static Droit getDroitByIdDemande(String idDemande) {
        try {
            DroitSearch droitSearch = PCAdaptationUtils.findTheCurrentDroit(idDemande);
            if (droitSearch.getSearchResults().length == 0) {
                // Aucun droit trouv?, retourner false
                return null;
            } else {
                Droit currentDroit = (Droit) droitSearch.getSearchResults()[0];
                return currentDroit;
            }
        } catch (DroitException e) {
            LOG.error("SimpleDemandeChecker#getDroitByIdDemande - Erreur ? la lecture du droit, la demande ne pourra pas ?tre trait? en \"purement r?trocactive\"", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOG.error("SimpleDemandeChecker#getDroitByIdDemande - Erreur Application non disponible, la demande ne pourra pas ?tre trait? en \"purement r?trocactive\"", e);
        } catch (JadePersistenceException e) {
            LOG.error("SimpleDemandeChecker#getDroitByIdDemande - Erreur de persistence Jade, la demande ne pourra pas ?tre trait? en \"purement r?trocactive\"", e);
        }
        return null;
    }


    private static boolean isVersionDroitUn(Droit currentDroit) {
        return "1".equals(currentDroit.getSimpleVersionDroit().getNoVersion());
    }

    /**
     *
     * @param currentDroit
     * @return
     */
    private static boolean droitIsNonValide(Droit currentDroit) {
       return !IPCDroits.CS_VALIDE.equals(currentDroit.getSimpleVersionDroit().getCsEtatDroit());
    }

    /**
     *
     * @param id
     * @return
     * @throws DemandeException
     */
    private static boolean demandeIsEnAttenteCalculOrEnAttenteJustificatifs(String id) throws DemandeException {
        SimpleDemandeSearch search = new SimpleDemandeSearch();
        search.setWhereKey("demandeInCsEtat");
        List<String> inCsEtat = new ArrayList<String>();
        inCsEtat.add(IPCDemandes.CS_EN_ATTENTE_CALCUL);
        inCsEtat.add(IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS);
        search.setForCsEtatDemandeIN(inCsEtat);
        search.setForIdDossier(id);
        boolean exist = false;
        try {
            int nb = PegasusImplServiceLocator.getSimpleDemandeService().count(search);
            if (nb > 0) {
                exist = true;
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Unable to check simpleDemande for existeDemandeInVlalidEtat", e);
        } catch (JadePersistenceException e) {
            throw new DemandeException("Unable to check simpleDemande for existeDemandeInVlalidEtat", e);
        }
        return exist;
    }

}
