/*
 * Créé le 15.10.07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.beneficiairesPC;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.naos.application.AFApplication;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TITiers;
import java.util.Iterator;
import java.util.Locale;

/**
 * <H1>Description</H1>
 * 
 * @author jpa
 */
public class AFQuittanceVerso_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NOM_DOC = "";
    private static final String P_TEXTE = "P_TEXTE";
    private static final String TEMPLATE_FILE_NAME = "NAOS_QUITTANCE_VERSO";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private boolean hasNext = true;

    private String numAffilie = "";

    private String numAvs = "";
    private TITiers tiers = null;
    private String user = "";

    private String userTelephone = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public AFQuittanceVerso_Doc() throws Exception {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe AFAttestationAffiliation.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws Exception
     */
    public AFQuittanceVerso_Doc(BSession session) throws Exception {
        super();
        // this(new BSession(globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            setDocumentTitle("test");
            getExporter().setExportFileName("test");

            // renseigner le texte
            ICTDocument document = loadCatalogue();
            StringBuffer buffer = new StringBuffer();

            // ***************
            // -- PARAGRAPHE -----------------------------------------------------------
            for (Iterator titresIter = document.getTextes(1).iterator(); titresIter.hasNext();) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }

                buffer.append(((ICTTexte) titresIter.next()).getDescription());
            }

            int i = 0;
            String[] valParams = new String[1];
            // {debutAnnee}
            valParams[0] = getUserTelephone();

            while (buffer.indexOf("{") != -1) {
                buffer = format(buffer, valParams[i++]);
            }
            this.setParametres(AFQuittanceVerso_Doc.P_TEXTE, buffer.toString());

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
        setTemplateFile(AFQuittanceVerso_Doc.TEMPLATE_FILE_NAME);
        /*
         * this.setNumAvs(" "); // rechercher l'id tiers et le le numéro AVS if
         * (JadeStringUtil.isEmpty(this.getIdTiers())) { try { AFAffiliationManager affM = new AFAffiliationManager();
         * affM.setSession(this.getSession()); affM.setForAffilieNumero(this.getNumAffilie());
         * affM.find(this.getTransaction()); AFAffiliation aff = (AFAffiliation) affM.getFirstEntity(); if (aff != null)
         * { this.setIdTiers(aff.getIdTiers()); this.setIdDestinataire(aff.getIdTiers()); } } catch (Exception e) { //
         * TODO : gestion de l'erreur ! } }
         */
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        // super.createDataSource();
        fillDocInfo();
    }

    /**
     * Après l'impression d'un document
     */

    protected void fillDocInfo() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getNumAffilie());
        try {
            IFormatData affilieFormater = ((AFApplication) getSession().getApplication()).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", getNumAffilie());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf((JACalendar.today().getYear())));
        getDocumentInfo().setArchiveDocument(true);
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
            res.append(CPToolBox.replaceString(paragraphe.toString(), chaineARemplacer, varTemp));
        } else {
            res.append(paragraphe.toString());
        }
        return res;
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

    public int getNbLevel() {
        return 0;
    }

    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFQuittanceVerso_Doc.NOM_DOC);
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

    protected String getTemplate() {
        return AFQuittanceVerso_Doc.TEMPLATE_FILE_NAME;
    }

    public TITiers getTiers() {
        return tiers;
    }

    public String getUser() {
        return user;
    }

    public String getUserTelephone() {
        return userTelephone;
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
        loader.setIdDocument(CodeSystem.ID_DOCUMENT_QUITTANCE_VERSO);
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

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setTiers(TITiers tiers) {
        this.tiers = tiers;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

}
