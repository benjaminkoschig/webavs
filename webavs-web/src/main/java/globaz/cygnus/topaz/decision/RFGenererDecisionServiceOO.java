/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.topaz.decision;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.api.decisions.IRFCodesIsoLangue;
import globaz.cygnus.api.paiement.IRFTypePaiement;
import globaz.cygnus.services.genererDecision.RFGenererDecisionRestitutionService;
import globaz.cygnus.services.validerDecision.RFDecisionDocumentData;
import globaz.cygnus.services.validerDecision.RFDemandeValidationData;
import globaz.cygnus.topaz.RFLettreBordereauOO;
import globaz.cygnus.topaz.RFLettreEnTeteOO;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.utils.ged.PRGedUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * author fha
 * 
 * - Process de création du document PDF, comptabilité
 */
public class RFGenererDecisionServiceOO {

    private static final String IS_DECISON_VALIDEE = "isDecisionValidee";
    private static final String IS_AVASAD = "isAvasad";
    public final static long MAX_MERGE_PART = 4000000;
    private static final int NOM_PRENOM_SIZE = 20;
    private Hashtable<String, ICTDocument> catalogueMultiLangue = new Hashtable<String, ICTDocument>();
    private String dateDocument = "";
    private ArrayList<RFDecisionDocumentData> decisionArray;
    private DocumentData docData = null;
    private JadePublishDocumentInfo docInfoFinal = null;
    protected ICTDocument documentHelper;
    protected ICTDocument documentPrincipaleDe;
    protected ICTDocument documentPrincipaleFr;
    protected ICTDocument documentPrincipaleIt;
    private ICTScalableDocumentProperties documentProperties;
    protected ICTDocument[] documentsDe;
    protected ICTDocument[] documentsFr;
    protected ICTDocument[] documentsIt;
    private String email = "";
    private String messageOvDecisionrestitution = "";
    private StringBuffer pdfDecisionURL = null;
    private BSession session;
    private BTransaction transaction = null;
    private boolean isAvasad = false;

    public RFGenererDecisionServiceOO() {
        super();
    }

    public RFGenererDecisionServiceOO(String email) {
        super();
        this.email = email;
    }

    public RFGenererDecisionServiceOO(boolean isAvasad) {
        super();
        this.isAvasad = isAvasad;
    }

    /**
     * Methode pour ajouter les décisions sans modèles, au memoryLog.
     * 
     * @param memoryLog
     * @param message
     * @param source
     */
    public void addWarningMail(FWMemoryLog memoryLog, String message, String source) {
        memoryLog.logMessage(message, new Integer(JadeBusinessMessageLevels.INFO).toString(), source);
    }

    private DocumentData ajoutAnnexeBordereau(RFDecisionDocumentData decisionDocument, String idTiersAssureConcerne)
            throws Exception {
        return createBordereauAccompagnement(decisionDocument, idTiersAssureConcerne);
    }

