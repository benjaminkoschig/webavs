package ch.globaz.param.businessimpl.checker.model;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.businessimpl.checker.ParamAbstractChecker;

public class ParameterModelChecker extends ParamAbstractChecker {
    /**
     * Vérification de l'intégrité business des données
     * 
     * @param parameterModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ParamAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * vérifie l'intégrité des données relatives aux codes systèmes , si non respectée lance un message sur l'intégrité
     * de(s) donnée(s)
     * 
     * @para parameterModel Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkCodesystemIntegrity(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ParamAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * vérifie l'intégrité des données relatives à la base de données , si non respectée lance un message sur
     * l'intégrité de(s) donnée(s)
     * 
     * @param parameterModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(ParameterModel parameterModel) throws JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // date validité

        if (!JadeDateUtil.isGlobazDate(parameterModel.getDateDebutValidite())) {
            JadeThread.logError(ParameterModelChecker.class.getName(),
                    "param.parameter.parameterModel.dateDebutValidity.databaseIntegrity.type");
        }

        if (!JadeNumericUtil.isNumeric(parameterModel.getValeurNumParametre())) {
            JadeThread.logError(ParameterModelChecker.class.getName(),
                    "param.parameter.parameterModel.valeurNumParametre.databaseIntegrity.type");
        }

    }

    /**
     * Vérification de l'intégrité des données pour la suppression d'un paramètre
     * 
     * @param parameterModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkDeleteIntegrity(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {

        // aucune vérification
    }

    /**
     * vérifie l'obligation des données, si non respectée lance un message sur l'obligation de(s) donnée(s)
     * 
     * @param parameterModel
     *            Modèle à valider
     */
    private static void checkMandatory(ParameterModel parameterModel) {

        // parameter

        // clé (nom) du paramètre
        if (JadeStringUtil.isEmpty(parameterModel.getIdCleDiffere())) {
            JadeThread.logError(ParameterModelChecker.class.getName(),
                    "param.parameter.parameterModel.idCleDiffere.mandatory");
        }

        // id application du paramètre
        if (JadeStringUtil.isEmpty(parameterModel.getIdApplParametre())) {
            JadeThread.logError(ParameterModelChecker.class.getName(),
                    "param.parameter.parameterModel.idApplParametre.mandatory");
        }

        // date de validité du paramètre
        if (JadeStringUtil.isEmpty(parameterModel.getDateDebutValidite())) {
            JadeThread.logError(ParameterModelChecker.class.getName(),
                    "param.parameter.parameterModel.dateDebutValidite.mandatory");
        }
    }

    /**
     * validation de l'intégrité des données et des règles métier, de l'obligation des données et de l'intégrité des
     * codes systèmes
     * 
     * @param parameterModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {
        ParameterModelChecker.checkMandatory(parameterModel);
        ParameterModelChecker.checkDatabaseIntegrity(parameterModel);
        ParameterModelChecker.checkCodesystemIntegrity(parameterModel);
        ParameterModelChecker.checkBusinessIntegrity(parameterModel);
    }

    /**
     * Valide l'intégrité des données avant suppression
     * 
     * @param parameterModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validateForDelete(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {
        ParameterModelChecker.checkDeleteIntegrity(parameterModel);
    }
}
