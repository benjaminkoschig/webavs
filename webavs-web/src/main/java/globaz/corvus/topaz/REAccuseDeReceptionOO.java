package globaz.corvus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.utils.REGedUtils;
import globaz.corvus.utils.REGedUtils.TypeRente;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.ged.PRGedHelper;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.Hashtable;
import ch.globaz.topaz.datajuicer.DocumentData;

public class REAccuseDeReceptionOO extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CDT_CONCERNE_NOMPRENOM = "{NomPrenom}";
    private static final String CDT_CONCERNE_NSS = "{NSS}";
    private static final String CDT_DATE_RECEPTION = "{dateReceptionDemande}";
    private static final String CDT_TITRE_SALUTATION = "{titreTiers}";

    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_ACCUSE_RECEPTION";

    private String adresse;
    private JadePrintDocumentContainer allDoc;
    private ICaisseReportHelperOO caisseHelper;
    private String codeIsoLangue;
    private DocumentData data;
    private String dateReceptionDemande;
    private ICTDocument document;
    private DocumentData documentData;
    private ICTDocument documentHelper;
    private String emailAdresse;
    private String idGestionnaire;
    private String idRequerant;
    private Boolean isSendToGed;
    private String nomRequerant;
    private String nssRequerant;
    private String prenomRequerant;
    private PRTiersWrapper tiersRequerant;
    private String titre;

    public REAccuseDeReceptionOO() {
        super();

        adresse = "";
        allDoc = null;
        caisseHelper = null;
        codeIsoLangue = "";
        data = null;
        dateReceptionDemande = "";
        document = null;
        documentData = null;
        documentHelper = null;
        emailAdresse = "";
        idGestionnaire = "";
        idRequerant = "";
        isSendToGed = Boolean.FALSE;
        nomRequerant = "";
        nssRequerant = "";
        prenomRequerant = "";
        tiersRequerant = null;
        titre = "";
    }

    private void chargementCatalogueTexte() throws Exception {

        // Chargement du catalogue de texte
        codeIsoLangue = getSession().getCode(tiersRequerant.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

        // creation du helper pour les catalogues de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_ACCUSE_DE_RECEPTION);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);
        documentHelper.setCodeIsoLangue(codeIsoLangue);

        ICTDocument[] documents = documentHelper.load();
        if ((documents == null) || (documents.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            document = documents[0];
        }
    }

    private void chargementDonneesLettre() throws Exception {
        try {
            String concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(1).getDescription(),
                    REAccuseDeReceptionOO.CDT_CONCERNE_NSS, nssRequerant);
            concerne = PRStringUtils.replaceString(concerne, REAccuseDeReceptionOO.CDT_CONCERNE_NOMPRENOM, nomRequerant
                    + " " + prenomRequerant);

            data.addData("LETTRE_CONCERNE", concerne);

            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tiersRequerant.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            titre = tiersTitre.getFormulePolitesse(tiersRequerant.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

            if (JadeStringUtil.isEmpty(titre)) {
                TITiers tiers = new TITiers();
                tiers.setSession(getSession());
                tiers.setIdTiers(getIdRequerant());
                tiers.retrieve();

                TIAdresseDataSource Ads = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), true);

                if (null != Ads) {
                    titre = Ads.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_TITRE);
                }
            }

            String date = JACalendar.format(dateReceptionDemande, codeIsoLangue);

            if (codeIsoLangue.equals("DE")) {
                data.addData("titreTiers", titre);
            } else {
                data.addData("titreTiers", titre + ",");
            }

            data.addData("LETTRE_PARA1", PRStringUtils.replaceString(
                    document.getTextes(3).getTexte(1).getDescription(), REAccuseDeReceptionOO.CDT_DATE_RECEPTION, date));
            data.addData("LETTRE_PARA2", document.getTextes(4).getTexte(1).getDescription());
            data.addData("LETTRE_PARA3", PRStringUtils.replaceString(
                    document.getTextes(5).getTexte(1).getDescription(), REAccuseDeReceptionOO.CDT_TITRE_SALUTATION,
                    titre));

            setDocumentData(data);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
        }
    }

    private void chargementSalutationLettre() throws Exception {
        try {
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            crBean.setAdresse(adresse);

            crBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), codeIsoLangue));

            // Ajoute le libellé CONFIDENTIEL dans l'adresse de l'entête du document
            if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                crBean.setConfidentiel(true);
            }

            // Ajoute dans l'entête de la lettre qui a traité le dossier si nécessaire
            // BZ 5080
            if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {

                if (JadeStringUtil.isEmpty(idGestionnaire)) {
                    crBean.setNomCollaborateur(document.getTextes(1).getTexte(1).getDescription() + " "
                            + getSession().getUserFullName());
                    crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
                } else {
                    JadeUser user = getSession().getApplication()._getSecurityManager()
                            .getUserForVisa(getSession(), idGestionnaire);
                    crBean.setNomCollaborateur(document.getTextes(1).getTexte(1).getDescription() + " "
                            + user.getFirstname() + " " + user.getLastname());
                    crBean.setTelCollaborateur(user.getPhone());

                }

            }

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(REAccuseDeReceptionOO.FICHIER_MODELE_ENTETE_CORVUS);

            data.addData("idEntete", "CAISSE");

            data = caisseHelper.addSignatureParameters(data, crBean);

            caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            try {
                data.addData("SIGNATURE", document.getTextes(6).getTexte(1).getDescription());
            } catch (IndexOutOfBoundsException e) {
                JadeLogger.warn(this, e.getMessage());
            }

            setDocumentData(data);
        } catch (Exception e) {
            throw new Exception(e.getMessage());

        }
    }

    public String getDateReceptionDemande() {
        return dateReceptionDemande;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("ACCUSE_DE_RECEPTION");
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdRequerant() {
        return idRequerant;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    @Override
    public String getName() {
        return getSession().getLabel("ACCUSE_DE_RECEPTION");
    }

    public String getNomRequerant() {
        return nomRequerant;
    }

    public String getNssRequerant() {
        return nssRequerant;
    }

    public String getPrenomRequerant() {
        return prenomRequerant;
    }

    @Override
    public void run() {
        try {

            allDoc = new JadePrintDocumentContainer();

            JadePublishDocumentInfo pubInfosDestination = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfosDestination.setOwnerEmail(getEmailAdresse());
            pubInfosDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
            pubInfosDestination.setDocumentTitle(getSession().getLabel("ACCUSE_DE_RECEPTION"));
            pubInfosDestination.setDocumentSubject(getSession().getLabel("ACCUSE_DE_RECEPTION"));
            pubInfosDestination.setArchiveDocument(false);
            pubInfosDestination.setPublishDocument(true);

            JadePublishDocumentInfo pubInfos = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfos.setDocumentTitle(getSession().getLabel("ACCUSE_DE_RECEPTION"));
            pubInfos.setDocumentSubject(getSession().getLabel("ACCUSE_DE_RECEPTION"));
            pubInfos.setOwnerEmail(getEmailAdresse());
            pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
            pubInfos.setArchiveDocument(isSendToGed.booleanValue());
            try {
                if (isSendToGed.booleanValue()) {
                    // bz-5941
                    PRGedHelper h = new PRGedHelper();
                    // Traitement uniquement pour la caisse concernée (CCB)
                    if (h.isExtraNSS(getSession())) {
                        pubInfos = h.setNssExtraFolderToDocInfo(getSession(), pubInfos, getIdRequerant());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            pubInfos.setPublishDocument(false);
            pubInfos.setDocumentType(IRENoDocumentInfoRom.ACCUSE_DE_RECEPTION);
            pubInfos.setDocumentTypeNumber(IRENoDocumentInfoRom.ACCUSE_DE_RECEPTION);
            pubInfos.setDocumentDate(JACalendar.todayJJsMMsAAAA());
            pubInfos.setDocumentProperty(REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                    REGedUtils.getCleGedPourTypeRente(getSession(), TypeRente.RenteAVS));

            TIDocumentInfoHelper.fill(pubInfos, getIdRequerant(), getSession(), null, null, null);

            allDoc.setMergedDocDestination(pubInfosDestination);

            tiersRequerant = PRTiersHelper.getTiersParId(getSession(), getIdRequerant());
            if (tiersRequerant == null) {
                tiersRequerant = PRTiersHelper.getAdministrationParId(getSession(), getIdRequerant());
            }

            // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                    tiersRequerant.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

            if (!JadeStringUtil.isEmpty(adresse)) {

                data = new DocumentData();

                data.addData("idProcess", "REAccuseDeReceptionOO");

                chargementCatalogueTexte();
                chargementDonneesLettre();
                chargementSalutationLettre();

                allDoc.addDocument(getDocumentData(), pubInfos);

                this.createDocuments(allDoc);

            } else {
                getLogSession().addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, "REEcheanceRenteOO", getSession()
                                .getLabel("ERREUR_AUCUNE_ADRESSE")
                                + " "
                                + nssRequerant
                                + " "
                                + nomRequerant
                                + " "
                                + prenomRequerant));
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
        } finally {
            try {
                if (getLogSession().hasMessages()) {
                    sendCompletionMail(new ArrayList<String>() {
                        /**
                         * 
                         */
                        private static final long serialVersionUID = 1L;

                        {
                            this.add(REAccuseDeReceptionOO.this.getEmailAdresse());
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println(getSession().getLabel("ERREUR_ENVOI_MAIL_COMPLETION"));
            }
        }
    }

    public void setDateReceptionDemande(String dateReceptionDemande) {
        this.dateReceptionDemande = dateReceptionDemande;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setNomRequerant(String nomRequerant) {
        this.nomRequerant = nomRequerant;
    }

    public void setNssRequerant(String nssRequerant) {
        this.nssRequerant = nssRequerant;
    }

    public void setPrenomRequerant(String prenomRequerant) {
        this.prenomRequerant = prenomRequerant;
    }
}