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
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.LEGenererEnvoi;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.bta.CIDossierBta;
import globaz.pavo.db.bta.CIDossierBtaManager;
import globaz.pavo.db.bta.CIRequerantBta;
import globaz.pavo.db.bta.CIRequerantBtaManager;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CIRepetitionBta_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CI_DOMAINE = "329000";
    public final static String CI_REPETITION = "336000";
    private final static String MODEL_NAME = "PAVO_LETTRE_REPETITION_BTA";
    private final static String NUM_INFOROM = "0205CCI";

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

    private String annee;
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

    private boolean simulation = false;

    public CIRepetitionBta_Doc() throws Exception {
        this(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO));
        /*
         * int anneeActuelle = JADate.getYear(JACalendar.todayJJsMMsAAAA()).intValue(); annee =
         * String.valueOf(anneeActuelle-1);
         */
    }

    public CIRepetitionBta_Doc(BSession session) throws Exception {
        super(session, CIApplication.APPLICATION_PAVO_REP, "ImpressionRepetitionBta");
        /*
         * int anneeActuelle = JADate.getYear(JACalendar.todayJJsMMsAAAA()).intValue(); annee =
         * String.valueOf(anneeActuelle-1);
         */
    }

    public CIRepetitionBta_Doc(BSession session, String rootApplication, String fileName) throws FWIException,
            JAException {
        super(session, rootApplication, fileName);
        /*
         * int anneeActuelle = JADate.getYear(JACalendar.todayJJsMMsAAAA()).intValue(); annee =
         * String.valueOf(anneeActuelle-1);
         */
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
            docInfo.setDocumentTitle(getSession().getLabel("TITRE_REPETITION_BTA"));
            docInfo.setDocumentType(CIRepetitionBta_Doc.NUM_INFOROM);
            docInfo.setDocumentTypeNumber(CIRepetitionBta_Doc.NUM_INFOROM);
            this.mergePDF(docInfo, true, 500, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            setDocumentTitle(getSession().getLabel("TITRE_REPETITION_BTA") + " " + indiceRequerant);
            super.setTemplateFile(CIRepetitionBta_Doc.MODEL_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // Impression par lot
        setImpressionParLot(true);
        setTailleLot(0);

        setFileTitle(getSession().getLabel("TITRE_REPETITION_BTA"));

        // Récupération de tous les dossiers ouverts
        CIDossierBtaManager dossierManager = new CIDossierBtaManager();
        dossierManager.setSession(getSession());
        dossierManager.setForEtatDossier(CIDossierBta.CS_ETAT_OUVERT);
        try {
            dossierManager.find(BManager.SIZE_NOLIMIT);

            // initialisation de annee = annee précédente
            int anneeActuelle = JADate.getYear(JACalendar.todayJJsMMsAAAA()).intValue();
            annee = String.valueOf(anneeActuelle - 1);

            // parcours des dossiers BTA
            for (int i = 0; i < dossierManager.size(); i++) {
                boolean updateDossier = false;
                CIDossierBta dossier = (CIDossierBta) dossierManager.getEntity(i);
                // on prend que les dossiers qui n'ont pas de date de fin
                if (JadeStringUtil.isBlank(dossier.getDateFinDossier())) {
                    // récupération des requerants du dossier
                    CIRequerantBtaManager requerantManager = new CIRequerantBtaManager();
                    requerantManager.setSession(getSession());
                    requerantManager.setForIdDossierBta(dossier.getIdDossierBta());
                    requerantManager.find();
                    // parcours des requerants du dossier
                    for (int j = 0; j < requerantManager.size(); j++) {
                        CIRequerantBta requerant = (CIRequerantBta) requerantManager.getEntity(j);
                        // recherche du CI du requerant
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
                            CICompteIndividuel compteInd = (CICompteIndividuel) compteIndManager.getFirstEntity();
                            // récupération des écriture
                            CIEcritureManager ecritureManager = new CIEcritureManager();
                            ecritureManager.setSession(getSession());
                            ecritureManager.setForCompteIndividuelId(compteInd.getCompteIndividuelId());
                            ecritureManager.setForAnnee(annee);
                            ecritureManager.find();
                            // parcours des écritures
                            for (int k = 0; k < ecritureManager.size(); k++) {
                                CIEcriture ecriture = (CIEcriture) ecritureManager.getEntity(k);
                                if (!ecriture.getPartBta().equals("0")) {
                                    // on a une inscription de type BTA pour
                                    // l'année => on ajoute le requerant à la
                                    // liste
                                    listeRequerants.add(requerant);
                                    updateDossier = true;
                                }
                            }
                        }
                    }
                    // mettre le dossier à l'état "à traiter" si on a imprimer
                    // une lettre pour l'un des requerant du dossier
                    if (updateDossier && !simulation) {
                        dossier.setEtatDossier(CIDossierBta.CS_ETAT_ATRAITER);
                        dossier.update(getTransaction());
                    }
                }
            }

            // itérateur pour la création des documents (méthode next)
            nbRequerant = listeRequerants.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void catalogue() throws FWIException {
        try {
            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            helper.setCsDomaine(CIRepetitionBta_Doc.CI_DOMAINE); // domaine avs
            helper.setCsTypeDocument(CIRepetitionBta_Doc.CI_REPETITION); // pour
            // le
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
        // remplissage du docInfo
        fillDocInfo();

        // récupération du requerant en cours et du tiers correspondant
        CIRequerantBta requerant = (CIRequerantBta) listeRequerants.get(indiceRequerant);
        TITiersViewBean tiers = getTiers(requerant.getIdTiersRequerant());

        // récupération de la langue du tiers
        langueIsoRequerant = tiers.getLangueIso();

        // récupération du catalogue de texte
        catalogue();

        // définit le titre (Madame, Monsieur) du requérant avec la langue du
        // tiers
        String titre = tiers.getFormulePolitesse(null);

        // définit la formule de politesse et le nom de l'impotent
        CIApplication app = (CIApplication) getSession().getApplication();
        loadInfoImpotent(requerant.getIdDossierBta());
        String impotent = "";
        if (sexeImpotent.equals(TITiersViewBean.CS_HOMME)) {
            impotent = app.getLabel("MSG_MONSIEUR", langueIsoRequerant) + " " + nomImpotent + " " + prenomImpotent;
        } else if (sexeImpotent.equals(TITiersViewBean.CS_FEMME)) {
            impotent = app.getLabel("MSG_MADAME", langueIsoRequerant) + " " + nomImpotent + " " + prenomImpotent;
        } else {
            impotent = nomImpotent + " " + prenomImpotent;
        }

        // envoi des paramètres
        this.setParametres("P_CONCERNE", this.getTexte(1, 1, null));
        this.setParametres("P_CORPS", this.getTexte(2, new Object[] { titre, impotent, titre }));
        // setParametres("P_SIGNATURE", getTexte(3,1,null));

        // mise en place du header
        setTemplateFile(CIRepetitionBta_Doc.MODEL_NAME);
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

        // Générer le suivi BTA dans LEO
        if (!simulation) {
            genererControle(tiers, annee);
        }
        indiceRequerant++;
    }

    private void fillDocInfo() throws JAException {
        getDocumentInfo().setDocumentTypeNumber(CIRepetitionBta_Doc.NUM_INFOROM);
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
        return CIRepetitionBta_Doc.formatMessage(message.toString(), args);
    }

    public void genererControle(TITiersViewBean tiers, String annee) throws Exception {
        // prépare les données pour l'envoi
        HashMap params = new HashMap();
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, tiers.getIdTiers());
        params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, tiers.getNumAvsActuel());
        params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_ASSURE);
        params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, CIApplication.DEFAULT_APPLICATION_PAVO);
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, tiers.getIdTiers());
        params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, JADate.getYear(JACalendar.todayJJsMMsAAAA()).toString());

        // execute le process de génération
        LEGenererEnvoi gen = new LEGenererEnvoi();
        gen.setSession(getSession());
        gen.setParamEnvoiList(params);
        gen.setSendCompletionMail(false);
        gen.setGenerateEtapeSuivante(new Boolean(true));
        gen.setCsDocument(ILEConstantes.CS_DEBUT_SUIVI_BTA);
        gen.executeProcess();
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

    public boolean getSimulation() {
        return simulation;
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
                texte = CIRepetitionBta_Doc.formatMessage(catalogue.getTextes(niveau).getTexte(position)
                        .getDescription(), args);
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
                resString = CIRepetitionBta_Doc.formatMessage(resString, args);
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

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Charge les informations sur l'impotent
     */
    private void loadInfoImpotent(String idDossierBta) throws Exception {
        CIDossierBta dossierBta = new CIDossierBta();
        dossierBta.setSession(getSession());
        dossierBta.setIdDossierBta(idDossierBta);
        try {
            dossierBta.retrieve();
            sexeImpotent = dossierBta.getTiersViewBean().getSexe();
            nomImpotent = dossierBta.getTiersViewBean().getDesignation1();
            prenomImpotent = dossierBta.getTiersViewBean().getDesignation2();

        } catch (Exception e1) {
            throw new Exception("Unable to load informations BTA");
        }
    }

    @Override
    public boolean next() throws FWIException {
        if (indiceRequerant < nbRequerant) {
            return true;
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

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }
}
