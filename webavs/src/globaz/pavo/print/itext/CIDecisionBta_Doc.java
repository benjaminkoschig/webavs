package globaz.pavo.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.bta.CIDossierBta;
import globaz.pavo.db.bta.CIRequerantBta;
import globaz.pavo.db.bta.CIRequerantBtaManager;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.db.compte.CIRassemblementOuvertureManager;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.text.MessageFormat;
import java.util.ArrayList;

public class CIDecisionBta_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CI_DECISION = "335000";
    public final static String CI_DOMAINE = "329000";
    private final static String MODEL_NAME = "PAVO_DECISION_OCTROI_BTA";
    private final static String NUM_INFOROM = "0204CCI";

    /**
     * Remplace dans message {n} par args[n].
     * <p>
     * Evite que {@link MessageFormat} ne lance une erreur ou ne se comporte pas correctement si le message contient des
     * apostrophes
     * </p>
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    public static final String formatMessage(String message, Object[] args) {
        StringBuffer buffer = new StringBuffer(message);

        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < buffer.length(); ++idChar) {
            if ((buffer.charAt(idChar) == '\'')
                    && ((idChar == (buffer.length() - 1)) || (buffer.charAt(idChar + 1) != '\''))) {
                buffer.insert(idChar, '\'');
                ++idChar;
            }
        }

        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }
        // remplacer et retourner
        return MessageFormat.format(buffer.toString(), args);
    }

    private String annee = JACalendar.todayJJsMMsAAAA().substring(6);
    private ICTDocument catalogue;
    private String dateDocument = JACalendar.todayJJsMMsAAAA();
    private String idDossierBta = "";
    private int indiceRequerant = 0;
    private String langueIsoRequerant = "fr";// langue du requerant
    private ArrayList listeRequerants = new ArrayList();
    private int nbRequerant;
    private String nomImpotent = "";
    private String prenomImpotent = "";

    private String sexeImpotent = "";

    public CIDecisionBta_Doc() throws Exception {
        this(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO));
    }

    public CIDecisionBta_Doc(BSession session) throws Exception {
        super(session, CIApplication.APPLICATION_PAVO_REP, "ImpressionDecisionBta");
    }

    public CIDecisionBta_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    private void _setHeader(CaisseHeaderReportBean bean, TITiersViewBean tiers) throws Exception {

        if (tiers == null) {
            CIRequerantBta requerant = (CIRequerantBta) listeRequerants.get(indiceRequerant);
            tiers = getTiers(requerant.getIdTiersRequerant());
        }
        bean.setAdresse(tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                CIApplication.CS_DOMAINE_ADRESSE_CI_ARC, JACalendar.todayJJsMMsAAAA()));
        bean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), langueIsoRequerant));
        bean.setNoAvs(tiers.getNumAvsActuel());
        bean.setConfidentiel(true);
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
    }

    protected void abort(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
        this.abort();
    }

    @Override
    public void afterExecuteReport() {
        // Fusionne tous les documents en 1 seul document
        try {
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentTitle(getSession().getLabel("TITRE_DECISION_BTA"));
            docInfo.setDocumentType(CIDecisionBta_Doc.NUM_INFOROM);
            docInfo.setDocumentTypeNumber(CIDecisionBta_Doc.NUM_INFOROM);
            this.mergePDF(docInfo, true, 500, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            super.setTemplateFile(CIDecisionBta_Doc.MODEL_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // Impression par lot
        setImpressionParLot(true);
        setTailleLot(0);
        setFileTitle(getSession().getLabel("TITRE_DECISION_BTA"));

        // Vérifier que le dossier est en état "ouvert" sinon annulation du
        // process
        CIDossierBta dossierBta = new CIDossierBta();
        dossierBta.setSession(getSession());
        dossierBta.setIdDossierBta(idDossierBta);
        try {
            dossierBta.retrieve();
            if (!dossierBta.getEtatDossier().equals(CIDossierBta.CS_ETAT_OUVERT)) {
                this.abort(getSession().getLabel("MSG_ERREUR_DOSSIER_NON_OUVERT"), FWMessage.ERREUR);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // Récupération information sur l'impotent
        getInfoImpotent();

        // Récupération des requérants du dossier
        CIRequerantBtaManager requerantManager = new CIRequerantBtaManager();
        requerantManager.setSession(getSession());
        requerantManager.setForIdDossierBta(idDossierBta);
        try {
            if (JadeStringUtil.isBlank(annee)) {
                annee = JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString();
            }

            requerantManager.find();

            // construction de la liste de requérant ayant une décision positive
            for (int i = 0; i < requerantManager.size(); i++) {
                String dateCloture = "";// date de cloture du CI si existe
                String sexeRequerantCi = "";

                // Récupération du requérant
                CIRequerantBta requerant = (CIRequerantBta) requerantManager.getEntity(i);

                // rechercher du CI du requérant
                CICompteIndividuelManager compteIndManager = new CICompteIndividuelManager();
                compteIndManager.setSession(getSession());
                compteIndManager.setForNumeroAvs(NSUtil.unFormatAVS(requerant.getNumeroNnssRequerant()));
                compteIndManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);// pour
                // ne
                // prendre
                // que
                // les
                // CI
                // ouverts
                compteIndManager.find();
                if (compteIndManager.size() > 0) {
                    // Récupération du CI
                    CICompteIndividuel compteInd = (CICompteIndividuel) compteIndManager.getFirstEntity();
                    sexeRequerantCi = compteInd.getSexe();

                    // recherche si il existe une date de cloture
                    CIRassemblementOuvertureManager rassemblementManager = new CIRassemblementOuvertureManager();
                    rassemblementManager.setSession(getSession());
                    rassemblementManager.setForCompteIndividuelId(compteInd.getCompteIndividuelId());
                    rassemblementManager.find();
                    if (rassemblementManager.size() > 0) {
                        for (int k = 0; k < rassemblementManager.size(); k++) {
                            CIRassemblementOuverture rassemblement = (CIRassemblementOuverture) rassemblementManager
                                    .getEntity(k);
                            // si le motif est de type 71 ou 81 on est dans le
                            // cas d'un rentier
                            if ((rassemblement.getMotifArc().equals("71") || rassemblement.getMotifArc().equals("81"))
                                    && JadeStringUtil.isBlank(rassemblement.getDateRevocation())) {
                                dateCloture = rassemblement.getDateCloture();
                            }
                        }
                    }

                    // ajout dans la liste des requerants dont la decision
                    // d'octroi est positive
                    if (!requerant.getDateDebut().equals(requerant.getDateFin())
                            && (JadeStringUtil.isBlank(requerant.getDateFin()) || (JADate.getYear(
                                    requerant.getDateFin()).intValue() >= Integer.parseInt(annee)))
                            && (JADate.getYear(requerant.getDateDebut()).intValue() <= Integer.parseInt(annee))) {
                        if (JadeStringUtil.isBlank(dateCloture)
                                && isMajeur(new JADate(requerant.getDateNaissanceRequerant()), Integer.parseInt(annee))
                                && !CIUtil.isRetraite(new JADate(requerant.getDateNaissanceRequerant()),
                                        sexeRequerantCi, Integer.parseInt(annee))) {
                            listeRequerants.add(requerant);
                        }
                    }
                } else {
                    this.abort(
                            getSession().getLabel("MSG_ERREUR_CI_INTROUVABLE") + " "
                                    + requerant.getNumeroNnssRequerant(), FWMessage.ERREUR);
                }
            }

            // itérateur pour la création des documents (méthode next)
            nbRequerant = listeRequerants.size();
            if (nbRequerant == 0) {
                getMemoryLog().logMessage(getSession().getLabel("MSG_AUCUNE_DECISION_BTA") + "(" + annee + ")",
                        FWMessage.INFORMATION, this.getClass().getName());
            }
            setProgressScaleValue(nbRequerant);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void catalogue() throws FWIException {
        try {
            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            helper.setCsDomaine(CIDecisionBta_Doc.CI_DOMAINE); // domaine avs
            helper.setCsTypeDocument(CIDecisionBta_Doc.CI_DECISION); // pour le
            // type
            // de
            // catalogue
            helper.setCodeIsoLangue(langueIsoRequerant); // dans la langue du
            // salarié
            helper.setActif(Boolean.TRUE); // actif
            helper.setDefault(Boolean.TRUE); // et par défaut

            // charger le catalogue de texte
            ICTDocument[] candidats = helper.load();

            if ((candidats != null) && (candidats.length > 0)) {
                catalogue = candidats[0];
            }
        } catch (Exception e) {
            catalogue = null;
        }

        if (catalogue == null) {
            this.abort(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR);
            throw new FWIException(getSession().getLabel("CATALOGUE_INTROUVABLE"));
        }
    }

    @Override
    public void createDataSource() throws Exception {
        // récupération du requerant en cours et du tiers correspondant
        CIRequerantBta requerant = (CIRequerantBta) listeRequerants.get(indiceRequerant);
        TITiersViewBean tiers = getTiers(requerant.getIdTiersRequerant());

        // nom du document
        setDocumentTitle(getSession().getLabel("TITRE_DECISION_BTA") + " " + tiers.getNumAvsActuel());

        // mise en GED
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setDocumentTypeNumber(CIDecisionBta_Doc.NUM_INFOROM);
        getDocumentInfo().setDocumentDate(JACalendar.todayJJsMMsAAAA());
        getDocumentInfo().setDocumentType(CIDecisionBta_Doc.NUM_INFOROM);
        getDocumentInfo().setDocumentProperty("numero.avs.non.formatte",
                CIUtil.unFormatAVS(requerant.getNumeroNnssRequerant()));
        getDocumentInfo().setDocumentProperty("numero.avs.formatte", requerant.getNumeroNnssRequerant());
        getDocumentInfo().setDocumentTitle(getSession().getLabel("TITRE_DECISION_BTA"));

        // récupération de la langue du tiers
        langueIsoRequerant = tiers.getLangueIso();

        // récupération du catalogue de texte
        catalogue();

        // définit le titre du requérant avec la langue du tiers
        String titre = tiers.getFormulePolitesse(null);

        // définit le lien entre l'impotent et le requérant
        CIApplication app = (CIApplication) getSession().getApplication();

        String typeImpotent = "";
        if (requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_ARRIEREGRANDPARENT)) {
            if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
                typeImpotent = app.getLabel("LIEN_ARRIERE_PETIT_FILS", langueIsoRequerant);
            } else {
                typeImpotent = app.getLabel("LIEN_ARRIERE_PETITE_FILLE", langueIsoRequerant);
            }
        } else if (requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_GRANDPARENT)) {
            if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
                typeImpotent = app.getLabel("LIEN_PETIT_FILS", langueIsoRequerant);
            } else {
                typeImpotent = app.getLabel("LIEN_PETITE_FILLE", langueIsoRequerant);
            }
        } else if (requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_BEAUPARENT)) {
            if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
                typeImpotent = app.getLabel("LIEN_BEAU_FILS", langueIsoRequerant);
            } else {
                typeImpotent = app.getLabel("LIEN_BELLE_FILLE", langueIsoRequerant);
            }
        } else if (requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_PARENT)) {
            if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
                typeImpotent = app.getLabel("LIEN_FILS", langueIsoRequerant);
            } else {
                typeImpotent = app.getLabel("LIEN_FILLE", langueIsoRequerant);
            }
        } else if (requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_FRERESOEUR)) {
            if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
                typeImpotent = app.getLabel("LIEN_FRERE", langueIsoRequerant);
            } else {
                typeImpotent = app.getLabel("LIEN_SOEUR", langueIsoRequerant);
            }
        } else if ((requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_ENFANT))
                || (requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_ENFANTAUTRELIT))) {
            if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
                typeImpotent = app.getLabel("LIEN_PERE", langueIsoRequerant);
            } else {
                typeImpotent = app.getLabel("LIEN_MERE", langueIsoRequerant);
            }
        } else if (requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_PETITENFANT)) {
            if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
                typeImpotent = app.getLabel("LIEN_GRAND_PERE", langueIsoRequerant);
            } else {
                typeImpotent = app.getLabel("LIEN_GRAND_MERE", langueIsoRequerant);
            }
        } else if (requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_CONJOINT)) {
            if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
                typeImpotent = app.getLabel("LIEN_EPOUX", langueIsoRequerant);
            } else {
                typeImpotent = app.getLabel("LIEN_EPOUSE", langueIsoRequerant);
            }
        } else if ((requerant.getLienParente().equals(CIRequerantBta.CS_LIEN_AUTRE))
                || requerant.getTypeRequerant().equals(CIRequerantBta.CS_TYPE_CONJOINT)) {
            if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
                typeImpotent = app.getLabel("MSG_MONSIEUR", langueIsoRequerant) + " " + nomImpotent + " "
                        + prenomImpotent;
            } else {
                typeImpotent = app.getLabel("MSG_MADAME", langueIsoRequerant) + " " + nomImpotent + " "
                        + prenomImpotent;
            }
        }

        // envoi des paramètres
        this.setParametres("P_CONCERNE", this.getTexte(1, 1, null));
        this.setParametres(
                "P_CORPS",
                this.getTexte(2, new Object[] { titre, typeImpotent, annee,
                        String.valueOf(Integer.parseInt(annee) + 1), titre }));
        // setParametres("P_SIGNATURE", getTexte(3,1,null));

        // mise en place du header
        setTemplateFile(CIDecisionBta_Doc.MODEL_NAME);
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), langueIsoRequerant);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        _setHeader(headerBean, tiers);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        // this.getImporter().getParametre().put(ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
        // ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() +
        // "/" + this.getTemplateProperty("header.filename"));

        // mise en place de la signature
        CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();
        signBean.setService2("");
        signBean.setSignataire2("");

        // on récupère la propriété "signature.nom.caisse" du
        // jasperGlobazProperties
        String caisseSignature = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE
                + langueIsoRequerant.toUpperCase());
        signBean.setSignatureCaisse(caisseSignature);

        // on récupère la propriété "signature.nom.service" du
        // jasperGlobazProperties
        String serviceSignature = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE
                + langueIsoRequerant.toUpperCase());
        // la méthode _replaceVars permet de remplacer les chaine de type
        // {user.service}
        String serviceSignatureFinal = ACaisseReportHelper._replaceVars(serviceSignature, getSession().getUserId(),
                null);
        signBean.setService(serviceSignatureFinal);

        // on récupère la propriété "signature.signataire" du
        // jasperGlobazProperties
        String signataireSignature = getTemplateProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_SIGN_SIGNATAIRE + langueIsoRequerant.toUpperCase());
        signBean.setSignataire(signataireSignature);

        caisseReportHelper.addSignatureParameters(this, signBean);

        indiceRequerant++;
    }

    /**
     * remplace dans message {n} par args[n].
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    protected String formatMessage(StringBuffer message, Object[] args) {
        return CIDecisionBta_Doc.formatMessage(message.toString(), args);
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getIdDossierBta() {
        return idDossierBta;
    }

    private void getInfoImpotent() {
        CIDossierBta dossierBta = new CIDossierBta();
        dossierBta.setSession(getSession());
        dossierBta.setIdDossierBta(idDossierBta);
        try {
            dossierBta.retrieve();
            sexeImpotent = dossierBta.getTiersViewBean().getSexe();
            nomImpotent = dossierBta.getTiersViewBean().getDesignation1();
            prenomImpotent = dossierBta.getTiersViewBean().getDesignation2();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Récupère le texte du catalogue en fonction du niveau et de la position, et remplace les {n} par les textes passés
     * dans le tableau d'objet "args"
     * 
     * @param niveau
     * @param position
     * @param args
     * @return
     * @throws FWIException
     */
    protected String getTexte(int niveau, int position, Object[] args) throws FWIException {
        String texte;
        try {
            if (args != null) {
                texte = CIDecisionBta_Doc.formatMessage(
                        catalogue.getTextes(niveau).getTexte(position).getDescription(), args);
            } else {
                texte = catalogue.getTextes(niveau).getTexte(position).getDescription();
            }
            return texte;
        } catch (Exception e) {
            return "";
        }
    }

    protected String getTexte(int niveau, Object[] args) throws FWIException {
        String resString = "";
        ICTTexte texte = null;
        try {

            ICTListeTextes listTexte = catalogue.getTextes(niveau);

            if (listTexte != null) {
                for (int i = 0; i < listTexte.size(); i++) {
                    texte = listTexte.getTexte(i + 1);
                    if (i + 1 < listTexte.size()) {
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }

            if (args != null) {
                resString = CIDecisionBta_Doc.formatMessage(resString, args);
            }

            return resString;

        } catch (Exception e) {
            return "";
        }
    }

    private TITiersViewBean getTiers(String idTiers) {
        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(getSession());
        tiers.setIdTiers(idTiers);
        try {
            tiers.retrieve();
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERREUR_GETTING_TIERS"));
        }
        return tiers;
    }

    public boolean isMajeur(JADate dateNaissance, int annee) {
        boolean majeur = false;
        int anneeDroitBta = dateNaissance.getYear() + 18;
        if (annee >= anneeDroitBta) {
            majeur = true;
        }

        return majeur;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        if (!isAborted()) {
            if (indiceRequerant < nbRequerant) {
                incProgressCounter();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setIdDossierBta(String idDossierBta) {
        this.idDossierBta = idDossierBta;
    }
}
