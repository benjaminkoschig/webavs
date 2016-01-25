package globaz.naos.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonProperties;
import globaz.webavs.common.ICommonConstantes;
import java.text.MessageFormat;
import net.sf.jasperreports.engine.JasperPrint;

public class AFDecisionCotisations_Doc extends FWIDocumentManager {

    private static final long serialVersionUID = 649554808209951775L;
    public final static String AF_DECISION = "836032";
    public final static String AF_DOMAINE = "835001";// affiliation
    private final static String MODEL_NAME = "NAOS_ACOMPTE_PARITAIRE";
    private final static String MODEL_NAME_VERSO = "NAOS_ACOMPTE_PARITAIRE_VERSO";
    public final static String NUM_INFOROM = "0207CAF";

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

    private AFAffiliation affilie;

    private ICTDocument catalogue;

    private String cotisation;

    private String dateDocument = JACalendar.todayJJsMMsAAAA();
    private String dateImprime = "";
    private String langueIsoAffilie = "fr";// langue du requerant, fr par defaut
    private String masseSalariale;
    private int nbDoc = 0;

    private String periodeDebut = "";
    private String periodeFin = "";
    private String periodicite = "";
    private String anneeEcran = "";

    public AFDecisionCotisations_Doc() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    public AFDecisionCotisations_Doc(BSession session) throws Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, session.getLabel("TITLE_ACOMPTE_PARITAIRE"));
    }

    public AFDecisionCotisations_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    private void _setHeader(CaisseHeaderReportBean bean, TITiersViewBean tiers) throws Exception {
        bean.setAdresse(tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA()));
        bean.setDate(JACalendar.format(getDateImprime(), langueIsoAffilie));
        bean.setNoAffilie(affilie.getAffilieNumero());

        // Renseinge le Numéro IDE
        AFIDEUtil.addNumeroIDEInDoc(bean, affilie.getNumeroIDE(), affilie.getIdeStatut());

        bean.setConfidentiel(false);
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
    }

    protected void abort(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
        this.abort();
    }

    @Override
    public void afterBuildReport() {
        try {
            JasperPrint verso = null;
            verso = getVerso();
            if (verso != null) {
                verso.setName(getSession().getLabel("TITLE_DECISION_COTISATIONS_VERSO"));
                super.getDocumentList().add(verso);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterExecuteReport() {

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            super.setTemplateFile(AFDecisionCotisations_Doc.MODEL_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {

    }

    protected void catalogue() throws FWIException {
        try {
            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            helper.setCsDomaine(AFDecisionCotisations_Doc.AF_DOMAINE); // domaine
            // avs
            helper.setCsTypeDocument(AFDecisionCotisations_Doc.AF_DECISION); // pour
            // le
            // type
            // de
            // catalogue
            helper.setCodeIsoLangue(langueIsoAffilie); // dans la langue du
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
        fillDocInfo();

        setDocumentTitle(getSession().getLabel("TITLE_ACOMPTE_PARITAIRE") + " " + affilie.getAffilieNumero());

        // récupération de la langue du tiers
        langueIsoAffilie = affilie.getTiers().getLangueIso();

        // récupération du catalogue de texte
        catalogue();

        // envoi des paramètres
        this.setParametres("P_CONCERNE",
                this.getTexte(1, 1, new Object[] { getPeriodeDebut() + " - " + getPeriodeFin() }));
        this.setParametres("P_CORPS_1", this.getTexte(2, 1, new Object[] { getPeriodicite() }));
        this.setParametres("P_COTISATION",
                this.getTexte(2, 2, new Object[] { new FWCurrency(getCotisation()).toStringFormat() }));
        this.setParametres("P_MASSE",
                this.getTexte(2, 3, new Object[] { new FWCurrency(getMasseSalariale()).toStringFormat() }));
        this.setParametres("P_CORPS_2", this.getTexte(3, null));
        this.setParametres("P_SIGNATURE", this.getTexte(4, 1, null));
        this.setParametres("P_REMARQUE1", this.getTexte(5, 1, null));
        this.setParametres("P_REMARQUE2", this.getTexte(5, 2, null));

        // mise en place du header
        setTemplateFile(AFDecisionCotisations_Doc.MODEL_NAME);
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), langueIsoAffilie);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        _setHeader(headerBean, affilie.getTiers());
        caisseReportHelper.addHeaderParameters(this, headerBean);
        getImporter().getParametre().put(
                ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), "header.filename"));
    }

    private void fillDocInfo() throws JAException {
        getDocumentInfo().setDocumentTypeNumber(AFDecisionCotisations_Doc.NUM_INFOROM);
        // getDocumentInfo().setDocumentProperty("pyxis.tiers.numero.avs.non.formatte",
        // NSUtil.unFormatAVS(affilie.getTiers().getNumAvsActuel()));
        getDocumentInfo().setDocumentProperty("annee", getAnneeEcran());

        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", affilie.getAffilieNumero());

        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setArchiveDocument(true);

        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affilie.getAffilieNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affilie.getAffilieNumero());
        }
        try {
            TIDocumentInfoHelper.fill(getDocumentInfo(), affilie.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    getDocumentInfo().getDocumentProperty("numero.affilie.formatte"), getDocumentInfo()
                            .getDocumentProperty("numero.affilie.non.formatte"));
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "createDataSource()", e);
        }

        getDocumentInfo().setDocumentProperty("document.date", getDateImprime());
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
        return AFDecisionCotisations_Doc.formatMessage(message.toString(), args);
    }

    public AFAffiliation getAffilie() {
        return affilie;
    }

    public String getCotisation() {
        return cotisation;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getDateImprime() {
        return dateImprime;
    }

    public String getAnneeEcran() {
        return anneeEcran;
    }

    private String getDecisionProVersoPourCaisse(String langue) {
        String numCaisse = null;
        try {
            numCaisse = (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);
        } catch (Exception e) {
            JadeLogger.error(this,
                    "Erreur pendant la lecture des properties du numéro de caisse pour l'impression du verso");
            return langue;
        }
        if (JadeStringUtil.isEmpty(numCaisse)) {
            return langue;
        }
        return numCaisse + "_" + langue;
    }

    public String getMasseSalariale() {
        return masseSalariale;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public String getPeriodicite() {
        return periodicite;
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
                texte = AFDecisionCotisations_Doc.formatMessage(catalogue.getTextes(niveau).getTexte(position)
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
                resString = AFDecisionCotisations_Doc.formatMessage(resString, args);
            }

            return resString;

        } catch (Exception e) {
            return "";
        }
    }

    protected JasperPrint getVerso() throws Exception {
        String documentKey = "";
        JasperPrint doc = null;

        if (!JadeStringUtil.isBlank(langueIsoAffilie)) {
            // séléction du model en fonction de langue et du numéro de caisse
            documentKey = AFDecisionCotisations_Doc.MODEL_NAME_VERSO + "_"
                    + getDecisionProVersoPourCaisse(langueIsoAffilie.toUpperCase());
        } else {
            // model par défaut (en francais)
            documentKey = AFDecisionCotisations_Doc.MODEL_NAME_VERSO;
        }

        try {
            doc = super.getImporter().importReport(documentKey, super.getImporter().getImportPath());
        } catch (Exception e) {
            doc = null;
        }
        if (doc == null) {
            // Recherche verso par défaut
            documentKey = AFDecisionCotisations_Doc.MODEL_NAME_VERSO + "_" + langueIsoAffilie.toUpperCase();
            try {
                doc = super.getImporter().importReport(documentKey, super.getImporter().getImportPath());
            } catch (Exception e) {
                doc = null;
            }
        }
        return doc;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        if (nbDoc < 1) {
            nbDoc++;
            return true;
        } else {
            return false;
        }

    }

    public void setAnneeEcran(String anneeEcran) {
        this.anneeEcran = anneeEcran;
    }

    public void setAffilie(AFAffiliation affilie) {
        this.affilie = affilie;
    }

    public void setCotisation(String cotisation) {
        this.cotisation = cotisation;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDateImprime(String newDateImprime) {
        dateImprime = newDateImprime;
    }

    public void setMasseSalariale(String masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

}
