package ch.globaz.al.businessimpl.checker.model.attribut;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.dossier.CopieModelChecker;

/**
 * Checker du modèle {@link ch.globaz.al.business.models.attribut.AttributEntiteModel}
 * 
 * @author GMO/PTA
 * 
 */
public abstract class AttributEntiteModelChecker extends ALAbstractChecker {

    /**
     * vérifie l'intégrité des données relatives à la base de données , si non respectée lance un message sur
     * l'intégrité de(s) donnée(s)
     * 
     * @param model
     *            Modèle à valider
     */
    private static void checkDatabaseIntegrity(AttributEntiteModel model) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // cleEntite
        if (!JadeNumericUtil.isInteger(model.getCleEntite())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.attribut.attributEntiteModel.cleEntite.databaseIntegrity.type");
        }

        // valeurNum
        if (JadeStringUtil.isEmpty(model.getValeurNum())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.attribut.attributEntiteModel.valeurNum.databaseIntegrity.type");
        }
    }

    /**
     * vérifie l'obligation des données, si non respectée lance un message sur l'obligation de(s) donnée(s)
     * 
     * @param model
     *            Modèle à valider
     */
    private static void checkMandatory(AttributEntiteModel model) {

        // cleEntite
        if (JadeStringUtil.isEmpty(model.getCleEntite())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.attribut.attributEntiteModel.cleEntite.mandatory");
        }

        // typeEntite
        if (JadeStringUtil.isEmpty(model.getTypeEntite())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.attribut.attributEntiteModel.typeEntite.mandatory");
        }

        // nomAttribut
        if (JadeStringUtil.isEmpty(model.getNomAttribut())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.attribut.attributEntiteModel.nomAttribut.mandatory");
        }

        // valeurAlpha
        if (JadeStringUtil.isEmpty(model.getValeurAlpha())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.attribut.attributEntiteModel.valeurAlpha.mandatory");
        }

        // valeurNum
        if (JadeStringUtil.isEmpty(model.getValeurNum())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.attribut.attributEntiteModel.valeurNum.mandatory");
        }
    }

    /**
     * validation de l'intégrité des données et des règles métier, de l'obligation des données et de l'intégrité des
     * codes systèmes
     * 
     * @param attrEntiteModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(AttributEntiteModel attrEntiteModel) throws JadePersistenceException,
            JadeApplicationException {
        AttributEntiteModelChecker.checkMandatory(attrEntiteModel);
        AttributEntiteModelChecker.checkDatabaseIntegrity(attrEntiteModel);
    }

    /**
     * Valide l'intégrité des données avant suppression
     * 
     * @param attrEntiteModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validateForDelete(AttributEntiteModel attrEntiteModel) throws JadePersistenceException,
            JadeApplicationException {
        // DO NOTHING, rien à vérifier avant suppression
    }
}
