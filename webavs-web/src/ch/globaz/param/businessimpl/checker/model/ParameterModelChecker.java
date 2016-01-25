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
     * V�rification de l'int�grit� business des donn�es
     * 
     * @param parameterModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ParamAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * v�rifie l'int�grit� des donn�es relatives aux codes syst�mes , si non respect�e lance un message sur l'int�grit�
     * de(s) donn�e(s)
     * 
     * @para parameterModel Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkCodesystemIntegrity(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ParamAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * v�rifie l'int�grit� des donn�es relatives � la base de donn�es , si non respect�e lance un message sur
     * l'int�grit� de(s) donn�e(s)
     * 
     * @param parameterModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(ParameterModel parameterModel) throws JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // date validit�

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
     * V�rification de l'int�grit� des donn�es pour la suppression d'un param�tre
     * 
     * @param parameterModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkDeleteIntegrity(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {

        // aucune v�rification
    }

    /**
     * v�rifie l'obligation des donn�es, si non respect�e lance un message sur l'obligation de(s) donn�e(s)
     * 
     * @param parameterModel
     *            Mod�le � valider
     */
    private static void checkMandatory(ParameterModel parameterModel) {

        // parameter

        // cl� (nom) du param�tre
        if (JadeStringUtil.isEmpty(parameterModel.getIdCleDiffere())) {
            JadeThread.logError(ParameterModelChecker.class.getName(),
                    "param.parameter.parameterModel.idCleDiffere.mandatory");
        }

        // id application du param�tre
        if (JadeStringUtil.isEmpty(parameterModel.getIdApplParametre())) {
            JadeThread.logError(ParameterModelChecker.class.getName(),
                    "param.parameter.parameterModel.idApplParametre.mandatory");
        }

        // date de validit� du param�tre
        if (JadeStringUtil.isEmpty(parameterModel.getDateDebutValidite())) {
            JadeThread.logError(ParameterModelChecker.class.getName(),
                    "param.parameter.parameterModel.dateDebutValidite.mandatory");
        }
    }

    /**
     * validation de l'int�grit� des donn�es et des r�gles m�tier, de l'obligation des donn�es et de l'int�grit� des
     * codes syst�mes
     * 
     * @param parameterModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {
        ParameterModelChecker.checkMandatory(parameterModel);
        ParameterModelChecker.checkDatabaseIntegrity(parameterModel);
        ParameterModelChecker.checkCodesystemIntegrity(parameterModel);
        ParameterModelChecker.checkBusinessIntegrity(parameterModel);
    }

    /**
     * Valide l'int�grit� des donn�es avant suppression
     * 
     * @param parameterModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validateForDelete(ParameterModel parameterModel) throws JadePersistenceException,
            JadeApplicationException {
        ParameterModelChecker.checkDeleteIntegrity(parameterModel);
    }
}
