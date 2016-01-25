package ch.globaz.al.businessimpl.checker.model.rafam;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * classe de validation des donn�es de {@link ch.globaz.al.business.models.rafam.ErreurErreurAnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class ErreurAnnonceRafamModelChecker extends ALAbstractChecker {

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
    protected static void checkBusinessIntegrity(ErreurAnnonceRafamModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification de l'existence de l'annonce
        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForIdAnnonce(model.getIdAnnonce());
        if (0 == ALServiceLocator.getAnnonceRafamModelService().count(search)) {
            JadeThread.logError(ErreurAnnonceRafamModelChecker.class.getName(),
                    "al.rafam.ErreurAnnonceRafamModel.idDroit.businessIntegrity.ExistingId");
        }
    }

    /**
     * v�rification de l'int�grit� des donn�es
     * 
     * @param model
     *            Mod�le � valider
     */
    protected static void checkDatabaseIntegrity(ErreurAnnonceRafamModel model) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // idAnnonce
        if (!JadeNumericUtil.isEmptyOrZero(model.getIdAnnonce())
                && !JadeNumericUtil.isNumericPositif(model.getIdAnnonce())) {
            JadeThread.logError(ErreurAnnonceRafamModelChecker.class.getName(),
                    "al.rafam.ErreurAnnonceRafamModel.idAnnonce.databaseIntegrity.type");
        }

        // code
        if (!JadeStringUtil.isEmpty(model.getCode()) && !JadeNumericUtil.isInteger(model.getCode())) {
            JadeThread.logError(ErreurAnnonceRafamModelChecker.class.getName(),
                    "al.rafam.ErreurAnnonceRafamModel.code.databaseIntegrity.type");
        }
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
    protected static void checkMandatory(ErreurAnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {

        // idAnnonce
        if (JadeStringUtil.isEmpty(model.getIdAnnonce())) {
            JadeThread.logError(ErreurAnnonceRafamModelChecker.class.getName(),
                    "al.rafam.ErreurAnnonceRafamModel.idAnnonce.mandatory");
        }

        // code
        if (JadeStringUtil.isEmpty(model.getCode())) {
            JadeThread.logError(ErreurAnnonceRafamModelChecker.class.getName(),
                    "al.rafam.ErreurAnnonceRafamModel.code.mandatory");
        }
    }

    /**
     * validation des donn�es de droitModel
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(ErreurAnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {

        ErreurAnnonceRafamModelChecker.checkMandatory(model);
        ErreurAnnonceRafamModelChecker.checkDatabaseIntegrity(model);
        ErreurAnnonceRafamModelChecker.checkBusinessIntegrity(model);
    }

    /**
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(ErreurAnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        // NOTHING TO CHECK
    }
}