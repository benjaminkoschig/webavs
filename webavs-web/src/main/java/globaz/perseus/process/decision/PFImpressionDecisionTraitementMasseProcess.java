package globaz.perseus.process.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.decision.DecisionChangementConditionsPersonnellesTraitementMasseBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionOctroiHorsRiTraitementMasseBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionOctroiPartielTraitementMasseBuilder;
import ch.globaz.perseus.businessimpl.services.decision.DecisionSuppressionEconomiqueTraitementMasseBuilder;

public class PFImpressionDecisionTraitementMasseProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String AGLAU_Benef = "aglau_beneficiaire";
    public static final String AGLAU_COPIE_OVAM = "aglau_copie_ovam";
    public static final String AGLAU_COPIE_RI = "aglau_copie_ri";
    public static final String AGLAU_COPIE_SUPP = "aglau_copie_supplementaire";
    public static final String CCVD_Benef = "ccvd_beneficiaire";
    public static final String CCVD_COPIE_OVAM = "ccvd_copie_ovam";
    public static final String CCVD_COPIE_RI = "ccvd_copie_ri";
    public static final String CCVD_COPIE_SUPP = "ccvd_copie_supplementaire";

    private JadePrintDocumentContainer AGLAU_Beneficiaire = new JadePrintDocumentContainer();
    private JadePrintDocumentContainer AGLAU_Copie_Ovam = new JadePrintDocumentContainer();
    private JadePrintDocumentContainer AGLAU_Copie_Ri = new JadePrintDocumentContainer();
    private JadePrintDocumentContainer AGLAU_Copie_Supplementaire = new JadePrintDocumentContainer();
    private JadePrintDocumentContainer CCVD_Beneficiaire = new JadePrintDocumentContainer();
    private JadePrintDocumentContainer CCVD_Copie_Ovam = new JadePrintDocumentContainer();
    private JadePrintDocumentContainer CCVD_Copie_Ri = new JadePrintDocumentContainer();
    private JadePrintDocumentContainer CCVD_Copie_Supplementaire = new JadePrintDocumentContainer();
    private HashMap<String, JadePrintDocumentContainer> hashAllDoc = null;

    private List<Decision> listeDecision = null;
    private String mailAd = null;

    /**
     * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et doit donc
     * être renommée différement (mailAd) pour fonctionner correctement.
     */
    private String mailAddressCaissePrincipale;
    private String mailAddressCaisseSecondaire;

    private void genererDecision(Decision decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        genererDecisionForTypeDecision(decision);
    }

    private void genererDecisionForTypeDecision(Decision decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            genererDecisionOctroiForTypeDemande(decision);
        } else if (CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                decision.getSimpleDecision().getCsTypeDecision())) {
            genererDecisionOctroiPartiel(decision);
        } else {
            logErrorTypeDecision(decision);
        }
    }

    private void genererDecisionOctroiCompletRevisionExtraordinaire(Decision decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        hashAllDoc = new DecisionChangementConditionsPersonnellesTraitementMasseBuilder().build(decision
                .getSimpleDecision().getId(), mailAddressCaissePrincipale, decision.getSimpleDecision()
                .getDateDocument(), Boolean.TRUE, hashAllDoc);
    }

    private void genererDecisionOctroiCompletRevisionPeriodique(Decision decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        hashAllDoc = new DecisionOctroiHorsRiTraitementMasseBuilder().build(decision.getSimpleDecision().getId(),
                mailAddressCaissePrincipale, decision.getSimpleDecision().getDateDocument(), Boolean.TRUE, hashAllDoc);
    }

    private void genererDecisionOctroiForTypeDemande(Decision decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        if (CSTypeDemande.REVISION_PERIODIQUE.getCodeSystem().equals(
                decision.getDemande().getSimpleDemande().getTypeDemande())) {
            genererDecisionOctroiCompletRevisionPeriodique(decision);
        } else if (CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(
                decision.getDemande().getSimpleDemande().getTypeDemande())) {
            genererDecisionOctroiCompletRevisionExtraordinaire(decision);
        } else {
            logErrorTypeDemande(decision);
        }
    }

    private void genererDecisionOctroiPartiel(Decision decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        Demande demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(
                decision.getDemande());
        if (null != demandePrecedante) {
            DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
            decisionSearchModel.setForIdDemande(demandePrecedante.getSimpleDemande().getId());
            decisionSearchModel.setForCsTypeDecision(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
            decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
            boolean hasDemandePartiel = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
            if (hasDemandePartiel) {
                genererDecisionOctroiPartielQuiResteEnPartiel(decision);
            } else {
                genererDecisionOctroiPartielApresOctroiComplet(decision);
            }
        } else {
            genererDecisionOctroiPartielQuiResteEnPartiel(decision);
        }

    }

    private void genererDecisionOctroiPartielApresOctroiComplet(Decision decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        hashAllDoc = new DecisionSuppressionEconomiqueTraitementMasseBuilder().build(decision.getSimpleDecision()
                .getId(), mailAddressCaissePrincipale, decision.getSimpleDecision().getDateDocument(), Boolean.TRUE,
                hashAllDoc);
    }

    private void genererDecisionOctroiPartielQuiResteEnPartiel(Decision decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        hashAllDoc = new DecisionOctroiPartielTraitementMasseBuilder().build(decision.getSimpleDecision().getId(),
                mailAddressCaissePrincipale, decision.getSimpleDecision().getDateDocument(), Boolean.TRUE, hashAllDoc);
    }

    @Override
    public String getDescription() {
        return "PFDecisionTraitementAdaptationProcess";
    }

    private HashMap<String, JadePrintDocumentContainer> getHashMapDeJadePrintDocumentContainer() {
        HashMap<String, JadePrintDocumentContainer> hashAllDoc = new HashMap<String, JadePrintDocumentContainer>();
        hashAllDoc.put(PFImpressionDecisionTraitementMasseProcess.CCVD_Benef, CCVD_Beneficiaire);
        hashAllDoc.put(PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_SUPP, CCVD_Copie_Supplementaire);
        hashAllDoc.put(PFImpressionDecisionTraitementMasseProcess.AGLAU_Benef, AGLAU_Beneficiaire);
        hashAllDoc.put(PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_SUPP, AGLAU_Copie_Supplementaire);
        hashAllDoc.put(PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_RI, AGLAU_Copie_Ri);
        hashAllDoc.put(PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_RI, CCVD_Copie_Ri);
        hashAllDoc.put(PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_OVAM, CCVD_Copie_Ovam);
        hashAllDoc.put(PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_OVAM, AGLAU_Copie_Ovam);
        return hashAllDoc;
    }

    public List<Decision> getListeDecision() {
        return listeDecision;
    }

    public String getMailAd() {
        return mailAd;
    }

    public String getMailAddressCaissePrincipale() {
        return mailAddressCaissePrincipale;
    }

    public String getMailAddressCaisseSecondaire() {
        return mailAddressCaisseSecondaire;
    }

    @Override
    public String getName() {
        return "PFDecisionTraitementAdaptationProcess";
    }

    private JadePublishDocumentInfo initialiseJadePublishDocInfoDestination(String eMail, String titre,
            String numInforom) {
        JadePublishDocumentInfo pubInfoDestination = new JadePublishDocumentInfo();
        pubInfoDestination.setOwnerEmail(eMail);
        pubInfoDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, eMail);
        pubInfoDestination.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfoDestination.setPublishDocument(true);
        pubInfoDestination.setArchiveDocument(false);
        pubInfoDestination.setDocumentTitle(titre);
        pubInfoDestination.setDocumentSubject(titre);
        pubInfoDestination.setDocumentType(numInforom);
        pubInfoDestination.setDocumentTypeNumber(numInforom);
        return pubInfoDestination;

    }

    private void logErrorTypeDecision(Decision decision) {
        JadeThread.logError(this.getClass().getName(), decision.getDemande().getDossier().getDemandePrestation()
                .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()
                + getSession().getLabel("PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_ERREUR_TYPE_DECISION")
                + decision.getSimpleDecision().getCsTypeDecision());
    }

    private void logErrorTypeDemande(Decision decision) {
        JadeThread.logError(this.getClass().getName(), decision.getDemande().getDossier().getDemandePrestation()
                .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()
                + getSession().getLabel("PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_ERREUR_TYPE_DEMANDE")
                + decision.getDemande().getSimpleDemande().getTypeDemande());
    }

    @Override
    protected void process() throws Exception {
        try {
            // Préparation pour retourner une liste de container
            hashAllDoc = getHashMapDeJadePrintDocumentContainer();

            for (Decision decision : getListeDecision()) {
                genererDecision(decision);
            }

            publicationDecision();

        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(this.getClass().getName(),
                    e.getMessage() + System.getProperty("line.separator") + System.getProperty("line.separator")
                            + getSession().getLabel("PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_ERREUR")
                            + System.getProperty("line.separator") + e.toString());

        }

        if (logErrorWithReturnBoolean(getSession().getLabel("PROCESS_ERREUR_MESSAGE"))) {
            List<String> email = new ArrayList<String>();
            email.add(mailAd);
            this.sendCompletionMail(email);
        }
    }

    private void publication(JadePrintDocumentContainer pubInfo) throws JadeServiceLocatorException,
            JadeServiceActivatorException, JadeClassCastException {
        this.createDocuments(pubInfo);
    }

    private void publicationAGLAU() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException {
        publicationAGLAUDecisionOriginale();
        publicationAGLAUDecisionCSR();
        publicationAGLAUDecisionOvam();
        publicationAGLAUDecisionSupplementaire();
    }

    private void publicationAGLAUDecisionCSR() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException {
        JadePublishDocumentInfo pubInfosDestination = initialiseJadePublishDocInfoDestination(
                mailAddressCaisseSecondaire,
                getSession().getLabel("PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_AGLAU_MAIL_COPIE_RI"),
                IPRConstantesExternes.PCF_DECISION_TRAITEMENT_MASSE_CAISSE_SECONDAIRE);
        AGLAU_Copie_Ri.setMergedDocDestination(pubInfosDestination);
        this.createDocuments(AGLAU_Copie_Ri);
    }

    private void publicationAGLAUDecisionOriginale() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException {
        JadePublishDocumentInfo pubInfosDestination = initialiseJadePublishDocInfoDestination(
                mailAddressCaisseSecondaire,
                getSession().getLabel("PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_AGLAU_MAIL_ORIGINAL"),
                IPRConstantesExternes.PCF_DECISION_TRAITEMENT_MASSE_CAISSE_SECONDAIRE);
        AGLAU_Beneficiaire.setMergedDocDestination(pubInfosDestination);
        this.createDocuments(AGLAU_Beneficiaire);
    }

    private void publicationAGLAUDecisionOvam() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException {
        JadePublishDocumentInfo pubInfosDestination = initialiseJadePublishDocInfoDestination(
                mailAddressCaisseSecondaire,
                getSession().getLabel("PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_AGLAU_MAIL_COPIE_OVAM"),
                IPRConstantesExternes.PCF_DECISION_TRAITEMENT_MASSE_CAISSE_SECONDAIRE);
        AGLAU_Copie_Ovam.setMergedDocDestination(pubInfosDestination);
        this.createDocuments(AGLAU_Copie_Ovam);
    }

    private void publicationAGLAUDecisionSupplementaire() throws JadeServiceLocatorException,
            JadeServiceActivatorException, JadeClassCastException {
        JadePublishDocumentInfo pubInfosDestination = initialiseJadePublishDocInfoDestination(
                mailAddressCaisseSecondaire,
                getSession().getLabel(
                        "PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_AGLAU_MAIL_COPIE_SUPPLEMENTAIRE"),
                IPRConstantesExternes.PCF_DECISION_TRAITEMENT_MASSE_CAISSE_SECONDAIRE);
        AGLAU_Copie_Supplementaire.setMergedDocDestination(pubInfosDestination);
        this.createDocuments(AGLAU_Copie_Supplementaire);
    }

    private void publicationCCVD() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException {
        publicationCCVDDecisionOriginale();
        publicationCCVDDecisionCopieCSR();
        publicationCCVDDecisionCopieSupplementaire();
        publicationCCVDDecisionCopieOvam();
    }

    private void publicationCCVDDecisionCopieCSR() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException {
        JadePublishDocumentInfo pubInfosDestination = initialiseJadePublishDocInfoDestination(
                mailAddressCaissePrincipale,
                getSession().getLabel("PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_CCVD_MAIL_COPIE_RI"),
                IPRConstantesExternes.PCF_DECISION_TRAITEMENT_MASSE_CAISSE_PRINCIPALE);
        CCVD_Copie_Ri.setMergedDocDestination(pubInfosDestination);
        publication(CCVD_Copie_Ri);
    }

    private void publicationCCVDDecisionCopieOvam() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException {
        JadePublishDocumentInfo pubInfosDestination = initialiseJadePublishDocInfoDestination(
                mailAddressCaissePrincipale,
                getSession().getLabel("PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_CCVD_MAIL_COPIE_OVAM"),
                IPRConstantesExternes.PCF_DECISION_TRAITEMENT_MASSE_CAISSE_PRINCIPALE);
        CCVD_Copie_Ovam.setMergedDocDestination(pubInfosDestination);
        publication(CCVD_Copie_Ovam);
    }

    private void publicationCCVDDecisionCopieSupplementaire() throws JadeServiceLocatorException,
            JadeServiceActivatorException, JadeClassCastException {
        JadePublishDocumentInfo pubInfosDestination = initialiseJadePublishDocInfoDestination(
                mailAddressCaissePrincipale,
                getSession().getLabel(
                        "PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_CCVD_MAIL_COPIE_SUPPLEMENTAIRE"),
                IPRConstantesExternes.PCF_DECISION_TRAITEMENT_MASSE_CAISSE_PRINCIPALE);
        CCVD_Copie_Supplementaire.setMergedDocDestination(pubInfosDestination);
        publication(CCVD_Copie_Supplementaire);
    }

    private void publicationCCVDDecisionOriginale() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException {
        JadePublishDocumentInfo pubInfosDestination = initialiseJadePublishDocInfoDestination(
                mailAddressCaissePrincipale,
                getSession().getLabel("PROCESS_PF_GENERATION_DECISION_TRAITEMENT_ADAPTATION_CCVD_MAIL_ORIGINAL"),
                IPRConstantesExternes.PCF_DECISION_TRAITEMENT_MASSE_CAISSE_PRINCIPALE);
        CCVD_Beneficiaire.setMergedDocDestination(pubInfosDestination);
        publication(CCVD_Beneficiaire);
    }

    private void publicationDecision() throws JadeServiceLocatorException, JadeServiceActivatorException,
            JadeClassCastException {
        publicationCCVD();
        publicationAGLAU();
    }

    public void setListeDecision(List<Decision> listeDecision) {
        this.listeDecision = listeDecision;
    }

    public void setMailAd(String mailAd) {
        this.mailAd = mailAd;
    }

    public void setMailAddressCaissePrincipale(String mailAddressCaissePrincipale) {
        this.mailAddressCaissePrincipale = mailAddressCaissePrincipale;
    }

    public void setMailAddressCaisseSecondaire(String mailAddressCaisseSecondaire) {
        this.mailAddressCaisseSecondaire = mailAddressCaisseSecondaire;
    }

}
