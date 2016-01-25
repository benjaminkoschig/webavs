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
 * Classe de validation des donn�es de TraitementPeriodiqueModel
 * 
 * @author GMO
 * 
 */
public abstract class TraitementPeriodiqueModelChecker extends ALAbstractChecker {

    /**
     * v�rifie l'int�grit� des donn�es relatives aux codes syst�mes , si non respect�e lance un message sur l'int�grit�
     * de(s) donn�e(s)
     * 
     * @param traitementPeriodiqueModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkCodesystemIntegrity(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // �tat du traitement p�riodique

        try {
            if (!JadeNumericUtil.isEmptyOrZero(traitementPeriodiqueModel.getEtat())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSProcessus.GROUP_ETAT_PROCESSUS,
                            traitementPeriodiqueModel.getEtat())) {
                JadeThread.logError(TraitementPeriodiqueModelChecker.class.getName(),
                        "al.processus.traitementPeriodiqueModel.etat.codesystemIntegrity");
            }
            // libell� du traitement
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
     * v�rifie l'int�grit� des donn�es relatives � la base de donn�es , si non respect�e lance un message sur
     * l'int�grit� de(s) donn�e(s)
     * 
     * @param traitementPeriodiqueModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadePersistenceException {
        // libell� du traitement
        if (!JadeNumericUtil.isIntegerPositif(traitementPeriodiqueModel.getTraitementLibelle())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.traitement.databaseIntegrity.type");
        }
        // id du processus p�riodique
        if (!JadeNumericUtil.isIntegerPositif(traitementPeriodiqueModel.getIdProcessusPeriodique())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.idprocessus.databaseIntegrity.type");
        }
        // �tat du traitement
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

        // heure du traitement peut-�tre vide � la cr�ation
        // if (!JadeStringUtil.isBlank(traitementPeriodiqueModel
        // .getHeureExecution())
        // ) {
        // JadeThread
        // .logError(ProcessusPeriodiqueModelChecker.class.getName(),
        // "al.processus.traitementPeriodiqueModel.heureExecution.databaseIntegrity.type");
        // }

        // user du traitement peut �tre vide la cr�ation... => pas de ctrl
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
     * V�rification de l'int�grit� m�tier avant suppression
     * 
     * @param traitementPeriodiqueModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * v�rifie l'obligation des donn�es, si non respect�e lance un message sur l'obligation de(s) donn�e(s)
     * 
     * @param traitementPeriodiqueModel
     *            Mod�le � valider
     */
    private static void checkMandatory(TraitementPeriodiqueModel traitementPeriodiqueModel) {
        // libell� du traitement
        if (JadeStringUtil.isEmpty(traitementPeriodiqueModel.getTraitementLibelle())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.traitement.mandatory");
        }

        // �tat du traitement p�riodique
        if (JadeStringUtil.isEmpty(traitementPeriodiqueModel.getEtat())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.etat.mandatory");
        }

        // id du processus p�riodique
        if (JadeStringUtil.isEmpty(traitementPeriodiqueModel.getIdProcessusPeriodique())) {
            JadeThread.logError(ProcessusPeriodiqueModelChecker.class.getName(),
                    "al.processus.traitementPeriodiqueModel.idprocessus.mandatory");
        }

    }

    /**
     * validation de l'int�grit� des donn�es et des r�gles m�tier, de l'obligation des donn�es et de l'int�grit� des
     * codes syst�mes
     * 
     * @param traitementPeriodiqueModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(TraitementPeriodiqueModel traitementPeriodiqueModel) throws JadePersistenceException,
            JadeApplicationException {
        TraitementPeriodiqueModelChecker.checkMandatory(traitementPeriodiqueModel);
        TraitementPeriodiqueModelChecker.checkDatabaseIntegrity(traitementPeriodiqueModel);
        TraitementPeriodiqueModelChecker.checkCodesystemIntegrity(traitementPeriodiqueModel);
    }

    /**
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param traitementPeriodiqueModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {
        TraitementPeriodiqueModelChecker.checkDeleteIntegrity(traitementPeriodiqueModel);
    }
}
