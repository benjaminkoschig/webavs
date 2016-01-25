package globaz.al.process.radiationaffilie;

import globaz.al.process.ALAbsrtactProcess;
import globaz.al.process.dossiers.ALRadiationAutomatiqueDossiersProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRendererDefaultStringAdapter;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.enumerations.affiliation.ALEnumProtocoleRadiationAffilie;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.affiliation.RadiationAffilieService;
import ch.globaz.al.business.services.decision.DecisionBuilderService;

/**
 * Process permettant la radiation des dossiers d'un affilié
 * 
 * Ref : InfoRom 439
 * 
 * @author jts
 * 
 */
public class ALRadiationAffilieProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String affilieDestinataire;
    private String affilieOrigine;
    private String dateDebutActivite;
    private String dateImpression;
    private String dateRadiation;
    private String email;
    private boolean printDecisions;
    private String reference;

    public String getAffilieDestinataire() {
        return affilieDestinataire;
    }

    public String getAffilieOrigine() {
        return affilieOrigine;
    }

    public String getDateDebutActivite() {
        return dateDebutActivite;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getDateRadiation() {
        return dateRadiation;
    }

    @Override
    public String getDescription() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                "globaz.al.process.radiationaffilie.ALRadiationAffilieProcess.description");
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                "globaz.al.process.radiationaffilie.ALRadiationAffilieProcess.name");
    }

    public boolean getPrintDecisions() {
        return printDecisions;
    }

    public String getReference() {
        return reference;
    }

    private boolean hasError() {
        return JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) != null;
    }

    private boolean hasWarn() {
        return JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN) != null;
    }

    @Override
    protected void process() {
        try {
            RadiationAffilieService serviceRadiation = ALServiceLocator.getRadiationAffilieService();
            DecisionBuilderService serviceDecision = ALServiceLocator.getDecisionBuilderService();

            boolean hasDecisions = false;
            JadePrintDocumentContainer containerDecision = new JadePrintDocumentContainer();

            List<Map<ALEnumProtocoleRadiationAffilie, Map<ALEnumProtocoleRadiationAffilie, Object>>> logs = new ArrayList<Map<ALEnumProtocoleRadiationAffilie, Map<ALEnumProtocoleRadiationAffilie, Object>>>();
            Map<String, DetailPrestationComplexSearchModel> prestations = new HashMap<String, DetailPrestationComplexSearchModel>();
            Map<String, String> logError = new HashMap<String, String>();

            DossierComplexSearchModel dossiers = serviceRadiation.getDossiersForAffilie(getAffilieOrigine(),
                    getDateRadiation());
            for (JadeAbstractModel dossier : dossiers.getSearchResults()) {

                DossierComplexModel oldDossier = (DossierComplexModel) dossier;
                String idDossier = oldDossier.getId();
                boolean newDossierIsActif = false;
                Map<ALEnumProtocoleRadiationAffilie, Map<ALEnumProtocoleRadiationAffilie, Object>> resultDossier = new HashMap<ALEnumProtocoleRadiationAffilie, Map<ALEnumProtocoleRadiationAffilie, Object>>();

                try {
                    // radiation
                    resultDossier.put(ALEnumProtocoleRadiationAffilie.RAD,
                            serviceRadiation.radierDossier(oldDossier, getDateRadiation(), getReference()));
                    oldDossier = (DossierComplexModel) resultDossier.get(ALEnumProtocoleRadiationAffilie.RAD).get(
                            ALEnumProtocoleRadiationAffilie.DOSSIER);

                    // copie
                    if (!JadeStringUtil.isBlankOrZero(getAffilieDestinataire())) {
                        // Note : le dossier est rechargé avant la copie pour éviter la modification de l'instance
                        // d'origine
                        HashMap<ALEnumProtocoleRadiationAffilie, Object> newDossier = serviceRadiation.copierDossier(
                                ALServiceLocator.getDossierComplexModelService().read(oldDossier.getId()),
                                getAffilieDestinataire(), getDateDebutActivite());

                        newDossierIsActif = ALCSDossier.ETAT_ACTIF.equals(((DossierComplexModel) newDossier
                                .get(ALEnumProtocoleRadiationAffilie.DOSSIER)).getDossierModel().getEtatDossier());

                        resultDossier.put(ALEnumProtocoleRadiationAffilie.COPIE, newDossier);
                    }

                    // préparation de la décision
                    if (getPrintDecisions() && !hasError()) {

                        ALServiceLocator.getCopiesBusinessService().createDefaultCopies(oldDossier,
                                ALCSCopie.TYPE_DECISION);

                        containerDecision = serviceDecision.getDecisionEtCopies(containerDecision, oldDossier, email,
                                getSession().getUserFullName(), getSession().getUserInfo().getPhone(), getSession()
                                        .getUserInfo().getVisa(), dateImpression, true, "", false);

                        if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {
                            try {
                                serviceDecision.journaliserDecision(oldDossier);
                            } catch (Exception e) {
                                getLogSession().error(
                                        this.getClass().getName(),
                                        "Une erreur s'est produite pendant la journalisation d'une décision (dossier "
                                                + oldDossier.getId() + ")");
                                JadeThread.logError(ALRadiationAutomatiqueDossiersProcess.class.getName(),
                                        e.getMessage());
                            }
                        }

                        hasDecisions = true;
                    }

                    if (!hasError()) {
                        // génération de prestations
                        boolean hasTransfert = !JadeStringUtil.isBlank(getAffilieDestinataire()) && newDossierIsActif;
                        prestations.put(oldDossier.getId(),
                                serviceRadiation.genererPrestationForDossier(oldDossier, hasTransfert));
                    }

                    // erreur pendant l'exécution
                    if (hasError()) {
                        // affichage des erreurs, suivies des warnings)
                        logError.put(
                                oldDossier.getId(),
                                new JadeBusinessMessageRendererDefaultStringAdapter().render(
                                        JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR),
                                        JadeThread.currentLanguage()).concat(
                                        new JadeBusinessMessageRendererDefaultStringAdapter().render(
                                                JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN),
                                                JadeThread.currentLanguage())));

                        JadeThread.rollbackSession();
                        JadeThread.logClear();
                        // OK
                    } else {

                        if (hasWarn()) {
                            logError.put(
                                    oldDossier.getId(),
                                    new JadeBusinessMessageRendererDefaultStringAdapter().render(
                                            JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN),
                                            JadeThread.currentLanguage()));
                        }

                        logs.add(resultDossier);
                        // Note : si des prestations ont été générées, le commit a déjà été fait par le service de
                        // radiation
                        JadeThread.commitSession();
                        JadeThread.logClear();
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
                containerDecision.setMergedDocDestination(serviceDecision.getMergedDocumentInfos(getEmail(),
                        dateImpression));
                this.createDocuments(containerDecision);
            }

            // envoi du protocole
            try {
                String file = ALServiceLocator.getRadiationAffilieProtocoleService().createProtocole(logs, prestations,
                        logError);
                StringBuilder description = new StringBuilder("Protocole de radiation de l'affilié ")
                        .append(getAffilieOrigine());

                if (!JadeStringUtil.isBlank(affilieDestinataire)) {
                    description.append(", transfert à ").append(affilieDestinataire);
                }

                JadeSmtpClient.getInstance().sendMail(getEmail(), description.toString(), description.toString(),
                        new String[] { file });
                JadeLogger.info(this, "Protocole de radiation de l'affilié " + getAffilieOrigine() + " envoyé");
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
                    "Une erreur s'est produite pendant l'exécution du process : " + e.getMessage());
            JadeLogger.error(this.getClass().getName(), "Une erreur s'est produite pendant l'exécution du process : "
                    + e.getMessage());
        } finally {
            ArrayList<String> emails = new ArrayList<String>();
            emails.add(JadeThread.currentUserEmail());
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

    public void setAffilieDestinataire(String affilieDestinataire) {
        this.affilieDestinataire = affilieDestinataire;
    }

    public void setAffilieOrigine(String affilieOrigine) {
        this.affilieOrigine = affilieOrigine;
    }

    public void setDateDebutActivite(String dateDebutActivite) {
        this.dateDebutActivite = dateDebutActivite;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setDateRadiation(String dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPrintDecisions(boolean printDecisions) {
        this.printDecisions = printDecisions;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}
