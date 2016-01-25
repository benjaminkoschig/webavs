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
 * Classe de validation des donn�es de ProcessusPeriodiqueModel
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

            // si il est ferm� on peut plus modifier
            if (ALCSProcessus.ETAT_TERMINE.equals(processusInDB.getEtat())) {
                JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                        "al.processus.processusPeriodiqueModel.etat.businessIntegrity.verrouille");
            }

            TraitementPeriodiqueSearchModel searchTraitements = new TraitementPeriodiqueSearchModel();
            searchTraitements.setForIdProcessusPeriodique(processusPeriodiqueModel.getId());
            searchTraitements = ALServiceLocator.getTraitementPeriodiqueModelService().search(searchTraitements);
            // si il y un traitement de pr�paration d�j� ferm� ou en cours, on ne peut plus changer de passage
            for (int i = 0; i < searchTraitements.getSize(); i++) {
                TraitementPeriodiqueModel currentTraitement = (TraitementPeriodiqueModel) searchTraitements
                        .getSearchResults()[i];

                if (ALCSProcessus.NAME_TRAITEMENT_PREPARATION_COMPENSATION.equals(currentTraitement
                        .getTraitementLibelle())
                        && (ALCSProcessus.ETAT_TERMINE.equals(currentTraitement.getEtat()) || ALCSProcessus.ETAT_ENCOURS
                                .equals(currentTraitement.getEtat()))) {

                    // on v�rifie si il y a eu un changement DB
                    if (!processusInDB.getIdPassageFactu().equals(processusPeriodiqueModel.getIdPassageFactu())) {
                        JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                                "al.processus.processusPeriodiqueModel.idPassageFactu.businessIntegrity.verrouille");
                    }

                }
            }

            // test si le passage n'est pas d�j� li� � un autre processus
            // car r�gle => 1 processus || 1 passage

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

            // si c'est le processus principal, on doit pas affecter un passage, car le p�riodique prend que le
            // processus =
            // 0
            if (!processusPeriodiqueModel.getIsPartiel()
                    && !JadeNumericUtil.isEmptyOrZero(processusPeriodiqueModel.getIdPassageFactu())) {
                JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                        "al.processus.processusPeriodiqueModel.idPassageFactu.businessIntegrity.principal");
            }

            // le passage p�riodique ne doit pas �tre explicitement li� avec son n�, mais on met 0 (�cran g�re ce
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
     * v�rifie l'int�grit� des donn�es relatives aux codes syst�mes , si non respect�e lance un message sur l'int�grit�
     * de(s) donn�e(s)
     * 
     * @param processusPeriodiqueModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkCodesystemIntegrity(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // processus m�tier

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
     * v�rifie l'int�grit� des donn�es relatives � la base de donn�es , si non respect�e lance un message sur
     * l'int�grit� de(s) donn�e(s)
     * 
     * @param processusPeriodiqueModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * V�rification de l'int�grit� m�tier avant suppression
     * 
     * @param processusPeriodiqueModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * v�rifie l'obligation des donn�es, si non respect�e lance un message sur l'obligation de(s) donn�e(s)
     * 
     * @param processusPeriodiqueModel
     *            Mod�le � valider
     */
    private static void checkMandatory(ProcessusPeriodiqueModel processusPeriodiqueModel) {
        // id de la p�riode
        if (JadeStringUtil.isEmpty(processusPeriodiqueModel.getIdPeriode())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.processusPeriodiqueModel.idperiode.mandatory");
        }

        // �tat du processus p�riodique
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
     * validation de l'int�grit� des donn�es et des r�gles m�tier, de l'obligation des donn�es et de l'int�grit� des
     * codes syst�mes
     * 
     * @param processusPeriodiqueModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(ProcessusPeriodiqueModel processusPeriodiqueModel) throws JadePersistenceException,
            JadeApplicationException {
        ProcessusPeriodiqueModelChecker.checkMandatory(processusPeriodiqueModel);
        ProcessusPeriodiqueModelChecker.checkDatabaseIntegrity(processusPeriodiqueModel);
        ProcessusPeriodiqueModelChecker.checkCodesystemIntegrity(processusPeriodiqueModel);
        ProcessusPeriodiqueModelChecker.checkBusinessIntegrity(processusPeriodiqueModel);
    }

    /**
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param droitModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {
        ProcessusPeriodiqueModelChecker.checkDeleteIntegrity(processusPeriodiqueModel);
    }

}
