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
 * Checker du mod�le {@link ch.globaz.al.business.models.attribut.AttributEntiteModel}
 * 
 * @author GMO/PTA
 * 
 */
public abstract class AttributEntiteModelChecker extends ALAbstractChecker {

    /**
     * v�rifie l'int�grit� des donn�es relatives � la base de donn�es , si non respect�e lance un message sur
     * l'int�grit� de(s) donn�e(s)
     * 
     * @param model
     *            Mod�le � valider
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
     * v�rifie l'obligation des donn�es, si non respect�e lance un message sur l'obligation de(s) donn�e(s)
     * 
     * @param model
     *            Mod�le � valider
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
     * validation de l'int�grit� des donn�es et des r�gles m�tier, de l'obligation des donn�es et de l'int�grit� des
     * codes syst�mes
     * 
     * @param attrEntiteModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(AttributEntiteModel attrEntiteModel) throws JadePersistenceException,
            JadeApplicationException {
        AttributEntiteModelChecker.checkMandatory(attrEntiteModel);
        AttributEntiteModelChecker.checkDatabaseIntegrity(attrEntiteModel);
    }

    /**
     * Valide l'int�grit� des donn�es avant suppression
     * 
     * @param attrEntiteModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validateForDelete(AttributEntiteModel attrEntiteModel) throws JadePersistenceException,
            JadeApplicationException {
        // DO NOTHING, rien � v�rifier avant suppression
    }
}
