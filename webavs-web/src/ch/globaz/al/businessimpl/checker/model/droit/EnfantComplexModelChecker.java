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
 * Checker du modèle <code>EnfantComplexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.droit.EnfantComplexModel
 */
public class EnfantComplexModelChecker extends ALAbstractChecker {

    /**
     * Vérification des données obligatoires
     * 
     * @param model
     *            Modèle à valider
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkMandatory(EnfantComplexModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // NSS
        if (!ALImportUtils.importFromAlfaGest) {
            // FIXME:model.isNew => permet de modifier un droit sans NSS...désactivé?
            if (model.isNew()
                    && JadeStringUtil.isEmpty(model.getPersonneEtendueComplexModel().getPersonneEtendue()
                            .getNumAvsActuel())) {
                JadeThread.logError(AllocataireModelChecker.class.getName(),
                        "al.droit.enfantComplexModel.nss.mandatory");
            }
        }
    }

    /**
     * Validation des données
     * 
     * @param model
     *            Modèle à valider
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(EnfantComplexModel model) throws JadePersistenceException, JadeApplicationException {
        EnfantComplexModelChecker.checkMandatory(model);
    }
}
