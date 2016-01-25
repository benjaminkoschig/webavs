package ch.globaz.al.businessimpl.checker.model.allocataire;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.allocataire.AllocataireSearchModel;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validation des données de revenuModel
 * 
 * @author PTA
 * 
 */
public abstract class RevenuModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégrité métier des données
     * 
     * @param revenuModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(RevenuModel revenuModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // on vérifie que l'id de l'allocataire fourni correspond bien à un
        // allocataire existant en base de données
        AllocataireSearchModel sa = new AllocataireSearchModel();
        sa.setForIdAllocataire(revenuModel.getIdAllocataire());
        if (0 == ALImplServiceLocator.getAllocataireModelService().count(sa)) {
            JadeThread.logError(RevenuModelChecker.class.getName(),
                    "al.allocataire.revenuModel.idAllocataire.businessIntegrity.ExistingId");
        }
    }

    /**
     * vérification de l'intégrité des données de la base
     * 
     * @param revenuModel
     *            Modèle à valider
     */
    private static void checkDatabaseIntegrity(RevenuModel revenuModel) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id de l'allocataire
        if (!JadeNumericUtil.isIntegerPositif(revenuModel.getIdAllocataire())) {
            JadeThread.logError(RevenuModelChecker.class.getName(),
                    "al.allocataire.revenuModel.idAllocataire.databaseIntegrity.type");
        }

        // montant
        if (!JadeNumericUtil.isNumericPositif(revenuModel.getMontant())
                && !JadeNumericUtil.isZeroValue(revenuModel.getMontant())) {
            JadeThread.logError(RevenuModelChecker.class.getName(),
                    "al.allocataire.revenuModel.montant.databaseIntegrity.type");

        }

        // date
        if (!JadeDateUtil.isGlobazDate(revenuModel.getDate())) {
            JadeThread.logError(RevenuModelChecker.class.getName(),
                    "al.allocataire.revenuModel.date.databaseIntegrity.dateFormat");
        }
    }

    /**
     * vérification des données requises
     * 
     * @param revenuModel
     *            Modèle à valider
     */
    private static void checkMandatory(RevenuModel revenuModel) {

        // id de l'allocataire
        if (JadeStringUtil.isEmpty(revenuModel.getIdAllocataire())) {
            JadeThread.logError(RevenuModelChecker.class.getName(),
                    "al.allocataire.revenuModel.idAllocataire.mandatory");
        }

        // date
        if (JadeStringUtil.isEmpty(revenuModel.getDate())) {
            JadeThread.logError(RevenuModelChecker.class.getName(), "al.allocataire.revenuModel.date.mandatory");
        }

        // montant
        if (JadeStringUtil.isEmpty(revenuModel.getMontant())) {
            JadeThread.logError(RevenuModelChecker.class.getName(), "al.allocataire.revenuModel.montant.mandatory");
        }

        // indication "revenu du conjoint"
        if (revenuModel.getRevenuConjoint() == null) {
            JadeThread.logError(RevenuModelChecker.class.getName(),
                    "al.allocataire.revenuModel.revenuConjoint.mandatory");
        }

        // indication "revenu IFD"
        if (revenuModel.getRevenuIFD() == null) {
            JadeThread.logError(RevenuModelChecker.class.getName(), "al.allocataire.revenuModel.revenuIFD.mandatory");
        }
    }

    /**
     * validation de l'obligation et de l'intégrité des données
     * 
     * @param revenuModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(RevenuModel revenuModel) throws JadePersistenceException, JadeApplicationException {
        RevenuModelChecker.checkMandatory(revenuModel);
        RevenuModelChecker.checkDatabaseIntegrity(revenuModel);
        RevenuModelChecker.checkBusinessIntegrity(revenuModel);
    }
}
