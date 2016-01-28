package ch.globaz.al.businessimpl.checker.model.tarif;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.model.tarif.ALLegislationTarifModelException;
import ch.globaz.al.business.models.tarif.LegislationTarifModel;
import ch.globaz.al.business.models.tarif.LegislationTarifSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validation des données de LegislationTarif
 * 
 * @author PTA
 * 
 */
public class LegislationTarifModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégrité métier des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkBusinessIntegrity(LegislationTarifModel model) throws JadeApplicationException,
            JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // deux législations ne peuvent utiliser le même type, on vérifie si
        // c'est le cas
        LegislationTarifSearchModel lts = new LegislationTarifSearchModel();
        lts.setForTypeLegislation(model.getTypeLegislation());
        if (ALImplServiceLocator.getLegislationTarifModelService().count(lts) > 0) {
            JadeThread.logError(LegislationTarifModelChecker.class.getName(),
                    "al.tarif.legislationTarifModel.typeLegislation.businessIntegrity.alreadyExisting");
        }
    }

    /**
     * Vérification de l'intégrité des codes système
     * 
     * @param legislationTarifModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodeSystemIntegrity(LegislationTarifModel legislationTarifModel)
            throws JadePersistenceException, JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_LEGISLATION,
                    legislationTarifModel.getTypeLegislation())) {
                JadeThread.logError(LegislationTarifModelChecker.class.getName(),
                        "al.tarif.legislationTarifModel.typeLegislation.codeSystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALLegislationTarifModelException(
                    "LegislationTarifModel problem during checking codes system integrity", e);
        }

    }

    /**
     * 
     * @param legislationTarifModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(LegislationTarifModel legislationTarifModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // type de données de type de législation du tarif
        if (!JadeNumericUtil.isIntegerPositif(legislationTarifModel.getTypeLegislation())) {
            JadeThread.logError(LegislationTarifModelChecker.class.getName(),
                    "al.tarif.legislationTarifModel.typeLegislation.databaseIntegrity.type");
        }
    }

    /**
     * 
     * @param legislationTarifModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(LegislationTarifModel legislationTarifModel) throws JadePersistenceException,
            JadeApplicationException {

        // obligation du type de législation
        if (JadeStringUtil.isEmpty(legislationTarifModel.getTypeLegislation())) {
            JadeThread.logError(LegislationTarifModelChecker.class.getName(),
                    "al.tarif.legislationTarifModel.typeLegislation.mandatory");
        }

    }

    /**
     * Vérification de l'intégrité des données
     * 
     * @param legislationTarifModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(LegislationTarifModel legislationTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        LegislationTarifModelChecker.checkMandatory(legislationTarifModel);
        LegislationTarifModelChecker.checkDatabaseIntegrity(legislationTarifModel);
        LegislationTarifModelChecker.checkCodeSystemIntegrity(legislationTarifModel);
        LegislationTarifModelChecker.checkBusinessIntegrity(legislationTarifModel);
    }

}
