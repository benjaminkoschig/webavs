package globaz.osiris.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.bean.JadeUserDetail;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntRole;
import globaz.osiris.translation.CACodeSystem;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

/**
 * Cette Classe permet d'imprimer une sommation
 * 
 * @author sda
 */
public class CASommation_Doc extends FWIScriptDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOMAINE_FACTURATION = "910010";
    private static final String NUMERO_REF_INFOROM_LTN = "0196GCA";
    private static final String NUMERO_REFERENCE_INFOROM = "0138GCA";
    // Constantes musca pour aller chercher le catalogue de texte musca pour
    // faire le BVR
    public final static String TYPE_FACTURE = "910021";
    public final static String USERDETAIL_PHONE = "Phone";
    ICTDocument document = null;
    // Variables pour la recherche des textes pour le document
    ICTDocument[] documents = null;
    // Variables pours la recherche des textes pour le BVR
    ICTDocument[] documentsBVR = null;
    ICTDocument res[] = null;
    ICTDocument resBVR[] = null;
    private String responsable = null;
    private CASommation sommation = null;
    private Iterator sommationIt = null;

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     */
    public CASommation_Doc() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BProcess
     */
    public CASommation_Doc(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "SommationContentieux");
        super.setDocumentTitle(getSession().getLabel("FILENAME_CTX_SOMMATION"));
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CASommation_Doc(BSession session) throws FWIException {
        super(session, CAApplication.DEFAULT_OSIRIS_ROOT, "SommationContentieux");
        super.setDocumentTitle(getSession().getLabel("FILENAME_CTX_SOMMATION"));
    }

    /**
     * Date de création : (21.07.2003 07:32:07)
     * 
     * @return java.lang.String
     */
    private String _getAdresseCourrier() {
        return sommation.getLigneAdresse1() + "\n" + sommation.getLigneAdresse2() + "\n" + sommation.getLigneAdresse3()
                + "\n" + sommation.getLigneAdresse4() + "\n" + sommation.getLigneAdresse5() + "\n"
                + sommation.getLigneAdresse6() + "\n" + sommation.getLigneAdresse7();
    }

    /**
     * Adresse affichée sur le BVR qui a un maximum de 5 lignes Date de création : (30.10.2006)
     * 
     * @return java.lang.String
     */
    private String _getAdresseCourrierCourt() {
        String adresse = "";

        adresse += sommation.getLigneAdresse1();
        adresse += "\n" + sommation.getLigneAdresse2();
        adresse += "\n" + sommation.getLigneAdresse3();
        adresse += "\n" + sommation.getLigneAdresse4();
        if (!JadeStringUtil.isBlank(sommation.getLigneAdresse7())) {
            adresse += "\n" + sommation.getLigneAdresse7();
        } else if (!JadeStringUtil.isBlank(sommation.getLigneAdresse6())) {
            adresse += "\n" + sommation.getLigneAdresse6();
        } else {
            adresse += "\n" + sommation.getLigneAdresse5();
        }

        return adresse;
    }

    /**
     * Retourne l'alias du responsable Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getResponsable() {
        // Si responsable est vide, on charge le responsable
        if (JadeStringUtil.isBlank(responsable)) {
            responsable = "";
            try {
                JadeUser user = JadeAdminServiceLocatorProvider.getLocator().getUserService()
                        .load(getSession().getUserId());
                if (user != null) {
                    responsable = user.getFirstname() + " " + user.getLastname() + "\n";
                    JadeUserDetail detail = null;
                    try {
                        detail = JadeAdminServiceLocatorProvider.getLocator().getUserDetailService()
                                .load(user.getIdUser(), FWSecureConstants.USER_DETAIL_PHONE);
                    } catch (Exception e) {
                        detail = null;
                    }
                    if (detail != null) {
                        responsable += detail.getValue();
                    }
                }
            } catch (Exception e) {
                // si on ne trouve pas de responsable, ce n'est pas grave...
            }
        }
        return responsable;
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _headerText() {
        try {
            super.setParametres(FWIImportParametre.PARAM_COMPANYNAME,
                    getSession().getApplication().getLabel("ADR_CAISSE", sommation.getTiers().getLangueISO()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Après la création de tous les documents Par defaut ne fait rien
     */
    @Override
    public void afterExecuteReport() {
        if (!sommation.isModePrevisionnel().booleanValue()) {
            try {
                JadePublishDocumentInfo info = createDocumentInfo();

                if (!APISection.ID_CATEGORIE_SECTION_LTN.equals(sommation.getSection().getCategorieSection())) {
                    info.setDocumentTypeNumber(CASommation_Doc.NUMERO_REFERENCE_INFOROM);
                } else {
                    info.setDocumentTypeNumber(CASommation_Doc.NUMERO_REF_INFOROM_LTN);
                }

                // Envoie un e-mail avec les pdf fusionnés
                info.setPublishDocument(true);
                info.setArchiveDocument(false);
                this.mergePDF(info, false, 500, false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        if (!APISection.ID_CATEGORIE_SECTION_LTN.equals(sommation.getSection().getCategorieSection())) {
            getDocumentInfo().setDocumentTypeNumber(CASommation_Doc.NUMERO_REFERENCE_INFOROM);
        } else {
            getDocumentInfo().setDocumentTypeNumber(CASommation_Doc.NUMERO_REF_INFOROM_LTN);
        }
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
        setTemplateFile("CASommationContentieux");
        setImpressionParLot(true);

        // On va initialiser les documents
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "Error while api for document");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument ()
     */
    @Override
    public boolean beforePrintDocument() {
        if (!sommation.isModePrevisionnel().booleanValue()) {
            // On sette la date du document
            getDocumentInfo().setDocumentDate(sommation.getDateDocument());
            // On dit qu'au début on ne veut pas envoyer un mail avec le
            // document
            getDocumentInfo().setPublishDocument(false);
            try {
                getSession().setApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            } catch (Exception e2) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERROR_SETTING_APPLICATION"));
            }

            try {
                CAApplication app = (CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS);
                IFormatData affilieFormater = app.getAffileFormater();
                // On rempli le documentInfo avec les infos du document
                TIDocumentInfoHelper.fill(getDocumentInfo(), sommation.getSection().getCompteAnnexe().getIdTiers(),
                        getSession(), sommation.getSection().getCompteAnnexe().getIdRole(), sommation.getSection()
                                .getCompteAnnexe().getIdExterneRole(),
                        affilieFormater.unformat(sommation.getSection().getCompteAnnexe().getIdExterneRole()));
                CADocumentInfoHelper.fill(getDocumentInfo(), sommation.getSection());

            } catch (Exception e1) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERROR_UNFORMATING_NUM_AFFILIE"));
            }
            try {
                getDocumentInfo().setDocumentProperty("babel.type.id", "CTX");
                // getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID,
                // "CTX");
                getDocumentInfo().setDocumentType(document.getCsTypeDocument());
                // On dit qu'on veut archiver le document dans la GED
                getDocumentInfo().setArchiveDocument(true);
                getDocumentInfo().setPublishDocument(false);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                        getSession().getLabel("GED_ERROR_GETTING_TIERS_INFO"));
            }
        }
        return ((super.size() > 0) && !super.isAborted());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIDocument#bindData(String)
     */
    @Override
    public void bindData(String arg0) throws Exception {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2003 07:17:28)
     * 
     * @param data
     *            java.lang.Object
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void bindObject(Object data) throws java.lang.Exception {
        sommationIt = ((Vector) data).iterator();
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            sommation = (CASommation) sommationIt.next();
            if (sommation.isModePrevisionnel().booleanValue()) {
                setTailleLot(500);
            } else {
                setTailleLot(1);
            }
            // On récupère les documents du catalogue de textes nécessaires
            documents = getICTDocument();
            documentsBVR = getICTDocumentBVR(getSession());
            // Header
            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    getDocumentInfo(), getSession().getApplication(), sommation.getTiers().getLangueISO());
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

            String adresse = "";
            if (!JadeStringUtil.isBlank(sommation.getSection().getTypeAdresse())
                    && !JadeStringUtil.isBlank(sommation.getSection().getDomaine())) {
                adresse = sommation.getTiers().getAdresseAsString(getDocumentInfo(),
                        sommation.getSection().getTypeAdresse(), sommation.getSection().getDomaine(),
                        sommation.getSection().getCompteAnnexe().getIdExterneRole(), sommation.getDate());
            } else {
                adresse = _getAdresseCourrier();
            }
            headerBean.setAdresse(adresse);
            headerBean.setDate(JACalendar.format(sommation.getDate(), sommation.getTiers().getLangueISO()));

            if (CAApplication.getApplicationOsiris().getCAParametres().isConfidentiel()) {
                headerBean.setConfidentiel(true);
            }
            // headerBean.setNomCollaborateur(_getResponsable());
            caisseReportHelper.addHeaderParameters(this, headerBean);
            // Labels
            super.setParametres(
                    CASommationParam.LABEL_NOCONTROLE,
                    getSession().getApplication().getLabel("CONTENTIEUX_NOCONTROLE",
                            sommation.getTiers().getLangueISO()));
            super.setParametres(CASommationParam.LABEL_NOFACTURE,
                    getSession().getApplication()
                            .getLabel("CONTENTIEUX_NOFACTURE", sommation.getTiers().getLangueISO()));
            super.setParametres(
                    CASommationParam.LABEL_DATEFACTURE,
                    getSession().getApplication().getLabel("CONTENTIEUX_DATEFACTURE",
                            sommation.getTiers().getLangueISO()));
            super.setParametres(
                    CASommationParam.LABEL_MONTANT_FACTURE,
                    getSession().getApplication().getLabel("CONTENTIEUX_MONTANT_FACTURE",
                            sommation.getTiers().getLangueISO()));
            super.setParametres(CASommationParam.LABEL_PMT_CPS,
                    getSession().getApplication().getLabel("CONTENTIEUX_PMT_CPS", sommation.getTiers().getLangueISO()));
            super.setParametres(CASommationParam.LABEL_SOLDE,
                    getSession().getApplication().getLabel("CONTENTIEUX_SOLDE", sommation.getTiers().getLangueISO()));
            super.setParametres(CASommationParam.LABEL_TAXE_SOMMATION,
                    getSession().getApplication()
                            .getLabel("CONTENTIEUX_TAXE_SOMM", sommation.getTiers().getLangueISO()));
            super.setParametres(CASommationParam.LABEL_TOTAL,
                    getSession().getApplication().getLabel("CONTENTIEUX_TOTAL", sommation.getTiers().getLangueISO()));
            super.setParametres(CASommationParam.LABEL_CONCERNE, getConcerne().toString());
            // Paramètres
            super.setParametres(CASommationParam.PARAM_DATEFACTURE, sommation.getDateSection());
            super.setParametres(CASommationParam.PARAM_NOCONTROLE, sommation.getNoCompte());
            super.setParametres(CASommationParam.PARAM_NOFACTURE, sommation.getNoFacture());
            super.setParametres(CASommationParam.PARAM_MONTANT_FACTURE,
            /*
             * new BigDecimal( JANumberFormatter.deQuote( sommation.getMontantFacture())).subtract( new BigDecimal(
             * JANumberFormatter.deQuote( sommation.getSection().getPmtCmp())))
             */
            JANumberFormatter.fmt(String.valueOf(new BigDecimal(
                    JANumberFormatter.deQuote(sommation.getMontantFacture())).subtract(new BigDecimal(JANumberFormatter
                    .deQuote(sommation.getSection().getPmtCmp())))), true, true, false, 2));
            super.setParametres(CASommationParam.PARAM_ADRESSE, getAdresseBVR().toString());
            super.setParametres(CASommationParam.PARAM_ADRESSECOPY, getAdresseBVR().toString());
            super.setParametres(CASommationParam.PARAM_REFERENCE, sommation.getReference());
            super.setParametres(CASommationParam.PARAM_COMPTE, getCompte().toString());
            super.setParametres(CASommationParam.PARAM_FRANC,
                    JANumberFormatter.deQuote(sommation.getMontantSansCentime()));
            super.setParametres(CASommationParam.PARAM_CENTIME, sommation.getCentimes());
            super.setParametres(CASommationParam.PARAM_VERSE, sommation.getReference() + "\n"
                    + _getAdresseCourrierCourt());
            super.setParametres(CASommationParam.PARAM_PAR, _getAdresseCourrier());
            super.setParametres(CASommationParam.PARAM_OCR, sommation.getOcrb());
            super.setParametres(CASommationParam.PARAM_PMT_CPS,
                    String.valueOf(JANumberFormatter.fmt(sommation.getSection().getPmtCmp(), true, true, false, 2)));
            super.setParametres(CASommationParam.PARAM_SOLDE, sommation.getMontantFacture());
            super.setParametres(CASommationParam.PARAM_TAXE_SOMMATION,
                    new BigDecimal(JANumberFormatter.deQuote(sommation.getTaxe())));
            super.setParametres(CASommationParam.PARAM_TOTAL, sommation.getMontant());
            super.setParametres(CASommationParam.PARAM_TEXTE, getTexte().toString());
            super.setParametres(CASommationParam.PARAM_TEXTE_N7P1, format(getLibelleTotal().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        _headerText();
    }

    /**
     * @return
     */
    private void dumpPosition(int niveau, StringBuffer out, String paraSep) {
        try {
            ICTListeTextes listeTextes = documents[0].getTextes(niveau);
            for (Iterator paraIter = listeTextes.iterator(); paraIter.hasNext();) {
                if (out.length() > 0) {
                    out.append(paraSep);
                }

                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.append("Texte introuvable !");
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
    }

    /**
     * Formate le texte. Remplace un {0} par la date d'échéance
     * 
     * @param paragraphe
     * @return
     * @throws Exception
     */
    private String format(String paragraphe) throws Exception {
        String res = "";
        for (int i = 0; i < paragraphe.length(); i++) {
            if (paragraphe.charAt(i) != '{') {
                res += paragraphe.charAt(i);
            } else if (paragraphe.charAt(i + 1) == '0') {
                res += JACalendar.format(sommation.getDateDelaiPaiement(), sommation.getTiers().getLangueISO());
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '9') {
                res += "\n";
                i = i + 2;
            }
        }
        return res;
    }

    /**
     * Retourne l'adresse pour le BVR (va rechercher dans le catalogue de textes MUSCA)
     * 
     * @return
     */
    private StringBuffer getAdresseBVR() {
        StringBuffer adresse = new StringBuffer("");
        try {
            // Si le document est nul on génère une erreur
            if (documentsBVR == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                // Dans le cas contraire on va rechercher les textes qui sont au
                // niveau 1
                ICTListeTextes listeTextes = null;
                listeTextes = documentsBVR[0].getTextes(1);
                // Dans le cas ou il n'y a pas de texte on génère une erreur
                if (listeTextes == null) {
                    getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE"), FWMessage.ERREUR, "");
                } else {
                    // Sinon on va chercher l'adresse qui va apparaître sur le
                    // BVR
                    for (int i = 0; i < listeTextes.size(); i++) {
                        adresse.append(listeTextes.getTexte(i + 1));
                        if (i + 1 < listeTextes.size()) {
                            adresse.append("\n");
                        }
                    }
                }
            }
        } catch (Exception e3) {
            getMemoryLog()
                    .logMessage(e3.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return adresse;
    }

    /**
     * Permet de récupérer le compte dans le catalogue du BVR de musca
     * 
     * @return
     */
    private StringBuffer getCompte() {
        StringBuffer compte = new StringBuffer("");
        try {
            // On va rechercher les textes au niveau 2 du document
            ICTListeTextes listeTextes = documentsBVR[0].getTextes(2);
            // On prend le texte qui est au premier niveau
            compte.append(listeTextes.getTexte(1));
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return compte;
    }

    /**
     * Récupère le texte concerne dans le catalogue de texte
     * 
     * @return
     */
    private StringBuffer getConcerne() {
        StringBuffer resString = new StringBuffer("");
        // Dans le cas où le document est nul on génère une erreur
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            try {
                // On va rechercher le texte qui est au niveau 1
                ICTListeTextes listeTextes = documents[0].getTextes(1);
                resString.append(listeTextes.getTexte(1));
            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                        getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            }
        }
        return resString;
    }

    /**
     * Récupère le document permettant d'ajouter les textes au document
     * 
     * @return
     */
    private ICTDocument[] getICTDocument() {
        document.setISession(getSession());
        document.setCsDomaine(CACodeSystem.CS_DOMAINE_CA);
        document.setCsTypeDocument(CACodeSystem.CS_TYPE_SOMMATION);
        document.setCodeIsoLangue(sommation.getTiers().getLangueISO());
        document.setActif(Boolean.TRUE);

        if (APISection.ID_CATEGORIE_SECTION_LTN.equals(sommation.getSection().getCategorieSection())) {
            document.setNom("Sommation LTN");
        } else {
            document.setDefault(Boolean.TRUE);
        }

        try {
            res = document.load();
            if (!JadeStringUtil.isBlank(document.getNom()) && ((res == null) || (res.length == 0))) {
                document.setNom("");
                document.setDefault(Boolean.TRUE);
                res = document.load();
            }

        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, "Error while getting document");
        }
        return res;
    }

    /**
     * Permet de récupérer le document permettant d'ajouter les textes au BVR
     * 
     * @return
     */
    public ICTDocument[] getICTDocumentBVR(BSession session) throws Exception {
        ICTDocument documentBVR = (ICTDocument) session.getAPIFor(ICTDocument.class);
        ICTDocument resBVR[] = null;
        if (resBVR == null) {
            documentBVR.setISession(session);
            documentBVR.setCsDomaine(CASommation_Doc.DOMAINE_FACTURATION);
            documentBVR.setCsTypeDocument(CASommation_Doc.TYPE_FACTURE);
            documentBVR.setDefault(new Boolean(true));
            documentBVR.setActif(new Boolean(true));
            resBVR = documentBVR.load();
        }
        return resBVR;
    }

    /**
     * Récupère le texte du libellé dans le catalogue de texte
     * 
     * @return
     */
    private StringBuffer getLibelleTotal() {
        StringBuffer resString = new StringBuffer("");
        try {
            // On va récupérer le texte du niveau 8 du catalogue de textes
            ICTListeTextes listeTextes = documents[0].getTextes(8);
            resString.append(listeTextes.getTexte(1));
        } catch (Exception e1) {
            getMemoryLog()
                    .logMessage(e1.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return resString;
    }

    /**
     * Récupère tout le texte du haut du document (va prendre les textes du catalogue de textes des niveaux 2 à 7)
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getTexte() throws Exception {
        StringBuffer resString = new StringBuffer("");
        try {
            ICTListeTextes listeTextes = null;
            // Si la section fait partie d'un plan de paiement on affiche la
            // phrase du niveau 2
            if (sommation.getSection().getContentieuxEstSuspendu().booleanValue()
                    && sommation.getSection().getIdMotifContentieuxSuspendu().equals(CACodeSystem.CS_PLAN_RECOUVREMENT)) {
                dumpPosition(2, resString, "");
                resString.append("\n\n");
            } else {
                // Si la section ne fait pas partie d'un plan de paiement on va
                // afficher la phrase du niveau 3
                dumpPosition(3, resString, "");

                listeTextes = documents[0].getTextes(4);
                // Si le tiers est un affilié on affiche la première phrase du
                // niveau 4
                if (sommation.getSection().getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE)) {
                    resString.append(listeTextes.getTexte(1));
                } else {
                    // Sinon on affiche la deuxième phrase du niveau 4
                    resString.append(listeTextes.getTexte(2));
                }
                resString.append("\n\n");
            }
            // On affiche phrase du niveau 5 pour tous les cas
            dumpPosition(5, resString, "");
            resString.append("\n\n");
            // Si l'affiliation est de type employeur on affiche la phrase de
            // niveau 6
            if (sommation.getSection().getCompteAnnexe().getIdCategorie().equals("804002")
                    || sommation.getSection().getCompteAnnexe().getIdCategorie().equals("804005")) {
                dumpPosition(6, resString, "");
                resString.append("\n\n");
            }
            // On affiche la phrase du niveau 7 pour tout les cas
            dumpPosition(7, resString, "");
            resString.append("\n\n");
        } catch (Exception e3) {
            getMemoryLog()
                    .logMessage(e3.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return new StringBuffer(format(resString.toString()));
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        return sommationIt.hasNext();
    }

    /**
     * Méthode appelé pour lancer l'exportation du document Par défaut ne pas utiliser car déjà implémenté par la
     * superClass Utile si on ne veut pas exporter en fichier temporaire Date de création : (17.02.2003 14:44:15)
     */
    @Override
    public void returnDocument() throws FWIException {
        super.imprimerListDocument();
    }
}
