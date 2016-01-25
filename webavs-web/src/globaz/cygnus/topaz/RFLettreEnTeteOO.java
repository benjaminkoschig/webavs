package globaz.cygnus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.api.decisions.IRFCodesIsoLangue;
import globaz.cygnus.api.decisions.IRFGenererDocumentDecision;
import globaz.cygnus.services.genererDecision.RFGenererDecisionService;
import globaz.cygnus.services.validerDecision.RFDecisionDocumentData;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater03;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.util.Hashtable;
import java.util.Map;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author fha
 * @modified mbo
 */
public class RFLettreEnTeteOO {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String DOMAINE_CYGNUS = "CYGNUS";

    public static final String FICHIER_MODELE_ENTETE_RFM = "RF_LETTRE_EN_TETE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private ICaisseReportHelperOO caisseHelper;
    private Hashtable catalogueMultiLangue = new Hashtable();
    private String catTexteLettre = "catTexteLettre";
    private DocumentData data;
    private String dateDecision = "";
    private String dateDocument = "";
    private RFDecisionDocumentData decisionDocument;
    private ICTDocument document;
    private DocumentData documentData;
    private ICTDocument documentHelper;
    private String domaineLettreEnTete = "";
    private String idAffilie;
    private boolean isDemandeCompensation = false;
    private Boolean isEnteteOAI = Boolean.FALSE;
    private int nbTrue = 0;
    private String noOfficeAI = "";
    private String numeroDeDecisionAI = "";
    private String periodeDecision = "";
    private String reference = "";
    private BSession session;
    private String specificHeader = "";
    private String telGestionnaireGrpLettre = "";
    private PRTiersWrapper tierAdresse;

    /**
     * Methode pour afficher en bas de page, les annexes et/ou copies de décisions ponctuelles et mensuelles
     * 
     * @param afficheAnnexes
     * @throws Exception
     */
    protected void ajoutAnnexesEtCopies() throws Exception {

        // Insertion du tableau des annexes et copies
        data.addData("isTableauAnnexesEtCopiesInclude", "STANDARD");

        // Initialisation du nom de tableau
        Collection tabAnnexesEtCopies = new Collection(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_ANNEXES_ET_COPIES);

        // Déclaration de la ligne du tableau
        DataList ligneAnnexe = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE_DATA);

