package ch.globaz.al.businessimpl.checker.model.prestation;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseModelException;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.utils.ALImportUtils;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

/**
 * 
 * classe de validation des donn�es de RecapitulatifEntrepriseModel
 * 
 * @author PTA
 * @see ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel
 */
public abstract class RecapitulatifEntrepriseModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit� m�tier des donn�es avant suppression
     * 
     * @param recap
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkBusinessIntegrity(RecapitulatifEntrepriseModel recap) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        if (!recap.getPeriodeDe().equals(recap.getPeriodeA())
                && JadeDateUtil.isDateMonthYearBefore(recap.getPeriodeA(), recap.getPeriodeDe())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.PeriodeDePeriodeA.businessIntegrity.chronology");
        }

        // v�rification du format du num�ro de l'affili�
        if (!AFBusinessServiceLocator.getAffiliationService().isNumeroAffilieValide(recap.getNumeroAffilie())
                .booleanValue()) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.numeroAffilie.businessIntegrity.format");

            // v�rification de l'existance de l'affili�
        } else if (AFBusinessServiceLocator.getAffiliationService().nombreAffiliationExists(recap.getNumeroAffilie()) == 0) {

            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.numeroAffilie.businessIntegrity.existingId",
                    new String[] { recap.getNumeroAffilie(), "01." + recap.getPeriodeDe() });
        }

        if (!ALCSPrestation.ETAT_SA.equals(recap.getEtatRecap())) {
            if (!recap.isNew()) {
                RecapitulatifEntrepriseModel recapInDb = ALServiceLocator.getRecapitulatifEntrepriseModelService()
                        .read(recap.getId());
                if (!recapInDb.getIdProcessusPeriodique().equals(recap.getIdProcessusPeriodique())) {
                    JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                            "al.prestation.recapitulatifEntrepriseModel.businessIntegrity.verouille");
                }
            }
        }
        // si on lie � un processus qui n'est pas compatible avec la r�cap / tests selon coti
        else if (!JadeStringUtil.isBlankOrZero(recap.getIdProcessusPeriodique())) {

            ProcessusPeriodiqueModel processusLie = ALServiceLocator.getProcessusPeriodiqueModelService().read(
                    recap.getIdProcessusPeriodique());
            ConfigProcessusModel config = ALServiceLocator.getConfigProcessusModelService().read(
                    processusLie.getIdConfig());
            if (ALCSAffilie.GENRE_ASSURANCE_PARITAIRE.equals(recap.getGenreAssurance())) {
                if (ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PERS.equals(config.getBusinessProcessus())
                        || ALCSProcessus.NAME_PROCESSUS_FACTURATION_HORLO_PERS.equals(config.getBusinessProcessus())) {
                    JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                            "al.prestation.recapitulatifEntrepriseModel.businessIntegrity.incompatible");
                }
            } else if (ALCSAffilie.GENRE_ASSURANCE_INDEP.equals(recap.getGenreAssurance())) {
                if (ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PAR.equals(config.getBusinessProcessus())
                        || ALCSProcessus.NAME_PROCESSUS_FACTURATION_HORLO_PAR.equals(config.getBusinessProcessus())) {
                    JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                            "al.prestation.recapitulatifEntrepriseModel.businessIntegrity.incompatible");
                }
            }
        }

        // TODO:on ne peut pas avoir une r�cap avec m�me n� de facture (� confirmer)

        // TODO: est-ce qu'on emp�che que selon p�riode OU n� factu => li� � un processus particulier?
    }

    /**
     * v�rifi� l'int�grit� des code syst�mes
     * 
     * @param recap
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(RecapitulatifEntrepriseModel recap) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // v�rification du type d'�tat de prestation
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, recap.getEtatRecap())) {
                JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                        "al.prestation.recapitulatifEntrepriseModel.etatRecap.codesystemIntegrity");
            }

            // bonification
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_BONI, recap.getBonification())) {
                JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                        "al.prestation.recapitulatifEntrepriseModel.bonification.codesystemIntegrity");
            }

            // genre assurance
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSAffilie.GROUP_GENRE_ASSURANCE, recap.getGenreAssurance())
                    && !ALConstPrestations.TYPE_ASS_INDETERMINE.equals(recap.getGenreAssurance())) {
                JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                        "al.prestation.recapitulatifEntrepriseModel.genreAssurance.codesystemIntegrity");

            }

        } catch (Exception e) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseModelChecker problem during checking codes system integrity", e);
        }

    }

    /**
     * v�rifie l'int�grit� des donn�es de RecapitulatifEntrpriseModel, si non respect�e lance un message sur l'int�grit�
     * de ces donn�es
     * 
     * @param recap
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkdDatabaseIntegrity(RecapitulatifEntrepriseModel recap) throws JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // d�but de la p�riode
        if (!JadeDateUtil.isGlobazDateMonthYear(recap.getPeriodeDe())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.PeriodeDe.databaseIntegrity.dateFormat");
        }

        // fin de la p�riode
        if (!JadeDateUtil.isGlobazDateMonthYear(recap.getPeriodeA())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.PeriodeA.databaseIntegrity.dateFormat");
        }

        // �tat de la r�cap
        if (!JadeNumericUtil.isIntegerPositif(recap.getEtatRecap())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.EtatRecap.databaseIntegrity.type");
        }

        // num�ro de facture
        if (!JadeNumericUtil.isIntegerPositif(recap.getNumeroFacture())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.numeroFacture.databaseIntegrity.type");
        }

        // bonification
        if (!JadeNumericUtil.isIntegerPositif(recap.getBonification())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.bonification.databaseIntegrity.type");
        }

        // genre d'assurance
        if (!JadeStringUtil.isEmpty(recap.getGenreAssurance()) && !JadeNumericUtil.isInteger(recap.getGenreAssurance())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.genreAssurance.databaseIntegrity.type");

        }

    }

    /**
     * Valide l'int�grit� m�tier des donn�es avant suppression
     * 
     * @param recap
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkDeleteIntegrity(RecapitulatifEntrepriseModel recap) throws JadePersistenceException,
            JadeApplicationException {

        if (ALServiceLocator.getRecapitulatifEntrepriseBusinessService().isRecapVerouillee(recap)) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapEntrepriseModel.etat.businessIntegrity.verrouille");
        }

    }

    /**
     * v�rifie l'obligation des donn�es de RecapitulatifEntrpriseModel, si non respect�e lance un message sur
     * l'obligation de ces donn�es
     * 
     * @param recap
     *            Mod�le � valider
     */
    private static void checkMandatory(RecapitulatifEntrepriseModel recap) {

        // num�ro de l'affili�
        if (JadeStringUtil.isEmpty(recap.getNumeroAffilie())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.numeroAffilie.mandatory");
        }

        // d�but de la p�riode
        if (JadeStringUtil.isEmpty(recap.getPeriodeDe())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.PeriodeDe.mandatory");
        }

        // fin de la p�riode
        if (JadeStringUtil.isEmpty(recap.getPeriodeA())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.PeriodeA.mandatory");
        }

        // �tat de la r�cap
        if (JadeStringUtil.isEmpty(recap.getEtatRecap())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.EtatRecap.mandatory");
        }

        // num�ro de facture
        if (JadeStringUtil.isEmpty(recap.getNumeroFacture())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.numeroFacture.mandatory");
        }

        // bonification
        if (JadeStringUtil.isEmpty(recap.getBonification())) {
            JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                    "al.prestation.recapitulatifEntrepriseModel.bonification.mandatory");
        }
        // genre d'assurance obligatoire dans le cas o� l'on importe pas les
        // donn�es depuis alfagest
        if (!ALImportUtils.importFromAlfaGest) {

            if (JadeStringUtil.isEmpty(recap.getGenreAssurance())) {
                JadeThread.logError(RecapitulatifEntrepriseModelChecker.class.getName(),
                        "al.prestation.recapitulatifEntrepriseModel.genreAssurance.mandatory");
            }

        }

    }

    /**
     * Valide les donn�es
     * 
     * @param recap
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(RecapitulatifEntrepriseModel recap) throws JadeApplicationException,
            JadePersistenceException {
        RecapitulatifEntrepriseModelChecker.checkMandatory(recap);
        RecapitulatifEntrepriseModelChecker.checkdDatabaseIntegrity(recap);
        RecapitulatifEntrepriseModelChecker.checkCodesystemIntegrity(recap);
        RecapitulatifEntrepriseModelChecker.checkBusinessIntegrity(recap);

    }

    /**
     * Valide l'int�grit� des donn�es avant suppression
     * 
     * @param recap
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(RecapitulatifEntrepriseModel recap) throws JadeApplicationException,
            JadePersistenceException {
        RecapitulatifEntrepriseModelChecker.checkDeleteIntegrity(recap);
    }
}
