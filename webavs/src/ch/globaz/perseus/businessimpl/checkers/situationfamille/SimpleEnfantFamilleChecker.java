package ch.globaz.perseus.businessimpl.checkers.situationfamille;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.perseus.business.constantes.CSTypeGarde;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.models.demande.SimpleDemandeSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfantFamilleSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class SimpleEnfantFamilleChecker extends PerseusAbstractChecker {
    /**
     * @param simpleEnfantFamille
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     */
    public static void checkForCreate(SimpleEnfantFamille simpleEnfantFamille) throws SituationFamilleException,
            JadePersistenceException {
        SimpleEnfantFamilleChecker.checkMandatory(simpleEnfantFamille);
        if (!PerseusAbstractChecker.threadOnError()) {
            SimpleEnfantFamilleChecker.checkIntegrity(simpleEnfantFamille);
        }
    }

    /**
     * @param simpleEnfantFamille
     */
    public static void checkForDelete(SimpleEnfantFamille simpleEnfantFamille) {

    }

    /**
     * @param simpleEnfantFamille
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws SituationFamilleException
     */
    public static void checkForUpdate(SimpleEnfantFamille simpleEnfantFamille) throws SituationFamilleException,
            JadePersistenceException {
        SimpleEnfantFamilleChecker.checkMandatory(simpleEnfantFamille);
    }

    private static void checkIntegrity(SimpleEnfantFamille simpleEnfantFamille) throws SituationFamilleException,
            JadePersistenceException {
        try {
            // Check que l'enfant n'est pas déjà dans la situation familiale
            SimpleEnfantFamilleSearchModel simpleEnfantFamilleSearchModel = new SimpleEnfantFamilleSearchModel();
            simpleEnfantFamilleSearchModel.setForIdEnfant(simpleEnfantFamille.getIdEnfant());
            simpleEnfantFamilleSearchModel.setForIdSituationFamiliale(simpleEnfantFamille.getIdSituationFamiliale());
            if (PerseusImplServiceLocator.getSimpleEnfantFamilleService().count(simpleEnfantFamilleSearchModel) > 0) {
                JadeThread.logError(SimpleEnfantFamilleChecker.class.getName(),
                        "perseus.situationfamiliale.simpleEnfantFamille.enfant.exist");
            }

            // Lecture de la demande
            Demande demande = null;
            DemandeSearchModel demandeSearchModel = new DemandeSearchModel();
            demandeSearchModel.setForIdSituationFamiliale(simpleEnfantFamille.getIdSituationFamiliale());
            demandeSearchModel = PerseusServiceLocator.getDemandeService().search(demandeSearchModel);
            if (demandeSearchModel.getSize() == 1) {
                demande = (Demande) demandeSearchModel.getSearchResults()[0];
            } else {
                throw new SituationFamilleException("Unable to check simpleEnfantfamille, cannot find demande");
            }

            // Check que l'enfant a moins de 25 ans
            Enfant enfant = PerseusServiceLocator.getEnfantService().read(simpleEnfantFamille.getIdEnfant());
            String dateNaissance = enfant.getMembreFamille().getPersonneEtendue().getPersonne().getDateNaissance();
            int ageEnfant = JadeDateUtil.getNbYearsBetween(dateNaissance, demande.getSimpleDemande().getDateDebut());
            if (ageEnfant >= IPFConstantes.AGE_25ANS) {
                JadeThread.logError(SimpleEnfantFamilleChecker.class.getName(),
                        "perseus.situationfamiliale.simpleEnfantFamille.enfant.plus25");
            }

            checkInegrityForCopie(simpleEnfantFamille, demande);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Unable to check simpleEnfantFamille, service is not available : "
                    + e.getMessage(), e);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new SituationFamilleException("Unable to check simpleEnfantFamille, JadeNoBusinessLogSession : "
                    + e.getMessage(), e);
        } catch (DemandeException e) {
            throw new SituationFamilleException("Unable to checke simpleEnfantFamille, DemandeException : "
                    + e.getMessage(), e);
        }
    }

    public static void checkInegrityForCopie(SimpleEnfantFamille simpleEnfantFamille, Demande demande)
            throws JadePersistenceException, SituationFamilleException, JadeApplicationServiceNotAvailableException,
            DemandeException, JadeNoBusinessLogSessionError {
        // Check que l'enfant n'est pas déjà dans une autre situation familiale
        SimpleEnfantFamilleSearchModel searchModel = new SimpleEnfantFamilleSearchModel();
        searchModel.setForIdEnfant(simpleEnfantFamille.getIdEnfant());
        searchModel = PerseusImplServiceLocator.getSimpleEnfantFamilleService().search(searchModel);
        // HashMap des enfants correspondants mise en correspondance des situations familiales
        HashMap<String, SimpleEnfantFamille> enfantsToCheck = new HashMap<String, SimpleEnfantFamille>();
        ArrayList<String> listeDossier = new ArrayList<String>();
        // Pour chaque enfant trouvé voir si les dates correspondent
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            SimpleEnfantFamille sef = (SimpleEnfantFamille) model;
            enfantsToCheck.put(sef.getIdSituationFamiliale(), sef);
        }
        // Si l'enfant se trouve dans une sitation familiale
        if (!enfantsToCheck.isEmpty()) {
            int gardePartagee = 0;
            SimpleDemandeSearchModel simpleDemandeSearchModel = new SimpleDemandeSearchModel();
            simpleDemandeSearchModel.setInIdSituationFamiliale(new ArrayList<String>(enfantsToCheck.keySet()));
            // Pour permettre les demandes multiples avec les mêmes enfants dans un dossier
            simpleDemandeSearchModel.setForNotIdDossier(demande.getDossier().getId());
            simpleDemandeSearchModel.setWhereKey(SimpleDemandeSearchModel.WITH_SITUATION_FAMILIALE);
            simpleDemandeSearchModel = PerseusImplServiceLocator.getSimpleDemandeService().search(
                    simpleDemandeSearchModel);
            String[] tMess = { "" };
            for (JadeAbstractModel model : simpleDemandeSearchModel.getSearchResults()) {
                SimpleDemande sd = (SimpleDemande) model;
                // Si c'est pas la demande actuelle
                if (!sd.getId().equals(demande.getId())) {
                    String dateDebut = demande.getSimpleDemande().getDateDebut();
                    String dateFin = demande.getSimpleDemande().getDateFin();
                    // Si la demande commence ou fini entre les dates de la demande où l'enfant se situe
                    // Donc qu'elle peut poser problème avec la demande actuelle
                    if (isDateMembreFamilleAlreadyExistsInAntoherDemand(sd, dateDebut, dateFin)) {
                        // Si il est en garde exclusive pas possible donc erreur
                        if (JadeStringUtil.isBlankOrZero(enfantsToCheck.get(sd.getIdSituationFamiliale()).getCsGarde())
                                || CSTypeGarde.GARDE_EXCLUSIVE.getCodeSystem().equals(
                                        enfantsToCheck.get(sd.getIdSituationFamiliale()).getCsGarde())) {
                            PersonneEtendueComplexModel personne = PerseusServiceLocator.getDemandeService()
                                    .read(sd.getId()).getDossier().getDemandePrestation().getPersonneEtendue();
                            tMess[0] = personne.getPersonneEtendue().getNumAvsActuel() + " "
                                    + personne.getTiers().getDesignation1() + " "
                                    + personne.getTiers().getDesignation2();
                            JadeThread.logWarn(SimpleEnfantFamilleChecker.class.getName(),
                                    "perseus.situationfamiliale.simpleEnfantFamille.enfant.gardeexclusive", tMess);
                        }
                        // Si il est en garde partagée
                        if (CSTypeGarde.GARDE_PARTAGEE.getCodeSystem().equals(
                                enfantsToCheck.get(sd.getIdSituationFamiliale()).getCsGarde())) {
                            PersonneEtendueComplexModel personne = PerseusServiceLocator.getDemandeService()
                                    .read(sd.getId()).getDossier().getDemandePrestation().getPersonneEtendue();
                            String idDossier = PerseusServiceLocator.getDemandeService().read(sd.getId()).getDossier()
                                    .getDossier().getIdDossier();
                            if (!listeDossier.contains(idDossier)) {
                                listeDossier.add(idDossier);
                                tMess[0] += personne.getPersonneEtendue().getNumAvsActuel() + " "
                                        + personne.getTiers().getDesignation1() + " "
                                        + personne.getTiers().getDesignation2() + " / ";
                                gardePartagee++;
                            }
                        }
                    }
                }
            }
            // Regarder que l'enfant ne soit pas dans 2 situations en garde partagée
            if (gardePartagee > 1) {
                JadeThread.logWarn(SimpleEnfantFamilleChecker.class.getName(),
                        "perseus.situationfamiliale.simpleEnfantFamille.enfant.gardepartagee", tMess);
            }
            if ((gardePartagee == 1)
                    && (CSTypeGarde.GARDE_EXCLUSIVE.getCodeSystem().equals(simpleEnfantFamille.getCsGarde()) || JadeStringUtil
                            .isBlankOrZero(simpleEnfantFamille.getCsGarde()))) {
                JadeThread.logWarn(SimpleEnfantFamilleChecker.class.getName(),
                        "perseus.situationfamiliale.simpleEnfantFamille.enfant.gardepartetexclu", tMess);
            }
        }
    }

    private static boolean isDateMembreFamilleAlreadyExistsInAntoherDemand(SimpleDemande sd, String dateDebut,
            String dateFin) {

        // date de fin nouvelle demande et demande comparée vide
        if (JadeStringUtil.isBlank(dateFin) && JadeStringUtil.isBlank(sd.getDateFin())) {
            return JadeDateUtil.areDatesEquals(dateDebut, sd.getDateDebut())
                    || JadeDateUtil.isDateAfter(dateDebut, sd.getDateDebut());

        } else if (JadeStringUtil.isBlank(dateFin)) {
            return JadeDateUtil.areDatesEquals(dateDebut, sd.getDateDebut())
                    || JadeDateUtil.isDateBefore(dateDebut, sd.getDateDebut());
        } else if (JadeStringUtil.isBlank(sd.getDateFin())) {
            return JadeDateUtil.areDatesEquals(dateDebut, sd.getDateDebut())
                    || (JadeDateUtil.isDateAfter(dateDebut, sd.getDateDebut()) && JadeDateUtil.isDateBefore(
                            sd.getDateDebut(), dateFin));
        } else {
            return JadeDateUtil.isDateAfter(dateDebut, sd.getDateDebut())
                    && JadeDateUtil.isDateBefore(dateDebut, sd.getDateFin())
                    || (JadeDateUtil.isDateAfter(dateFin, sd.getDateDebut()) && JadeDateUtil.isDateBefore(dateFin,
                            sd.getDateFin()))
                    || (JadeDateUtil.isDateBefore(dateDebut, sd.getDateDebut()) && JadeDateUtil.isDateAfter(dateFin,
                            sd.getDateFin()))
                    || (JadeDateUtil.areDatesEquals(dateDebut, sd.getDateDebut()) || JadeDateUtil.areDatesEquals(
                            dateFin, sd.getDateFin()));
        }

    }

    /**
     * @param simpleEnfantFamille
     */
    private static void checkMandatory(SimpleEnfantFamille simpleEnfantFamille) {
        if (JadeStringUtil.isEmpty(simpleEnfantFamille.getIdSituationFamiliale())) {
            JadeThread.logError(SimpleEnfantFamilleChecker.class.getName(),
                    "perseus.situationfamiliale.simpleEnfantFamille.idSituationFamiliale.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleEnfantFamille.getIdEnfant())) {
            JadeThread.logError(SimpleEnfantFamilleChecker.class.getName(),
                    "perseus.situationfamiliale.simpleEnfantFamille.idEnfant.mandatory");
        }
    }

}