        // Insertion du titre : Annexe, dans la ligne
        ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE, document.getTextes(2).getTexte(4)
                .getDescription());

        // Insertion du texte : Annexe, dans la ligne
        ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_ANNEXE, document.getTextes(2).getTexte(5)
                .getDescription());

        // Ajout de la ligne à la collection
        tabAnnexesEtCopies.add(ligneAnnexe);

        data.add(tabAnnexesEtCopies);

    }

    @SuppressWarnings("unchecked")
    private void chargementCatalogueTexte() throws Exception {

        // chargement du catalogue de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());

        if (getDomaineLettreEnTete().equals(RFLettreEnTeteOO.DOMAINE_CYGNUS)) {
            documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
            documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_LETTRE_ENTETE);
        }
        documentHelper.setDefault(Boolean.TRUE);
        documentHelper.setActif(Boolean.TRUE);

        // Chargement des textes en français
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_FR);
        ICTDocument[] documentsFr = documentHelper.load();
        catalogueMultiLangue.put(catTexteLettre + "_" + IRFCodesIsoLangue.LANGUE_ISO_FR, documentsFr[0]);

        // Chargement des textes en allemand
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_DE);
        ICTDocument[] documentsDe = documentHelper.load();
        catalogueMultiLangue.put(catTexteLettre + "_" + IRFCodesIsoLangue.LANGUE_ISO_DE, documentsDe[0]);

        // Chargement des textes en italien
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_IT);
        ICTDocument[] documentsIt = documentHelper.load();
        catalogueMultiLangue.put(catTexteLettre + "_" + IRFCodesIsoLangue.LANGUE_ISO_IT, documentsIt[0]);

    }

    public void generationLettre() throws Exception {

        if (catalogueMultiLangue.isEmpty()) {
            chargementCatalogueTexte();
        }

        data = new DocumentData();

        // Chargement du document
        data.addData("idProcess", "RFMLettreEnTeteOO");

        remplissageDocument();

        gestionEnTeteEtSignature();

    }

    private void gestionEnTeteEtSignature() throws Exception {

        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

        String adresse = "";

        if (getDomaineLettreEnTete().equals(RFLettreEnTeteOO.DOMAINE_CYGNUS)) {
            adresse = PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getIdAffilie(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG);

            // Si langue français, utilisation du texte en français
            if (IRFCodesIsoLangue.LANGUE_ISO_FR.equals(getSession().getCode(
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)))) {
                document = (ICTDocument) catalogueMultiLangue.get(catTexteLettre + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_FR);
            }
            // Sinon si langue allemande, utilisation du texte en allemand
            else if (IRFCodesIsoLangue.LANGUE_ISO_DE.equals(getSession().getCode(
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)))) {
                document = (ICTDocument) catalogueMultiLangue.get(catTexteLettre + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_DE);
            }
            // Sinon si langue en italien, utilisation du texte en italien
            else if (IRFCodesIsoLangue.LANGUE_ISO_IT.equals(getSession().getCode(
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)))) {
                document = (ICTDocument) catalogueMultiLangue.get(catTexteLettre + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_IT);
            }
            // Si le tiers n'a pas de langue, on remonte l'exception avec le détail du tiers
            else {
                throw new Exception("\n" + "Pas de langue trouvée pour le tiers : " + "\n" + "id tiers : "
                        + tierAdresse.getIdTiers() + "\n" + "nom tiers : " + tierAdresse.getNom() + " "
                        + tierAdresse.getPrenom());
            }

            // Ajoute dans l'entête de la lettre qui a traité le dossier si
            // necessaire
            if ((Boolean.TRUE.toString()).equals(getSession().getApplication()
                    .getProperty("isAfficherDossierTraitePar"))) {
                crBean.setNomCollaborateur(document.getTextes(1).getTexte(5).getDescription() + " "
                        + getSession().getUserFullName());
                crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            }

            if (Boolean.TRUE.toString().equals(getSession().getApplication().getProperty("documents.is.confidentiel"))) {
                crBean.setConfidentiel(true);
            }

        }
        // Sinon, erreur dans le chargement du catalogue de texte
        else {
            throw new Exception("Erreur dans le chargement du texte de la lettre en tête / gestionEnTeteEtSignature");
        }

        crBean.setAdresse(adresse);

        RFGenererDecisionService rfGenererDecisionService = new RFGenererDecisionService();
        // Ajoute la date dans l'entête de la lettre sauf si la date est
        // renseignée
        if (JadeStringUtil.isBlankOrZero(getDateDocument())) {
            String dateOfTheDay = JACalendar.todayJJsMMsAAAA();
            crBean.setDate(rfGenererDecisionService.getDateFormatterWithLanguage(dateOfTheDay,
                    getSession().getCode(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));
        } else {
            crBean.setDate(rfGenererDecisionService.getDateFormatterWithLanguage(getDateDocument(), getSession()
                    .getCode(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));

        }

        caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                getSession().getCode(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)));

        if (getDomaineLettreEnTete().equals(RFLettreEnTeteOO.DOMAINE_CYGNUS)) {
            caisseHelper.setTemplateName(RFLettreEnTeteOO.FICHIER_MODELE_ENTETE_RFM);
        }

        JadeUser user = getSession().getApplication()._getSecurityManager()
                .getUserForVisa(session, decisionDocument.getGestionnaire());

        // 1) EnTete et signature OAI
        if (getIsEnteteOAI().booleanValue()) {

            // En-tête
            data.addData("idEntete", "HEADER_CAISSE");

            // Recherche de l'adresse selon code Administration
            String adresseOfficeAi = "";

            TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
            tiAdministrationMgr.setSession(getSession());
            tiAdministrationMgr.setForCodeAdministration(getNoOfficeAI());
            tiAdministrationMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
            tiAdministrationMgr.changeManagerSize(0);
            tiAdministrationMgr.find();

            TIAdministrationViewBean tiAdministration = (TIAdministrationViewBean) tiAdministrationMgr.getFirstEntity();

            if (getDomaineLettreEnTete().equals(RFLettreEnTeteOO.DOMAINE_CYGNUS)) {
                adresseOfficeAi = tiAdministration.getAdresseAsString(TIAvoirAdresse.CS_COURRIER,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_DEFAULT, JACalendar.todayJJsMMsAAAA(),
                        new PRTiersAdresseCopyFormater03());
            }

            data.addData("HEADER_ADRESSE_CAISSE", adresseOfficeAi);

            // Signature
            if (null != tiAdministration) {
                data.addData("SIGNATURE_NOM_CAISSE", tiAdministration.getNomPrenom());
            } else {
                data.addData("SIGNATURE_NOM_CAISSE", "Office AI introuvable");
            }
            // 2) EnTete et signature Caisse
        } else {

            // Chargement de l'En-tête
            data.addData("idEntete", "HEADER_CAISSE");
            // Chargement de la Signature
            data.addData("idSignature", "SIGNATURE_RFM_STANDART");

            // Insertion de l'entête
            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            // Insertion de la signature
            data = caisseHelper.addSignatureParameters(data, crBean);
        }

        // ### RUBRIQUES SPECIFIQUES A LAUSANNE ###
        // ----------------------------------------

        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_GESTIONNAIRE_AGLA,
                getNomGestionnaireAAfficher(user.getLastname() + " " + user.getFirstname()));
        if (!JadeStringUtil.isEmpty(telGestionnaireGrpLettre)) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE_AGLAU, telGestionnaireGrpLettre);
        } else {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE_AGLAU, user.getPhone());
        }
        data.addData(IRFGenererDocumentDecision.ID_GESTIONNAIRE_VALIDATION, decisionDocument.getGestionnaire());
        // --------------------------------------------
        // ### FIN RUBRIQUES SPECIFIQUES A LAUSANNE ###

        String nssTiers = decisionDocument.getNss();
        if (JadeStringUtil.isEmpty(nssTiers) && !JadeStringUtil.isEmpty(decisionDocument.getIdTiers())) {
            PRTiersWrapper tiersW = PRTiersHelper.getTiersParId(session, decisionDocument.getIdTiers());
            nssTiers = tiersW.getNSS();
        }

        data.addData("NSS_BENEFICIAIRE", nssTiers);

        data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public String getDomaineLettreEnTete() {
        return domaineLettreEnTete;
    }

    /**
     * Methode pour retourner le nom + prenom du gestionnaire en charge de la decision
     * 
     * @param gestionnaire
     * @return
     * @throws Exception
     */
    private String getFirstNameLastNameGestionnaire(String gestionnaire) throws Exception {

        JadeUser userName;

        try {
            userName = getSession().getApplication()._getSecurityManager().getUserForVisa(getSession(), gestionnaire);

            telGestionnaireGrpLettre = userName.getPhone();

        } catch (Exception ex) {
            throw new Exception("Problem with id gestionnaire: [" + gestionnaire + "] /n" + ex.getMessage());
        }
        return userName.getFirstname() + " " + userName.getLastname();
    }

    /**
     * Methode pour retourner le visa du gestionnaire en charge de la décision
     * 
     * @param premiereLettreNomBeneficiaire
     * @return
     * @throws Exception
     */
    private String getGroupeGestionnaire(char premiereLettreNomBeneficiaire) throws Exception {

        // On récupère une map d'interval de lettre (Ex: D-O) par gestionnaire
        Map<String, String> intervalDeLettreParGestMap = RFPropertiesUtils.getIntervalDeLettreParGestionnaire();

        // On détermine à quel interval de lettre appartient la décision
        for (String intervalleKey : intervalDeLettreParGestMap.keySet()) {

            String[] intervalles = intervalleKey.split("-");
            char lettreDebut = intervalles[0].charAt(0);
            char lettreFin = intervalles[1].charAt(0);

            if ((premiereLettreNomBeneficiaire >= lettreDebut) && (premiereLettreNomBeneficiaire <= lettreFin)) {
                return intervalDeLettreParGestMap.get(intervalleKey);
            }
        }
        return "";
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public Boolean getIsEnteteOAI() {
        return isEnteteOAI;
    }

    public int getNbTrue() {
        return nbTrue;
    }

    /**
     * Methode pour retourner le nom du gestionnaire ayant valider la décision ou du gestionnaire en cahrge de la
     * decision, selon propriété
     * 
     * @param nomGestionnaireDecision
     * @return
     * @throws Exception
     */
    private String getNomGestionnaireAAfficher(String nomGestionnaireDecision) throws Exception {
        String nomGestionnaireAAfficher = "";
        if (!RFPropertiesUtils.utiliserGroupesGestionnaires()) {
            nomGestionnaireAAfficher = nomGestionnaireDecision;
        } else {
            if (!JadeStringUtil.isEmpty(decisionDocument.getNom())) {
                nomGestionnaireAAfficher = getFirstNameLastNameGestionnaire(getGroupeGestionnaire(decisionDocument
                        .getNom().substring(0, 1).toUpperCase().charAt(0)));

            }
        }
        return nomGestionnaireAAfficher;
    }

    public String getNoOfficeAI() {
        return noOfficeAI;
    }

    public String getNumeroDeDecisionAI() {
        return numeroDeDecisionAI;
    }

    public String getPeriodeDecision() {
        return periodeDecision;
    }

    public BSession getSession() {
        return session;
    }

    public String getSpecificHeader() {
        return specificHeader;
    }

    public PRTiersWrapper getTierAdresse() {
        return tierAdresse;
    }

    public boolean isDemandeCompensation() {
        return isDemandeCompensation;
    }

    private void remplissageDocument() throws Exception {

        try {

            // Insertion du titre du tiers
            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }

            // Si langue français, utilisation du texte en français
            if (IRFCodesIsoLangue.LANGUE_ISO_FR.equals(getSession().getCode(
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)))) {
                document = (ICTDocument) catalogueMultiLangue.get(catTexteLettre + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_FR);

            }
            // Sinon si langue allemande, utilisation du texte en allemand
            else if (IRFCodesIsoLangue.LANGUE_ISO_DE.equals(getSession().getCode(
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)))) {
                document = (ICTDocument) catalogueMultiLangue.get(catTexteLettre + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_DE);
            }
            // Sinon si langue en italien, utilisation du texte en italien
            else if (IRFCodesIsoLangue.LANGUE_ISO_IT.equals(getSession().getCode(
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)))) {
                document = (ICTDocument) catalogueMultiLangue.get(catTexteLettre + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_IT);
            }
            // Si le tiers n'a pas de langue, on remonte l'exception avec le détail du tiers
            else {
                throw new Exception("\n" + "Pas de langue trouvée pour le tiers : " + "\n" + "id tiers : "
                        + tierAdresse.getIdTiers() + "\n" + "nom tiers : " + tierAdresse.getNom() + " "
                        + tierAdresse.getPrenom());
            }

            StringBuffer buffer = new StringBuffer();

            buffer.append(tiersTitre.getFormulePolitesse(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)));

            // Récupération du gestionnaire
            JadeUser userName = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), getSession().getUserId());

            // Insertion de la personne de référence
            if (userName != null) {
                data.addData("LIBELLE_REF", document.getTextes(1).getTexte(5).getDescription());
                data.addData("GESTIONNAIRE", userName.getFirstname() + " " + userName.getLastname());
                data.addData("TEL_GESTIONNAIRE", userName.getPhone());
            }

            // Chargement du NSS de l'assuré
            // this.data.addData("NSS_BENEFICIAIRE", this.tierAdresse.getNSS());

            // Insertion du titre de l'ayant droit
            data.addData("titreTiers", buffer.toString());

            // Insertion du texte
            data.addData("TEXTE_LETTRE", document.getTextes(2).getTexte(2).getDescription());

            // Insertion des salutations
            data.addData("SALUTATIONS", PRStringUtils.replaceString(document.getTextes(2).getTexte(3).getDescription(),
                    "{titreTiers}",
                    tiersTitre.getFormulePolitesse(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));

            // Insertion de l'annexe en bas de page
            // this.data.addData("annexe", this.document.getTextes(2).getTexte(4).getDescription());
            ajoutAnnexesEtCopies();

            setDocumentData(data);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    /**
     * @param decisionDocument
     *            the decisionDocument to set
     */
    public void setDecisionDocument(RFDecisionDocumentData decisionDocument) {
        this.decisionDocument = decisionDocument;
    }

    public void setDemandeCompensation(boolean isDemandeCompensation) {
        this.isDemandeCompensation = isDemandeCompensation;
    }

    public void setDocument(ICTDocument document) {
        this.document = document;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setDomaineLettreEnTete(String domaineLettreEnTete) {
        this.domaineLettreEnTete = domaineLettreEnTete;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIsEnteteOAI(Boolean isEnteteOAI) {
        this.isEnteteOAI = isEnteteOAI;
    }

    public void setNbTrue(int nbTrue) {
        this.nbTrue = nbTrue;
    }

    public void setNoOfficeAI(String noOfficeAI) {
        this.noOfficeAI = noOfficeAI;
    }

    public void setNumeroDeDecisionAI(String numeroDeDecisionAI) {
        this.numeroDeDecisionAI = numeroDeDecisionAI;
    }

    public void setPeriodeDecision(String periodeDecision) {
        this.periodeDecision = periodeDecision;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSpecificHeader(String specificHeader) {
        this.specificHeader = specificHeader;
    }

    public void setTierAdresse(PRTiersWrapper tierAdresse) {
        this.tierAdresse = tierAdresse;
    }

}
