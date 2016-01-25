package ch.globaz.perseus.businessimpl.checkers.demande;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleDemandeChecker extends PerseusAbstractChecker {

    /**
     * @param demande
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws Exception
     */
    public static void checkForCreate(SimpleDemande demande) throws DemandeException, JadePersistenceException {
        SimpleDemandeChecker.checkMandatory(demande);
        if (!PerseusAbstractChecker.threadOnError()) {
            SimpleDemandeChecker.checkIntegrity(demande);
        }
    }

    /**
     * @param demande
     * @throws DemandeException
     *             , JadePersistenceException
     */
    public static void checkForDelete(SimpleDemande demande) throws DemandeException, JadePersistenceException {
        try {
            // Check qu'il n'existe pas de PCF Accordée
            PCFAccordeeSearchModel pcfAccordeeSearchModel = new PCFAccordeeSearchModel();
            pcfAccordeeSearchModel.setForIdDemande(demande.getId());
            if (PerseusServiceLocator.getPCFAccordeeService().count(pcfAccordeeSearchModel) > 0) {
                JadeThread.logError(SimpleDemandeChecker.class.getName(), "perseus.demandes.pcfaccordees.existe");
            }
            // Check qu'il n'existe pas de decisions
            DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
            decisionSearchModel.setForIdDemande(demande.getId());
            if (PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0) {
                JadeThread.logError(SimpleDemandeChecker.class.getName(), "perseus.demande.decisions.existe");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Unable to check demande, service not available", e);
        } catch (PCFAccordeeException e) {
            throw new DemandeException("PCFAccordee exception during demande check : " + e.toString(), e);
        } catch (DecisionException e) {
            throw new DemandeException("DecisionException exception during demande check : " + e.toString(), e);
        }
    }

    /**
     * @param demande
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws Exception
     */
    public static void checkForUpdate(SimpleDemande demande) throws DemandeException, JadePersistenceException {
        SimpleDemandeChecker.checkMandatory(demande);
        if (!PerseusAbstractChecker.threadOnError()) {
            SimpleDemandeChecker.checkIntegrity(demande);
        }
    }

    /**
     * @param demande
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public static void checkIntegrity(SimpleDemande demande) throws DemandeException, JadePersistenceException {
        // Mettre le message d'avertissement pour la date d'arrivée dans le canton
        // Variable métier
        // if (JadeDateUtil.getNbYearsBetween(demande.getDateArrivee(), demande.getDateDepot()) < 3) {
        // JadeThread.logWarn(SimpleDemandeChecker.class.getName(), "Attention !!!");
        // }

        String pattern = "((\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d);{0,1})*";

        // Controle du format de la date séparée par des ';'
        if (!JadeStringUtil.isEmpty(demande.getDateListeNonEntreeEnMatiere())) {
            if (!demande.getDateListeNonEntreeEnMatiere().matches(pattern)) {
                JadeThread.logError(SimpleDemandeChecker.class.getName(),
                        "perseus.demande.simpleDemande.dateListeNonEntreeEnMatiere.badFormat");
            }
        }

        // Contrôle de la validité des dates
        if (demande.getNonEntreeEnMatiere()) {
            String[] dateTableau = demande.getDateListeNonEntreeEnMatiere().split(";");
            for (String myDate : dateTableau) {
                if (!JadeDateUtil.isGlobazDate(myDate)) {
                    JadeThread.logError(SimpleDemandeChecker.class.getName(),
                            "perseus.demande.simpleDemande.dateListeNonEntreeEnMatiere.badDate");
                }
            }
        }

        // Contrôle de la liste de date. Vérifier si elle n'est pas vide
        if (demande.getNonEntreeEnMatiere() && JadeStringUtil.isEmpty(demande.getDateListeNonEntreeEnMatiere())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.dateListeNonEntreeEnMatiere.dateVide");
        }

        // Contrôle si on a une demande de type Non Entrée en Matière alors on ne doit pas avoir une révision de type
        // extraordinaire
        if (demande.getNonEntreeEnMatiere()
                && demande.getTypeDemande().equals(CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.dateNonEntreeEnMatiere.RevisionExtraordinaire");
        }

        // Contrôle que la date de début ne soit pas avant le 01.10.2011
        if (!JadeStringUtil.isEmpty(demande.getDateDebut())) {
            if (JadeDateUtil.isDateBefore(demande.getDateDebut(), "01.10.2011")) {
                JadeThread.logError(SimpleDemandeChecker.class.getName(),
                        "perseus.demande.simpleDemande.dateDebut.avantMiseEnProd");
            }

            if (!JadeDateUtil.isGlobazDate(demande.getDateDebut())) {
                JadeThread.logError(SimpleDemandeChecker.class.getName(),
                        "perseus.demande.simpleDemande.dateDebut.format");
            }
        }

        // Contrôle que la date de fin soit plus tard que la date de début
        if (!JadeStringUtil.isEmpty(demande.getDateFin())) {

            if (!JadeDateUtil.isGlobazDate(demande.getDateFin())) {
                JadeThread.logError(SimpleDemandeChecker.class.getName(),
                        "perseus.demande.simpleDemande.dateFin.format");
            }

            if (!JadeDateUtil.isDateBefore(demande.getDateDebut(), demande.getDateFin())) {
                JadeThread.logError(SimpleDemandeChecker.class.getName(),
                        "perseus.demande.simpleDemande.dateFin.tooEarly");
            }
        }

        if (!JadeStringUtil.isEmpty(demande.getDateDepot()) && !JadeDateUtil.isGlobazDate(demande.getDateDepot())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(), "perseus.demande.date.depot");
        }

        if (!JadeStringUtil.isEmpty(demande.getDateArrivee()) && !JadeDateUtil.isGlobazDate(demande.getDateArrivee())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(), "perseus.demande.date.arriver.canton");
        }

        // Contrôle qu'il n'y a pas de demande supperposée au niveau des dates
        // SimpleDemandeSearchModel simpleDemandeSearchModel = new SimpleDemandeSearchModel();
        // simpleDemandeSearchModel.setForDateDebutCheck(demande.getDateDebut());
        // simpleDemandeSearchModel.setForDateFinCheck(demande.getDateFin());
        // simpleDemandeSearchModel.setForIdDossier(demande.getIdDossier());
        // if (!demande.isNew()) {
        // simpleDemandeSearchModel.setForNotIdDemande(demande.getId());
        // }
        //
        // try {
        // if (PerseusImplServiceLocator.getSimpleDemandeService().count(simpleDemandeSearchModel) > 0) {
        // JadeThread.logError(SimpleDemandeChecker.class.getName(),
        // "perseus.demande.simpleDemande.dates.superpose");
        // }
        // } catch (JadeApplicationServiceNotAvailableException e) {
        // throw new DemandeException("Service not avaiable : " + e.getMessage());
        // } catch (JadeNoBusinessLogSessionError e) {
        // throw new DemandeException("JadeNoBusinessLogSessionError : " + e.getMessage());
        // }

    }

    /**
     * @param demande
     */
    private static void checkMandatory(SimpleDemande demande) {
        if (JadeStringUtil.isEmpty(demande.getIdDossier())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.idDossier.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getCsEtatDemande())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.csEtatDemande.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getDateArrivee())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.dateArrivee.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getDateDebut())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.dateDebut.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getDateDepot())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.dateDepot.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getIdGestionnaire())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.idGestionnaire.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getIdAgenceCommunale())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.agencecommunale.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getIdAgenceRi())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.agenceri.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getTypeDemande())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(),
                    "perseus.demande.simpleDemande.typedemande.mandatory");
        }
        if (JadeStringUtil.isEmpty(demande.getCsCaisse())) {
            JadeThread.logError(SimpleDemandeChecker.class.getName(), "perseus.demande.simpleDemande.caisse.mandatory");
        }
    }
}
