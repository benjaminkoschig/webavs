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
 * Classe de validation des donn�es TraitementHistoriqueModel
 * 
 * @author GMO
 * 
 */
public abstract class TraitementHistoriqueModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit� m�tier des donn�es de l'historique
     * 
     * @param traitementHistoriqueModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * v�rifie l'int�grit� des codes syst�me
     * 
     * @param traitementHistoriqueModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadeApplicationException, JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * v�rifie l'int�grit� des donn�es, si non respect�e lance un message sur l'int�grit�
     * 
     * @param traitementHistoriqueModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadeApplicationException, JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * Effectue les v�rifications
     * 
     * @param traitementHistoriqueModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

    }

    /**
     * v�rifie l'obligation des donn�es si non respect�e lance un message sur l'obligation
     * 
     * @param traitementHistoriqueModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadeApplicationException, JadePersistenceException {

        // id
        if (JadeStringUtil.isEmpty(traitementHistoriqueModel.getId())) {
            JadeThread.logError(TraitementHistoriqueModelChecker.class.getName(),
                    "al.processus.traitementHistoriqueModel.id.mandatory");
        }
        // id traitement p�riodique
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
     * valide l'int�git� et l'obligation des donn�es
     * 
     * @param traitementHistoriqueModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param traitementHistoriqueModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validateForDelete(TraitementHistoriqueModel traitementHistoriqueModel)
            throws JadePersistenceException, JadeApplicationException {
        TraitementHistoriqueModelChecker.checkDeleteIntegrity(traitementHistoriqueModel);
    }

}
