package globaz.corvus.topaz;

import globaz.babel.utils.CatalogueText;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TIHistoriqueTiers;
import globaz.pyxis.db.tiers.TIHistoriqueTiersManager;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RECommunicationMutationOO extends REAbstractJobOO {

    private static final long serialVersionUID = 1L;

    // Début Paramètres process
    private String codeIsoLangueOfficeAI;
    private String emailAdresse;
    private Boolean sendToGed;

    private String adresseOfficeAi;
    private String adresseActuelleTiers = "";
    private String adresseAncienneTiers = "";

    private String dateDecesTiers;
    private Boolean isNouvelleAdresseTiers;
    private Boolean isNouvelleAdresseAutre;

    private String dateDebutHospitalisation;
    private String dateFinHospitalisation;
    private String dateDebutHome;
    private String dateFinHome;
    private String texteObservation;
    private List<String> listeAnnexes;
    private String idTiers;

    private String nssTiers;
    private String nomTiers;
    private String prenomTiers;
    // Fin Paramètres process

    private Boolean changementNom;
    private Boolean changementPrenom;
    private Boolean changementNSS;
    private Boolean changementAutre;
    private String inputChangementAutre;

    public Boolean getChangementNom() {
        return changementNom;
    }

    public void setChangementNom(Boolean changementNom) {
        this.changementNom = changementNom;
    }

    public Boolean getChangementPrenom() {
        return changementPrenom;
    }

    public void setChangementPrenom(Boolean changementPrenom) {
        this.changementPrenom = changementPrenom;
    }

    public Boolean getChangementNSS() {
        return changementNSS;
    }

    public void setChangementNSS(Boolean changementNSS) {
        this.changementNSS = changementNSS;
    }

    public Boolean getChangementAutre() {
        return changementAutre;
    }

    public void setChangementAutre(Boolean changementAutre) {
        this.changementAutre = changementAutre;
    }

    public String getInputChangementAutre() {
        return inputChangementAutre;
    }

    public void setInputChangementAutre(String inputChangementAutre) {
        this.inputChangementAutre = inputChangementAutre;
    }

    private CatalogueText catalogueLettreCommunicationMutation;

    // Variables dans le modèle de document
    private static final String VAR_TITRE_DOCUMENT = "TITRE_DOCUMENT";
    private static final String VAR_TITRE_NSS = "TITRE_NSS";
    private static final String VAR_NSS_TIERS = "NSS_TIERS";
    private static final String VAR_TITRE_DETAIL_ADRESSE_TIERS = "TITRE_DETAIL_ADRESSE_TIERS";
    private static final String VAR_DETAIL_ADRESSE_TIERS = "DETAIL_ADRESSE_TIERS";
    private static final String VAR_TITRE_MOTIF = "TITRE_MOTIFS";
    private static final String VAR_MOTIF = "MOTIF";
    private static final String VAR_ADRESSE_REPRESENTANT = "ADRESSE_REP";
    private static final String VAR_TEXTE_MOTIF = "TEXTE_MOTIF";
    private static final String VAR_TITRE_OBSERVATION = "TITRE_OBSERVATION";
    private static final String VAR_TEXTE_OBSERVATION = "TEXTE_OBSERVATION";
    private static final String VAR_TITRE_ANNEXE = "TITRE_ANNEXE";
    private static final String VAR_TEXTE_ANNEXE = "TEXTE_ANNEXE";
    private static final String VAR_DATE_DEB_HOSPITALISATION = "{dateDebutHospitalisation}";
    private static final String VAR_DATE_FIN_HOSPITALISATION = "{dateFinHospitalisation}";
    private static final String VAR_DATE_DEB_HOME = "{dateEntreeHome}";
    private static final String VAR_DATE_FIN_HOME = "{dateSortieHome}";

    public String getAdresseActuelleTiers() {
        return adresseActuelleTiers;
    }

    public void setAdresseActuelleTiers(String adresseActuelleTiers) {
        this.adresseActuelleTiers = adresseActuelleTiers;
    }

    public String getAdresseAncienneTiers() {
        return adresseAncienneTiers;
    }

    public void setAdresseAncienneTiers(String adresseAncienneTiers) {
        this.adresseAncienneTiers = adresseAncienneTiers;
    }

    public void setCatalogueLettreCommunicationMutation(CatalogueText catalogueLettreCommunicationMutation) {
        this.catalogueLettreCommunicationMutation = catalogueLettreCommunicationMutation;
    }

    public RECommunicationMutationOO() {
        super(false);
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("LETTRE_COMMUNICATION_MUTATION_TITRE_MAIL");
    }

    @Override
    public String getName() {
        return getSession().getLabel("LETTRE_COMMUNICATION_MUTATION_TITRE_MAIL");
    }

    @Override
    protected List<CatalogueText> definirCataloguesDeTextes() {
        catalogueLettreCommunicationMutation = new CatalogueText();
        catalogueLettreCommunicationMutation.setCodeIsoLangue(getCodeIsoLangueOfficeAI());
        catalogueLettreCommunicationMutation.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogueLettreCommunicationMutation.setCsTypeDocument(IRECatalogueTexte.CS_LETTRE_COMMUNICATION_MUTATION);
        catalogueLettreCommunicationMutation.setNomCatalogue("openOffice");

        return Arrays.asList(catalogueLettreCommunicationMutation);
    }

    @Override
    protected void genererDocument() throws Exception {
        JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

        creerDocument(allDoc);

        this.createDocuments(allDoc);

    }

    private void loadTemplateDocument(DocumentData docData) {
        docData.addData("idProcess", "RECommunicationMutationOO");
        docData.addData("idEntete", "CAISSE");
        docData.addData("idSignature", "Signature_Caisse");
    }

    private String findDataForChampBeforeLastChange(String champ) {

        String ancienneValeur = "";
        if (!JadeStringUtil.isBlankOrZero(getIdTiers())) {
            try {
                // L'historique est ordré par date de début ascendante
                TIHistoriqueTiersManager histManager = new TIHistoriqueTiersManager();
                histManager.setSession(getSession());
                histManager.setForIdTiers(getIdTiers());
                histManager.setForChamp(champ);
                histManager.find(BManager.SIZE_NOLIMIT);

                // L'historique contient aussi la modification représentant l'état actuel
                // C'est pourquoi on prend l'avant dernière modification
                if (histManager.getSize() > 1) {
                    TIHistoriqueTiers histEntity = (TIHistoriqueTiers) histManager.getEntity(histManager.getSize() - 2);
                    return histEntity.getValeur();
                }

            } catch (Exception e) {
                ancienneValeur = "";
            }
        }

        return ancienneValeur;
    }

    private String findAncienNumeroAVS() {

        String ancienneValeur = "";
        if (!JadeStringUtil.isBlankOrZero(getIdTiers())) {
            try {
                // L'historique est ordré par date de début ascendante
                TIHistoriqueAvsManager histManager = new TIHistoriqueAvsManager();
                histManager.setSession(getSession());
                histManager.setForIdTiers(getIdTiers());
                histManager.find(BManager.SIZE_NOLIMIT);

                // L'historique contient aussi la modification représentant l'état actuel
                // C'est pourquoi on prend l'avant dernière modification
                if (histManager.getSize() > 1) {
                    TIHistoriqueAvs histEntity = (TIHistoriqueAvs) histManager.getEntity(histManager.getSize() - 2);
                    return histEntity.getNumAvs();
                }

            } catch (Exception e) {
                ancienneValeur = "";
            }
        }

        return ancienneValeur;
    }

    private void remplirCorpsDocument(DocumentData docData) throws Exception {

        // Insertion du titre du document
        docData.addData(RECommunicationMutationOO.VAR_TITRE_DOCUMENT,
                getTexte(catalogueLettreCommunicationMutation, 1, 1));

        // Insertion du titre NSS
        docData.addData(RECommunicationMutationOO.VAR_TITRE_NSS, getTexte(catalogueLettreCommunicationMutation, 2, 1));

        // Insertion du nss/nom+prenom du tiers
        String theNssTiers = getNssTiers();
        String theNomTiers = getNomTiers();
        String thePrenomTiers = getPrenomTiers();

        if (changementNom) {
            String ancienneValeur = findDataForChampBeforeLastChange("1");
            if (!JadeStringUtil.isBlankOrZero(ancienneValeur)) {
                theNomTiers = ancienneValeur;
            }
        }

        if (changementPrenom) {
            String ancienneValeur = findDataForChampBeforeLastChange("2");
            if (!JadeStringUtil.isBlankOrZero(ancienneValeur)) {
                thePrenomTiers = ancienneValeur;
            }
        }

        if (changementNSS) {
            String ancienneValeur = findAncienNumeroAVS();
            if (!JadeStringUtil.isBlankOrZero(ancienneValeur)) {
                theNssTiers = ancienneValeur;
            }
        }

        docData.addData(RECommunicationMutationOO.VAR_NSS_TIERS, theNssTiers + "\n" + theNomTiers + " "
                + thePrenomTiers);

        // Insertion du titre "assuré adresse"
        docData.addData(RECommunicationMutationOO.VAR_TITRE_DETAIL_ADRESSE_TIERS,
                getTexte(catalogueLettreCommunicationMutation, 2, 2));

        // Insertion de l'adresse du tiers
        String adresseTiers = getAdresseActuelleTiers();
        if (getIsNouvelleAdresseAutre() || getIsNouvelleAdresseTiers()) {
            adresseTiers = getAdresseAncienneTiers();
        }
        docData.addData(RECommunicationMutationOO.VAR_DETAIL_ADRESSE_TIERS, adresseTiers);

        // Gestion du tableau des motifs de changement
        // -------------------------------------------
        creerTableauMotifs(docData);
        // -------------------------------------------

        // Insertion du titre 'Observation'si non vide
        if (!JadeStringUtil.isEmpty(getTexteObservation())
                || !JadeStringUtil.isBlankOrZero(getDateDebutHospitalisation())
                || !JadeStringUtil.isBlankOrZero(getDateDebutHome())) {
            docData.addData(RECommunicationMutationOO.VAR_TITRE_OBSERVATION,
                    getTexte(catalogueLettreCommunicationMutation, 2, 9));
        }

        StringBuffer texteObservationBuffer = new StringBuffer();

        // Insertion du texte d'observation si non vide
        if (!JadeStringUtil.isEmpty(getTexteObservation())) {
            texteObservationBuffer.append(getTexteObservation());
            texteObservationBuffer.append("\n");
        }
        // Insertion de la période d'hospitalisation si non vide
        if (!JadeStringUtil.isBlankOrZero(getDateDebutHospitalisation())) {
            texteObservationBuffer.append("\n");
            if (!JadeStringUtil.isBlankOrZero(getDateFinHospitalisation())) {
                String texteHosp = PRStringUtils.replaceString(getTexte(catalogueLettreCommunicationMutation, 2, 10),
                        RECommunicationMutationOO.VAR_DATE_DEB_HOSPITALISATION, getDateDebutHospitalisation());
                texteObservationBuffer.append(PRStringUtils.replaceString(texteHosp,
                        RECommunicationMutationOO.VAR_DATE_FIN_HOSPITALISATION, getDateFinHospitalisation()));
            } else {
                texteObservationBuffer.append(PRStringUtils.replaceString(
                        getTexte(catalogueLettreCommunicationMutation, 2, 11),
                        RECommunicationMutationOO.VAR_DATE_DEB_HOSPITALISATION, getDateDebutHospitalisation()));
            }
        }

        // Insertion de la période en home si non vide
        if (!JadeStringUtil.isBlankOrZero(getDateDebutHome())) {
            texteObservationBuffer.append("\n");
            if (!JadeStringUtil.isBlankOrZero(getDateFinHome())) {
                String texteHome = PRStringUtils.replaceString(getTexte(catalogueLettreCommunicationMutation, 2, 12),
                        RECommunicationMutationOO.VAR_DATE_DEB_HOME, getDateDebutHome());
                texteObservationBuffer.append(PRStringUtils.replaceString(texteHome,
                        RECommunicationMutationOO.VAR_DATE_FIN_HOME, getDateFinHome()));
            } else {
                texteObservationBuffer.append(PRStringUtils.replaceString(
                        getTexte(catalogueLettreCommunicationMutation, 2, 13),
                        RECommunicationMutationOO.VAR_DATE_DEB_HOME, getDateDebutHome()));
            }
        }

        if (texteObservationBuffer.length() > 0) {
            docData.addData(RECommunicationMutationOO.VAR_TEXTE_OBSERVATION, texteObservationBuffer.toString());
        }

        // Insertion des annexes si non vide
        List<String> annexes = getListeAnnexes();
        if (annexes != null && annexes.size() > 0) {
            Collection collectionAnnexe = new Collection("tabAnnexes");
            DataList data = new DataList("ligneannexes");

            data.addData(RECommunicationMutationOO.VAR_TITRE_ANNEXE,
                    getTexte(catalogueLettreCommunicationMutation, 3, 1));

            StringBuffer texteAnnexeBuffer = new StringBuffer();
            for (String texte : annexes) {
                texteAnnexeBuffer.append(texte);
                texteAnnexeBuffer.append("\n");
            }

            data.addData(RECommunicationMutationOO.VAR_TEXTE_ANNEXE, texteAnnexeBuffer.toString());

            collectionAnnexe.add(data);
            docData.add(collectionAnnexe);
        } else {
            Collection collectionAnnexe = new Collection("tabAnnexes");
            docData.add(collectionAnnexe);
        }
    }

    private String getLabelInLangueOfficeAI(String id) {
        try {
            if (getSession().getApplication() == null) {
                return "Unknown label " + id + " (Application not found)";
            }
            String isoLanguage = getCodeIsoLangueOfficeAI();
            return getSession().getApplication().getLabel(id, isoLanguage);
        } catch (Exception e) {
            return "Unknown label " + id + " (Application not found)";
        }
    }

    private void creerTableauMotifs(DocumentData docData) {
        // Création d'une collection pour les motifs de changement
        Collection collection = new Collection("tabMotif");

        // Insertion du titre "Motif de changement" si motif
        boolean isChangementNomPrenomNSS = changementNom || changementPrenom || changementNSS;
        if (!JadeStringUtil.isEmpty(getDateDecesTiers()) || getIsNouvelleAdresseTiers() || getIsNouvelleAdresseAutre()
                || isChangementNomPrenomNSS || changementAutre) {
            docData.addData(RECommunicationMutationOO.VAR_TITRE_MOTIF,
                    getTexte(catalogueLettreCommunicationMutation, 2, 3));
        }

        // Si date de décès, insertion du motif 'date de décès'
        if (!JadeStringUtil.isEmpty(getDateDecesTiers())) {
            DataList data = new DataList("ligneMotif");
            data.addData(RECommunicationMutationOO.VAR_MOTIF, getTexte(catalogueLettreCommunicationMutation, 2, 4));
            data.addData(RECommunicationMutationOO.VAR_TEXTE_MOTIF, getDateDecesTiers());
            collection.add(data);

        }

        if (changementNom || changementPrenom || changementNSS) {
            StringBuffer changementNomPrenomNss = new StringBuffer(getTexte(catalogueLettreCommunicationMutation, 2, 7)
                    + " ");
            if (changementNom) {
                changementNomPrenomNss.append(getLabelInLangueOfficeAI("JSP_MUTATION_TEXTE_NOM"));
            }

            if (changementPrenom) {
                if (changementNom) {
                    changementNomPrenomNss.append(" / ");
                }
                changementNomPrenomNss.append(getLabelInLangueOfficeAI("JSP_MUTATION_TEXTE_PRENOM"));
            }

            if (changementNSS) {

                if (changementNom || changementPrenom) {
                    changementNomPrenomNss.append(" / ");
                }

                changementNomPrenomNss.append(getLabelInLangueOfficeAI("JSP_MUTATION_TEXTE_NSS"));
            }

            DataList data = new DataList("ligneMotif");
            data.addData(RECommunicationMutationOO.VAR_MOTIF, changementNomPrenomNss.toString());
            collection.add(data);

        }

        // Si nouvelle adresse tiers insertion du motif correspondant
        if (getIsNouvelleAdresseTiers()) {
            DataList data = new DataList("ligneMotif");
            data.addData(RECommunicationMutationOO.VAR_MOTIF, getTexte(catalogueLettreCommunicationMutation, 2, 5));
            collection.add(data);

        }

        // Si nouvelle adresse représentant du tiers insertion du motif correspondant
        if (getIsNouvelleAdresseAutre()) {
            DataList data = new DataList("ligneMotif");
            data.addData(RECommunicationMutationOO.VAR_MOTIF, getTexte(catalogueLettreCommunicationMutation, 2, 6));
            collection.add(data);

        }

        if (changementAutre && !JadeStringUtil.isBlankOrZero(inputChangementAutre)) {
            DataList data = new DataList("ligneMotif");
            data.addData(RECommunicationMutationOO.VAR_MOTIF, inputChangementAutre);
            collection.add(data);
        }

        Collection collectionAdresse = new Collection("tabAdresse");

        // Ajout d'une ligne blanche avant la nouvelle adresse
        DataList dataBlank = new DataList("ligneAdresse");
        collectionAdresse.add(dataBlank);

        if (changementNom) {
            DataList data = new DataList("ligneAdresse");
            data.addData(RECommunicationMutationOO.VAR_ADRESSE_REPRESENTANT, getNomTiers());
            collectionAdresse.add(data);
        }

        if (changementPrenom) {
            DataList data = new DataList("ligneAdresse");
            data.addData(RECommunicationMutationOO.VAR_ADRESSE_REPRESENTANT, getPrenomTiers());
            collectionAdresse.add(data);
        }

        if (changementNSS) {
            DataList data = new DataList("ligneAdresse");
            data.addData(RECommunicationMutationOO.VAR_ADRESSE_REPRESENTANT, getNssTiers());
            collectionAdresse.add(data);
        }

        // Si nouvelle adresse (tiers ou représentant), insertion de cette nouvelle adresse
        if (getIsNouvelleAdresseTiers() || getIsNouvelleAdresseAutre()) {

            // Insertion de la nouvelle adresse
            DataList data3 = new DataList("ligneAdresse");
            data3.addData(RECommunicationMutationOO.VAR_ADRESSE_REPRESENTANT, getAdresseActuelleTiers());
            collectionAdresse.add(data3);

        }

        docData.add(collectionAdresse);
        docData.add(collection);
    }

    private void remplirEnteteEtSignature(DocumentData docData) throws Exception {

        ICaisseReportHelperOO caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(
                getSession().getApplication(), getCodeIsoLangueOfficeAI());
        caisseHelper.setTemplateName("RE_DECISION");

        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

        crBean.setAdresse(getAdresseOfficeAi());

        // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du document
        if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
            crBean.setConfidentiel(true);
        }

        // Ajouter la mention traité par :
        if ("true".equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
            crBean.setNomCollaborateur(getTexte(catalogueLettreCommunicationMutation, 2, 14) + " "
                    + getSession().getUserFullName());
            crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        }

        Locale currentLocal = new Locale(getCodeIsoLangueOfficeAI());
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, currentLocal);
        Date d = JadeDateUtil.getGlobazDate(JACalendar.todayJJsMMsAAAA());

        crBean.setDate(df.format(d));
        docData = caisseHelper.addHeaderParameters(docData, crBean, false);
        docData = caisseHelper.addSignatureParameters(docData, crBean);
    }

    protected void creerDocument(JadePrintDocumentContainer allDoc) throws Exception {

        DocumentData docData = new DocumentData();

        // - Récupération des information nécessaire à la génération du document
        // Chargement du modèle de document
        loadTemplateDocument(docData);

        // - Chargement du catalogue de textes
        definirCataloguesDeTextes();

        // Préparation du document
        remplirCorpsDocument(docData);

        // - En-tête et signature
        remplirEnteteEtSignature(docData);

        // - Publication du document
        JadePublishDocumentInfo lettreDocInfo = publicationDocument(allDoc);

        // - envoi au serveur de fusion
        allDoc.addDocument(docData, lettreDocInfo);
    }

    private JadePublishDocumentInfo publicationDocument(JadePrintDocumentContainer allDoc) throws Exception {
        // Récupération du nss/nom+prenom de l'assuré, pour l'afficher dans le mail
        String infoAssure = getNssTiers() + " / " + getNomPrenomTiers();

        String emailAdresse = getEmailAdresse();
        JadePublishDocumentInfo publishDestination = JadePublishDocumentInfoProvider.newInstance(this);
        publishDestination.setOwnerEmail(emailAdresse);
        publishDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, emailAdresse);
        publishDestination.setDocumentTitle(getSession().getLabel("LETTRE_COMMUNICATION_MUTATION_TITRE_MAIL") + "\n"
                + infoAssure);
        publishDestination.setDocumentSubject(getSession().getLabel("LETTRE_COMMUNICATION_MUTATION_TITRE_MAIL") + "\n"
                + infoAssure);
        publishDestination.setDocumentNotes(getSession().getLabel("LETTRE_COMMUNICATION_MUTATION_SUBJECT_MAIL")
                + infoAssure);
        publishDestination.setPublishDocument(true);
        publishDestination.setArchiveDocument(false);
        publishDestination.setDocumentType(IRENoDocumentInfoRom.LETTRE_COMMUNICATION_MUTATION_OAI);
        publishDestination.setDocumentTypeNumber(IRENoDocumentInfoRom.LETTRE_COMMUNICATION_MUTATION_OAI);

        allDoc.setMergedDocDestination(publishDestination);

        // ------------------------------//
        // DocInfo du document

        JadePublishDocumentInfo lettreDocInfo = JadePublishDocumentInfoProvider.newInstance(this);
        lettreDocInfo.setOwnerEmail(emailAdresse);
        lettreDocInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, emailAdresse);
        lettreDocInfo.setDocumentTitle(getSession().getLabel("LETTRE_COMMUNICATION_MUTATION_TITRE_MAIL"));
        lettreDocInfo.setDocumentSubject(getSession().getLabel("LETTRE_COMMUNICATION_MUTATION_TITRE_MAIL"));
        lettreDocInfo.setDocumentNotes(getSession().getLabel("LETTRE_COMMUNICATION_MUTATION_SUBJECT_MAIL"));
        lettreDocInfo.setArchiveDocument(getSendToGed());
        lettreDocInfo.setPublishDocument(false);
        lettreDocInfo.setArchiveDocument(false);
        lettreDocInfo.setDocumentType(IRENoDocumentInfoRom.LETTRE_COMMUNICATION_MUTATION_OAI);
        lettreDocInfo.setDocumentTypeNumber(IRENoDocumentInfoRom.LETTRE_COMMUNICATION_MUTATION_OAI);

        // Mise en GED
        if (getSendToGed()) {
            lettreDocInfo.setArchiveDocument(true);
        }

        TIDocumentInfoHelper.fill(lettreDocInfo, String.valueOf(idTiers), getSession(), null, null, null);

        return lettreDocInfo;
    }

    private String getNomPrenomTiers() {
        return getNomTiers() + " " + getPrenomTiers();
    }

    /**
     * @return the codeIsoLangueOfficeAI
     */
    public final String getCodeIsoLangueOfficeAI() {
        return codeIsoLangueOfficeAI;
    }

    /**
     * @param codeIsoLangueOfficeAI the codeIsoLangueOfficeAI to set
     */
    public final void setCodeIsoLangueOfficeAI(String codeIsoLangueOfficeAI) {
        this.codeIsoLangueOfficeAI = codeIsoLangueOfficeAI;
    }

    /**
     * @return the emailAdresse
     */
    public final String getEmailAdresse() {
        return emailAdresse;
    }

    /**
     * @param emailAdresse the emailAdresse to set
     */
    public final void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    /**
     * @return the sendToGed
     */
    public final Boolean getSendToGed() {
        return sendToGed;
    }

    /**
     * @param sendToGed the sendToGed to set
     */
    public final void setSendToGed(Boolean sendToGed) {
        this.sendToGed = sendToGed;
    }

    /**
     * @return the adresseOfficeAi
     */
    public final String getAdresseOfficeAi() {
        return adresseOfficeAi;
    }

    /**
     * @param adresseOfficeAi the adresseOfficeAi to set
     */
    public final void setAdresseOfficeAi(String adresseOfficeAi) {
        this.adresseOfficeAi = adresseOfficeAi;
    }

    /**
     * @return the dateDecesTiers
     */
    public final String getDateDecesTiers() {
        return dateDecesTiers;
    }

    /**
     * @param dateDecesTiers the dateDecesTiers to set
     */
    public final void setDateDecesTiers(String dateDecesTiers) {
        this.dateDecesTiers = dateDecesTiers;
    }

    /**
     * @return the isNouvelleAdresseTiers
     */
    public final Boolean getIsNouvelleAdresseTiers() {
        return isNouvelleAdresseTiers;
    }

    /**
     * @param isNouvelleAdresseTiers the isNouvelleAdresseTiers to set
     */
    public final void setIsNouvelleAdresseTiers(Boolean isNouvelleAdresseTiers) {
        this.isNouvelleAdresseTiers = isNouvelleAdresseTiers;
    }

    /**
     * @return the isNouvelleAdresseAutre
     */
    public final Boolean getIsNouvelleAdresseAutre() {
        return isNouvelleAdresseAutre;
    }

    /**
     * @param isNouvelleAdresseAutre the isNouvelleAdresseAutre to set
     */
    public final void setIsNouvelleAdresseAutre(Boolean isNouvelleAdresseAutre) {
        this.isNouvelleAdresseAutre = isNouvelleAdresseAutre;
    }

    /**
     * @return the dateDebutHospitalisation
     */
    public final String getDateDebutHospitalisation() {
        return dateDebutHospitalisation;
    }

    /**
     * @param dateDebutHospitalisation the dateDebutHospitalisation to set
     */
    public final void setDateDebutHospitalisation(String dateDebutHospitalisation) {
        this.dateDebutHospitalisation = dateDebutHospitalisation;
    }

    /**
     * @return the dateFinHospitalisation
     */
    public final String getDateFinHospitalisation() {
        return dateFinHospitalisation;
    }

    /**
     * @param dateFinHospitalisation the dateFinHospitalisation to set
     */
    public final void setDateFinHospitalisation(String dateFinHospitalisation) {
        this.dateFinHospitalisation = dateFinHospitalisation;
    }

    /**
     * @return the dateDebutHome
     */
    public final String getDateDebutHome() {
        return dateDebutHome;
    }

    /**
     * @param dateDebutHome the dateDebutHome to set
     */
    public final void setDateDebutHome(String dateDebutHome) {
        this.dateDebutHome = dateDebutHome;
    }

    /**
     * @return the dateFinHome
     */
    public final String getDateFinHome() {
        return dateFinHome;
    }

    /**
     * @param dateFinHome the dateFinHome to set
     */
    public final void setDateFinHome(String dateFinHome) {
        this.dateFinHome = dateFinHome;
    }

    /**
     * @return the texteObservation
     */
    public final String getTexteObservation() {
        return texteObservation;
    }

    /**
     * @param texteObservation the texteObservation to set
     */
    public final void setTexteObservation(String texteObservation) {
        this.texteObservation = texteObservation;
    }

    /**
     * @return the listeAnnexes
     */
    public final List<String> getListeAnnexes() {
        return listeAnnexes;
    }

    /**
     * @param listeAnnexes the listeAnnexes to set
     */
    public final void setListeAnnexes(List<String> listeAnnexes) {
        this.listeAnnexes = listeAnnexes;
    }

    /**
     * @return the idTiers
     */
    public final String getIdTiers() {
        return idTiers;
    }

    /**
     * @param idTiers the idTiers to set
     */
    public final void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @return the nssTiers
     */
    public final String getNssTiers() {
        return nssTiers;
    }

    /**
     * @param nssTiers the nssTiers to set
     */
    public final void setNssTiers(String nssTiers) {
        this.nssTiers = nssTiers;
    }

    /**
     * @return the nomTiers
     */
    public final String getNomTiers() {
        return nomTiers;
    }

    /**
     * @param nomTiers the nomTiers to set
     */
    public final void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    /**
     * @return the prenomTiers
     */
    public final String getPrenomTiers() {
        return prenomTiers;
    }

    /**
     * @param prenomTiers the prenomTiers to set
     */
    public final void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }
}
