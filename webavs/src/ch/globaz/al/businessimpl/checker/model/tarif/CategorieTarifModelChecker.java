package ch.globaz.al.businessimpl.checker.model.tarif;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.model.tarif.ALCategorieTarifModelException;
import ch.globaz.al.business.models.tarif.CategorieTarifModel;
import ch.globaz.al.business.models.tarif.CategorieTarifSearchModel;
import ch.globaz.al.business.models.tarif.LegislationTarifSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validité des données de CategrotieTarifModel
 * 
 * @author PTA
 * 
 */
public abstract class CategorieTarifModelChecker extends ALAbstractChecker {

    /**
     * vérifier l’intégrité des données au niveau métier AF categorietarif
     * 
     * @param categorieTarifModel
     *            Modèle à valider
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    private static void checkBusinessIntegrity(CategorieTarifModel categorieTarifModel)
            throws JadeApplicationException, JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // vérifie que l'idLegislation fourni existe bien dans la table
        // Legislation Tarif
        LegislationTarifSearchModel sl = new LegislationTarifSearchModel();
        sl.setForIdLegislationTarif(categorieTarifModel.getIdLegislation());
        if (ALImplServiceLocator.getLegislationTarifModelService().count(sl) == 0) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.idLegislation.businessIntegrity.existingId");
        }

        // deux catégories de même type et de même législation ne peuvent
        // exister
        CategorieTarifSearchModel cts = new CategorieTarifSearchModel();
        cts.setForCategorieTarif(categorieTarifModel.getCategorieTarif());
        cts.setForIdCategorieTarif(categorieTarifModel.getIdCategorieTarif());
        if (ALImplServiceLocator.getCategorieTarifModelService().count(cts) > 0) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.categorieTarif.businessIntegrity.alreadyExisting");
        }
    }

    /**
     * Vérification de l'intégrité des codes système
     * 
     * @param categorieTarifModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkCodeSystemIntegrity(CategorieTarifModel categorieTarifModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {

            // contrôle de catégorie de tarif
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE,
                    categorieTarifModel.getCategorieTarif())) {
                JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                        "al.tarif.categorieTarifModel.categorieTarif.codesystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALCategorieTarifModelException(
                    "CategorieTarifModel problem during checking codes system integrity", e);
        }
    }

    /**
     * vérifie l'intégrité des données de TarifModel, si non respectée lance un message sur l'intégrité de ces données
     * 
     * @param categorieTarifModel
     *            Modèle à valider
     */
    private static void checkDatabaseIntegrity(CategorieTarifModel categorieTarifModel) {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // contrôle type catégorie du tarif
        if (!JadeNumericUtil.isIntegerPositif(categorieTarifModel.getCategorieTarif())) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.categorieTarif.databaseIntegrity.type");

        }
        // contrôle type l'identifiant de la prestation du tarif
        if (!JadeNumericUtil.isIntegerPositif(categorieTarifModel.getIdLegislation())) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.idLegislation.databaseIntegrity.type");
        }

    }

    /**
     * * vérifie l'obligation des données de TarifModel, si non respectée lance un message sur l'intégrité de ces
     * données
     * 
     * @param categorieTarifModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkMandatory(CategorieTarifModel categorieTarifModel) throws JadeApplicationException {

        // obligation de catégorie de tarif
        if (JadeStringUtil.isEmpty(categorieTarifModel.getCategorieTarif())) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.categorieTarif.mandatory.");
        }
        // obligation de l'identifiant de la legislation du tarif
        if (JadeStringUtil.isEmpty(categorieTarifModel.getIdLegislation())) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.categorieTarifModel.idLegislation.mandatory");
        }

    }

    /**
     * valide les données de TarifModel
     * 
     * @param categorieTarifModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(CategorieTarifModel categorieTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        CategorieTarifModelChecker.checkMandatory(categorieTarifModel);
        CategorieTarifModelChecker.checkDatabaseIntegrity(categorieTarifModel);
        CategorieTarifModelChecker.checkCodeSystemIntegrity(categorieTarifModel);
        CategorieTarifModelChecker.checkBusinessIntegrity(categorieTarifModel);
    }
}