    private void ajoutCopie(RFDecisionDocumentData decisionDocument,
            RFGenererDecisionMainService rfGenererDecisionMainService, RFCopieDecisionsValidationData copie,
            JadePrintDocumentContainer documentContainer, FWMemoryLog memoryLog, boolean miseEnGed,
            boolean isDecisionPonctuelle, boolean isDecisionMensuelle, boolean isDecisionRestitution,
            ICTDocument documentHelper) throws Exception {

        // on ajoute la lettre d'entete que si page de garde vaut vrai
        if (copie.getHasPageDeGarde()) {

            RFLettreEnTeteOO lettreEnTete = createLettreEnTete(copie.getIdDestinataire(), decisionDocument);
            JadePublishDocumentInfo docInfoLettreEntete = new JadePublishDocumentInfo();
            docInfoLettreEntete.setPublishDocument(false);
            docInfoLettreEntete.setArchiveDocument(false);
            docInfoLettreEntete.setDocumentTypeNumber(IPRConstantesExternes.RFM_LETTRE_EN_TETE);
            docInfoLettreEntete.setDocumentType(IPRConstantesExternes.RFM_LETTRE_EN_TETE);

            if (RFPropertiesUtils.imprimerDecisionsRectoVerso()) {
                docInfoLettreEntete.setDuplex(Boolean.TRUE);
                docInfoLettreEntete.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
            }

            if (RFPropertiesUtils.insererLigneTechniqueDeRegroupement()) {

                docInfoLettreEntete.setDocumentProperty("AGLA_LT_PCRFM_DOC_TYPE_NUMBER",
                        IPRConstantesExternes.RFM_LETTRE_EN_TETE);
                docInfoLettreEntete.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "C");
                docInfoLettreEntete.setDocumentProperty("AGLA_LT_PCRFM_NSS_TIERS",
                        rfGenererDecisionMainService.getNss());
                docInfoLettreEntete.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", copie.getIdDestinataire());
                docInfoLettreEntete.setDocumentProperty("AGLA_LT_PCRFM_ID_DECISION", decisionDocument.getIdDecision());
                docInfoLettreEntete.setDocumentProperty(
                        "AGLA_LT_PCRFM_NOM_TIERS",
                        limiteStringSize(rfGenererDecisionMainService.getNom(),
                                RFGenererDecisionServiceOO.NOM_PRENOM_SIZE));
                docInfoLettreEntete.setDocumentProperty(
                        "AGLA_LT_PCRFM_PRENOM_TIERS",
                        limiteStringSize(rfGenererDecisionMainService.getPrenom(),
                                RFGenererDecisionServiceOO.NOM_PRENOM_SIZE));

            }

            TIDocumentInfoHelper.fill(docInfoLettreEntete, copie.getIdDestinataire(), getSession(), null, null, null);
            if (miseEnGed) {
                // Mise en GED du document si ce n'est pas une copie
                if (PRGedUtils.isDocumentInGed(IPRConstantesExternes.RFM_LETTRE_EN_TETE, session)) {
                    docInfoLettreEntete.setArchiveDocument(true);
                }
            }
            documentContainer.addDocument(lettreEnTete.getDocumentData(), docInfoLettreEntete);
        }

        // Création copie lettre refus
        rfGenererDecisionMainService.setIsCopie(true);

