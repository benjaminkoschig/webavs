package globaz.naos.itext.taxeCo2;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.util.FAUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.taxeCo2.AFTaxeCo2;
import globaz.naos.process.taxeCo2.AFImprimerLettreTaxeCo2Process;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.application.CAApplication;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;

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
public class AFLettreTaxeCo2UnAffilie_doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String NUM_REF_INFOROM_LETTRE_TAXECO2 = "0237CAF";

    protected final static String TEMPLATE_FILENAME = "NAOS_LETTRE_TAXE_CO2";
    protected final static String TEMPLATE_FILENAME_PARAM = "template.taxeco2.filename";

    protected String adressePrincipalePaiement;
    private String annee = new String();
    private String dateImpression = new String();
    ICTDocument[] document = null;
    private String idTaxeCo2 = new String();
    private String signataire1 = new String();

    private String signataire2 = new String();

    private boolean start = true;
    protected AFTaxeCo2 taxe = null;
    protected TITiers tiers = null;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public AFLettreTaxeCo2UnAffilie_doc() throws Exception {
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
    public AFLettreTaxeCo2UnAffilie_doc(AFImprimerLettreTaxeCo2Process parent) throws java.lang.Exception {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "REDISTRIBUTIONCO2");
        super.setDocumentTitle(getSession().getLabel("TITRE_LETTRE_REDISTRIBUTION"));
        super.setSendCompletionMail(false);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFLettreTaxeCo2UnAffilie_doc(BSession session) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "REDISTRIBUTIONCO2");
        super.setDocumentTitle(getSession().getLabel("TITRE_LETTRE_REDISTRIBUTION"));
        super.setSendCompletionMail(false);
    }

    /**
     * Insert the method's description here. Creation date: (05.06.2003 08:55:49)
     */
    @Override
    public void _executeCleanUp() {
        super._executeCleanUp();
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _headerText(CaisseHeaderReportBean headerBean) {
        try {
            document = getICTDocument();
            headerBean.setUser(getSession().getUserInfo());
            headerBean.setNomCollaborateur(getSession().getUserFullName());
            headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());

            // adresse du tiers
            headerBean.setAdresse(adressePrincipalePaiement);
            // Ajout du confidentiel
            if (CAApplication.getApplicationOsiris().getCAParametres().isConfidentiel()) {
                headerBean.setConfidentiel(true);
            }
            // texte de la date
            headerBean.setDate(getDateImpression());

            // numéro AVS
            headerBean.setNoAvs("");

            // No affilié
            headerBean.setNoAffilie(taxe.getNumAffilie());

            AFIDEUtil.addNumeroIDEInDoc(getSession(), headerBean, taxe.getAffiliationId());

            super.setParametres(AFLettreTaxeCo2_Param.P_TITLE, getTexte(1, document));
            super.setParametres(
                    AFLettreTaxeCo2_Param.P_TITLE2,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(2, document), false),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false)));

        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header de la lettre Taxe Co2: " + taxe.getNumAffilie()
                    + e.getMessage());
        }
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _letterBody() {
        try {
            // On set le texte du document
            FWCurrency montantTaxe = new FWCurrency(taxe.getMontantCalculer());
            // On travaille en positif
            montantTaxe.abs();
            super.setParametres(
                    AFLettreTaxeCo2_Param.P_TEXT,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(3, document), false),
                            JANumberFormatter.fmt(montantTaxe.toString(), true, true, false, 2),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false)));
        } catch (Exception e) {
            this._addError("Erreur lors de la création du corp de texte de la Taxe CO2: " + taxe.getNumAffilie()
                    + e.getMessage());
        }
    }

    /**
     * Retourne la décision ou null en cas d'exception Insérez la description de la méthode ici. Date de création :
     * (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        super.setSendMailOnError(false);
        super.setDocumentTitle(taxe.getNumAffilie() + " - " + taxe.getNomTiers());
    }

    @Override
    public final void beforeExecuteReport() {
        super.setTemplateFile(AFLettreTaxeCo2UnAffilie_doc.TEMPLATE_FILENAME);
        setImpressionParLot(true);
        setTailleLot(1);
        // Initialise le document pour le catalogue de texte
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {
        fillDocInfo();

        // si un autre template est définit dans les params des docs, on l'utilise
        String templateName = getImporter().getTemplateProperty(getDocumentInfo(),
                AFLettreTaxeCo2UnAffilie_doc.TEMPLATE_FILENAME_PARAM);
        if (!JadeStringUtil.isEmpty(templateName)) {
            super.setTemplateFile(templateName);
        }

        start = false;

        // initialiser les variables d'aide
        adressePrincipalePaiement = getTiers().getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION, globaz.globall.util.JACalendar.today().toStr("."));

        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), taxe.getISOLangueTiers());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        tiers = getTiers();

        _headerText(headerBean);
        _letterBody();

        caisseReportHelper.addHeaderParameters(this, headerBean);
        caisseReportHelper.addSignatureParameters(this);
    }

    private void fillDocInfo() {
        String numAff = taxe.getNumAffilie();
        String idTiers = taxe.getIdTiers();

        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affilieFormater.unformat(numAff));
            getDocumentInfo().setDocumentProperty("annee", getAnnee());
            getDocumentInfo().setDocumentDate(getDateImpression());
            TIDocumentInfoHelper.fill(getDocumentInfo(), idTiers, getSession(), ITIRole.CS_AFFILIE, numAff,
                    affilieFormater.unformat(numAff));

        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
        }
        getDocumentInfo().setDocumentTypeNumber(AFLettreTaxeCo2UnAffilie_doc.NUM_REF_INFOROM_LETTRE_TAXECO2);
        getDocumentInfo().setDocumentType(AFLettreTaxeCo2UnAffilie_doc.NUM_REF_INFOROM_LETTRE_TAXECO2);

        try {
            getDocumentInfo().setPublishDocument(true);
            getDocumentInfo().setArchiveDocument(false);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    // Retourne le document à utiliser
    public ICTDocument[] getICTDocument() {
        ICTDocument res[] = null;
        ICTDocument document = null;

        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, "Error while api for document");
        }
        // On charge le document
        document.setISession(getSession());
        document.setCsDomaine(CodeSystem.DOMAINE_CAT_AFF);
        document.setCsTypeDocument(CodeSystem.TYPE_LETTRE_REDISTRIBUTION_TAXECO2);

        document.setCodeIsoLangue(taxe.getISOLangueTiers());
        document.setActif(new Boolean(true));

        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWViewBeanInterface.ERROR, "Error while getting document");
        }
        return res;
    }

    public String getIdTaxeCo2() {
        return idTaxeCo2;
    }

    /**
     * @return
     */
    public String getSignataire1() {
        return signataire1;
    }

    /**
     * @return
     */
    public String getSignataire2() {
        return signataire2;
    }

    public AFTaxeCo2 getTaxe() {
        return taxe;
    }

    // Retourne le texte du niveau entré en paramètres
    private String getTexte(int niveau, ICTDocument[] document) throws Exception {
        String resString = "";
        ICTTexte texte = null;
        // Si le document est null, on retourne un message d'erreur
        if (document == null) {
            getMemoryLog().logMessage("Il n'y a pas de document par défaut", FWViewBeanInterface.ERROR, "");
        } else {
            ICTListeTextes listeTextes = null;
            // On charge la liste de textes du niveau donné
            try {
                listeTextes = document[0].getTextes(niveau);
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWViewBeanInterface.ERROR,
                        "Error while getting listes de textes");
            }
            // S'il n'y a pas de texte on retourne une erreur
            if (listeTextes == null) {
                getMemoryLog().logMessage("Il n'y a pas de texte", FWViewBeanInterface.ERROR, "");
            } else {
                // Dans le cas ou on a un texte, on va parcourir toutes les
                // positions de ce texte
                for (int i = 0; i < listeTextes.size(); i++) {
                    // On charge le texte de la position donnée
                    texte = listeTextes.getTexte(i + 1);
                    // On regarde si c'est la dernière position du niveau
                    if ((i + 1) < listeTextes.size()) {
                        // Sinon c'est que le niveau doit être séparé par un
                        // paragraphe
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        // Dans le cas ou c'est la dernière position du niveau,
                        // on n'ajoute aucun retour à la ligne ni paragraphe
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }
        }
        // return format(resString);
        return resString;
    }

    public TITiers getTiers() {
        try {
            String idTiers = "";
            AFAffiliationManager aff = new AFAffiliationManager();
            aff.setSession(getSession());
            aff.setForAffilieNumero(taxe.getNumAffilie());
            aff.setFromDateFin("0101" + taxe.getAnneeMasse());
            aff.setForDateDebutAffLowerOrEqualTo("3112" + taxe.getAnneeMasse());
            aff.find();
            if (aff.size() > 0) {
                // this.setAffiliationId(((AFAffiliation) aff.getFirstEntity()).getAffiliationId());
                idTiers = ((AFAffiliation) aff.getFirstEntity()).getIdTiers();
                // this.setIdTiers(((AFAffiliation) aff.getFirstEntity()).getIdTiers());
            } else {
                AFAffiliationManager aff2 = new AFAffiliationManager();
                aff2.setSession(getSession());
                aff2.setForAffilieNumero(taxe.getNumAffilie());
                aff2.setFromDateFin("0101" + JACalendar.getYear(JACalendar.todayJJsMMsAAAA()));
                aff2.setForDateDebutAffLowerOrEqualTo("3112" + JACalendar.getYear(JACalendar.todayJJsMMsAAAA()));
                aff2.find();
                if (aff2.size() > 0) {
                    // this.setAffiliationId(((AFAffiliation) aff2.getFirstEntity()).getAffiliationId());
                    idTiers = ((AFAffiliation) aff.getFirstEntity()).getIdTiers();
                    // this.setIdTiers(((AFAffiliation) aff2.getFirstEntity()).getIdTiers());
                } else {
                    this._addError(getTransaction(), getSession().getLabel("ERREUR_NUM_AFFILIE") + taxe.getNumAffilie());
                }
            }

            tiers = new TITiers();
            tiers.setSession(taxe.getSession());
            tiers.setIdTiers(idTiers);
            tiers.retrieve();
        } catch (Exception e) {
            this._addError("Erreur lors du retrieve du tiers pour la lettre sur la taxe CO2: " + taxe.getNumAffilie()
                    + e.getMessage());
        }
        return tiers;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
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
        return start;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setIdTaxeCo2(String idTaxeCo2) {
        this.idTaxeCo2 = idTaxeCo2;
    }

    /**
     * @param string
     */
    public void setSignataire1(String string) {
        signataire1 = string;
    }

    /**
     * @param string
     */
    public void setSignataire2(String string) {
        signataire2 = string;
    }

    public void setTaxe(AFTaxeCo2 taxe) {
        this.taxe = taxe;
    }

}
