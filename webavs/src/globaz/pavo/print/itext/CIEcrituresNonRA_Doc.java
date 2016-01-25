/*
 * Créé le 4 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
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
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.text.MessageFormat;
import java.util.Hashtable;

/**
 * Permet de générer le document de l'impression des CI non connus au registre des assurés
 * 
 * @author sda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIEcrituresNonRA_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CI_DOMAINE = "329000";
    public final static String CI_RECLAMATION_CA = "339000";
    private final static String MODEL_NAME = "PAVO_ECRITURESNONRA";
    private final static String NUM_INFOROM = "0062CCI";

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

    private ICTDocument catalogue;
    /** Numéro d'affilié */
    public String idAffilie = new String();
    /** Numéro du journal */
    public String idJournal = new String();
    public String idLangueIso = "fr";
    public boolean isFirst = true;

    /** Description du journal */
    public String journalDescription = new String();

    public CIEcrituresNonRA_Doc() throws Exception {
        this(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO));
    }

    /**
     * CIEcrituresNonRA_Doc constructor comment.
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @param filenameRoot
     *            java.lang.String
     * @param companyName
     *            java.lang.String
     * @param documentTitle
     *            java.lang.String
     * @param source
     *            net.sf.jasperreports.engine.JRDataSource
     */
    public CIEcrituresNonRA_Doc(FWProcess parent) throws Exception {
        super(parent, CIApplication.APPLICATION_PAVO_REP, "ImpressionCA");
    }

    /**
     * CIEcrituresNonRA_Doc constructor comment.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CIEcrituresNonRA_Doc(globaz.globall.db.BSession session) throws Exception {
        super(session, CIApplication.APPLICATION_PAVO_REP, "ImpressionCA");
    }

    /**
     * On lui sette tous les parametres qui doivent être présents dans l'en-tête du document
     */
    private void _setHeader(CaisseHeaderReportBean bean) throws Exception {
        bean.setAdresse(getTiers());
        bean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), idLangueIso));
        bean.setNoAffilie(idAffilie);
        bean.setNoAvs(" ");
        bean.setEmailCollaborateur(" ");

        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport ()
     */
    @Override
    public void beforeBuildReport() throws FWIException {

        super.setTemplateFile(CIEcrituresNonRA_Doc.MODEL_NAME);
        super.setFileTitle(getSession().getLabel("MSG_MAIL_JOURNAL_ENTETE") + idJournal);
        super.setDocumentTitle(getSession().getLabel("MSG_MAIL_JOURNAL_ENTETE") + idJournal);

        super.setParametres(
                "P_SUBREPORT_FOOTER",
                getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), FWIImportProperties.SIGNATURE_FILENAME));
        String codeIsoLangue = "";
        codeIsoLangue = getSession().getIdLangueISO();
        /*
         * if (getTemplateProperty(ACaisseReportHelper.JASP_PROP_SIGN_IMAGE) != null) {
         * super.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_IMG,
         * getTemplateProperty(ACaisseReportHelper.JASP_PROP_SIGN_IMAGE)); }
         * 
         * if (getTemplateProperty(ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE + codeIsoLangue) != null) {
         * super.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_CAISSE,
         * getTemplateProperty(ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE + codeIsoLangue)); }
         * 
         * if (getTemplateProperty(ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE + codeIsoLangue) != null) {
         * super.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SERVICE,
         * getTemplateProperty(ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE + codeIsoLangue)); }
         */

        //
        // //ICaisseReportHelper caisseReportHelper;
        // //try {
        // // caisseReportHelper =
        // // CaisseHelperFactory.getInstance().getCaisseReportHelper(
        // // getSession().getApplication(),
        // // codeIsoLangue);
        // //caisseReportHelper.addFooterParameters(getImporter());
        // //caisseReportHelper.addSignatureParameters(getImporter());
        // //caisseReportHelper.addFooterParameters(getImporter());
        //
        // } catch (Exception e) {
        // // TODO Bloc catch auto-généré
        // e.printStackTrace();
        // }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport ()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {

    }

    protected void catalogue() throws FWIException {
        try {
            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            helper.setCsDomaine(CIEcrituresNonRA_Doc.CI_DOMAINE); // domaine avs
            helper.setCsTypeDocument(CIEcrituresNonRA_Doc.CI_RECLAMATION_CA); // pour le type de
            // catalogue
            helper.setCodeIsoLangue(idLangueIso); // dans la langue du salarié
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
            abort();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber(CIEcrituresNonRA_Doc.NUM_INFOROM);
        getDocumentInfo().setDocumentType(CIEcrituresNonRA_Doc.NUM_INFOROM);
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getIdAffilie());
        try {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    CIUtil.UnFormatNumeroAffilie(getSession(), getIdAffilie()));
        } catch (Exception e) {

        }
        getDocumentInfo().setPublishDocument(true);
        getDocumentInfo().setArchiveDocument(true);

        CIEcrituresNonRA_DS manager = new CIEcrituresNonRA_DS();
        manager.setSession(getSession());
        manager.setEcrituresNonRA(true);
        manager.setForIdJournal(getIdJournal());
        manager.setCertificat(true);
        manager.setExclureRA(true);
        super.setDataSource(manager.getCollectionData());
        // Cas ou il n'y a pas d'affilié non existant au CA à imprimer
        if (manager.size() == 0) {
            abort();
            getMemoryLog().logMessage(getSession().getLabel("MSG_PAS_AFFILIES_NON_RA"), FWMessage.INFORMATION,
                    "traitement impressionAffilies");
        }
        // On charge le modèle pour l'en-tête standard

        ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
        Hashtable params = new Hashtable();
        params.put(ITITiers.FIND_FOR_IDTIERS, getIdTiers());
        ITITiers[] t = tiersTitre.findTiers(params);
        if ((t != null) && (t.length > 0)) {
            tiersTitre = t[0];
        }
        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(getSession());
        tiers.setIdTiers(getIdTiers());
        tiers.retrieve();
        idLangueIso = tiers.getLangueIso();
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), idLangueIso);

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), idLangueIso));
        String titre = tiersTitre.getFormulePolitesse(tiersTitre.getLangue());
        catalogue();
        super.setParametres("P_TITRE", this.getTexte(1, 1, null));
        super.setParametres("P_POLITESSE", this.getTexte(2, 1, getParams(titre)));
        super.setParametres("P_PAR_1", this.getTexte(2, 2, getParams(titre)));
        super.setParametres("P_PAR_2", this.getTexte(2, 3, getParams(titre)));
        super.setParametres("P_SIGNATURE", this.getTexte(3, 1, null));
        try {
            if (JadeStringUtil.isBlankOrZero(this.getTexte(4, 1, null))) {
                super.setParametres("P_TEXT_ANNEXE", "");
            } else {
                super.setParametres("P_TEXT_ANNEXE", this.getTexte(4, 1, null));
            }

        } catch (Exception e) {
            super.setParametres("P_TEXT_ANNEXE", "");
            e.printStackTrace();
        }

        setTemplateFile("PAVO_ECRITURESNONRA");
        _setHeader(headerBean);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();
        signBean.setService2("");
        signBean.setSignataire2("");
        signBean.setService(getSession().getLabel("MSG_SERVICE_NOM"));
        signBean.setSignataire(getSession().getUserFullName());
        signBean.setSignatureCaisse(getTemplateProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        caisseReportHelper.addSignatureParameters(this, signBean);
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
        return CIEcrituresNonRA_Doc.formatMessage(message.toString(), args);
    }

    public String getDefaultModelPath() {
        try {
            return JadeStringUtil.change(getSession().getApplication().getExternalModelPath() + "defaultModel", '\\',
                    '/');
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    protected String getEMailObject() {
        StringBuffer buffer = new StringBuffer("L'impression du document '");
        buffer.append(getSession().getLabel("MSG_MAIL_JOURNAL_ENTETE") + idJournal);
        if (isOnError()) {
            buffer.append("' s'est terminée en erreur");
        } else {
            buffer.append("' s'est terminée avec succès");
        }
        return buffer.toString();
    }

    /**
     * Renvoie l'id de l'affilie
     * 
     * @return idAffilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * Renvoie l'id du journal
     * 
     * @return idJournal
     */
    public String getIdJournal() {
        return idJournal;
    }

    public String getIdLangueIso() {
        return idLangueIso;
    }

    /** Renvoie l'adresse de l'affilié */
    public String getIdTiers() throws Exception {
        AFAffiliationManager aff = new AFAffiliationManager();
        aff.setSession(getSession());
        aff.setForAffilieNumero(idAffilie);
        aff.find();
        AFAffiliation aff2 = new AFAffiliation();
        aff2 = (AFAffiliation) aff.getEntity(0);
        return aff2.getTiers().getIdTiers();
    }

    /**
     * Renvoie la description du journal
     * 
     * @return journalDescription
     */
    public String getJournalDescription() {
        return journalDescription;
    }

    private String[] getParams(String formule) {
        String[] params = new String[2];
        params = new String[2];
        params[0] = formule;
        params[1] = params[0];
        return params;

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
                texte = CIEcrituresNonRA_Doc.formatMessage(catalogue.getTextes(niveau).getTexte(position)
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
                resString = CIEcrituresNonRA_Doc.formatMessage(resString, args);
            }

            return resString;

        } catch (Exception e) {
            return "";
        }
    }

    /** Renvoie l'adresse de l'affilié */
    public String getTiers() throws Exception {
        AFAffiliationManager affMgr = new AFAffiliationManager();
        affMgr.setSession(getSession());
        affMgr.setForAffilieNumero(idAffilie);
        affMgr.find();
        AFAffiliation aff = new AFAffiliation();
        if (affMgr.size() > 0) {
            aff = (AFAffiliation) affMgr.getEntity(0);
        }
        return aff.getTiers().getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                CIApplication.CS_DOMAINE_ADRESSE_CI_ARC, JACalendar.todayJJsMMsAAAA(), aff.getAffilieNumero());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

    /**
     * Sette l'id de l'affilie
     * 
     * @param string
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    /**
     * Sette l'id du journal
     * 
     * @param string
     */
    public void setIdJournal(String string) {
        idJournal = string;
    }

    public void setIdLangueIso(String idLangueIso) {
        this.idLangueIso = idLangueIso;
    }

    /**
     * Sette la description du journal
     * 
     * @param string
     */
    public void setJournalDescription(String string) {
        journalDescription = string;
    }

}
