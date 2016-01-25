package globaz.al.process.dossiers;

import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.radiation.PrestationRadiationDossierComplexModel;
import ch.globaz.al.business.models.prestation.radiation.PrestationRadiationDossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionBuilderService;
import ch.globaz.al.business.services.dossiers.RadiationAutomatiqueService;
import ch.globaz.al.businessimpl.services.dossiers.RadiationAutomatiqueResult;

public class ALRadiationAutomatiqueDossiersProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateImpression = null;
    private String email = null;
    private boolean GED = false;
    private boolean printDecisions = false;

    /**
     * Retourne la date d'impression spécifiée lors de l'appel du process. Si elle est vide, retourne la date du jour
     * 
     * @return la date d'impression
     */
    public String getDateImpression() {
        return JadeStringUtil.isEmpty(dateImpression) ? JadeDateUtil.getGlobazFormattedDate(new Date())
                : dateImpression;
    }

    @Override
    public String getDescription() {
        return "Process de radiation de masse de dossiers AF";
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return ALRadiationAutomatiqueDossiersProcess.class.getName();
    }

    public boolean getPrintDecisions() {
        return printDecisions;
    }

    public boolean isGED() {
        return GED;
    }

    private HashMap<String, String> logError(HashMap<String, String> logError, DossierComplexModel dossier)
            throws Exception {
        JadeBusinessMessage[] logMessages = JadeThread.logMessages();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < logMessages.length; i++) {
            sb.append(
                    JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), logMessages[i].getMessageId(),
                            logMessages[i].getParameters())).append("\n");
        }

        logError.put(dossier.getId(), sb.toString());
        return logError;
    }

    @Override
    protected void process() {

        List<RadiationAutomatiqueResult> logDossiers = new ArrayList<RadiationAutomatiqueResult>();
        HashMap<String, String> logError = new HashMap<String, String>();
        JadePrintDocumentContainer containerDecision = new JadePrintDocumentContainer();
        boolean hasDecisions = false;

        try {
            RadiationAutomatiqueService serviceRadiationAuto = ALServiceLocator.getRadiationAutomatiqueService();
            DecisionBuilderService serviceDecisionBuilder = ALServiceLocator.getDecisionBuilderService();

            String moisCourant = JadeDateUtil.convertDateMonthYear(JadeDateUtil.getGlobazFormattedDate(new Date()));
            PrestationRadiationDossierComplexSearchModel prestations = serviceRadiationAuto
                    .loadLastPrestations(moisCourant);

            // traitement des prestations
            for (JadeAbstractModel prest : prestations.getSearchResults()) {

                RadiationAutomatiqueResult result = null;
                DossierComplexModel dossier = null;
                String idDossier = ((PrestationRadiationDossierComplexModel) prest).getDossierComplexModel().getId();

                try {
                    result = serviceRadiationAuto.radierDossier((PrestationRadiationDossierComplexModel) prest);

                    dossier = result.getDossier();

                    // erreur lors de la radiation/journalisation
                    if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) != null) {
                        logError = logError(logError, dossier);
                        JadeThread.rollbackSession();
                        JadeThread.logClear();
                        // OK
                    } else if ((dossier != null) && !dossier.isNew()
                            && ALCSDossier.ETAT_RADIE.equals(dossier.getDossierModel().getEtatDossier())) {

                        // ///////////////////////////////////////////////////////////////////////////////////////
                        // Création de la décision
                        // ///////////////////////////////////////////////////////////////////////////////////////

                        if (getPrintDecisions()) {

                            ALServiceLocator.getCopiesBusinessService().createDefaultCopies(dossier,
                                    ALCSCopie.TYPE_DECISION);

                            containerDecision = serviceDecisionBuilder.getDecisionEtCopies(containerDecision, dossier,
                                    email, getSession().getUserFullName(), getSession().getUserInfo().getPhone(),
                                    getSession().getUserInfo().getVisa(), dateImpression, isGED(), "", false);

                            if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {
                                try {
                                    serviceDecisionBuilder.journaliserDecision(dossier);
                                } catch (Exception e) {
                                    getLogSession().error(
                                            this.getClass().getName(),
                                            "Une erreur s'est produite pendant la journalisation d'une décision (dossier "
                                                    + dossier.getId() + ")");
                                    JadeThread.logError(ALRadiationAutomatiqueDossiersProcess.class.getName(),
                                            e.getMessage());
                                }
                            }

                            hasDecisions = true;
                        }

                        // ///////////////////////////////////////////////////////////////////////////////////////

                        // ///////////////////////////////////////////////////////////////////////////////////////
                        // Commit/Rollback
                        // ///////////////////////////////////////////////////////////////////////////////////////

                        if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {
                            JadeThread.commitSession();
                            logDossiers.add(result);
                            JadeThread.logClear();
                        } else {
                            logError(logError, dossier);
                            JadeThread.rollbackSession();
                            JadeThread.logClear();
                        }
                    }

                } catch (Exception e) {
                    getLogSession().error(
                            this.getClass().getName(),
                            "Une erreur s'est produite pendant la radiation du dossier " + idDossier + " : "
                                    + e.getMessage());
                    logError.put(idDossier, e.getMessage());
                    JadeThread.rollbackSession();
                    JadeThread.logClear();
                }
            }

            // impression des décisions
            if (hasDecisions) {
                containerDecision.setMergedDocDestination(serviceDecisionBuilder.getMergedDocumentInfos(getEmail(),
                        dateImpression));
                this.createDocuments(containerDecision);
            }

            // envoi du protocole
            try {
                String file = ALServiceLocator.getRadiationAutomatiqueDossiersProtocoleService().createProtocole(
                        logDossiers, logError);
                String description = "Protocole de radiation automatique des dossiers";
                JadeSmtpClient.getInstance().sendMail(getEmail(), description, description, new String[] { file });
                JadeLogger.info(this, "Protocole de radiation automatique des dossiers envoyé");
            } catch (Exception e) {
                getLogSession()
                        .error(this.getClass().getName(),
                                "Une erreur s'est produite pendant la génération du protocole des radiations automatiques de dossiers n'est pas disponible : "
                                        + e.getMessage());
                JadeLogger
                        .error(this,
                                "Une erreur s'est produite pendant la génération du protocole des radiations automatiques de dossiers n'est pas disponible : "
                                        + e.getMessage());
            }

        } catch (Exception e) {
            getLogSession().error(this.getClass().getName(),
                    "Une erreur s'est produite pendant l'exécution du traitement : " + e.getMessage());
            JadeLogger.error(this, "Une erreur s'est produite pendant l'exécution du traitement : " + e.getMessage());
        } finally {
            ArrayList<String> emails = new ArrayList<String>();
            emails.add(getEmail());
            try {
                sendCompletionMail(emails);
            } catch (Exception e) {
                JadeLogger.error(
                        this,
                        "Impossible d'envoyer le mail de résultat du traitement. Raison : " + e.getMessage() + ", "
                                + e.getCause());
            }
        }
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGED(boolean gED) {
        GED = gED;
    }

    public void setPrintDecisions(boolean printDecisions) {
        this.printDecisions = printDecisions;
    }
}