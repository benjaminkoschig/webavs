package globaz.draco.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.draco.application.DSApplication;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.process.generation.ILEGeneration;
import globaz.naos.application.AFApplication;
import globaz.naos.util.AFUtil;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.text.MessageFormat;

public class DSContMandatAgentAvs_Doc extends FWIDocumentManager implements ILEGeneration {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CI_DOMAINE = "124001";
    public final static String CI_RAPPEL_REPETITION = "125008";
    public final static String CI_RAPPORT_AGENT = "1250011";
    private final static String MODEL_NAME = "DRACO_MANDAT_AGENT";
    private final static String NUM_INFOROM = "0215CDS";

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

    private String affiliationNumero;
    private ICTDocument catalogue;
    private String dateImpression;
    protected LEParamEnvoiDataSource docCourant;
    protected LEEnvoiDataSource documentDataSource;
    private String idDestinataire;
    private String idEnvoiParent;
    private String idTiers;

    private String langueIsoDest = "fr";// langue du requerant

    int pageNum = 1;
    private String periode;

    private boolean publishDocument = true;

    public DSContMandatAgentAvs_Doc() throws Exception {
        this(new BSession(DSApplication.DEFAULT_APPLICATION_DRACO));
    };

    /**
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public DSContMandatAgentAvs_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    public DSContMandatAgentAvs_Doc(globaz.globall.db.BSession session) throws Exception {
        super(session, DSApplication.DEFAULT_APPLICATION_ROOT, "ImpressionMandatAgent");
    }

    private void _setHeader(CaisseHeaderReportBean bean, TITiers tiers) throws Exception {
        bean.setAdresse(tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA()));
        String documentDate = JadeStringUtil.isEmpty(getDateImpression()) ? JACalendar.todayJJsMMsAAAA()
                : getDateImpression();
        bean.setDate(JACalendar.format(documentDate, langueIsoDest));
        // bean.setNoAvs(tiers.getNumAvsActuel());
        bean.setNoAffilie(getAffiliationNumero());
        bean.setConfidentiel(false);
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
            setPeriode(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ID_ENVOI_PRECEDENT.equals(csTypeProp)) {
            setIdEnvoiParent(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE.equals(csTypeProp)) {
            setIdDestinataire(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_NUMERO.equals(csTypeProp)) {
            setAffiliationNumero(valeur);
        }
    }

    @Override
    public void afterExecuteReport() {
        // Fusionne tous les documents en 1 seul document
        try {
            this.mergePDF(getDocumentInfo(), true, 500, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setImpressionParLot(true);
        setTailleLot(0);
        setFileTitle("MandatAgentAvs");
    }

    protected void catalogue(String domaineCatalogue, String typeCatalogue) throws FWIException {
        try {
            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            helper.setCsDomaine(domaineCatalogue); // domaine avs
            helper.setCsTypeDocument(typeCatalogue); // pour le type de catalogue
            helper.setCodeIsoLangue(langueIsoDest); // dans la langue du salarié
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
        for (int i = 0; i < docCourant.size(); i++) {
            LEParamEnvoiDataSource.paramEnvoi p = docCourant.getParamEnvoi(i);
            addPropriete(p.getCsType(), p.getValeur());
        }

        // récupération du tiers destinataire et du tiers
        // TITiersViewBean tiersDest = getTiersDestinataire(idDestinataire);
        TITiers tiersDest = getTiersDestinataire(idDestinataire);
        TITiersViewBean tiers = getTiers(idTiers);

        TIDocumentInfoHelper.fill(getDocumentInfo(), getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                getAffiliationNumero(), AFUtil.giveNumeroAffilieNonFormate(getAffiliationNumero()));

        getDocumentInfo().setDocumentTypeNumber(DSContMandatAgentAvs_Doc.NUM_INFOROM);
        getDocumentInfo().setDocumentProperty("annee", getPeriode());
        getDocumentInfo().setDocumentProperty("document.date",
                JadeStringUtil.isEmpty(getDateImpression()) ? JACalendar.todayJJsMMsAAAA() : getDateImpression());
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getAffiliationNumero());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(getAffiliationNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", getAffiliationNumero());
        }

        // récupération de la langue du tiers
        langueIsoDest = tiersDest.getLangueIso();

        if (pageNum == 1) {
            setTemplateFile(DSContMandatAgentAvs_Doc.MODEL_NAME);
            setDocumentTitle(getSession().getLabel("AGENTAVS_MANDAT"));
            // récupération du catalogue de texte
            catalogue(DSContMandatAgentAvs_Doc.CI_DOMAINE, DSContMandatAgentAvs_Doc.CI_RAPPEL_REPETITION);
            // setParametres("P_TIERS_LABEL", getSession().getLabel("CONCERNE_TIERS"));
            this.setParametres(
                    "P_TIERS",
                    getTiers(getIdTiers()).getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA()));
            // setParametres("P_OBJET_LABEL", getSession().getLabel("OBJET_AGENT"));
            // setParametres("P_OBJET", getSession().getLabel("DECOMPTE_EMPLOYEUR") + " " + getPeriode());
            this.setParametres("P_OBJET", this.getTexte(1, 1, new Object[] { getPeriode() }));
            this.setParametres("P_CONCERNE", this.getTexte(1, 2, null));
            this.setParametres(
                    "P_CORPS",
                    this.getTexte(2,
                            new Object[] { getSession().getApplication().getLabel("MADAME_MONSIEUR", langueIsoDest) }));
            this.setParametres("P_SIGNATURE", this.getTexte(3, null));
            // mise en place du header

            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    getDocumentInfo(), getSession().getApplication(), langueIsoDest);
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
            _setHeader(headerBean, tiersDest);
            caisseReportHelper.addHeaderParameters(this, headerBean);
            getImporter().getParametre().put(
                    ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                    ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                            + getTemplateProperty(getDocumentInfo(), "header.filename"));
            pageNum = 2;
        } else if (pageNum == 2) {
            setTemplateFile("DRACO_RAPPORT_MANDAT_AGENT");
            setDocumentTitle("Rapport Agent");
            // récupération du catalogue de texte
            catalogue(DSContMandatAgentAvs_Doc.CI_DOMAINE, DSContMandatAgentAvs_Doc.CI_RAPPORT_AGENT);
            this.setParametres("P_TITRE", this.getTexte(1, 1, null));
            // setParametres("P_CONCERNE", getSession().getLabel("DECOMPTE_EMPLOYEUR") + " " + getPeriode());
            this.setParametres("P_CONCERNE", this.getTexte(1, 2, new Object[] { getPeriode() }));
            this.setParametres("P_QUESTION_1", this.getTexte(2, 1, null));
            this.setParametres("P_QUESTION_2", this.getTexte(2, 2, null));
            this.setParametres("P_QUESTION_3", this.getTexte(2, 3, null));
            this.setParametres("P_FRAIS", this.getTexte(2, 4, null));
            this.setParametres("P_DATE", this.getTexte(3, 1, null));
            this.setParametres("P_SIGNATURE", this.getTexte(3, 2, null));

            // mise en place du header
            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    getDocumentInfo(), getSession().getApplication(), langueIsoDest);
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
            _setHeader(headerBean, tiers);
            caisseReportHelper.addHeaderParameters(this, headerBean);
            getImporter().getParametre().put(
                    ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                    ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                            + getTemplateProperty(getDocumentInfo(), "header.filename"));
            pageNum = 0;
        }
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
        return DSContMandatAgentAvs_Doc.formatMessage(message.toString(), args);
    }

    public String getAffiliationNumero() {
        return affiliationNumero;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public LEEnvoiDataSource getDocumentDataSource() {
        return documentDataSource;
    }

    public String getIdDestinataire() {
        return idDestinataire;
    }

    public String getIdEnvoiParent() {
        return idEnvoiParent;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getPeriode() {
        return periode;
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
                texte = DSContMandatAgentAvs_Doc.formatMessage(catalogue.getTextes(niveau).getTexte(position)
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
                resString = DSContMandatAgentAvs_Doc.formatMessage(resString, args);
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

    private TITiers getTiersDestinataire(String idTiers) {
        TITiers tiers = new TITiers();
        tiers.setSession(getSession());
        tiers.setIdTiers(getIdDestinataire());
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
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        boolean hasNext = false;
        /*
         * if(docCourant==null){ hasNext = true; docCourant = getDocumentDataSource().getParamEnvoi(); }
         */

        if (docCourant == null) {
            docCourant = getDocumentDataSource().getParamEnvoi();
        }

        if ((pageNum == 1) || (pageNum == 2)) {
            hasNext = true;
        }

        return hasNext;
    }

    public void setAffiliationNumero(String affiliationNumero) {
        this.affiliationNumero = affiliationNumero;
    }

    @Override
    public void setDateImpression(String date) {
        dateImpression = date;
    }

    @Override
    public void setDocumentDataSource(LEEnvoiDataSource source) {
        documentDataSource = source;
    }

    public void setIdDestinataire(String idDestinataire) {
        this.idDestinataire = idDestinataire;
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

    public void setPeriode(String periode) {
        this.periode = periode;
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
