package ch.globaz.al.businessimpl.checker.model.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.allocataire.AllocataireModelChecker;
import ch.globaz.al.utils.ALImportUtils;

/**
 * Checker du mod�le <code>EnfantComplexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.droit.EnfantComplexModel
 */
public class EnfantComplexModelChecker extends ALAbstractChecker {

    /**
     * V�rification des donn�es obligatoires
     * 
     * @param model
     *            Mod�le � valider
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkMandatory(EnfantComplexModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // NSS
        if (!ALImportUtils.importFromAlfaGest) {
            // FIXME:model.isNew => permet de modifier un droit sans NSS...d�sactiv�?
            if (model.isNew()
                    && JadeStringUtil.isEmpty(model.getPersonneEtendueComplexModel().getPersonneEtendue()
                            .getNumAvsActuel())) {
                JadeThread.logError(AllocataireModelChecker.class.getName(),
                        "al.droit.enfantComplexModel.nss.mandatory");
            }
        }
    }

    /**
     * Validation des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(EnfantComplexModel model) throws JadePersistenceException, JadeApplicationException {
        EnfantComplexModelChecker.checkMandatory(model);
    }
}
