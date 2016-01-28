package globaz.osiris.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntRole;
import globaz.osiris.translation.CACodeSystem;
import java.util.Iterator;
import java.util.Vector;

/**
 * Cette classe permet d'imprimer un rappel
 * 
 * @author sda
 */
public class CARappel_Doc extends FWIScriptDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String DOMAINE_FACTURATION = "910010";

    private static final String NUMERO_REFERENCE_INFOROM = "0137GCA";
    // Constantes musca pour aller chercher le catalogue de texte musca pour
    // faire le BVR
    public static final String TYPE_FACTURE = "910021";
    ICTDocument document = null;
    // Variables nécessaires à la récupération du catalogue de texte
    ICTDocument[] documents = null;
    private CARappel rappel = null;
    private Iterator rappelIt = null;
    ICTDocument res[] = null;

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     */
    public CARappel_Doc() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BProcess
     */
    public CARappel_Doc(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "RappelContentieux");
        super.setDocumentTitle(getSession().getLabel("FILENAME_CTX_RAPPEL"));
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CARappel_Doc(BSession session) throws FWIException {
        super(session, CAApplication.DEFAULT_OSIRIS_ROOT, "RappelContentieux");
        super.setDocumentTitle(getSession().getLabel("FILENAME_CTX_RAPPEL"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.07.2003 07:32:07)
     * 
     * @return java.lang.String
     */
    private String _getAdresseCourrier() {
        return rappel.getLigneAdresse1() + "\n" + rappel.getLigneAdresse2() + "\n" + rappel.getLigneAdresse3() + "\n"
                + rappel.getLigneAdresse4() + "\n" + rappel.getLigneAdresse5() + "\n" + rappel.getLigneAdresse6()
                + "\n" + rappel.getLigneAdresse7();
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _headerText() {
        try {
            super.setParametres(FWIImportParametre.PARAM_COMPANYNAME,
                    getSession().getApplication().getLabel("ADR_CAISSE", rappel.getTiers().getLangueISO()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Après la création de tous les documents Par defaut ne fait rien
     */
    @Override
    public void afterExecuteReport() {
        if (!rappel.isModePrevisionnel().booleanValue()) {
            try {
                JadePublishDocumentInfo info = createDocumentInfo();
                // Envoie un e-mail avec les pdf fusionnés
                this.mergePDF(info, false, 200, false, null);
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
        getDocumentInfo().setDocumentTypeNumber(CARappel_Doc.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
        setTemplateFile("CARappelContentieux");
        setImpressionParLot(true);

        // Initialise le document pour le catalogue de texte
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
        if (!rappel.isModePrevisionnel().booleanValue()) {
            // On sette la date du document
            getDocumentInfo().setDocumentDate(rappel.getDateDocument());
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
                TIDocumentInfoHelper.fill(getDocumentInfo(), rappel.getSection().getCompteAnnexe().getIdTiers(),
                        getSession(), rappel.getSection().getCompteAnnexe().getIdRole(), rappel.getSection()
                                .getCompteAnnexe().getIdExterneRole(),
                        affilieFormater.unformat(rappel.getSection().getCompteAnnexe().getIdExterneRole()));
                CADocumentInfoHelper.fill(getDocumentInfo(), rappel.getSection());
                getDocumentInfo().setDocumentProperty("babel.type.id", "CTX");
                getDocumentInfo().setDocumentType(document.getCsTypeDocument());
            } catch (Exception e1) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERROR_UNFORMATING_NUM_AFFILIE"));
            }

            try {
                // On dit qu'on veut archiver le document dans la GED
                getDocumentInfo().setArchiveDocument(true);
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
        rappelIt = ((Vector) data).iterator();
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            rappel = (CARappel) rappelIt.next();
            if (rappel.isModePrevisionnel().booleanValue()) {
                setTailleLot(500);
            } else {
                setTailleLot(1);
            }
            // On va rechercher le document nécessaire à l'affichage des textes
            documents = getICTDocument();
            // Header
            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    getDocumentInfo(), getSession().getApplication(), rappel.getTiers().getLangueISO());
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

            String adresse = "";
            if (!JadeStringUtil.isBlank(rappel.getSection().getTypeAdresse())
                    && !JadeStringUtil.isBlank(rappel.getSection().getDomaine())) {
                adresse = rappel.getTiers().getAdresseAsString(getDocumentInfo(), rappel.getSection().getTypeAdresse(),
                        rappel.getSection().getDomaine(), rappel.getSection().getCompteAnnexe().getIdExterneRole(),
                        rappel.getDate());
            } else {
                adresse = _getAdresseCourrier();
            }

            headerBean.setAdresse(adresse);
            headerBean.setDate(JACalendar.format(rappel.getDate(), rappel.getTiers().getLangueISO()));
            if (CAApplication.getApplicationOsiris().getCAParametres().isConfidentiel()) {
                headerBean.setConfidentiel(true);
            }
            // headerBean.setNomCollaborateur(_getResponsable());
            caisseReportHelper.addHeaderParameters(this, headerBean);
            // Paramètres
            super.setParametres(CARappelParam.PARAM_CONCERNE, format(getTexteConcerne().toString()));
            super.setParametres(CARappelParam.PARAM_TEXTE_DEBUT, format(getTexteBeginDoc().toString()));
            super.setParametres(CARappelParam.PARAM_TEXTE_CENTRE, getLibelleDevise() + " " + rappel.getMontantFacture());
            super.setParametres(CARappelParam.PARAM_TEXTE_FIN, format(getTexteEndDoc().toString()));
            super.setParametres(CARappelParam.PARAM_SIGNATURE, getTexteSignature().toString());
            // Lables
            super.setParametres("P_ESSAI", rappel.getSection().getPmtCmp());
            if (Double.parseDouble(rappel.getSection().getPmtCmp()) != 0.00) {
                super.setParametres(CARappelParam.LABEL_SOLDE,
                        getSession().getApplication().getLabel("CONTENTIEUX_SOLDE", rappel.getTiers().getLangueISO()));
            } else {
                super.setParametres(CARappelParam.LABEL_SOLDE, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _headerText();
    }

    /**
     * Permet de formater un texte. {0}=est remplacé par le numéro de facture {1}=est remplacé par le numéro de compte
     * {2}=est remplacé par la date de la section
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
                res += rappel.getNoFacture();
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '1') {
                res += rappel.getNoCompte();
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '2') {
                res += rappel.getDateSection();
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '3') {
                res += rappel.getTiers().getPolitesse();
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '4') {
                res += JACalendar.format(rappel.getDateDelaiPaiement(), rappel.getTiers().getLangueISO());
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '5') {
                res += rappel.getSection().getDescription();
                i = i + 2;
            }
        }
        return res;
    }

    /**
     * Récupère le document contenant les textes à afficher
     * 
     * @return
     */
    private ICTDocument[] getICTDocument() {
        document.setISession(getSession());
        document.setCsDomaine(CACodeSystem.CS_DOMAINE_CA);
        document.setCsTypeDocument(CACodeSystem.CS_TYPE_RAPPEL);
        document.setDefault(new Boolean(true));
        document.setCodeIsoLangue(rappel.getTiers().getLangueISO());
        document.setActif(new Boolean(true));
        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, "Error while getting document");
        }
        return res;
    }

    /**
     * Affiche le texte pour le libellé de la devise
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getLibelleDevise() throws Exception {
        StringBuffer resString = new StringBuffer("");
        // Si le document est vide, on génère une erreur
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;
            // On affiche le texte qui est au niveau 9
            try {
                listeTextes = documents[0].getTextes(9);
                resString.append(listeTextes.getTexte(99));
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                        getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            }
        }
        return resString;
    }

    /**
     * Retourne le texte du début du document
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getTexteBeginDoc() throws Exception {
        StringBuffer resString = new StringBuffer("");
        try {
            // Si le document est vide on génère une erreur
            if (document == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                ICTListeTextes listeTextes = null;
                // On affiche les texte du niveau 2 pour tous les cas
                listeTextes = documents[0].getTextes(2);
                resString.append(listeTextes.getTexte(1));
                // resString.append(listeTextes.getTexte(1));
                resString.append("\n\n");
                resString.append(listeTextes.getTexte(2));
                // On récupère le texte du niveau 3
                listeTextes = documents[0].getTextes(3);
                if (rappel.getSection().getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE)) {
                    resString.append(listeTextes.getTexte(1));
                } else {
                    resString.append(listeTextes.getTexte(2));
                }
                resString.append("\n\n");
                // On affiche la phrase du niveau 4 pour tout le monde
                listeTextes = documents[0].getTextes(4);
                resString.append(listeTextes.getTexte(1));
            }
        } catch (Exception e3) {
            getMemoryLog()
                    .logMessage(e3.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return resString;
    }

    /**
     * Retourne le texte qui doit apparaître dans le concerne
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getTexteConcerne() throws Exception {
        StringBuffer resString = new StringBuffer("");
        // Si le document est nul, on génère une erreur
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;
            try {
                // On récupère les textes du premier niveau
                listeTextes = documents[0].getTextes(1);
                // Si le tiers est un affilié on affiche le texte de la position
                // 1
                if (rappel.getSection().getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE)) {
                    resString.append(listeTextes.getTexte(1));
                } else {
                    // Sinon on affiche le texte de la position 2
                    resString.append(listeTextes.getTexte(2));
                }
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                        getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            }
        }
        return resString;
    }

    /**
     * Affiche le texte de fin du document
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getTexteEndDoc() throws Exception {
        StringBuffer resString = new StringBuffer("");
        try {
            // Si le document est vide, on génère une erreur
            if (document == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                ICTListeTextes listeTextes = null;
                // On affiche le texte du niveau 4 pour tous les cas
                listeTextes = documents[0].getTextes(4);
                resString.append(listeTextes.getTexte(2));
                resString.append("\n\n");
                resString.append(listeTextes.getTexte(3));
                resString.append("\n\n");
                resString.append(listeTextes.getTexte(4));
                resString.append("\n\n");
                resString.append(listeTextes.getTexte(5));
                resString.append("\n\n");
                resString.append(listeTextes.getTexte(6));
            }
        } catch (Exception e3) {
            getMemoryLog()
                    .logMessage(e3.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return resString;
    }

    /**
     * Affiche le texte pour la signature
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getTexteSignature() throws Exception {
        StringBuffer resString = new StringBuffer("");
        // Si le document est vide, on génère une erreur
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;
            // On affiche le texte qui est au niveau 5
            try {
                listeTextes = documents[0].getTextes(5);
                resString.append(listeTextes.getTexte(1));
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                        getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            }
        }
        return resString;
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
        return rappelIt.hasNext();
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