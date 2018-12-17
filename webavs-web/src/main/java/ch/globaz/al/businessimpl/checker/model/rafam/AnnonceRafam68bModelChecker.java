package ch.globaz.al.businessimpl.checker.model.rafam;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;

/**
 * classe de validation des donn�es de {@link ch.globaz.al.business.models.rafam.AnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class AnnonceRafam68bModelChecker extends AnnonceRafam68aModelChecker {

    /**
     * V�rifie l'int�grit�e "business" des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected static void checkBusinessIntegrity(AnnonceRafamModel model) throws JadePersistenceException,
            JadeApplicationException {
        // DO NOTHING
    }

    /**
     * V�rification des codesSystems
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkCodesystemIntegrity(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        // DO NOTHING
    }

    /**
     * v�rification de l'int�grit� des donn�es
     * 
     * @param model
     *            Mod�le � valider
     */
    protected static void checkDatabaseIntegrity(AnnonceRafamModel model) {
        // DO NOTHING
    }

    /**
     * v�rification des donn�es requises
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkMandatory(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        AnnonceRafam68aModelChecker.checkMandatory(model);

        if (JadeNumericUtil.isEmptyOrZero(model.getRecordNumber())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.recordNumber.mandatory");
        }

        if (JadeStringUtil.isBlank(model.getOfficeIdentifier())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.officeIdentifier.mandatory");
        }

        if (JadeStringUtil.isBlank(model.getLegalOffice())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.legalOffice.mandatory");
        }

//        if (JadeStringUtil.isBlank(model.getCodeCentralePaysEnfant())) {
//            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
//                    "al.rafam.annonceRafamModel.codeCentralePaysEnfant.mandatory");
//        }
    }
}