        documentContainer = rfGenererDecisionMainService.remplirDecision(miseEnGed, memoryLog, documentContainer,
                isDecisionPonctuelle, isDecisionMensuelle, isDecisionRestitution, documentHelper, catalogueMultiLangue,
                copie);
    }

    public String buildMessageWarningRegimeSansModele(BSession session, RFDecisionDocumentData decisionData) {

        String texteDecision = "\n" + session.getLabel("PROCESS_NUMERO_DECISION_SANS_MODELE");

        String infoDecisionsRefusees = "<b>" + decisionData.getNumeroDecision() + "</b>" + " : "
                + decisionData.getNss() + " - " + decisionData.getNom() + " " + decisionData.getPrenom();

        return texteDecision + " " + infoDecisionsRefusees;
    }

    /*
     * charge 3 catalogues de texte : allemand, francais, italien
     */
    public void chargerCataloguesTextes() throws Exception {

        // Set info au documentHelper
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);

        // CHARGEMENT DU CATALOGUE DE TEXTES FR/DE/IT POUR LES DECISIONS DE RESTITUTIONS
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_RESTITUTION);
        // Restitution en français
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_FR);
        documentsFr = documentHelper.load();
        if ((documentsFr == null) || (documentsFr.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_RESTITUTION + "_" + IRFCodesIsoLangue.LANGUE_ISO_FR,
                    documentsFr[0]);
        }
        // Restitution en allemand
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_DE);
        documentsDe = documentHelper.load();
        if ((documentsDe == null) || (documentsDe.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_RESTITUTION + "_" + IRFCodesIsoLangue.LANGUE_ISO_DE,
                    documentsDe[0]);
        }
        // Restitution en italien
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_IT);
        documentsIt = documentHelper.load();
        if ((documentsIt == null) || (documentsIt.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_RESTITUTION + "_" + IRFCodesIsoLangue.LANGUE_ISO_IT,
                    documentsIt[0]);
        }

        // CHARGEMENT DU CATALOGUE DE TEXTES FR/DE/IT POUR LES DECISIONS PONCTUELLES
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_PONCTUELLE);
        // Ponctuelle en français
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_FR);
        documentsFr = documentHelper.load();
        if ((documentsFr == null) || (documentsFr.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_PONCTUELLE + "_" + IRFCodesIsoLangue.LANGUE_ISO_FR,
                    documentsFr[0]);
        }
        // Ponctuelle en allemand
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_DE);
        documentsDe = documentHelper.load();
        if ((documentsDe == null) || (documentsDe.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_PONCTUELLE + "_" + IRFCodesIsoLangue.LANGUE_ISO_DE,
                    documentsDe[0]);
        }
        // Ponctuelle en italien
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_IT);
        documentsIt = documentHelper.load();
        if ((documentsIt == null) || (documentsIt.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_PONCTUELLE + "_" + IRFCodesIsoLangue.LANGUE_ISO_IT,
                    documentsIt[0]);
        }

        // CHARGEMENT DU CATALOGUE DE TEXTES FR/DE/IT POUR LES DECISIONS MENSUELLES DE REGIME STANDARD (OCTROI)
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_STANDARD_OCTROI);
        // Mensuelle en français
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_FR);
        documentsFr = documentHelper.load();
        if ((documentsFr == null) || (documentsFr.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_STANDARD_OCTROI + "_"
                    + IRFCodesIsoLangue.LANGUE_ISO_FR, documentsFr[0]);
        }
        // Mensuelle en allemand
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_DE);
        documentsDe = documentHelper.load();
        if ((documentsDe == null) || (documentsDe.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_STANDARD_OCTROI + "_"
                    + IRFCodesIsoLangue.LANGUE_ISO_DE, documentsDe[0]);
        }
        // Mensuelle en italien
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_IT);
        documentsIt = documentHelper.load();
        if ((documentsIt == null) || (documentsIt.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_STANDARD_OCTROI + "_"
                    + IRFCodesIsoLangue.LANGUE_ISO_IT, documentsIt[0]);
        }

        // CHARGEMENT DU CATALOGUE DE TEXTES FR/DE/IT POUR LES DECISIONS MENSUELLES DE REGIME AVEC EXCEDENT(OCTROI)
        if (RFPropertiesUtils.utiliserDecisionAvecExcedantRevenu()) {
            documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI);
            // Mensuelle en français
            documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_FR);
            documentsFr = documentHelper.load();
            if ((documentsFr == null) || (documentsFr.length == 0)) {
                throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
            } else {
                catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_FR, documentsFr[0]);
            }
            // Mensuelle en allemand
            documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_DE);
            documentsDe = documentHelper.load();
            if ((documentsDe == null) || (documentsDe.length == 0)) {
                throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
            } else {
                catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_DE, documentsDe[0]);
            }
            // Mensuelle en italien
            documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_IT);
            documentsIt = documentHelper.load();
            if ((documentsIt == null) || (documentsIt.length == 0)) {
                throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
            } else {
                catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_IT, documentsIt[0]);
            }
        }
        // CHARGEMENT DU CATALOGUE DE TEXTES FR/DE/IT POUR LES DECISIONS MENSUELLE DE REGIME AVEC EXCEDENT(REFUS)
        if (RFPropertiesUtils.utiliserDecisionAvecExcedantRevenu()) {
            documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS);
            // Régime de refus en français
            documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_FR);
            documentsFr = documentHelper.load();
            if ((documentsFr == null) || (documentsFr.length == 0)) {
                throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
            } else {
                catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_FR, documentsFr[0]);
            }
            // Régime de refus en allemand
            documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_DE);
            documentsDe = documentHelper.load();
            if ((documentsDe == null) || (documentsDe.length == 0)) {
                throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
            } else {
                catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_DE, documentsDe[0]);
            }
            // Régime de refus en italien
            documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_IT);
            documentsIt = documentHelper.load();
            if ((documentsIt == null) || (documentsIt.length == 0)) {
                throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
            } else {
                catalogueMultiLangue.put(IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_IT, documentsIt[0]);
            }
        }
    }

    /**
     * Methode pour afficher la différence entre un OV et une décision de restitution dans le docInfoFinal.
     */
    private void chargerMessageOvDecisionRestitution() {
        docInfoFinal.setDocumentNotes("<u>"
                + getSession().getLabel("PROCESS_OV_DIFFERENT_DECISION_RESTITUTION").toUpperCase() + "</u>" + "\n \n"
                + "<u>" + getSession().getLabel("PROCESS_NSS_DECISION_RESTITUTION") + "</u>"
                + "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp" + "<u>"
                + getSession().getLabel("PROCESS_TIERS_DECISION_RESTITUTION") + "</u>"
                + "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp" + "<u>"
                + getSession().getLabel("PROCESS_ID_DECISION_DECISION_RESTITUTION") + "</u>"
                + "&nbsp &nbsp &nbsp &nbsp" + "<u>" + getSession().getLabel("PROCESS_MONTANT_DECISION_RESTITUTION")
                + "</u>" + messageOvDecisionrestitution);
    }

    private DocumentData createBordereauAccompagnement(RFDecisionDocumentData decisionDocument,
            String idTiersAssureConcerne) throws Exception {

        DocumentData docData = new DocumentData();

        RFLettreBordereauOO lettreAnnexe = new RFLettreBordereauOO();
        lettreAnnexe.setSession(getSession());
        lettreAnnexe.setDomaineLettreEnTete(RFLettreBordereauOO.DOMAINE_CYGNUS);

        // retrieve du tiers
        PRTiersWrapper tiers = PRTiersHelper.getTiersAdresseParId(getSession(), decisionDocument.getIdTiers());

        // if (null == decisionDocument.getIdTiers()) {
        if (null == idTiersAssureConcerne) {
            tiers = PRTiersHelper.getAdministrationParId(getSession(), decisionDocument.getIdTiers());
        }

        // if (null != tiers) {
        if (null != idTiersAssureConcerne) {
            lettreAnnexe.setTierAdresse(tiers);
            // lettreAnnexe.setIdTiers(decisionDocument.getIdTiers());
            lettreAnnexe.setIdTiers(idTiersAssureConcerne);
            lettreAnnexe.setSession(getSession());
            docData = lettreAnnexe.generationLettre();

        }

        return docData;

    }

    private JadePublishDocumentInfo createDocInfoFinal(AbstractJadeJob process, boolean isMiseEnGed,
            boolean isSimulation, String idLot) {

        JadePublishDocumentInfo pubInfosFinal = JadePublishDocumentInfoProvider.newInstance(process);
        pubInfosFinal.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmail());
        pubInfosFinal.setOwnerEmail(getEmail());

        if (isSimulation) {
            pubInfosFinal.setDocumentTitle(session.getLabel("PROCESS_SIMULER_DECISIONS_DOCUMENTS"));
            pubInfosFinal.setDocumentSubject(session.getLabel("PROCESS_SIMULER_DECISIONS_DOCUMENTS"));
        } else if (isMiseEnGed && (idLot != null)) {
            pubInfosFinal.setDocumentTitle(session.getLabel("JSP_PROCESS_MISE_EN_GED_VALIDATION"));
            pubInfosFinal.setDocumentSubject(session
                    .getLabel("PROCESS_MISE_EN_GED_REUSSIE_DECISIONS_COMPTABILISATION_LOT") + idLot);
        } else {
            pubInfosFinal.setDocumentTitle(session.getLabel("PROCESS_VALIDER_DECISIONS_DOCUMENTS"));
            pubInfosFinal.setDocumentSubject(session.getLabel("PROCESS_VALIDER_DECISIONS_DOCUMENTS"));
        }

        pubInfosFinal.setArchiveDocument(false);

        // Si la génération provient de la comptabilisation du lot, aucune publication de documents souhaitée.
        if (JadeStringUtil.isEmpty(idLot)) {
            pubInfosFinal.setPublishDocument(true);
        } else {
            pubInfosFinal.setPublishDocument(false);
        }
        // pubInfosFinal.setDocumentSubject(this.session.getCodeLibelle(IRFCatalogueTexte.CS_DECISION_VALIDATION));

        return pubInfosFinal;

    }

    private RFLettreEnTeteOO createLettreEnTete(String idTiers, RFDecisionDocumentData decisionDocument)
            throws Exception {
        RFLettreEnTeteOO lettreEnTete = new RFLettreEnTeteOO();

        // =======================================================
        // PRLettreEnTeteOO lettreEnTete = new PRLettreEnTeteOO();
        lettreEnTete.setSession(getSession());
        lettreEnTete.setDateDocument(dateDocument);

        // retrieve du tiers
        PRTiersWrapper tiers = PRTiersHelper.getTiersAdresseParId(getSession(), idTiers);

        if (null == tiers) {
            tiers = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
        }

        if (null != tiers) {
            lettreEnTete.setTierAdresse(tiers);
            lettreEnTete.setSession(getSession());
            lettreEnTete.setDomaineLettreEnTete(RFLettreEnTeteOO.DOMAINE_CYGNUS);
            lettreEnTete.setDecisionDocument(decisionDocument);
            lettreEnTete.generationLettre();

        }

        return lettreEnTete;
    }

    protected String formateDateDebutTraitement(String dateDebutTraitement) {
        if (dateDebutTraitement.length() == 8) {
            dateDebutTraitement = (dateDebutTraitement.substring(6) + "." + dateDebutTraitement.substring(4, 6) + "." + dateDebutTraitement
                    .substring(0, 4));
        }
        return dateDebutTraitement;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public ArrayList<RFDecisionDocumentData> getDecisionArray() {
        return decisionArray;
    }

    public String getDescription() {
        return null;
    }

    public DocumentData getDocData() {
        return docData;
    }

    public ICTDocument getDocumentHelper() {
        return documentHelper;
    }

    public ICTDocument getDocumentPrincipaleDe() {
        return documentPrincipaleDe;
    }

    public ICTDocument getDocumentPrincipaleFr() {
        return documentPrincipaleFr;
    }

    public ICTDocument getDocumentPrincipaleIt() {
        return documentPrincipaleIt;
    }

    public ICTScalableDocumentProperties getDocumentProperties() {
        return documentProperties;
    }

    public ICTDocument[] getDocumentsDe() {
        return documentsDe;
    }

    public ICTDocument[] getDocumentsFr() {
        return documentsFr;
    }

    public ICTDocument[] getDocumentsIt() {
        return documentsIt;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return null;
    }

    public StringBuffer getPdfDecisionURL() {
        return pdfDecisionURL;
    }

    public BSession getSession() {
        return session;
    }

    public BTransaction getTransaction() {
        return transaction;
    }

    /**
     * Methode pour initialiser les attributs pour la validation des décisions.
     * 
     * @param documentData
     * @param decisionDocument
     * @param memoryLog
     * @param isDecisionPonctuelle
     * @param isDecisionMensuelle
     * @param isDecisionRestitution
     * @return
     * @throws Exception
     */
    public RFGenererDecisionMainService initialiserGenerationPDFDecision(DocumentData documentData,
            RFDecisionDocumentData decisionDocument, FWMemoryLog memoryLog, boolean isDecisionPonctuelle,
            boolean isDecisionMensuelle, boolean isDecisionRestitution) throws Exception {

        RFGenererDecisionMainService rfGenererDecisionMainService = new RFGenererDecisionMainService(false);

        rfGenererDecisionMainService.setDocumentData(documentData);
        rfGenererDecisionMainService.setDecisionDocument(decisionDocument);
        rfGenererDecisionMainService.setDocumentHelper(documentHelper);
        rfGenererDecisionMainService.setDateSurDocument(getDateDocument());
        rfGenererDecisionMainService.seteMail(email);
        rfGenererDecisionMainService.setSessionCygnus(session);
        rfGenererDecisionMainService.setTypePaiement(decisionDocument.getTypePaiement());
        rfGenererDecisionMainService.setPdfDecisionURL(pdfDecisionURL);
        rfGenererDecisionMainService.setMemoryLog(memoryLog);

        return rfGenererDecisionMainService;
    }

    private String limiteStringSize(String origin, int size) {
        StringBuilder s = new StringBuilder(origin);
        s.setLength(size);
        return s.toString().trim();
    }

    /**
     * Methode pour log un message d'erreur si aucune adresse existe pour la décision courante.
     * 
     * @param memoryLog
     * @param decisionDocument
     * @throws Exception
     */
    private void logMessageSiAdresseManquante(FWMemoryLog memoryLog, RFDecisionDocumentData decisionDocument)
            throws Exception {
        memoryLog.logMessage(getSession().getLabel("ERREUR_ADRESSE_MANQUANTE") + " " + decisionDocument.getIdTiers()
                + " (" + decisionDocument.getNss() + " id décision: " + decisionDocument.getIdDecision() + ")",
                new Integer(FWMessage.ERREUR).toString(), "RFGenererDecisionServiceOO:startGenererDecisionServiceOO");
        throw new Exception(getSession().getLabel("ERREUR_ADRESSE_MANQUANTE") + " " + decisionDocument.getIdTiers()
                + " (" + decisionDocument.getNss() + " id décision: " + decisionDocument.getIdDecision() + ")");
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDecisionArray(ArrayList<RFDecisionDocumentData> decisionArray) {
        this.decisionArray = decisionArray;
    }

    public void setDocData(DocumentData docData) {
        this.docData = docData;
    }

    public void setDocumentHelper(ICTDocument documentHelper) {
        this.documentHelper = documentHelper;
    }

    public void setDocumentPrincipaleDe(ICTDocument documentPrincipaleDe) {
        this.documentPrincipaleDe = documentPrincipaleDe;
    }

    public void setDocumentPrincipaleFr(ICTDocument documentPrincipaleFr) {
        this.documentPrincipaleFr = documentPrincipaleFr;
    }

    public void setDocumentPrincipaleIt(ICTDocument documentPrincipaleIt) {
        this.documentPrincipaleIt = documentPrincipaleIt;
    }

    public void setDocumentProperties(ICTScalableDocumentProperties documentProperties) {
        this.documentProperties = documentProperties;
    }

    public void setDocumentsDe(ICTDocument[] documentsDe) {
        this.documentsDe = documentsDe;
    }

    public void setDocumentsFr(ICTDocument[] documentsFr) {
        this.documentsFr = documentsFr;
    }

    public void setDocumentsIt(ICTDocument[] documentsIt) {
        this.documentsIt = documentsIt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPdfDecisionURL(StringBuffer pdfDecisionURL) {
        this.pdfDecisionURL = pdfDecisionURL;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Methode qui va charger le catalogue de texte et parcourir chaque décision
     * 
     * @param process
     * @param memoryLog
     * @param miseEnGed
     * @param isSimulation
     * @param idLot
     * @return
     * @throws Exception
     */
    public JadePrintDocumentContainer startGenererDecisionServiceOO(AbstractJadeJob process, FWMemoryLog memoryLog,
            boolean miseEnGed, boolean isSimulation, String idLot, boolean isDecisionValide) throws Exception {

        Date d1 = new Date();
        JadePrintDocumentContainer documentContainer = null;

        docInfoFinal = createDocInfoFinal(process, miseEnGed, isSimulation, idLot);
        if (isAvasad) {
            docInfoFinal.setDocumentProperty(RFGenererDecisionServiceOO.IS_AVASAD, "true");
            docInfoFinal.setDocumentType("avasadRFM");
        } else if (isSimulation) {
            docInfoFinal.setDocumentProperty(RFGenererDecisionServiceOO.IS_DECISON_VALIDEE, "true");
            docInfoFinal.setDocumentType("simulationRFM");
            docInfoFinal.setDocumentTypeNumber("simulationRFM");
        } else {
            docInfoFinal.setDocumentType("validationRFM");
        }

        documentContainer = new JadePrintDocumentContainer();

        // Parcours des décisions pour voir si il faut créer une décision de restitution.
        RFGenererDecisionRestitutionService rfDecisionRestitutionService = new RFGenererDecisionRestitutionService();
        decisionArray = (ArrayList<RFDecisionDocumentData>) rfDecisionRestitutionService
                .createAndAddDecisionRestitution(decisionArray);

        // on charge le catalogue de texte dans une HashTable dans les 3 langues et les différents types de documents.
        chargerCataloguesTextes();

        // initialisation de la map intervalDeLettreParGestionnaire
        RFPropertiesUtils.intervalDeLettreParGestMap.clear();

        for (RFDecisionDocumentData decisionDocument : decisionArray) {
            // on vérifie qu'il a une adresse
            if (!JadeStringUtil.isBlankOrZero((decisionDocument.getIdTiers()))
                    && !JadeStringUtil.isBlankOrZero(decisionDocument.getAdresse())) {

                // On recherche quelle type de décision doit être générée
                // Traitement d'une d'écision de type restitution
                if (decisionDocument.getIsRestitution()) {

                    traiterDecision(miseEnGed, getDocData(), decisionDocument, documentContainer, transaction,
                            memoryLog, false, false, true);
                }

                // Traitement d'une d'écision de type mensuelle
                if (IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02.equals(decisionDocument.getIdTypeSoin())
                        && !decisionDocument.getIsRestitution()) {

                    traiterDecision(miseEnGed, getDocData(), decisionDocument, documentContainer, transaction,
                            memoryLog, false, true, false);
                }

                // Traitement d'une d'écision de type ponctuelle
                if (IRFTypePaiement.PAIEMENT_RETROACTIF.equals(decisionDocument.getTypePaiement())
                        && !IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02.equals(decisionDocument.getIdTypeSoin())
                        && !decisionDocument.getIsRestitution()) {
                    traiterDecision(miseEnGed, getDocData(), decisionDocument, documentContainer, transaction,
                            memoryLog, true, false, false);
                }
            }
            // Log message si pas d'adresse de paiement.
            else if (JadeStringUtil.isBlankOrZero(decisionDocument.getAdresse())) {
                logMessageSiAdresseManquante(memoryLog, decisionDocument);
            }
        }

        // Insertion dans le mail, des OV non compensés si tiers différent pour les décisions de restitution
        if (!JadeStringUtil.isBlank(messageOvDecisionrestitution)) {
            chargerMessageOvDecisionRestitution();

        }

        // Boolean reçu de la génération d'une décision unique (déjà validée). Afin de redéfinir si cette décision doit
        // être déposée sur FTP ou envoyé sur Mail
        if (isDecisionValide) {
            docInfoFinal.setDocumentProperty("noFtp", "true");
        }

        String message = docInfoFinal.getDocumentSubject() + "\n \n " + getSession().getErrors();
        docInfoFinal.setDocumentSubject(message);
        documentContainer.setMergedDocDestination(docInfoFinal);

        Date d2 = new Date();
        System.out.println("Durée pour tout la generation des décisions " + (d2.getTime() - d1.getTime()) + "\n");

        return documentContainer;
    }

    /**
     * Methode qui va traiter chaque décision
     * 
     * @param miseEnGed
     * @param documentData
     * @param decisionDocument
     * @param documentContainer
     * @param transaction
     * @param memoryLog
     * @param isDecisionPonctuelle
     * @param isDecisionMensuelle
     * @param isDecisionRestitution
     * @throws Exception
     */
    protected void traiterDecision(boolean miseEnGed, DocumentData documentData,
            RFDecisionDocumentData decisionDocument, JadePrintDocumentContainer documentContainer,
            BTransaction transaction, FWMemoryLog memoryLog, boolean isDecisionPonctuelle, boolean isDecisionMensuelle,
            boolean isDecisionRestitution) throws Exception {

        RFGenererDecisionMainService rfGenererDecisionMainService = initialiserGenerationPDFDecision(documentData,
                decisionDocument, memoryLog, isDecisionPonctuelle, isDecisionMensuelle, isDecisionRestitution);

        // RFGenererDecisionMainService rfGenererDecisionMainService = new RFGenererDecisionMainService();

        // remplir la décision principale
        documentContainer = rfGenererDecisionMainService.remplirDecision(miseEnGed, memoryLog, documentContainer,
                isDecisionPonctuelle, isDecisionMensuelle, isDecisionRestitution, documentHelper, catalogueMultiLangue);

        if (!JadeStringUtil.isBlank(rfGenererDecisionMainService.getMessageOv())) {
            messageOvDecisionrestitution = messageOvDecisionrestitution + "\n"
                    + rfGenererDecisionMainService.getMessageOv();
        }

        // Création d'une liste pour supprimer les doublons
        Set<String> listeDemande = new HashSet<String>();

        // Ajout de chaque idTiers de l'assuré concerné dans la liste
        for (RFDemandeValidationData demande : decisionDocument.getDecisionDemande()) {
            listeDemande.add(demande.getIdTiersAssureConcerne());
        }

        // Création d'un iterateur pour parcourir chaque élément inséré dans la liste
        Iterator<String> elementListeDemande = listeDemande.iterator();

        // ajout de l'annexe bordereau d'accompagnement si coché
        if ((decisionDocument.getIsBordereauAccompagnement()) && (!decisionDocument.getIsRestitution())) {

            JadePublishDocumentInfo docInfoBordereau = new JadePublishDocumentInfo();
            docInfoBordereau.setPublishDocument(false);
            docInfoBordereau.setArchiveDocument(false);
            docInfoBordereau.setDocumentTypeNumber(IPRConstantesExternes.RFM_BORDEREAU);
            docInfoBordereau.setDocumentType(IPRConstantesExternes.RFM_BORDEREAU);

            if (RFPropertiesUtils.imprimerDecisionsRectoVerso()) {
                docInfoBordereau.setDuplex(Boolean.TRUE);
                docInfoBordereau.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
            }

            if (RFPropertiesUtils.insererLigneTechniqueDeRegroupement()) {

                docInfoBordereau.setDocumentProperty("AGLA_LT_PCRFM_DOC_TYPE_NUMBER",
                        IPRConstantesExternes.RFM_BORDEREAU);
                docInfoBordereau.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "O");
                docInfoBordereau.setDocumentProperty("AGLA_LT_PCRFM_NSS_TIERS", decisionDocument.getNss());
                docInfoBordereau.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", decisionDocument.getIdTiers());
                docInfoBordereau.setDocumentProperty("AGLA_LT_PCRFM_ID_DECISION", decisionDocument.getIdDecision());
                docInfoBordereau.setDocumentProperty("AGLA_LT_PCRFM_NOM_TIERS",
                        limiteStringSize(decisionDocument.getNom(), RFGenererDecisionServiceOO.NOM_PRENOM_SIZE));
                docInfoBordereau.setDocumentProperty("AGLA_LT_PCRFM_PRENOM_TIERS",
                        limiteStringSize(decisionDocument.getPrenom(), RFGenererDecisionServiceOO.NOM_PRENOM_SIZE));

                String idTiersConcerne = elementListeDemande.next().toString();
                DocumentData dataBordereau = ajoutAnnexeBordereau(decisionDocument, idTiersConcerne);

                TIDocumentInfoHelper.fill(docInfoBordereau, decisionDocument.getIdTiers(), getSession(), null, null,
                        null);

                if (miseEnGed) {
                    if (PRGedUtils.isDocumentInGed(IPRConstantesExternes.RFM_BORDEREAU, session)) {
                        docInfoBordereau.setArchiveDocument(true);
                    }
                }

                documentContainer.addDocument(dataBordereau, docInfoBordereau);
                documentContainer.addDocument(dataBordereau, docInfoBordereau);

            } else {
                // Création de 2 bordereaux pour chaque assuré concerné
                while (elementListeDemande.hasNext()) {

                    TIDocumentInfoHelper.fill(docInfoBordereau, decisionDocument.getIdTiers(), getSession(), null,
                            null, null);

                    if (miseEnGed) {
                        if (PRGedUtils.isDocumentInGed(IPRConstantesExternes.RFM_BORDEREAU, session)) {
                            docInfoBordereau.setArchiveDocument(true);
                        }
                    }

                    String idTiersConcerne = elementListeDemande.next().toString();
                    DocumentData dataBordereau = ajoutAnnexeBordereau(decisionDocument, idTiersConcerne);
                    documentContainer.addDocument(dataBordereau, docInfoBordereau);
                    documentContainer.addDocument(dataBordereau, docInfoBordereau);
                }
            }
        }

        // Création des copies : il faut récupérer le tableau de copies attaché à la décision
        ArrayList<RFCopieDecisionsValidationData> copies = decisionDocument.getCopieDecision();

        // on vérifie qu'il a une adresse
        for (RFCopieDecisionsValidationData copie : copies) {
            if (!JadeStringUtil.isBlankOrZero((copie.getIdDestinataire()))
                    && !JadeStringUtil.isBlankOrZero(decisionDocument.getAdresse())) {
                ajoutCopie(decisionDocument, rfGenererDecisionMainService, copie, documentContainer, memoryLog,
                        miseEnGed, isDecisionPonctuelle, isDecisionMensuelle, isDecisionRestitution, documentHelper);
            } else if (JadeStringUtil.isBlankOrZero(decisionDocument.getAdresse())) {

                memoryLog.logMessage(
                        getSession().getLabel("ERREUR_ADRESSE_MANQUANTE") + " " + copie.getIdDestinataire(),
                        new Integer(FWMessage.ERREUR).toString(), "RFGenererDecisionServiceOO:traiterDecision");
                throw new Exception(getSession().getLabel("ERREUR_ADRESSE_MANQUANTE"));

            }
        }
    }

}
