/**
 * 
 */
package ch.globaz.al.businessimpl.checker.model.tarif;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.model.tarif.ALCategorieTarifModelException;
import ch.globaz.al.business.models.tarif.CritereTarifModel;
import ch.globaz.al.business.models.tarif.CritereTarifSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validation des règles d'intégrrité de CritereTarifModel
 * 
 * @author PTA
 * 
 */
public class CritereTarifModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégrité métier des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    private static void checkBusinessIntegrity(CritereTarifModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // deux critères identiques ne peuvent exister
        CritereTarifSearchModel cts = new CritereTarifSearchModel();
        cts.setForCritereTarif(model.getCritereTarif());
        cts.setForDebut(model.getDebutCritere());
        cts.setForFin(model.getFinCritere());

        if (ALImplServiceLocator.getCritereTarifModelService().count(cts) > 0) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.categorieTarif.businessIntegrity.alreadyExisting");
        }
    }

    /**
     * vérification des Codes Systèmes
     * 
     * @param critereTarifModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkCodeSystemIntegrity(CritereTarifModel critereTarifModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            // code système de critère du tarif
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CRITERE, critereTarifModel.getCritereTarif())) {
                JadeThread.logError(CritereTarifModelChecker.class.getName(),
                        "al.tarif.critereTarifModel.critereTarif.codeSystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALCategorieTarifModelException(
                    "CritereTarifModel problem during checking codes system integrity", e);
        }
    }

    /**
     * vérification de la validité des données
     * 
     * @param critereTarifModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    private static void checkDatabaseIntegrity(CritereTarifModel critereTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // type de données du critère du tarif
        if (!JadeNumericUtil.isIntegerPositif(critereTarifModel.getCritereTarif())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.critereTarif.databaseIntegrity.type");
        }
        // type de données du début du critère
        if (!JadeNumericUtil.isInteger(critereTarifModel.getDebutCritere())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.debutCritere.type");
        }

        // type de données de fin du critère
        if (!JadeNumericUtil.isInteger(critereTarifModel.getFinCritere())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(), "al.tarif.critereTarifModel.finCritere.type");
        }

    }

    /**
     * vérification des données obligatoires
     * 
     * @param critereTarifModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    private static void checkMandatory(CritereTarifModel critereTarifModel) throws JadePersistenceException,
            JadeApplicationException {

        // obligation du critère du tarif
        if (JadeStringUtil.isEmpty(critereTarifModel.getCritereTarif())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.critereTarif.mandatory");
        }
        // obligation du début du critère
        if (JadeStringUtil.isEmpty(critereTarifModel.getDebutCritere())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.debutCritere.mandatory");
        }
        // obligation du critère de fin
        if (JadeStringUtil.isEmpty(critereTarifModel.getFinCritere())) {
            JadeThread.logError(CritereTarifModelChecker.class.getName(),
                    "al.tarif.critereTarifModel.finCritere.mandatory");
        }
    }

    /**
     * validation des critères tarif selon le modèle passé en paramètre
     * 
     * @param critereTarifModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    public static void validate(CritereTarifModel critereTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        CritereTarifModelChecker.checkMandatory(critereTarifModel);
        CritereTarifModelChecker.checkDatabaseIntegrity(critereTarifModel);
        CritereTarifModelChecker.checkCodeSystemIntegrity(critereTarifModel);
        CritereTarifModelChecker.checkBusinessIntegrity(critereTarifModel);
    }

}
