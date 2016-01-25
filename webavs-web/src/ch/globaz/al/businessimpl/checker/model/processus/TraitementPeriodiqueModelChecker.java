package ch.globaz.al.businessimpl.checker.model.processus;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireModelException;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * 
 * Classe de validation des données de TraitementPeriodiqueModel
 * 
 * @author GMO
 * 
 */
public abstract class TraitementPeriodiqueModelChecker extends ALAbstractChecker {

    /**
     * vérifie l'intégrité des données relatives aux codes systèmes , si non respectée lance un message sur l'intégrité
     * de(s) donnée(s)
     * 
     * @param traitementPeriodiqueModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkCodesystemIntegrity(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // état du traitement périodique

        try {
            if (!JadeNumericUtil.isEmptyOrZero(traitementPeriodiqueModel.getEtat())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSProcessus.GROUP_ETAT_PROCESSUS,
                            traitementPeriodiqueModel.getEtat())) {
                JadeThread.logError(TraitementPeriodiqueModelChecker.class.getName(),
                        "al.processus.traitementPeriodiqueModel.etat.codesystemIntegrity");
            }
            // libellé du traitement
            if (!JadeNumericUtil.isEmptyOrZero(traitementPeriodiqueModel.getTraitementLibelle())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSProcessus.GROUP_NAME_PROCESSUS_TRAITEMENT,
                            traitementPeriodiqueModel.getTraitementLibelle())) {
                JadeThread.logError(TraitementPeriodiqueModelChecker.class.getName(),
                        "al.processus.traitementPeriodiqueModel.traitement.codesystemIntegrity");
            }

        } catch (JadeNoBusinessLogSessionError e) {
            throw new ALAllocataireModelException(
                    "TraitementPeriodiqueModelChecker#checkCodesystemIntegrity : unable to check codes system", e);
        } catch (Exception e) {
            throw new ALAllocataireModelException(
                    "TraitementPeriodiqueModelChecker#checkCodesystemIntegrity : unable to check codes system", e);
        }

    }

    /**
     * vérifie l'intégrité des données relatives à la base de données , si non respectée lance un message sur
     * l'intégrité de(s) donnée(s)
     * 
     * @param traitementPeriodiqueModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadePersistenceException {
        // libellé du traitement
        if (!JadeNumericUtil.isIntegerPositif(traitementPeriodiqueModel.getTraitementLibelle())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.traitement.databaseIntegrity.type");
        }
        // id du processus périodique
        if (!JadeNumericUtil.isIntegerPositif(traitementPeriodiqueModel.getIdProcessusPeriodique())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.idprocessus.databaseIntegrity.type");
        }
        // état du traitement
        if (!JadeNumericUtil.isIntegerPositif(traitementPeriodiqueModel.getEtat())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.etat.databaseIntegrity.type");
        }

        // date du traitement
        if (!JadeStringUtil.isBlank(traitementPeriodiqueModel.getDateExecution())
                && !JadeDateUtil.isGlobazDate(traitementPeriodiqueModel.getDateExecution())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.dateExecution.databaseIntegrity.type");
        }

        // heure du traitement peut-être vide à la création
        // if (!JadeStringUtil.isBlank(traitementPeriodiqueModel
        // .getHeureExecution())
        // ) {
        // JadeThread
        // .logError(ProcessusPeriodiqueModelChecker.class.getName(),
        // "al.processus.traitementPeriodiqueModel.heureExecution.databaseIntegrity.type");
        // }

        // user du traitement peut être vide la création... => pas de ctrl
        // if (!JadeStringUtil.isBlank(traitementPeriodiqueModel
        // .getUserExecution())
        // && !JadeStringUtil.isEmpty(traitementPeriodiqueModel
        // .getUserExecution())) {
        // JadeThread
        // .logError(ProcessusPeriodiqueModelChecker.class.getName(),
        // "al.processus.traitementPeriodiqueModel.userExecution.databaseIntegrity.type");
        // }

    }

    /**
     * Vérification de l'intégrité métier avant suppression
     * 
     * @param traitementPeriodiqueModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        if (!ALCSProcessus.ETAT_OUVERT.equals(traitementPeriodiqueModel.getEtat())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.idTraitement.deleteIntegrity.nonouvert");
        }

    }

    /**
     * vérifie l'obligation des données, si non respectée lance un message sur l'obligation de(s) donnée(s)
     * 
     * @param traitementPeriodiqueModel
     *            Modèle à valider
     */
    private static void checkMandatory(TraitementPeriodiqueModel traitementPeriodiqueModel) {
        // libellé du traitement
        if (JadeStringUtil.isEmpty(traitementPeriodiqueModel.getTraitementLibelle())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.traitement.mandatory");
        }

        // état du traitement périodique
        if (JadeStringUtil.isEmpty(traitementPeriodiqueModel.getEtat())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.etat.mandatory");
        }

        // id du processus périodique
        if (JadeStringUtil.isEmpty(traitementPeriodiqueModel.getIdProcessusPeriodique())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.idprocessus.mandatory");
        }

    }

    /**
     * validation de l'intégrité des données et des règles métier, de l'obligation des données et de l'intégrité des
     * codes systèmes
     * 
     * @param traitementPeriodiqueModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(TraitementPeriodiqueModel traitementPeriodiqueModel) throws JadePersistenceException,
            JadeApplicationException {
        TraitementPeriodiqueModelChecker.checkMandatory(traitementPeriodiqueModel);
        TraitementPeriodiqueModelChecker.checkDatabaseIntegrity(traitementPeriodiqueModel);
        TraitementPeriodiqueModelChecker.checkCodesystemIntegrity(traitementPeriodiqueModel);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param traitementPeriodiqueModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {
        TraitementPeriodiqueModelChecker.checkDeleteIntegrity(traitementPeriodiqueModel);
    }
}
