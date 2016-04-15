package globaz.al.process.decision;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import globaz.al.process.dossiers.ALRadiationAutomatiqueDossiersProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeConcurrentAccessException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;

/**
 * Process permettant l'impression d'un lot de décision dont les ID sont contenu dans un fichier txt (Ref : InfoRom 580)
 *
 * @author jts
 */
public class ALDecisionsMasseProcess extends ALDecisionsMasseAbstractProcess {
    private static final Logger LOG = LoggerFactory.getLogger(ALDecisionsMasseProcess.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected void process() {

        try {
            serviceDecision = ALServiceLocator.getDecisionBuilderService();
            serviceDossier = ALServiceLocator.getDossierComplexModelService();

            if (DossierComplexSearchModel.ETATACTIF.equals(getEtatFilter())) {
                processDossiers();
            } else {
                processDossiersRadie();
            }
            printDecisions();
            printProtocole();

        } catch (Exception e) {
            getLogSession().error(this.getClass().getName(),
                    JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "globaz.al.process.generic.err_process") + e.getMessage());
            LOG.error(this.getClass().getName(), JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "globaz.al.process.generic.err_process") + e.getMessage());
        } finally {
            sendMail();
        }
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                "globaz.al.process.ALDecisionsMasseProcess.name");
    }

    @Override
    public String getDescription() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                "globaz.al.process.decision.ALDecisionsMasseProcess.description");
    }

    /**
     * Traite la liste de dossier transmis au process
     *
     * <ul>
     * <li>mise à jour de la date de validité</li>
     * <li>génération de la décision</li>
     * <li>journalisation</li>
     * </ul>
     *
     * @throws IOException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private void processDossiers()
            throws IOException, JadeApplicationException, JadePersistenceException, JadeNoBusinessLogSessionError {
        List<DossierComplexModel> listDossiers = getDossierOrderedFiltered();
        for (DossierComplexModel dossierComplex : listDossiers) {

            boolean isDossierUpdated = updateValiditeDossier(dossierComplex, getDateDebutValidite());

            if (isDossierUpdated) {
                try {
                    addDecision(dossierComplex);
                    journaliser(dossierComplex);
                } catch (JadeApplicationException e) {
                    JadeBusinessMessage message = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                            ALDecisionsMasseProcess.class.getName(),
                            "al.protocoles.impressionDecisionsMasse.warning.dossierTraiteDecisionNonImprimee",
                            new String[] { e.getMessage() });
                    addProtocoleMessage(dossierComplex, message);
                }
            }
            getProgressHelper().setCurrent(getProgressHelper().getCurrent() + 1);
        }
    }

    /**
     * Traite la liste de dossier radié transmis au process
     *
     * <ul>
     * <li>génération de la décision(certificat de radiation)</li>
     * </ul>
     *
     * @throws IOException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private void processDossiersRadie() throws IOException, JadeApplicationException, JadePersistenceException {
        List<DossierComplexModel> listDossiers = getDossierOrderedFiltered();
        for (DossierComplexModel dossierComplex : listDossiers) {
            try {
                addDecision(dossierComplex);
            } catch (JadeApplicationException e) {
                JadeBusinessMessage message = new JadeBusinessMessage(JadeBusinessMessageLevels.WARN,
                        ALDecisionsMasseProcess.class.getName(),
                        "al.protocoles.impressionDecisionsMasse.warning.dossierNoProcessed");
                addProtocoleMessage(dossierComplex, message);
            }
            getProgressHelper().setCurrent(getProgressHelper().getCurrent() + 1);
        }
    }

    /**
     * Génère un décision et ses copies pour le dossier passé en paramètre. La décision est ajouté dans le document
     * container du process
     *
     * @param dossierComplex
     *            Le dossier pour lequel la journalisation doit être générée
     *
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    protected void addDecision(DossierComplexModel dossierComplex)
            throws JadeApplicationException, JadePersistenceException {

        ALServiceLocator.getCopiesBusinessService().createDefaultCopies(dossierComplex, ALCSCopie.TYPE_DECISION);

        containerDecisions = serviceDecision.getDecision(containerDecisions, dossierComplex, getEmail(),
                getSession().getUserFullName(), getSession().getUserInfo().getPhone(),
                getSession().getUserInfo().getVisa(), getDateImpression(), getInsertionGED(), getGestionCopie(),
                getTexteLibre(), getGestionTexteLibre());

        JadeBusinessMessage message = new JadeBusinessMessage(JadeBusinessMessageLevels.INFO,
                ALDecisionsMasseProcess.class.getName(),
                "al.protocoles.impressionDecisionsMasse.info.decisionImprimee");
        addProtocoleMessage(dossierComplex, message);
    }

    /**
     * Met à jour la date de début de validité du dossier.
     *
     * Si le dossier a l'état actif la date de début de validité est mise à jour.
     *
     * Si le dossier est radié la date de début est mise à jour seulement si la nouvelle date de début de validité est
     * antérieur à la date de fin de validité actuelle du dossier.
     *
     * Dans tous les autres cas aucune modification n'est faite
     *
     * @param dossier
     *            le dossier à mettre à jour
     * @param dateDebutvalidite
     *            Nouvelle date de validité
     *
     * @return <code>true</code> si une mise à jour a été faite
     * @throws Exception
     */
    private boolean updateValiditeDossier(DossierComplexModel dossier, String dateDebutvalidite) {

        boolean isUpdated = false;

        if (ALCSDossier.ETAT_ACTIF.equals(dossier.getDossierModel().getEtatDossier())) {
            try {
                dossier.getDossierModel().setDebutValidite(dateDebutvalidite);
                dossier.getDossierModel().setFinValidite(null);
                dossier.getDossierModel().setNbJoursFin(null);
                if (JadeDateUtil.getFirstDateOfMonth(dateDebutvalidite).equals(dateDebutvalidite)) {
                    dossier.getDossierModel().setNbJoursDebut(null);
                }
                dossier = ALServiceLocator.getDossierBusinessService().updateDossier(dossier, null, null);
                isUpdated = true;
            } catch (JadeConcurrentAccessException e) {
                JadeBusinessMessage message = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                        ALDecisionsMasseProcess.class.getName(),
                        "al.protocoles.impressionDecisionsMasse.warning.dossierNoProcessedConcurrentAccess");
                addProtocoleMessage(dossier, message);
                doRollback(dossier);
                isUpdated = false;
                return isUpdated;
            } catch (Exception e) {
                dataProtocoleRecapitulatif.addFatalError(e);
                doRollback(dossier);
                isUpdated = false;
                return isUpdated;
            }

        }

        if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {
            try {
                JadeThread.commitSession();
            } catch (Exception e) {
                dataProtocoleRecapitulatif.addFatalError(e);
                isUpdated = false;
            }
        } else {
            doRollback(dossier);
            isUpdated = false;
        }

        JadeThread.logClear();
        return isUpdated;
    }

    /**
     * Journalise la décision pour le dossier passé en paramètre
     *
     * @param dossierComplex
     * @throws JadeNoBusinessLogSessionError
     */
    protected void journaliser(DossierComplexModel dossierComplex) throws JadeNoBusinessLogSessionError {

        try {
            serviceDecision.journaliserDecision(dossierComplex);
        } catch (Exception e) {
            getLogSession().error(this.getClass().getName(),
                    "Une erreur s'est produite pendant la journalisation d'une décision (dossier "
                            + dossierComplex.getId() + ")");
            JadeThread.logError(ALRadiationAutomatiqueDossiersProcess.class.getName(), e.getMessage());
        }
    }

    /**
     * Génère le document du protocole et le transmet au serveur de fusion
     *
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    protected void printProtocole() throws JadePersistenceException, JadeApplicationException,
            JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException {
        // paramètres
        HashMap<String, String> params = getParam();

        JadePublishDocumentInfo publishDocInfo = new JadePublishDocumentInfo();
        publishDocInfo
                .setDocumentTitle(JadeThread.getMessage("al.protocoles.impressionDecisionsMasse.info.processus.val"));
        publishDocInfo
                .setDocumentSubject(JadeThread.getMessage("al.protocoles.impressionDecisionsMasse.info.processus.val"));
        publishDocInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        publishDocInfo.setOwnerEmail(getEmail());
        publishDocInfo.setOwnerId(getSession().getUserId());
        publishDocInfo.setArchiveDocument(false);

        DocumentDataContainer documentDataContainer = ALServiceLocator.getProtocoleDecisionsMasseService()
                .getDocumentData(dataProtocoleRecapitulatif, params);
        JadePrintDocumentContainer containerProtocole = new JadePrintDocumentContainer();
        containerProtocole.addDocument(documentDataContainer.getDocument(), publishDocInfo);
        this.createDocuments(containerProtocole);
    }

    /**
     * Transmet les décisions à imprimer au serveur de fusion
     *
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    protected void printDecisions()
            throws JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException {
        containerDecisions
                .setMergedDocDestination(serviceDecision.getMergedDocumentInfos(getEmail(), getDateImpression()));
        this.createDocuments(containerDecisions);
    }

    /**
     * Effectue un rollback de la transaction
     *
     * @param dossier
     *            Dossier en cours de traitement, permet l'ajout d'un message dans le logger (protocole)
     */
    private void doRollback(DossierComplexModel dossier) {

        try {

            JadeBusinessMessage[] messages = JadeThread.logMessages();

            if (messages != null) {
                for (JadeBusinessMessage message : messages) {
                    addProtocoleMessage(dossier, message);
                }
            }

            JadeThread.rollbackSession();
        } catch (JadeConcurrentAccessException e) {
            JadeBusinessMessage message = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                    ALDecisionsMasseProcess.class.getName(),
                    "al.protocoles.impressionDecisionsMasse.warning.dossierNoProcessedConcurrentAccess");
            addProtocoleMessage(dossier, message);
        } catch (Exception e) {
            dataProtocoleRecapitulatif.addFatalError(e);
        }
    }

}
