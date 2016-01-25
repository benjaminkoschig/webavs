package ch.globaz.al.businessimpl.checker.model.processus;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireModelException;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueSearchModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.musca.business.constantes.FACSPassage;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.services.FABusinessServiceLocator;

/**
 * 
 * Classe de validation des données de ProcessusPeriodiqueModel
 * 
 * @author GMO
 * 
 */
public abstract class ProcessusPeriodiqueModelChecker extends ALAbstractChecker {

    private static void checkBusinessIntegrity(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // ctrl que dans le cas d'update
        if (!processusPeriodiqueModel.isNew()) {

            ProcessusPeriodiqueModel processusInDB = ALServiceLocator.getProcessusPeriodiqueModelService().read(
                    processusPeriodiqueModel.getId());

            // si il est fermé on peut plus modifier
            if (ALCSProcessus.ETAT_TERMINE.equals(processusInDB.getEtat())) {
                JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                        "al.processus.processusPeriodiqueModel.etat.businessIntegrity.verrouille");
            }

            TraitementPeriodiqueSearchModel searchTraitements = new TraitementPeriodiqueSearchModel();
            searchTraitements.setForIdProcessusPeriodique(processusPeriodiqueModel.getId());
            searchTraitements = ALServiceLocator.getTraitementPeriodiqueModelService().search(searchTraitements);
            // si il y un traitement de préparation déjà fermé ou en cours, on ne peut plus changer de passage
            for (int i = 0; i < searchTraitements.getSize(); i++) {
                TraitementPeriodiqueModel currentTraitement = (TraitementPeriodiqueModel) searchTraitements
                        .getSearchResults()[i];

                if (ALCSProcessus.NAME_TRAITEMENT_PREPARATION_COMPENSATION.equals(currentTraitement
                        .getTraitementLibelle())
                        && (ALCSProcessus.ETAT_TERMINE.equals(currentTraitement.getEtat()) || ALCSProcessus.ETAT_ENCOURS
                                .equals(currentTraitement.getEtat()))) {

                    // on vérifie si il y a eu un changement DB
                    if (!processusInDB.getIdPassageFactu().equals(processusPeriodiqueModel.getIdPassageFactu())) {
                        JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                                "al.processus.processusPeriodiqueModel.idPassageFactu.businessIntegrity.verrouille");
                    }

                }
            }

            // test si le passage n'est pas déjà lié à un autre processus
            // car règle => 1 processus || 1 passage

            ProcessusPeriodiqueSearchModel searchProcessus = new ProcessusPeriodiqueSearchModel();
            searchProcessus.setForIdPassageFactu(processusPeriodiqueModel.getIdPassageFactu());
            searchProcessus.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            if (!JadeNumericUtil.isEmptyOrZero(processusPeriodiqueModel.getIdPassageFactu())) {
                searchProcessus = ALServiceLocator.getProcessusPeriodiqueModelService().search(searchProcessus);
            }

