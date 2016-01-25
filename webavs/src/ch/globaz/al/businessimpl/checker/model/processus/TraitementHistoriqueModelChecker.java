package ch.globaz.al.businessimpl.checker.model.processus;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.processus.TraitementHistoriqueModel;
import ch.globaz.al.business.models.processus.TraitementHistoriqueSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe de validation des données TraitementHistoriqueModel
 * 
 * @author GMO
 * 
 */
public abstract class TraitementHistoriqueModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégrité métier des données de l'historique
     * 
     * @param traitementHistoriqueModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadePersistenceException, JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        TraitementHistoriqueSearchModel searchModel = new TraitementHistoriqueSearchModel();
        searchModel.setForIdEntite(traitementHistoriqueModel.getCleEntite());
        searchModel.setForIdTraitementPeriodique(traitementHistoriqueModel.getIdTraitementPeriodique());
        searchModel = ALImplServiceLocator.getTraitementHistoriqueModelService().search(searchModel);
        if (searchModel.getSize() > 0) {
            JadeThread.logError(TraitementHistoriqueModelChecker.class.getName(),
                    "al.processus.traitementHistoriqueModel.businessIntegrity.already");
        }

    }

    /**
     * vérifie l'intégrité des codes système
     * 
     * @param traitementHistoriqueModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadeApplicationException, JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * vérifie l'intégrité des données, si non respectée lance un message sur l'intégrité
     * 
     * @param traitementHistoriqueModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadeApplicationException, JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * Effectue les vérifications
     * 
     * @param traitementHistoriqueModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * vérifie l'obligation des données si non respectée lance un message sur l'obligation
     * 
     * @param traitementHistoriqueModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadeApplicationException, JadePersistenceException {

        // id
        if (JadeStringUtil.isEmpty(traitementHistoriqueModel.getId())) {
            JadeThread.logError(TraitementHistoriqueModelChecker.class.getName(),
                    "al.processus.traitementHistoriqueModel.id.mandatory");
        }
        // id traitement périodique
        if (JadeStringUtil.isEmpty(traitementHistoriqueModel.getIdTraitementPeriodique())) {
            JadeThread.logError(TraitementHistoriqueModelChecker.class.getName(),
                    "al.processus.traitementHistoriqueModel.idTraitementPeriodique.mandatory");
        }
        // id entite
        if (JadeStringUtil.isEmpty(traitementHistoriqueModel.getCleEntite())) {
            JadeThread.logError(TraitementHistoriqueModelChecker.class.getName(),
                    "al.processus.traitementHistoriqueModel.identite.mandatory");
        }
        // type entite
        if (JadeStringUtil.isEmpty(traitementHistoriqueModel.getTypeEntite())) {
            JadeThread.logError(TraitementHistoriqueModelChecker.class.getName(),
                    "al.processus.traitementHistoriqueModel.typeEntite.mandatory");
        }
        // status
        if (JadeStringUtil.isEmpty(traitementHistoriqueModel.getStatus())) {
            JadeThread.logError(TraitementHistoriqueModelChecker.class.getName(),
                    "al.processus.traitementHistoriqueModel.status.mandatory");
        }

    }

    /**
     * valide l'intégité et l'obligation des données
     * 
     * @param traitementHistoriqueModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(TraitementHistoriqueModel traitementHistoriqueModel) throws JadeApplicationException,
            JadePersistenceException {
        TraitementHistoriqueModelChecker.checkMandatory(traitementHistoriqueModel);
        TraitementHistoriqueModelChecker.checkDatabaseIntegrity(traitementHistoriqueModel);
        TraitementHistoriqueModelChecker.checkCodesystemIntegrity(traitementHistoriqueModel);
        TraitementHistoriqueModelChecker.checkBusinessIntegrity(traitementHistoriqueModel);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param traitementHistoriqueModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validateForDelete(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadePersistenceException, JadeApplicationException {
        TraitementHistoriqueModelChecker.checkDeleteIntegrity(traitementHistoriqueModel);
    }

}
