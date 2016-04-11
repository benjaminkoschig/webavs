/**
 * 
 */
package ch.globaz.pegasus.businessimpl.checkers.decision;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.common.mail.MailDebugInfo;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.domaine.decision.EtatDecision;
import ch.globaz.pegasus.business.domaine.decision.TypeDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.adresse.TechnicalExceptionWithTiers;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class SimpleDecisionHeaderChecker extends PegasusAbstractChecker {
    /**
     * Verification des dates de la decision après le dernier paiement, et avant le prochain paiement Uniquement appelé
     * pour la préparation et la validation des décision après calcul et des décision de suppression
     * 
     * @param decision
     * @throws PmtMensuelException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    public static void checkForCoherenceDateDecision(SimpleDecisionHeader decision) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException {

        // Check du format de la date
        SimpleDecisionHeaderChecker.checkForDateDecisionFormat(decision.getDateDecision());

        // vérifie que la date de décision soit après le dernier paiement
        String dateDernierPmt = "01." + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        String dateProchainPmt = "01." + PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        if (JadeDateUtil.isDateBefore(decision.getDateDecision(), dateDernierPmt)) {
            String[] params = new String[2];
            params[0] = decision.getDateDecision();
            params[1] = dateDernierPmt;
            JadeThread.logError(decision.getClass().getName(),
                    "pegasus.simpleDecisionHeader.dateDecision.integrity.dateDernierPaiement", params);
        }
        if (!JadeDateUtil.isDateBefore(decision.getDateDecision(), dateProchainPmt)) {
            String[] params = new String[2];
            params[0] = decision.getDateDecision();
            params[1] = dateProchainPmt;
            JadeThread.logError(decision.getClass().getName(),
                    "pegasus.simpleDecisionHeader.dateDecision.integrity.dateProchainPaiement", params);
        }
    }

    /**
     * Validation d'un header de decision lors de l'insertion
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForCreate(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // Check mandatory
        SimpleDecisionHeaderChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionHeaderChecker.checkIntegrity(decision);
        }
    }

    public static void checkForDateDecisionFormat(String date) {
        // format de date incorrect
        if (!JadeDateUtil.isGlobazDate(date)) {
            JadeThread.logError(SimpleDecisionHeader.class.getName(),
                    "pegasus.simpleDecisionHeader.formatdate.integrity");

        }
    }

    /**
     * Validation lors de l'effacement d'un header de decision
     * 
     * @param decision
     * @throws DecisionException
     */
    public static void checkForDelete(SimpleDecisionHeader decision) throws DecisionException {
        EtatDecision etat = EtatDecision.fromValue(decision.getCsEtatDecision());
        if (etat.isValide()) {
            MailDebugInfo.sendMail(EPCProperties.MAILS_DEBUG, "Tentative de suppression d'une décision validé !",
                    toBodyMail(decision));
            throwException("pegasus.simpleDecisionHeader.etat.delete.integrity", decision);
        }
    }

    /**
     * Validation lors de la mise à jour d'un header de decision
     * 
     * @param decision
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * 
     * @throws DroitException
     */
    public static void checkForUpdate(SimpleDecisionHeader decision) throws DecisionException, JadePersistenceException {
        // Check mandatory
        SimpleDecisionHeaderChecker.checkMandatory(decision);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionHeaderChecker.checkIntegrity(decision);
            checkEtatDecision(decision);
        }
    }

    private static void checkEtatDecision(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException {

        if (decision.getEtat().isPreValide()) {
            SimpleDecisionHeader oldDecision = readDecision(decision);
            EtatDecision etatOld = EtatDecision.fromValue(oldDecision.getCsEtatDecision());
            if (etatOld.isValide()) {
                String body = toBodyMail(decision);
                MailDebugInfo.sendMail(EPCProperties.MAILS_DEBUG,
                        "Changement d'état à prés-validé pour une décision dans l'état validé!", body);
                throwException("pegasus.simpleDecisionHeader.etat.valideApresValide.integrity", decision);
            }
        } else if (decision.getEtat().isEnregistre()) {
            SimpleDecisionHeader oldDecision = readDecision(decision);
            TypeDecision typeDecision = TypeDecision.fromValue(decision.getCsTypeDecision());

            // Le changement d'état de validé à enregistré se fait lors de la dévalidation c'est pour cette raison
            // que l'on test l'état du droit ne peut pas être validé
            if (decision.getEtat().isValide()) {
                SimpleVersionDroit simpleVersionDroit = null;
                // On ne fait la verification que pour le décisions qui sont liée à une version de droit
                if (typeDecision.isSuppression() || typeDecision.isTypeApresCalcul()) {
                    try {
                        simpleVersionDroit = PegasusServiceLocator.getDecisionHeaderService().loadSimpleVersionDroit(
                                decision);
                    } catch (JadeApplicationServiceNotAvailableException e) {
                        throw new RuntimeException(e);
                    }

                    if (simpleVersionDroit.getEtat().isValide()) {
                        String body = toBodyMail(decision);
                        MailDebugInfo.sendMail(
                                EPCProperties.MAILS_DEBUG,
                                "Changement d'état à enregistre pour une décision dans l'état"
                                        + oldDecision.getCsEtatDecision() + " non authorisé ", body);
                        throwException("pegasus.simpleDecisionHeader.etat.valide.integrity", decision);
                    }
                }
            }
        }
    }

    private static String toBodyMail(SimpleDecisionHeader decision) {
        String body = "Décision header en cause: " + MailDebugInfo.toJson(decision) + "\n\n"
                + TechnicalExceptionWithTiers.readDescriptionTiers(decision.getIdTiersBeneficiaire());
        return body;
    }

    private static void throwException(String message, SimpleDecisionHeader decision) {
        String[] p = new String[2];
        p[0] = TechnicalExceptionWithTiers.readDescriptionTiers(decision.getIdTiersBeneficiaire());
        p[1] = decision.getIdDecisionHeader();
        JadeThread.logError(decision.getClass().getName(), message, p);
        throw new TechnicalExceptionWithTiers(JadeThread.getMessage(message, p), decision.getIdTiersBeneficiaire());
    }

    private static SimpleDecisionHeader readDecision(SimpleDecisionHeader decision) {
        try {
            SimpleDecisionHeader oldDecision = PegasusImplServiceLocator.getSimpleDecisionHeaderService().read(
                    decision.getIdDecisionHeader());
            return oldDecision;
        } catch (Exception e) {

        }
        return decision;
    }

    /**
     * Verification de l'integrite des donness
     * 
     * @param decision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDecisionHeader decision) {

        // Le decision header doit avoir un type
        if (JadeStringUtil.isEmpty(decision.getCsTypeDecision())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.type.integrity");
        }
        // L'id du tiers doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getIdTiersBeneficiaire())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.tiers.integrity");
        }
        // L'id du tier courrier doit etre spécifié
        // if (JadeStringUtil.isEmpty(decision.getIdTiersCourrier())) {
        // JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.tiersCourrier.integrity");
        // }
        // Le no de decision doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getNoDecision())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.noDecision.integrity");
        }
        // La date de preparation doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getDatePreparation())) {
            JadeThread
                    .logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.datePreparation.integrity");
        }
        // La personne qui a préparé la decision doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getPreparationPar())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.preparationPar.integrity");
        }
    }

    /**
     * Validation des champs obligatoires
     * 
     * @param decision
     */
    private static void checkMandatory(SimpleDecisionHeader decision) {

        // La date de decision doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getDateDecision())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionHeader.dateDecision.mandatory");
        }

    }
}
