/*
 * Créé le 15.10.07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.beneficiairesPC;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.util.Iterator;
import java.util.Locale;

/**
 * <H1>Description</H1>
 * 
 * @author jpa
 */
public class AFQuittanceRemboursement_Doc extends AFAbstractTiersDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Numéro du document
    private static final String DOC_NO = "0173CAF";
    private static final String L_ADRESSE = "L_ADRESSE";

    private static final String L_AVERTISSEMENT = "L_AVERTISSEMENT";

    private static final String L_BUREAU_AFF = "L_BUREAU_AFF";
    private static final String L_BUREAU_PC = "L_BUREAU_PC";
    private static final String L_CODE = "L_CODE";
    private static final String L_DATE = "L_DATE";
    private static final String L_DATE2 = "L_DATE2";
    private static final String L_MONTANT = "L_MONTANT";
    private static final String L_NBRE_HEURES = "L_NBRE_HEURES";
    private static final String L_NOM = "L_NOM";
    private static final String L_NOM_AIDE = "L_NOM_AIDE";
    private static final String L_NUMAVS_AIDE = "L_NUMAVS_AIDE";
    private static final String L_PERIODE = "L_PERIODE";
    private static final String L_PRIX_HEURE = "L_PRIX_HEURE";
    private static final String L_REMARQUE = "L_REMARQUE";
    private static final String L_REMARQUE2 = "L_REMARQUE2";
    private static final String L_SIGNATURE_AIDE = "L_SIGNATURE_AIDE";
    private static final String L_SIGNATURE_BENEF = "L_SIGNATURE_BENEF";
    private static final String L_TOTAL = "L_TOTAL";
    private static final String L_VISA = "L_VISA";
    private static final String L_VISA2 = "L_VISA2";
    private static final String NOM_DOC = "QUITTANCE_REMBOURSEMENT";
    private static final String P_ADRESSE = "P_ADRESSE";
    private static final String P_NAVS = "P_NAVS";
    private static final String P_NOM = "P_NOM";
    private static final String TEMPLATE_FILE_NAME = "NAOS_QUITTANCE_REMBOURSEMENT";

    private String dateEvaluation = "";

    private String dossier = "";

    private boolean hasNext = true;

    private int nbreQuittances;
    private String numAffilie = "";

    private String numAvs = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private TITiers tiers;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public final static String replaceString(String baseString, String textToReplace, String replaceWith) {
        if ((JadeStringUtil.isEmpty(baseString)) || (JadeStringUtil.isEmpty(textToReplace)) || (replaceWith == null)) {
            return baseString;
        }
        StringBuffer buffer = new StringBuffer();
        int index = -1;
        while ((index = baseString.indexOf(textToReplace)) != -1) {
            buffer.append(baseString.substring(0, index));
            buffer.append(replaceWith);
            baseString = baseString.substring(index + textToReplace.length());
        }
        buffer.append(baseString);
        return buffer.toString();
    }

    public AFQuittanceRemboursement_Doc() throws Exception {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAttestationAffiliation.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws Exception
     */
    public AFQuittanceRemboursement_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFQuittanceRemboursement_Doc.NOM_DOC));
    }

    @Override
    public void afterBuildReport() {
        // // Dans le cas d'un nouveau cas, on ajoute un verso
        // if (getNombreAImprimer() >= 1) {
        // JasperPrint verso = null;
        // verso = getVerso();
        // if (verso != null) {
        // verso.setName("");
        // super.getDocumentList().add(verso);
        // }
        // }
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            // remplir l'en-tête et la signature
            ICaisseReportHelper crh = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), getLangueDestinataire());
            CaisseHeaderReportBean hb = new CaisseHeaderReportBean();

            hb.setAdresse(getTiers().getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(),
                    getNumAffilie() != null ? getNumAffilie() : null));
            hb.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), getTiers().getLangueIso()));
            hb.setEmailCollaborateur(getSession().getUserEMail());
            hb.setNoAffilie(getNumAffilie());
            hb.setNoAvs(getNumAvs());
            hb.setNomCollaborateur(getSession().getUserFullName());
            hb.setTelCollaborateur(getSession().getUserInfo().getPhone());
            hb.setUser(getSession().getUserInfo());
            crh.addHeaderParameters(this, hb);
            crh.addSignatureParameters(this);

            setDocumentTitle(getSession().getLabel("QUITTANCE_REMBOURSEMENT") + " " + getNumAffilie());
            getExporter().setExportFileName(getSession().getLabel("QUITTANCE_REMBOURSEMENT") + " " + getNumAffilie());
        } catch (Exception e) {
            abort();
            throw new FWIException("Erreur: " + e.getMessage(), e);
        }
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(AFQuittanceRemboursement_Doc.TEMPLATE_FILE_NAME);
        setNumAvs(" ");
        // rechercher l'id tiers et le le numéro AVS
        if (JadeStringUtil.isEmpty(getIdTiers())) {
            try {
                AFAffiliationManager affM = new AFAffiliationManager();
                affM.setSession(getSession());
                affM.setForAffilieNumero(getNumAffilie());
                affM.setForTypesAffParitaires();
                affM.find(getTransaction());
                AFAffiliation aff = (AFAffiliation) affM.getFirstEntity();
                if (aff != null) {
                    setIdTiers(aff.getIdTiers());
                    setIdDestinataire(aff.getIdTiers());
                }
            } catch (Exception e) {
                // TODO : gestion de l'erreur !
            }
        }

    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        fillDocInfo();
        // super.createDataSource();
        // renseigner le texte
        ICTDocument document = loadCatalogue();
        StringBuffer buffer = new StringBuffer();

        Iterator titresIter = document.getTextes(1).iterator();
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_NOM, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_ADRESSE, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_REMARQUE, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_NOM_AIDE, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_NUMAVS_AIDE, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_PERIODE, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_NBRE_HEURES, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_PRIX_HEURE, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_TOTAL, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_DATE, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_AVERTISSEMENT, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_BUREAU_AFF, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_BUREAU_PC, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_REMARQUE2,
                buffer.toString() + " " + JACalendar.todayJJsMMsAAAA());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        setDossier(buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_SIGNATURE_BENEF, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_SIGNATURE_AIDE, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_DATE2, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_VISA, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_CODE, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_MONTANT, buffer.toString());
        buffer.setLength(0);
        buffer.append(((ICTTexte) titresIter.next()).getDescription());
        this.setParametres(AFQuittanceRemboursement_Doc.L_VISA2, buffer.toString());
        buffer.setLength(0);

        this.setParametres(AFQuittanceRemboursement_Doc.P_NOM, getNomDestinataire());
        this.setParametres(AFQuittanceRemboursement_Doc.P_ADRESSE, getTiers().getAdresseAsString());

        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(getSession());
        tiers.setIdTiers(getIdDestinataire());
        tiers.retrieve();
        if (!tiers.isNew()) {
            setNumAvs(tiers.getNumAvsActuel());
        } else {
            setNumAvs(" ");
        }

        this.setParametres(AFQuittanceRemboursement_Doc.P_NAVS, getNumAvs() + "    " + getDossier() + " "
                + getNumAffilie());
    }

    /**
     * Après l'impression d'un document
     */
    @Override
    public void fillDocInfo() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getNumAffilie());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            TIDocumentInfoHelper.fill(getDocumentInfo(), getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    getNumAffilie(), affilieFormater.unformat(getNumAffilie()));
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", getNumAffilie());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setDocumentTypeNumber(AFQuittanceRemboursement_Doc.DOC_NO);
    }

    public StringBuffer format(StringBuffer paragraphe, String varTemp) {
        StringBuffer res = new StringBuffer("");
        String chaineModifiee = paragraphe.toString();
        ;
        int index1 = chaineModifiee.indexOf("{");
        int index2 = chaineModifiee.indexOf("}");
        if ((index1 != -1) && (index2 != -1)) {
            String chaineARemplacer = chaineModifiee.substring(index1, index2 + 1);
            // remplacement de la variable par sa valeur (varTemp)
            if (varTemp == "") {
                varTemp = " ";
            }
            res.append(AFQuittanceRemboursement_Doc.replaceString(paragraphe.toString(), chaineARemplacer, varTemp));
        } else {
            res.append(paragraphe.toString());
        }
        return res;
    }

    @Override
    public String getCategorie() {
        return ILEConstantes.CS_CATEGORIES_NOUVELLE_AFFILIATION;
    }

    public String getDateEvaluation() {
        return dateEvaluation;
    }

    @Override
    public String getDomaine() {
        return null;
    }

    public String getDossier() {
        return dossier;
    }

    /**
     * retourne la langue de l'affilie (doit être appellé ap.
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    private String getLangueDestinataire() throws Exception {
        String retValue = getTiers().getLangueIso().toLowerCase();

        if (Locale.FRENCH.getLanguage().equals(retValue) || Locale.GERMAN.getLanguage().equals(retValue)
                || Locale.ITALIAN.getLanguage().equals(retValue)) {
            return retValue;
        } else {
            return Locale.FRENCH.getLanguage();
        }
    }

    // private JasperPrint getVerso() {
    // try {
    // java.util.List versoGenere = null;
    // AFQuittanceRemboursement_Doc quittance = new
    // AFQuittanceRemboursement_Doc();
    // quittance.setISession(getSession());
    // quittance.setDeleteOnExit(false);
    // quittance.setIdDestinataire(getIdDestinataire());
    // quittance.setNumAffilie(getNumAffilie());
    // quittance.setNombreAImprimer(getNombreAImprimer() - 1);
    // quittance.setSendCompletionMail(false);
    // quittance.setSendMailOnError(false);
    // //quittance.setParent(this);
    // quittance.executeProcess();
    // versoGenere = quittance.getDocumentList();
    // if (versoGenere.isEmpty()) {
    // return null;
    // } else {
    // return (JasperPrint) versoGenere.get(0);
    // }
    // } catch (Exception e) {
    // return null;
    // }
    // }

    @Override
    public int getNbLevel() {
        return 0;
    }

    public int getNombreAImprimer() {
        return nbreQuittances;
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFQuittanceRemboursement_Doc.NOM_DOC);
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNumeroRappel() {
        return "";
    }

    @Override
    protected String getTemplate() {
        return AFQuittanceRemboursement_Doc.TEMPLATE_FILE_NAME;
    }

    /**
     * private AFAffiliation loadAffiliation() throws Exception { if (affiliation == null) { affiliation = new
     * AFAffiliation(); affiliation.setAffiliationId(getIdAffiliation()); affiliation.setSession(getSession());
     * affiliation.retrieve(); } return affiliation; }
     */

    private TITiers getTiers() throws Exception {
        if (tiers == null) {
            tiers = new TITiers();
            tiers.setIdTiers(getIdTiers());
            tiers.setSession(getSession());
            tiers.retrieve();
        }
        return tiers;
    }

    @Override
    protected void initDocument(String isoLangue) throws Exception {
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private ICTDocument loadCatalogue() throws Exception {
        // préparer le chargement
        ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

        loader.setActif(Boolean.TRUE);
        loader.setDefault(Boolean.TRUE);
        loader.setCsDomaine(CodeSystem.DOMAINE_BENEF_PC);

        loader.setIdDocument(CodeSystem.ID_DOCUMENT_QUITTANCE_REMBOURSEMENT);

        loader.setCodeIsoLangue(getLangueDestinataire());
        // trouver le catalogue
        ICTDocument[] candidats = loader.load();

        if ((candidats == null) || (candidats.length == 0)) {
            throw new Exception("Impossible de trouver le catalogue de texte");
        }

        return candidats[0];
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = !hasNext;
        }

        return retValue;
    }

    public void setDateEvaluation(String _dateEvaluation) {
        dateEvaluation = _dateEvaluation;
    }

    public void setDossier(String dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
    }

    public void setNombreAImprimer(int _nbreQuittances) {
        nbreQuittances = _nbreQuittances;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }
}
