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
 * classe de validation des donn�es de PeriodeAFModel
 * 
 * @author GMO
 * 
 */
public abstract class PeriodeAFModelChecker extends ALAbstractChecker {

    /**
     * V�rification des codesSystems
     * 
     * @param periodeAFModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // �tat de la p�riode
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
     * v�rification de l'int�grit� des donn�es
     * 
     * @param periodeAFModel
     *            Mod�le � valider
     */
    private static void checkDatabaseIntegrity(PeriodeAFModel periodeAFModel) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // date p�riode
        if (!JadeStringUtil.isEmpty(periodeAFModel.getDatePeriode())
                && !JadeDateUtil.isGlobazDate(periodeAFModel.getDatePeriode())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(),
                    "al.periode.periodeAFModel.datePeriode.databaseIntegrity.dateFormat");
        }

        // �tat
        if (!JadeNumericUtil.isIntegerPositif(periodeAFModel.getEtat())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(),
                    "al.periode.periodeAFModel.etat.databaseIntegrity.etat");
        }

    }

    /**
     * V�rification de l'int�grit� m�tier avant suppression
     * 
     * @param periodeAFModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(PeriodeAFModel periodeAFModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // v�rification si la p�riode est bien ouverte, on ne peut supprimer que
        // celles-l�
        if (ALCSProcessus.ETAT_OUVERT.equals(periodeAFModel.getEtat())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(),
                    "al.periode.periodeAFModel.idPeriode.deleteIntegrity.isClosed");
        }

    }

    /**
     * v�rification des donn�es requises
     * 
     * @param periodeAFModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        // date p�riode
        if (JadeStringUtil.isEmpty(periodeAFModel.getDatePeriode())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(),
                    "al.periodeAF.periodeAFModel.datePeriode.mandatory");
        }
        // �tat p�riode
        if (JadeStringUtil.isEmpty(periodeAFModel.getEtat())) {
            JadeThread.logError(PeriodeAFModelChecker.class.getName(), "al.periodeAF.periodeAFModel.etat.mandatory");
        }
    }

    /**
     * validation des donn�es de droitModel
     * 
     * @param periodeAFModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        PeriodeAFModelChecker.checkMandatory(periodeAFModel);
        PeriodeAFModelChecker.checkDatabaseIntegrity(periodeAFModel);
        PeriodeAFModelChecker.checkCodesystemIntegrity(periodeAFModel);
        // Pas d'int�grit� m�tier � v�rifier
        // PeriodeAFModelChecker.checkBusinessIntegrity(periodeAFModel);
    }

    /**
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param periodeAFModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        PeriodeAFModelChecker.checkDeleteIntegrity(periodeAFModel);
    }

}
