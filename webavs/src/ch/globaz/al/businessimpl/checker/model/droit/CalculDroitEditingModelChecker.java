/**
 * 
 */
package ch.globaz.al.businessimpl.checker.model.droit;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.droit.CalculDroitEditingModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe de validation des données de CalculDroitBusinessModel
 * 
 * @author pta
 * 
 */

public abstract class CalculDroitEditingModelChecker extends ALAbstractChecker {

    /**
     * Vérification de l'intégrité business des données
     * 
     * @param calculDroiBusinesModel
     *            modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    private static void checkBusinessIntegrity(CalculDroitEditingModel calculDroiBusinesModel)
            throws JadeApplicationException, JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // vérification de l'existence du droit
        DroitSearchModel droit = new DroitSearchModel();
        droit.setForIdDroit(calculDroiBusinesModel.getIdDroit());
        if (0 == ALImplServiceLocator.getDroitModelService().count(droit)) {
            JadeThread.logError(CalculDroitEditingModelChecker.class.getName(),
                    "al.droit.calculDroitEditingModel.idDroit.businessIntegrity");
        }

    }

    /**
     * Contrôle intégrité des données
     * 
     * @param calculDroiBusinesModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDataBaseIntegrity(CalculDroitEditingModel calculDroiBusinesModel)
            throws JadeApplicationException, JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id du droit
        if (!JadeNumericUtil.isIntegerPositif(calculDroiBusinesModel.getIdDroit())) {
            JadeThread.logError(CalculDroitEditingModelChecker.class.getName(),
                    "al.droit.calculDroitEditingModel.idDroit.integrity");
        }
        // montant effectif
        if (!JadeStringUtil.isEmpty(calculDroiBusinesModel.getMontantResultEffectif())
                && !JadeNumericUtil.isNumeric(calculDroiBusinesModel.getMontantResultEffectif())) {
            JadeThread.logError(CalculDroitEditingModelChecker.class.getName(),
                    "al.droit.calculDroitEditingModel.montantResultEffectif.integrity");
        }

        // montant autre parent
        if (!JadeStringUtil.isEmpty(calculDroiBusinesModel.getMontantAutreParent())
                && !JadeNumericUtil.isNumeric(calculDroiBusinesModel.getMontantAutreParent())) {
            JadeThread.logError(CalculDroitEditingModelChecker.class.getName(),
                    "al.droit.calculDroitEditingModel.montantAutreParent.integrity");
        }
        // monant allocataire
        if (!JadeStringUtil.isEmpty(calculDroiBusinesModel.getMontantAllocataire())
                && !JadeNumericUtil.isNumeric(calculDroiBusinesModel.getMontantAllocataire())) {
            JadeThread.logError(CalculDroitEditingModelChecker.class.getName(),
                    "al.droit.calculDroitEditingModel.montantAllocataire.integrity");
        }
    }

    /**
     * Vérification des paramètres obligatoires
     * 
     * @param calculDroiBusinesModel
     *            modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(CalculDroitEditingModel calculDroiBusinesModel) throws JadeApplicationException,
            JadePersistenceException {
        // id du drfoit
        if (JadeStringUtil.isEmpty(calculDroiBusinesModel.getIdDroit())) {
            JadeThread.logError(CalculDroitEditingModelChecker.class.getName(),
                    "al.droit.calculDroitEditingModel.idDroit.mandatory");
        }
        // isHide
        if (null == calculDroiBusinesModel.getIsHide()) {
            JadeThread.logError(CalculDroitEditingModelChecker.class.getName(),
                    "al.droit.calculDroitEditingModel.isHide.mandatory");
        }
    }

    /**
     * Exécute les vérifications nécessaires avant l'enregistement du modèle
     * 
     * @param calculDroiBusinesModel
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */

    public static void validate(CalculDroitEditingModel calculDroiBusinesModel) throws JadeApplicationException,
            JadePersistenceException {
        CalculDroitEditingModelChecker.checkMandatory(calculDroiBusinesModel);
        CalculDroitEditingModelChecker.checkDataBaseIntegrity(calculDroiBusinesModel);
        CalculDroitEditingModelChecker.checkBusinessIntegrity(calculDroiBusinesModel);

    }
}
