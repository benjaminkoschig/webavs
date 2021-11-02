package ch.globaz.pegasus.businessimpl.checkers.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public abstract class DroitChecker extends PegasusAbstractChecker {
    /**
     * @param droit
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCorriger(Droit droit) throws DroitException, JadePersistenceException,
            JadeNoBusinessLogSessionError, JadeApplicationServiceNotAvailableException {

        // Pour pouvoir etre corrige un droit doit etre dans l'etat OCTROYE
        if (!IPCDroits.CS_VALIDE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
            JadeThread.logError(droit.getClass().getName(), "pegasus.droit.corriger.etat.integrity");
        }

        // La demande du droit ne doit pas avoir de date de fin
        if (isDemandeGetDatedeFin(droit) && !isDateValable(droit.getDemande().getSimpleDemande().getDateFin())) {
            JadeThread.logError(DroitChecker.class.getName(),"pegasus.droit.corriger.demandeNonReouverte.integrity");
        }

        // Il ne doit pas exister de droit dans les etats ENREGISTRE, AU_CALCUL
        // ou CALCULE pour la demande PC du droit
        DroitSearch dSearch = new DroitSearch();
        dSearch.setForIdDemandePc(droit.getSimpleDroit().getIdDemandePC());
        dSearch.setForCsEtatDroitIn(Arrays
                .asList(IPCDroits.CS_ENREGISTRE, IPCDroits.CS_AU_CALCUL, IPCDroits.CS_CALCULE));

        if (PegasusImplServiceLocator.getDroitBusinessService().count(dSearch) > 0) {
            JadeThread.logError(droit.getClass().getName(), "pegasus.droit.corriger.droitNonValideExistant.integrity");
        }

    }

    /**
     * Méthode qui permet de savoir si la date est valable, ultérieur à la date du jour
     *
     * @param dateFin
     * @return
     */
    private static boolean isDateValable(String dateFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateFinDroit = LocalDate.parse("01."+dateFin, formatter);
        dateFinDroit = dateFinDroit.withDayOfMonth(dateFinDroit.lengthOfMonth());
        LocalDate today = LocalDate.now();
        return dateFinDroit.isAfter(today);
    }

    private static boolean isDemandeGetDatedeFin(Droit droit) {
        return !droit.getDemande().getSimpleDemande().getDateFin().isEmpty();
    }

    public static void checkForCreateDroitMemembreFamille(MembreFamilleVO membreFamille) {
        if (JadeStringUtil.isIntegerEmpty(membreFamille.getIdTiers())) {
            String[] param = { membreFamille.getNom(), membreFamille.getPrenom() };
            JadeThread.logError(Droit.class.getName(), "pegasus.droit.createDroitMemebreFamille.idTier.mandatory",
                    param);
        }
    }

    public static void checkForCreationInitial(Demande demande) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeNoBusinessLogSessionError {

        if (demande == null) {
            JadeThread.logError(Droit.class.getName(), "pegasus.droit.createDroitInitial.demande.mandatory");
        }

        if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateDepot())) {
            JadeThread.logError(Droit.class.getName(), "pegasus.droit.createDroitInitial.datedepot.mandatory");
            throw new DroitException("Unable to create droit initial");
        }

        // comme son nom l'indique le droit initial doit etre le premier droit
        // d'une demande
        if (DroitChecker.existDroit(demande.getSimpleDemande().getIdDemande())) {
            JadeThread.logError(Droit.class.getName(), "pegasus.droit.createDroitInitial.firstDroit.integrity");
        }

    }

    public static void checkIsDroitMembrefamilleSynchronisable(Droit droit) {
        if (!DroitChecker.isDroitMembrefamilleSynchronisableWithOutException(droit)) {
            JadeThread.logError(Droit.class.getName(), "pegasus.droit.syncroniserMembreFamille.integrity");
        }
    }

    public static boolean existDroit(String idDemande) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeNoBusinessLogSessionError {
        boolean exist = true;
        // comme son nom l'indique le droit initial doit etre le premier droit
        // d'une demande
        DroitSearch dSearch = new DroitSearch();
        dSearch.setForIdDemandePc(idDemande);
        if (PegasusImplServiceLocator.getDroitBusinessService().count(dSearch) > 0) {
            exist = true;
        } else {
            exist = false;
        }
        return exist;
    }

    public static boolean isDeletable(SimpleVersionDroit simpleVersionDroit) throws DroitException {
        if (JadeStringUtil.isIntegerEmpty(simpleVersionDroit.getCsEtatDroit())) {
            throw new DroitException("Unable to check, the etatDroit passed is null!");
        }
        return !IPCDroits.CS_VALIDE.equals(simpleVersionDroit.getCsEtatDroit())
                && !IPCDroits.CS_HISTORISE.equals(simpleVersionDroit.getCsEtatDroit())
                && !IPCDroits.CS_ANNULE.equals(simpleVersionDroit.getCsEtatDroit());
    }

    public static boolean isDroitMembrefamilleSynchronisableWithOutException(Droit droit) {
        if ((Integer.valueOf(droit.getSimpleVersionDroit().getNoVersion()) > 1)
                || IPCDroits.CS_VALIDE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
                || IPCDroits.CS_HISTORISE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
                || IPCDroits.CS_ANNULE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isUpdatable(Droit droit) throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        boolean updatable = false;
        DroitSearch dSearch = new DroitSearch();
        dSearch.setForIdVersionDroit(droit.getSimpleVersionDroit().getIdVersionDroit());
        dSearch.setForCsEtatDroitIn(Arrays
                .asList(IPCDroits.CS_ENREGISTRE, IPCDroits.CS_AU_CALCUL, IPCDroits.CS_CALCULE));
        if (PegasusImplServiceLocator.getDroitBusinessService().count(dSearch) > 0) {
            updatable = true;
        }
        return updatable;
    }

    public static boolean isUpdatableWithOutException(Droit droit) {
        boolean updatable = false;
        try {
            updatable = DroitChecker.isUpdatable(droit);
        } catch (Exception e) {
            updatable = false;
        }
        return updatable;
    }
}
