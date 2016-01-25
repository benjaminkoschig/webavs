package ch.globaz.al.businessimpl.checker.model.periodeAF;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.exceptions.model.periodeAF.ALPeriodeAFException;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * classe de validation des données de PeriodeAFModel
 * 
 * @author GMO
 * 
 */
public abstract class PeriodeAFModelChecker extends ALAbstractChecker {

    /**
     * Vérification des codesSystems
     * 
     * @param periodeAFModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // état de la période
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSProcessus.GROUP_ETAT_PROCESSUS, periodeAFModel.getEtat())) {
                JadeThread.logError(PeriodeAFModelChecker.class.getName(),
                        "al.periodeAF.periodeAFModel.etat.codesystemIntegrity");
            }

        } catch (Exception e) {
            throw new ALPeriodeAFException("PeriodeAFModelChecker problem during checking codes system integrity", e);
        }

    }

    /**
     * vérification de l'intégrité des données
     * 
     * @param periodeAFModel
     *            Modèle à valider
     */
    private static void checkDatabaseIntegrity(PeriodeAFModel periodeAFModel) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // date période
        if (!JadeStringUtil.isEmpty(periodeAFModel.getDatePeriode())
                && !JadeDateUtil.isGlobazDate(periodeAFModel.getDatePeriode())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(),
                    "al.periode.periodeAFModel.datePeriode.databaseIntegrity.dateFormat");
        }

        // état
        if (!JadeNumericUtil.isIntegerPositif(periodeAFModel.getEtat())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(),
                    "al.periode.periodeAFModel.etat.databaseIntegrity.etat");
        }

    }

    /**
     * Vérification de l'intégrité métier avant suppression
     * 
     * @param periodeAFModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(PeriodeAFModel periodeAFModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // vérification si la période est bien ouverte, on ne peut supprimer que
        // celles-là
        if (ALCSProcessus.ETAT_OUVERT.equals(periodeAFModel.getEtat())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(),
                    "al.periode.periodeAFModel.idPeriode.deleteIntegrity.isClosed");
        }

    }

    /**
     * vérification des données requises
     * 
     * @param periodeAFModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        // date période
        if (JadeStringUtil.isEmpty(periodeAFModel.getDatePeriode())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(),
                    "al.periodeAF.periodeAFModel.datePeriode.mandatory");
        }
        // état période
        if (JadeStringUtil.isEmpty(periodeAFModel.getEtat())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(), "al.periodeAF.periodeAFModel.etat.mandatory");
        }
    }

    /**
     * validation des données de droitModel
     * 
     * @param periodeAFModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        PeriodeAFModelChecker.checkMandatory(periodeAFModel);
        PeriodeAFModelChecker.checkDatabaseIntegrity(periodeAFModel);
        PeriodeAFModelChecker.checkCodesystemIntegrity(periodeAFModel);
        // Pas d'intégrité métier à vérifier
        // PeriodeAFModelChecker.checkBusinessIntegrity(periodeAFModel);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param periodeAFModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        PeriodeAFModelChecker.checkDeleteIntegrity(periodeAFModel);
    }

}
