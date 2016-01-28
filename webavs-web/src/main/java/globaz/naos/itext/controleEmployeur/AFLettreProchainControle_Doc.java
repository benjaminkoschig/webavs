package globaz.naos.itext.controleEmployeur;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.util.FAUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.db.tiers.TITiers;
import java.util.Map;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Le document imprime les zones de facture selon les paramètres suivants: _modeRecouvrement : aucun, bvr,
 * remboursement, recouvrement direct _critereDecompte : interne, positif, note de credit, decompte zéro Date de
 * création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class AFLettreProchainControle_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String NUM_REF_INFOROM_LETTRE_PROCHAIN_CONTROLE = "0192CAF";
    public final static String PROP_SIGN_NOM_CAISSE = "signature.nom.caisse.";
    protected final static String TEMPLATE_FILENAME = "NAOS_LETTRE_PROCHAIN_CONTROLE";

    public static String getTemplateFilename() {
        return AFLettreProchainControle_Doc.TEMPLATE_FILENAME;
    }

    protected java.lang.String adresseDomicile;
    protected java.lang.String adressePrincipalePaiement;
    private AFControleEmployeur controle;
    private String dateHeure = "";
    ICTDocument[] document = null;
    ICTDocument[] documentDe = null;
    ICTDocument[] documentIt = null;
    private String idControleEmployeur = new String();
    private String idDocument = "";
    protected String idDocumentDefaut = "";
    private String reviseur = "";
    private boolean start = true;

    private TITiers tiers = null;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public AFLettreProchainControle_Doc() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFLettreProchainControle_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "LETTREPROCHAINCONTROLE");
        super.setDocumentTitle(getSession().getLabel("OPTIONS_LETTRE_PROCHAIN_CONTROLE"));
        setParentWithCopy(parent);

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFLettreProchainControle_Doc(BSession session) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "LETTREPROCHAINCONTROLE");
        super.setDocumentTitle(getSession().getLabel("OPTIONS_LETTRE_PROCHAIN_CONTROLE"));
    }

    /**
     * Insert the method's description here. Creation date: (05.06.2003 08:55:49)
     */
    @Override
    public void _executeCleanUp() {
        super._executeCleanUp();
    }

    public TITiers _getTiers() {
        try {
            tiers = new TITiers();
            tiers.setSession(controle.getSession());
            tiers.setIdTiers(controle.getIdTiers());
            tiers.retrieve();
        } catch (Exception e) {
            this._addError("Erreur lors du retrieve du tiers pour le contrôle d'employeur: " + e.getMessage());
        }
        return tiers;
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _headerText(CaisseHeaderReportBean headerBean) {

        try {
            String dateImpression2 = JACalendar.format(JACalendar.today());

            // texte de la date
            headerBean.setDate(JACalendar.format(dateImpression2, controle.getLangueTiers()));

            // adresse du tiers
            headerBean.setAdresse(adressePrincipalePaiement);

            headerBean.setNoAvs(" "); // PO 10337

            // No affilié
            headerBean.setNoAffilie(controle.getNumAffilie());

            headerBean.setUser(getSession().getUserInfo());
            headerBean.setNomCollaborateur(getSession().getUserFullName());
            headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());

            // Partie concerne du document
            super.setParametres(AFLettreProchainControle_Param.PARAM_TITLE, FWMessageFormat.format(FAUtil
                    .prepareQuotes(getTexte(1, controle.getLangueTiers()), false), FAUtil.prepareQuotes(
                    JACalendar.format(controle.getDateDebutControle(), controle.getLangueTiers()), false), FAUtil
                    .prepareQuotes(JACalendar.format(controle.getDateFinControle(), controle.getLangueTiers()), false)));

        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header du détail d'un intérêt: " + e.getMessage());

        }

    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _letterBody() {

        try {
            tiers = _getTiers();
            // On set le texte du haut du document
            super.setParametres(
                    AFLettreProchainControle_Param.PARAM_TITLE2,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(2, controle.getLangueTiers()), false),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false)));
            super.setParametres(
                    AFLettreProchainControle_Param.PARAM_TEXTTOP,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(3, controle.getLangueTiers()), false),
                            FAUtil.prepareQuotes(getReviseur(), false), FAUtil.prepareQuotes(getDateHeure(), false)));
            super.setParametres(
                    AFLettreProchainControle_Param.PARAM_TEXTTOP1,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(4, controle.getLangueTiers()), false),
                            FAUtil.prepareQuotes(getReviseur(), false), FAUtil.prepareQuotes(getDateHeure(), false)));
            super.setParametres(AFLettreProchainControle_Param.PARAM_TEXTTOP2, FWMessageFormat.format(
                    FAUtil.prepareQuotes(getTexte(5, controle.getLangueTiers()), false),
                    FAUtil.prepareQuotes(getReviseur(), false), FAUtil.prepareQuotes(getDateHeure(), false),
                    FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false)));

        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header de la lettre prochain contrôle: " + e.getMessage());
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:33)
     */
    protected void _summaryText() {
        try {
            super.setParametres(
                    AFLettreProchainControle_Param.PARAM_TEXTBOTTOM,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(6, controle.getLangueTiers()), false),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false)));
            // super.setParametres(
            // AFLettreProchainControle_Param.PARAM_TEXTBOTTOM,getTexte(7,
            // controle.getLangueTiers()));
        } catch (Exception e) {
            this._addError("Erreur lors de la création du text de la lettre prochain contrôle: " + e.getMessage());
        }

    }

    /**
     * Retourne la décision ou null en cas d'exception Insérez la description de la méthode ici. Date de création :
     * (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // super.setSendMailOnError(true);
        super.setDocumentTitle(controle.getNumAffilie() + " - " + controle.getNomTiers());
    }

    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTemplateFile(AFLettreProchainControle_Doc.TEMPLATE_FILENAME);
        setTailleLot(500);
        document = getICTDocument("FR");
        documentDe = getICTDocument("DE");
        documentIt = getICTDocument("IT");
        controle = new AFControleEmployeur();
        controle.setSession(getSession());
        controle.setControleEmployeurId(getIdControleEmployeur());
        try {
            controle.retrieve();
        } catch (Exception e) {
            this._addError("false");
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());

            } finally {
            }
        } finally {
        }

        // Initialise le document pour le catalogue de texte
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {
        // super.setTemplateFile(TEMPLATE_FILENAME);
        // app = (FAApplication) getSession().getApplication();
        fillDocInfo();
        start = false;
        // initialiser les variables d'aide
        adressePrincipalePaiement = controle.getAdressePrincipale(getTransaction(), globaz.globall.util.JACalendar
                .today().toStr("."));
        adresseDomicile = controle.getAdresseDomicile(getTransaction(), JACalendar.today().toStr("."));

        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), controle.getLangueTiers());

        CaisseHeaderReportBean hb = new CaisseHeaderReportBean();
        // hb.setNomCollaborateur(getSession().getUserFullName());
        // hb.setUser(getSession().getUserInfo());

        // ICaisseReportHelper caisseReportHelper =
        // CaisseHelperFactory.getInstance().getCaisseReportHelper(getSession().getApplication(),
        // controle.getLangueTiers());
        // CaisseHeaderReportBean hb = new CaisseHeaderReportBean();
        // hb.setNomCollaborateur(getSession().getUserFullName());
        // hb.setUser(getSession().getUserInfo());

        _headerText(hb);
        _letterBody();
        _summaryText();

        String reviseur = "";
        if (!JadeStringUtil.isEmpty(controle.getControleurNom())) {
            reviseur = controle.getControleurNom();
        }
        caisseReportHelper.addHeaderParameters(this, hb);
        caisseReportHelper.addSignatureParameters(this);

        String nomReviseurSign = (getSession().getApplication()).getProperty("nomReviseurSignature");

        if (!JadeStringUtil.isBlank(nomReviseurSign) && nomReviseurSign.equals("true")) {
            Map map = getImporter().getParametre();

            String signataires = map.get(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE) + "";

            this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE, signataires + "       " + reviseur);
        }

    }

    // //Retourne le document à utiliser
    // private ICTDocument[] getICTDocument() {
    // ICTDocument res[] = null;
    // ICTDocument document = null;
    // try {
    // document = (ICTDocument)getSession().getAPIFor(ICTDocument.class);
    // } catch (Exception e) {
    // getMemoryLog().logMessage(e.toString(),FWMessage.ERROR,"Error while api for document");
    // }
    // //On charge le document
    // document.setISession(getSession());
    // document.setCsDomaine(FAImpressionFacturation.DOMAINE_FACTURATION);
    // document.setCsTypeDocument(FAImpressionFacturation.TYPE_LETTRE);
    // document.setCodeIsoLangue(entity.getISOLangueTiers());
    // document.setActif(new Boolean(true));
    // try {
    // res = document.load();
    // } catch (Exception e1) {
    // getMemoryLog().logMessage(e1.toString(),FWMessage.ERROR,"Error while getting document");
    // }
    // return res;
    // }

    public void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber(AFLettreProchainControle_Doc.NUM_REF_INFOROM_LETTRE_PROCHAIN_CONTROLE);
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", controle.getNumAffilie());

        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(controle.getNumAffilie()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), controle.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    controle.getNumAffilie(), affilieFormater.unformat(controle.getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", controle.getNumAffilie());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
    }

    // //Retourne le texte du niveau entré en paramètres
    // private String getTexte(int niveau, ICTDocument[] document) throws
    // Exception {
    // String resString = "";
    // ICTTexte texte = null;
    // //Si le document est null, on retourne un message d'erreur
    // if (document == null) {
    // getMemoryLog().logMessage(
    // "Il n'y a pas de document par défaut",
    // FWMessage.ERROR,
    // "");
    // } else {
    // ICTListeTextes listeTextes = null;
    // //On charge la liste de textes du niveau donné
    // try {
    // listeTextes = document[0].getTextes(niveau);
    // } catch (Exception e3) {
    // getMemoryLog().logMessage(e3.toString(),FWMessage.ERROR,"Error while getting listes de textes");
    // }
    // //S'il n'y a pas de texte on retourne une erreur
    // if (listeTextes == null) {
    // getMemoryLog().logMessage(
    // "Il n'y a pas de texte",
    // FWMessage.ERROR,
    // "");
    // } else {
    // //Dans le cas ou on a un texte, on va parcourir toutes les positions de
    // ce texte
    // for (int i = 0; i < listeTextes.size(); i++) {
    // //On charge le texte de la position donnée
    // texte = listeTextes.getTexte(i + 1);
    // //On regarde si c'est la dernière position du niveau
    // if(i+1<listeTextes.size()){
    // resString =
    // resString.concat(texte.getDescription() + "\n\n");
    // }else{
    // //Dans le cas ou c'est la dernière position du niveau, on n'ajoute aucun
    // retour à la ligne ni paragraphe
    // resString =resString.concat(texte.getDescription());
    // }
    // }
    // }
    // }
    // //return format(resString);
    // return resString;
    // }

    public String getDateHeure() {
        return dateHeure;
    }

    private ICTDocument[] getICTDocument(String langue) {
        ICTDocument res[] = null;
        ICTDocument document = null;
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_API"));
        }
        document.setISession(getSession());
        if (!JadeStringUtil.isEmpty(getIdDocument())) {
            document.setIdDocument(getIdDocument());
        } else {
            document.setIdDocument(getIdDocumentDefaut());
        }
        document.setCsDomaine(CodeSystem.DOMAINE_CONT_EMPL);
        document.setCsTypeDocument(CodeSystem.TYPE_LETTRE_PROCHAIN_CONTROLE);
        document.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);
        document.setCodeIsoLangue(langue);
        // document.setDefault(new Boolean(true));
        document.setActif(new Boolean(true));
        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_DOC"));
        }
        return res;
    }

    public String getIdControleEmployeur() {
        return idControleEmployeur;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdDocumentDefaut() {
        return idDocumentDefaut;
    }

    public String getReviseur() {
        return reviseur;
    }

    private String getTexte(int niveau, String langue) throws Exception {
        String resString = "";
        ICTTexte texte = null;
        if ((langue != null) && langue.equals("de")) {
            if (documentDe == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                ICTListeTextes listeTextes = null;
                try {
                    listeTextes = documentDe[0].getTextes(niveau);
                } catch (Exception e3) {
                    // getMemoryLog().logMessage(e3.toString(),FWMessage.ERREUR,getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
                }
                if (listeTextes == null) {
                    /*
                     * getMemoryLog().logMessage( getSession().getLabel("PAS_TEXTE"), FWMessage.ERREUR, "");
                     */
                } else {
                    for (int i = 0; i < listeTextes.size(); i++) {
                        texte = listeTextes.getTexte(i + 1);
                        if (i + 1 < listeTextes.size()) {
                            resString = resString.concat(texte.getDescription() + "\n\n");
                        } else {
                            resString = resString.concat(texte.getDescription());
                        }
                    }
                }
            }
        } else if ((langue != null) && langue.equals("it")) {
            if (documentIt == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                ICTListeTextes listeTextes = null;
                try {
                    listeTextes = documentIt[0].getTextes(niveau);
                } catch (Exception e3) {
                    // getMemoryLog().logMessage(e3.toString(),FWMessage.ERREUR,getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
                }
                if (listeTextes == null) {
                    /*
                     * getMemoryLog().logMessage( getSession().getLabel("PAS_TEXTE"), FWMessage.ERREUR, "");
                     */
                } else {
                    for (int i = 0; i < listeTextes.size(); i++) {
                        texte = listeTextes.getTexte(i + 1);
                        if (i + 1 < listeTextes.size()) {
                            resString = resString.concat(texte.getDescription() + "\n\n");
                        } else {
                            resString = resString.concat(texte.getDescription());
                        }
                    }
                }
            }
        } else {
            if (document == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                ICTListeTextes listeTextes = null;
                try {
                    listeTextes = document[0].getTextes(niveau);
                } catch (Exception e3) {
                    // getMemoryLog().logMessage(e3.toString(),FWMessage.ERREUR,getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
                }
                if (listeTextes == null) {
                    /*
                     * getMemoryLog().logMessage( getSession().getLabel("PAS_TEXTE"), FWMessage.ERREUR, "");
                     */
                } else {
                    for (int i = 0; i < listeTextes.size(); i++) {
                        texte = listeTextes.getTexte(i + 1);
                        if (i + 1 < listeTextes.size()) {
                            resString = resString.concat(texte.getDescription() + "\n\n");
                        } else {
                            resString = resString.concat(texte.getDescription());
                        }
                    }
                }
            }
        }
        return resString;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        return start;
    }

    public void setDateHeure(String dateHeure) {
        this.dateHeure = dateHeure;
    }

    public void setIdControleEmployeur(String idControleEmployeur) {
        this.idControleEmployeur = idControleEmployeur;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdDocumentDefaut(String idDocumentDefaut) {
        this.idDocumentDefaut = idDocumentDefaut;
    }

    public void setReviseur(String reviseur) {
        this.reviseur = reviseur;
    }

}