            if (searchProcessus.getSize() > 1) {
                JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                        "al.processus.processusPeriodiqueModel.idPassageFactu.businessIntegrity.utilise");
            }

            // si c'est le processus principal, on doit pas affecter un passage, car le périodique prend que le
            // processus =
            // 0
            if (!processusPeriodiqueModel.getIsPartiel()
                    && !JadeNumericUtil.isEmptyOrZero(processusPeriodiqueModel.getIdPassageFactu())) {
                JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                        "al.processus.processusPeriodiqueModel.idPassageFactu.businessIntegrity.principal");
            }

            // le passage périodique ne doit pas être explicitement lié avec son n°, mais on met 0 (écran gère ce
            // "filtre")
            if (!JadeNumericUtil.isEmptyOrZero(processusPeriodiqueModel.getIdPassageFactu())) {
                PassageModel passageLie = FABusinessServiceLocator.getPassageModelService().read(
                        processusPeriodiqueModel.getIdPassageFactu());
                if (FACSPassage.TYPE_FACTU_PERIODIQUE.equals(passageLie.getTypeFacturation())) {
                    JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                            "al.processus.processusPeriodiqueModel.idPassageFactu.businessIntegrity.nonPeriodique");
                }
            }
        }

    }

    /**
     * vérifie l'intégrité des données relatives aux codes systèmes , si non respectée lance un message sur l'intégrité
     * de(s) donnée(s)
     * 
     * @param processusPeriodiqueModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkCodesystemIntegrity(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // processus métier

        try {
            if (!JadeNumericUtil.isEmptyOrZero(processusPeriodiqueModel.getEtat())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSProcessus.GROUP_ETAT_PROCESSUS,
                            processusPeriodiqueModel.getEtat())) {
                JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                        "al.processus.processusPeriodiqueModel.etat.codesystemIntegrity");
            }
        } catch (JadeNoBusinessLogSessionError e) {
            throw new ALAllocataireModelException(
                    "ProcessusPeriodiqueModelChecker#checkCodesystemIntegrity : unable to check codes system", e);
        } catch (Exception e) {
            throw new ALAllocataireModelException(
                    "ProcessusPeriodiqueModelChecker#checkCodesystemIntegrity : unable to check codes system", e);
        }

    }

    /**
     * vérifie l'intégrité des données relatives à la base de données , si non respectée lance un message sur
     * l'intégrité de(s) donnée(s)
     * 
     * @param processusPeriodiqueModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadePersistenceException {
        // id de la configuration
        if (!JadeNumericUtil.isIntegerPositif(processusPeriodiqueModel.getIdConfig())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.idconfig.databaseIntegrity.type");
        }

        if (!JadeNumericUtil.isIntegerPositif(processusPeriodiqueModel.getIdPeriode())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.idperiode.databaseIntegrity.type");
        }

        if (!JadeNumericUtil.isIntegerPositif(processusPeriodiqueModel.getEtat())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.etat.databaseIntegrity.type");
        }

    }

    /**
     * Vérification de l'intégrité métier avant suppression
     * 
     * @param processusPeriodiqueModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        if (!processusPeriodiqueModel.getIsPartiel()) {

            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.idProcessus.deleteIntegrity.partiel");

        }
        if (ALCSProcessus.ETAT_TERMINE.equals(processusPeriodiqueModel.getEtat())) {

            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.idProcessus.deleteIntegrity.ferme");

        }

        RecapitulatifEntrepriseSearchModel searchRecapLiees = new RecapitulatifEntrepriseSearchModel();
        searchRecapLiees.setForNumProcessusLie(processusPeriodiqueModel.getId());
        searchRecapLiees = ALServiceLocator.getRecapitulatifEntrepriseModelService().search(searchRecapLiees);
        if (searchRecapLiees.getSize() > 0) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.idProcessus.deleteIntegrity.hasRecaps");
        }

    }

    /**
     * vérifie l'obligation des données, si non respectée lance un message sur l'obligation de(s) donnée(s)
     * 
     * @param processusPeriodiqueModel
     *            Modèle à valider
     */
    private static void checkMandatory(ProcessusPeriodiqueModel processusPeriodiqueModel) {
        // id de la période
        if (JadeStringUtil.isEmpty(processusPeriodiqueModel.getIdPeriode())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.idperiode.mandatory");
        }

        // état du processus périodique
        if (JadeStringUtil.isEmpty(processusPeriodiqueModel.getEtat())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.etat.mandatory");
        }

        // id de la configuration
        if (JadeStringUtil.isEmpty(processusPeriodiqueModel.getIdConfig())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.idconfig.mandatory");
        }

    }

    /**
     * validation de l'intégrité des données et des règles métier, de l'obligation des données et de l'intégrité des
     * codes systèmes
     * 
     * @param processusPeriodiqueModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(ProcessusPeriodiqueModel processusPeriodiqueModel) throws JadePersistenceException,
            JadeApplicationException {
        ProcessusPeriodiqueModelChecker.checkMandatory(processusPeriodiqueModel);
        ProcessusPeriodiqueModelChecker.checkDatabaseIntegrity(processusPeriodiqueModel);
        ProcessusPeriodiqueModelChecker.checkCodesystemIntegrity(processusPeriodiqueModel);
        ProcessusPeriodiqueModelChecker.checkBusinessIntegrity(processusPeriodiqueModel);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param droitModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {
        ProcessusPeriodiqueModelChecker.checkDeleteIntegrity(processusPeriodiqueModel);
    }

}
