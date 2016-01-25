package globaz.pavo.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.process.generation.ILEGeneration;
import globaz.leo.process.handler.LEEnvoiHandler;
import globaz.pavo.application.CIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.text.MessageFormat;

public class CIImpressionRappelBta_Doc extends FWIDocumentManager implements ILEGeneration {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CI_DOMAINE = "329000";
    public final static String CI_RAPPEL_REPETITION = "338000";
    private final static String MODEL_NAME = "PAVO_RAPPEL_REPETITION_BTA";
    private final static String NUM_INFOROM = "0206CCI";

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
    protected LEParamEnvoiDataSource docCourant;
    protected LEEnvoiDataSource documentDataSource;
    private String idDossierBta = "";
    private String idEnvoiParent;

    private String idTiers;
    private String langueIsoRequerant = "fr";// langue du requerant

    private boolean publishDocument = true;

    public CIImpressionRappelBta_Doc() throws Exception {
        this(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO));
    }

    public CIImpressionRappelBta_Doc(BSession session) throws Exception {
        super(session, CIApplication.APPLICATION_PAVO_REP, "ImpressionRepetitionBta");
    }

    public CIImpressionRappelBta_Doc(BSession session, String rootApplication, String fileName) throws FWIException,
            JAException {
        super(session, rootApplication, fileName);
    }

    private void _setHeader(CaisseHeaderReportBean bean, TITiersViewBean tiers) throws Exception {
        if (tiers == null) {
            tiers = getTiers(idTiers);
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

    public void addPropriete(String csTypeProp, String valeur) {
        if (ILEConstantes.CS_PARAM_GEN_ID_TIERS.equals(csTypeProp)) {
            setIdTiers(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_PERIODE.equals(csTypeProp)) {
            setAnnee(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ID_ENVOI_PRECEDENT.equals(csTypeProp)) {
            setIdEnvoiParent(valeur);
        }
    }

    @Override
    public void afterExecuteReport() {

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            setDocumentTitle(getSession().getLabel("TITRE_RAPPEL_REPETITION_BTA"));
            super.setTemplateFile(CIImpressionRappelBta_Doc.MODEL_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setFileTitle(getSession().getLabel("TITRE_RAPPEL_REPETITION_BTA"));
    }

    protected void catalogue() throws FWIException {
        try {
            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            helper.setCsDomaine(CIImpressionRappelBta_Doc.CI_DOMAINE); // domaine
            // avs
            helper.setCsTypeDocument(CIImpressionRappelBta_Doc.CI_RAPPEL_REPETITION); // pour
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

        for (int i = 0; i < docCourant.size(); i++) {
            LEParamEnvoiDataSource.paramEnvoi p = docCourant.getParamEnvoi(i);
            addPropriete(p.getCsType(), p.getValeur());
        }

        // récupération du requerant en cours et du tiers correspondant
        TITiersViewBean tiers = getTiers(idTiers);

        // récupération de la langue du tiers
        langueIsoRequerant = tiers.getLangueIso();

        // récupération du catalogue de texte
        catalogue();

        // définit le titre (Madame, Monsieur) du requérant
        String titre = tiers.getFormulePolitesse(null);

        // recherche de la date d'envoi de la lettre de répétition
        LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
        String dateEnvoiRepetition = JACalendar.format(envoiHandler.getDateEnvoi(getIdEnvoiParent(), getSession()),
                langueIsoRequerant);

        // envoi des paramètres
        this.setParametres("P_CONCERNE", this.getTexte(1, 1, null));
        this.setParametres("P_CORPS", this.getTexte(2, new Object[] { titre, dateEnvoiRepetition, annee, titre }));
        // setParametres("P_SIGNATURE", getTexte(3,1,null));

        // mise en place du header
        setTemplateFile(CIImpressionRappelBta_Doc.MODEL_NAME);
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
    }

    private void fillDocInfo() throws JAException {
        getDocumentInfo().setDocumentTypeNumber(CIImpressionRappelBta_Doc.NUM_INFOROM);
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentDate(getDateDocument());
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
        return CIImpressionRappelBta_Doc.formatMessage(message.toString(), args);
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public LEEnvoiDataSource getDocumentDataSource() {
        return documentDataSource;
    }

    public String getIdDossierBta() {
        return idDossierBta;
    }

    public String getIdEnvoiParent() {
        return idEnvoiParent;
    }

    public String getIdTiers() {
        return idTiers;
    }

    @Override
    public LEEnvoiDataSource getResult() {
        return documentDataSource;
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
                texte = CIImpressionRappelBta_Doc.formatMessage(catalogue.getTextes(niveau).getTexte(position)
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
                resString = CIImpressionRappelBta_Doc.formatMessage(resString, args);
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

    public boolean isPublishDocument() {
        return publishDocument;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        boolean hasNext = false;
        if (docCourant == null) {
            hasNext = true;
            docCourant = getDocumentDataSource().getParamEnvoi();
        }
        return hasNext;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    @Override
    public void setDateImpression(String date) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDocumentDataSource(LEEnvoiDataSource source) {
        documentDataSource = source;
    }

    public void setIdDossierBta(String idDossierBta) {
        this.idDossierBta = idDossierBta;
    }

    public void setIdEnvoiParent(String idEnvoiParent) {
        this.idEnvoiParent = idEnvoiParent;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    @Override
    public void setNomDoc(String nomDoc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPublishDocument(boolean value) {
        publishDocument = value;
    }

    @Override
    public void setSessionModule(BSession session) throws Exception {
        // TODO Auto-generated method stub

    }
}
