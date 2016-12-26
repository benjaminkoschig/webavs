package globaz.corvus.process;

import globaz.babel.db.copies.CTCopies;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.db.decisions.RECopieDecision;
import globaz.corvus.db.decisions.RECopieDecisionManager;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.topaz.REDecisionOO;
import globaz.corvus.utils.REGedUtils;
import globaz.corvus.vb.decisions.RECopieDecisionViewBean;
import globaz.corvus.vb.decisions.REDecisionsContainer;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.ged.PRGedHelper;
import globaz.prestation.helpers.PRHybridHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.itext.PRLettreEnTete;
import globaz.prestation.topaz.PRLettreEnTeteOO;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DocumentData;

public class REImprimerDecisionProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDocument = "";
    private REDecisionEntity decision = new REDecisionEntity();
    private REDecisionsContainer decisionContainer = new REDecisionsContainer();
    private String eMailAddress = "";
    private StringBuffer errorBuffer = new StringBuffer("");
    private String idDecision = "";
    private String idDemandeRente = "";
    private Boolean isOnlySendGED = Boolean.FALSE;
    private Boolean isSendToGed = null;

    private REDecisionOO createDecisionCopie(RECopieDecision copie) throws Exception {

        REDecisionOO decisionCopie = new REDecisionOO();
        decisionCopie.setSession(getSession());
        decisionCopie.setAdresseEmail(eMailAddress);
        decisionCopie.setDecision(decision);
        decisionCopie.setDecisionsContainer(decisionContainer);
        decisionCopie.setIsCopie(true);
        decisionCopie.setCopieDecision(copie);
        decisionCopie.setDateDocument(getDateDocument());
        decisionCopie.run();

        if (!JadeStringUtil.isBlankOrZero(decisionCopie.getErrorBuffer().toString())) {
            errorBuffer.append(decisionCopie.getErrorBuffer() + "\n");
        }

        return decisionCopie;
    }

    private REDecisionOO createDecisionOriginale() throws Exception {

        REDecisionOO decisionOriginale = new REDecisionOO();
        decisionOriginale.setSession(getSession());
        decisionOriginale.setAdresseEmail(eMailAddress);
        decisionOriginale.setDecision(decision);
        decisionOriginale.setDecisionsContainer(decisionContainer);
        decisionOriginale.setIsCopie(false);
        decisionOriginale.setDateDocument(getDateDocument());

        decisionOriginale.run();

        if (!JadeStringUtil.isBlankOrZero(decisionOriginale.getErrorBuffer().toString())) {
            errorBuffer.append(decisionOriginale.getErrorBuffer() + "\n");
        }

        return decisionOriginale;
    }

    private DocumentData createLettreEntete(RECopieDecision copie) throws Exception {

        PRLettreEnTeteOO lettreEnTete = new PRLettreEnTeteOO();
        lettreEnTete.setSession(getSession());

        // retrieve du tiers
        PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), copie.getIdTiersCopie());

        // BZ 5536
        // retrieve de la procédure de communication si elle est définie
        CTCopies procedureCommunication = null;
        if (!JadeStringUtil.isBlankOrZero(copie.getIdProcedureCommunication())) {
            procedureCommunication = new CTCopies();
            procedureCommunication.setSession(getSession());
            procedureCommunication.setIdCopie(copie.getIdProcedureCommunication());
            procedureCommunication.retrieve();
        }

        if (null == tier) {
            tier = PRTiersHelper.getAdministrationParId(getSession(), copie.getIdTiersCopie());
        }

        if (null == tier) {
            errorBuffer.append("Impossible de créer la lettre en-tête : TIER = NULL (id=" + copie.getIdTiersCopie()
                    + ")");
            return null;
        } else {
            lettreEnTete.setTierAdresse(tier);
            lettreEnTete.setSession(getSession());
            lettreEnTete.setDomaineLettreEnTete(PRLettreEnTete.DOMAINE_CORVUS);
            // lettreEnTete.setDateDocument(JACalendar.format(this.getDateDocument(),
            // tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE)));
            lettreEnTete.setDateDocument(getDateDocument());
            // BZ 5536
            if ((procedureCommunication != null) && !procedureCommunication.isNew()) {
                lettreEnTete.setReferenceProcedureComunication(procedureCommunication.getReference());
            }
            lettreEnTete.generationLettre();

        }

        return lettreEnTete.getDocumentData();
    }

    public String getDateDocument() {
        return dateDocument;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW");
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public Boolean getIsOnlySendGED() {
        return isOnlySendGED;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    @Override
    public String getName() {
        return getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW");
    }

    @Override
    public void run() {
        try {
            PRHybridHelper.initContext(getSession(), this);

            JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

            JadePublishDocumentInfo pubInfosGen = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfosGen.setOwnerEmail(getEMailAddress());
            pubInfosGen.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            pubInfosGen.setDocumentTitle(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW"));
            pubInfosGen.setDocumentSubject(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW"));
            pubInfosGen.setArchiveDocument(false);

            if (isOnlySendGED.booleanValue()) {
                pubInfosGen.setPublishDocument(false);
            } else {
                pubInfosGen.setPublishDocument(true);
            }

            // 1. Charger la structure des décisions (REDecisionsContainer)

            // 1a. Retrieve de la décision
            decision.setIdDecision(getIdDecision());
            decision.setId(getIdDecision());
            decision.setSession(getSession());
            decision.retrieve();

            // Inforom 529 : gérer le document TYPE NUMBER en fonction du type de rente
            // On vas se baser sur le type de la demande pour déterminer le type de décision
            REDemandeRente demande = new REDemandeRente();
            demande.setSession(getSession());
            demande.setIdDemandeRente(decision.getIdDemandeRente());
            demande.retrieve();

            PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), decision.getIdTiersBeneficiairePrincipal());

            if (tier != null) {
                pubInfosGen.setDocumentSubject(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW") + " " + "("
                        + tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " / "
                        + tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + ")");
            } else {
                pubInfosGen.setDocumentSubject(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW"));
            }

            allDoc.setMergedDocDestination(pubInfosGen);

            // 1b. Chargement de la structure (REDecisionsContainer)
            decisionContainer.loadDecision(getSession(), decision);
            decisionContainer.parcourDecisionsIC(getSession());
            decisionContainer.setIdDemandeRente(decision.getIdDemandeRente());

            REDecisionOO decisionOO;

            JadePublishDocumentInfo pubInfosDecision = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfosDecision.setOwnerEmail(getEMailAddress());
            pubInfosDecision.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            pubInfosDecision.setDocumentTitle(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW"));
            pubInfosDecision.setDocumentSubject(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW"));
            pubInfosDecision.setArchiveDocument(false);
            pubInfosDecision.setPublishDocument(false);
            pubInfosDecision.setDocumentType(IRENoDocumentInfoRom.DECISION_DE_RENTES);
            pubInfosDecision.setDocumentProperty(
                    REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                    REGedUtils.getCleGedPourTypeRente(getSession(),
                            REGedUtils.getTypeRentePourCetteDecision(getSession(), decision)));
            pubInfosDecision.setDocumentDate(getDateDocument());

            TIDocumentInfoHelper.fill(pubInfosDecision, decision.getIdTiersBeneficiairePrincipal(), getSession(), null,
                    null, null);

            // GED ??
            if ((isSendToGed != null) && isSendToGed.booleanValue()) {
                pubInfosDecision.setArchiveDocument(true);

                try {
                    // bz-5941
                    PRGedHelper h = new PRGedHelper();
                    // Traitement uniquement pour la caisse concernée (CCB)
                    if (h.isExtraNSS(getSession())) {
                        pubInfosDecision = h.setNssExtraFolderToDocInfo(getSession(), pubInfosDecision,
                                decision.getIdTiersBeneficiairePrincipal());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            // 2. Création de la décision originale
            decisionOO = createDecisionOriginale();
            pubInfosDecision.setDocumentTypeNumber(decisionOO.getDocumentTypeNumber());
            allDoc.addDocument(decisionOO.getDocumentData(), pubInfosDecision);

            // 2a. Création des copies prédéfinis par les properties (caisse et
            // OAI)

            JadePublishDocumentInfo pubInfosCopieDecision = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfosCopieDecision.setOwnerEmail(getEMailAddress());
            pubInfosCopieDecision.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            pubInfosCopieDecision.setDocumentTitle(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW"));
            pubInfosCopieDecision.setDocumentSubject(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW"));
            pubInfosCopieDecision.setArchiveDocument(false);
            pubInfosCopieDecision.setPublishDocument(false);
            pubInfosCopieDecision.setDocumentType(IRENoDocumentInfoRom.DECISION_DE_RENTES);
            pubInfosCopieDecision.setDocumentTypeNumber(decisionOO.getDocumentTypeNumber());
            pubInfosCopieDecision.setDocumentDate(getDateDocument());

            TIDocumentInfoHelper.fill(pubInfosCopieDecision, decision.getIdTiersBeneficiairePrincipal(), getSession(),
                    null, null, null);

            JadePublishDocumentInfo pubInfosLettreEnTete = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfosLettreEnTete.setOwnerEmail(getEMailAddress());
            pubInfosLettreEnTete.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            pubInfosLettreEnTete.setDocumentTitle(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW"));
            pubInfosLettreEnTete.setDocumentSubject(getSession().getLabel("TITRE_REIMPRIMERDECISIONPROCESSNEW"));
            pubInfosLettreEnTete.setArchiveDocument(false);
            pubInfosLettreEnTete.setPublishDocument(false);
            pubInfosLettreEnTete.setDocumentType(IRENoDocumentInfoRom.LETTRE_ACCOMPAGNEMENT_DE_COPIE_RENTES);
            pubInfosLettreEnTete.setDocumentTypeNumber(IRENoDocumentInfoRom.LETTRE_ACCOMPAGNEMENT_DE_COPIE_RENTES);
            pubInfosLettreEnTete.setDocumentDate(JACalendar.todayJJsMMsAAAA());

            TIDocumentInfoHelper.fill(pubInfosLettreEnTete, decision.getIdTiersBeneficiairePrincipal(), getSession(),
                    null, null, null);

            // Copie caisse ?
            boolean isCopieCaissePrint = false;

            if (decisionOO.getTypeDecision().startsWith("AVS") || decisionOO.getTypeDecision().endsWith("AVS")) {

                if (getSession().getApplication().getProperty("isCopieCaisse").equals("true")) {

                    // Ne pas stocker, directement imprimer
                    DocumentData copieExacte = new DocumentData();

                    List<Collection> collections = null;
                    for (Entry<String, List<Collection>> e : decisionOO.getDocumentData().getCollections().entrySet()) {
                        if (collections == null) {
                            collections = new ArrayList<Collection>(e.getValue());
                        } else {
                            collections.addAll(e.getValue());
                        }
                    }
                    for (Iterator<Collection> iterator = collections.iterator(); iterator.hasNext();) {
                        Collection coll = iterator.next();
                        copieExacte.add(coll);
                    }

                    Map<String, String> map = decisionOO.getDocumentData().getDatabag();
                    Iterator<String> iter = decisionOO.getDocumentData().getDatabag().keySet().iterator();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        String value = map.get(key);
                        copieExacte.addData(key, value);
                        copieExacte.addData("IS_COPIE", decisionOO.getTexteCopie());
                    }
                    allDoc.addDocument(copieExacte, pubInfosCopieDecision);
                    isCopieCaissePrint = true;
                }
            }

            // Copie Office AI ?
            if (decisionOO.isCopieOAI()) {
                String noOfficeAI = decisionOO.officeAI;

                REDemandeRenteInvalidite demOAI = new REDemandeRenteInvalidite();
                demOAI.setIdDemandeRente(decisionOO.getDecision().getIdDemandeRente());
                demOAI.setSession(getSession());
                demOAI.retrieve();

                if (demOAI.isNew()) {
                    REDemandeRenteAPI demOAIAPI = new REDemandeRenteAPI();
                    demOAIAPI.setIdDemandeRente(decisionOO.getDecision().getIdDemandeRente());
                    demOAIAPI.setSession(getSession());
                    demOAIAPI.retrieve();
                    if (!demOAIAPI.isNew()) {
                        noOfficeAI = demOAIAPI.getCodeOfficeAI();
                    }
                } else {
                    noOfficeAI = demOAI.getCodeOfficeAI();
                }

                TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
                tiAdministrationMgr.setSession(getSession());
                tiAdministrationMgr.setForCodeAdministration(noOfficeAI);
                tiAdministrationMgr.setForGenreAdministration("509004");
                tiAdministrationMgr.find();

                TIAdministrationViewBean tiAdministration = PRTiersHelper.resolveAdminFromTiersLanguage(tier,
                        tiAdministrationMgr);

                if (null != tiAdministration) {

                    // Ne pas stocker, directement imprimer
                    RECopieDecisionViewBean copie = new RECopieDecisionViewBean();
                    copie.setSession(getSession());
                    copie.setIdDecision(decision.getIdDecision());
                    copie.setIdTiersCopie(tiAdministration.getIdTiers());
                    copie.setIdAffilie(tiAdministration.getIdTiersExterne());

                    if (getSession().getApplication().getProperty("isLettreEnTeteCopieOAI").equals("true")) {
                        allDoc.addDocument(createLettreEntete(copie), pubInfosLettreEnTete);
                    }

                    DocumentData copieExacte = new DocumentData();

                    List<Collection> collections = null;
                    for (Entry<String, List<Collection>> e : decisionOO.getDocumentData().getCollections().entrySet()) {
                        if (collections == null) {
                            collections = new ArrayList<Collection>(e.getValue());
                        } else {
                            collections.addAll(e.getValue());
                        }
                    }
                    for (Iterator<Collection> iterator = collections.iterator(); iterator.hasNext();) {
                        Collection coll = iterator.next();
                        copieExacte.add(coll);
                    }

                    Map<String, String> map = decisionOO.getDocumentData().getDatabag();
                    Iterator<String> iter = decisionOO.getDocumentData().getDatabag().keySet().iterator();
                    while (iter.hasNext()) {

                        String key = iter.next();
                        String value = map.get(key);
                        copieExacte.addData(key, value);
                        copieExacte.addData("IS_COPIE", decisionOO.getTexteCopie());

                    }

                    allDoc.addDocument(copieExacte, pubInfosCopieDecision);

                } else {
                    errorBuffer.append(getSession().getLabel("WARNING_OFFICE_AI") + "\n");
                }
            }

            // 3. Création des annexes et copie
            RECopieDecisionManager copieMgr = new RECopieDecisionManager();
            copieMgr.setSession(getSession());
            copieMgr.setForIdDecision(getIdDecision());
            copieMgr.setOrderBy(RECopieDecision.FIELDNAME_ID_TIERS_COPIE);
            copieMgr.find();

            for (int i = 0; i < copieMgr.size(); i++) {
                RECopieDecision copieDecision = (RECopieDecision) copieMgr.get(i);

                if (copieDecision.getIdTiersCopie().equals("1") && isCopieCaissePrint) {
                    continue;
                } else {
                    if (copieDecision.getIsPageGarde().booleanValue()) {
                        allDoc.addDocument(createLettreEntete(copieDecision), pubInfosLettreEnTete);
                    }

                    allDoc.addDocument(createDecisionCopie(copieDecision).getDocumentData(), pubInfosCopieDecision);
                }

            }

            this.createDocuments(allDoc);

        } catch (Exception e) {
            errorBuffer.append(e.getMessage());
        } finally {
            PRHybridHelper.stopUsingContext(this);
        }

        if (errorBuffer.length() > 0) {
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, "REDecisionOO", errorBuffer.toString()));
            List<String> emails = new ArrayList<String>();
            emails.add(getEMailAddress());
            try {
                sendCompletionMail(emails);
            } catch (Exception e) {
                JadeLogger.error(this, e);
                e.printStackTrace();
            }
        }
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIsOnlySendGED(Boolean isOnlySendGED) {
        this.isOnlySendGED = isOnlySendGED;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }
}
