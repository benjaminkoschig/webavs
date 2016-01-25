package globaz.hercule.itext.controleEmployeur;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.utils.CEUtils;
import globaz.naos.application.AFApplication;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.api.ITIRole;

/**
 * @author ald
 * @since Créé le 14 mars 05
 */
public class CERapport_P1_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOC_NO = "0282CCE";
    public static final String TEMPLATE_FILENAME = "HERCULE_RAPPORT_CE";

    public static String getTemplateFilename() {
        return CERapport_P1_Doc.TEMPLATE_FILENAME;
    }

    protected String adresseDomicile;
    protected String adressePaiement;
    protected String adressePrincipale;

    CEControleEmployeur controle = new CEControleEmployeur();
    private String dateImpression = new String();
    ICTDocument document = null;
    ICTDocument[] documents = null;
    private String emailAdress = new String();
    private Boolean envoyerGed = new Boolean(false);
    private boolean hasNext = true;
    private String idControle = new String();
    private String idPersRef = new String();
    int index = 1;
    CEControleEmployeurManager manager = new CEControleEmployeurManager();
    int nbNiveaux = 0;
    protected String numeroCompte;

    // Numéro du document

    private boolean preRapport = false;

    /**
     * Constructeur de CERapport_P1_Doc
     */
    public CERapport_P1_Doc() throws Exception {
        this(new BSession(CEApplication.DEFAULT_APPLICATION_HERCULE));
    }

    /**
     * Constructeur de CERapport_P1_Doc
     */
    public CERapport_P1_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, CEApplication.APPLICATION_HERCULE_ROOT, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("CONTROLES_TITRE_P1"));
    }

    /**
     * Constructeur de CERapport_P1_Doc
     */
    public CERapport_P1_Doc(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("CONTROLES_TITRE_P1"));
    }

    /**
     * Constructeur de CERapport_P1_Doc
     */
    public CERapport_P1_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("CONTROLES_TITRE_P1"));
        this.idControle = idControle;
    }

    protected void _headerText(CaisseHeaderReportBean headerBean, String dateImpression) {

        try {
            headerBean.setDate(JACalendar.format(dateImpression, controle.getLangueTiers()));

            // adresse du tiers
            headerBean.setAdresse(adressePrincipale);

            // numéro AVS
            headerBean.setNoAvs("");

            // No affilié
            headerBean.setNoAffilie(controle.getNumAffilie());

            // Renseignement du numéro ide
            AFIDEUtil.addNumeroIDEInDoc(getSession(), headerBean, controle.getAffiliationId());

            headerBean.setConfidentiel(true);

        } catch (Exception e) {
            getMemoryLog().logMessage("Les paramêtres de l'objet peuvent ne pas avoir été mis correctement",
                    FWMessage.AVERTISSEMENT, headerBean.getClass().getName());
        }

    }

    protected void _summaryText() throws Exception {

        try {
            String titreRapport = getSession().getApplication().getLabel("RAPPORT_TITRE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_TITRE, titreRapport);
            String titrePreRapport = "";
            if (isPreRapport()) {
                titrePreRapport = getSession().getApplication().getLabel("RAPPORT_PRERAPPORT",
                        controle.getLangueTiers());
            }
            super.setParametres(CERapport_Param.L_PRERAPPORT, titrePreRapport);
            String caracteristique = getSession().getApplication().getLabel("RAPPORT_CARACTERISTIQUE",
                    controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_CARACTERISTIQUE, caracteristique);
            String document = getSession().getApplication().getLabel("RAPPORT_DOCUMENT", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_DOCUMENT, document);
            String numRapport = getSession().getApplication()
                    .getLabel("RAPPORT_NUM_RAPPORT", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_NUM_RAPPORT, numRapport);
            String nomReviseur = getSession().getApplication().getLabel("RAPPORT_NOM_REVISEUR",
                    controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_NOM_REVISEUR, nomReviseur);
            String dateControle = getSession().getApplication().getLabel("RAPPORT_DATE_CONTROLE",
                    controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_DATE_CONTROLE, dateControle);
            String periode = getSession().getApplication().getLabel("RAPPORT_PERIODE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_PERIODE, periode);
            String datePrecedent = getSession().getApplication().getLabel("RAPPORT_DATE_PRECEDENT",
                    controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_DATE_PRECEDENT, datePrecedent);
            String brancheEco = getSession().getApplication()
                    .getLabel("RAPPORT_BRANCHE_ECO", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_BRANCHE_ECO, brancheEco);
            String formeJuri = getSession().getApplication().getLabel("RAPPORT_FORME_JURI", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_FORME_JURI, formeJuri);
            String succursale = getSession().getApplication().getLabel("RAPPORT_SUCCURSALE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_SUCCURSALE, succursale);
            String inscriRc = getSession().getApplication().getLabel("RAPPORT_INSCRI_RC", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_INSCRI_RC, inscriRc);
            String nbSalarie = getSession().getApplication().getLabel("RAPPORT_NB_SALARIE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_NB_SALARIE, nbSalarie);
            String dateBoucle = getSession().getApplication()
                    .getLabel("RAPPORT_DATE_BOUCLE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_DATE_BOUCLEMENT, dateBoucle);
            String nomFidu = getSession().getApplication().getLabel("RAPPORT_NOM_FIDU", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_NOM_FIDU, nomFidu);
            String contact = getSession().getApplication().getLabel("RAPPORT_CONTACT", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_CONTACT, contact);
            String affLaa = getSession().getApplication().getLabel("RAPPORT_AFF_LAA", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_AFF_LAA, affLaa);
            String affLpp = getSession().getApplication().getLabel("RAPPORT_AFF_LPP", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_AFF_LPP, affLpp);
            String complet = getSession().getApplication().getLabel("RAPPORT_COMPLET", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_COMPLET, complet);
            String sondage = getSession().getApplication().getLabel("RAPPORT_SONDAGE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_SONDAGE, sondage);
            String compta = getSession().getApplication().getLabel("RAPPORT_COMPTA", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_COMPTA, compta);
            String bilan = getSession().getApplication().getLabel("RAPPORT_BILAN", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_BILAN, bilan);
            String pourboire = getSession().getApplication().getLabel("RAPPORT_POURBOIRE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_POURBOIRE, pourboire);
            String nature = getSession().getApplication().getLabel("RAPPORT_NATURE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_NATURE, nature);
            String honoraire = getSession().getApplication().getLabel("RAPPORT_HONORAIRE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_HONORAIRE, honoraire);
            String indemnite = getSession().getApplication().getLabel("RAPPORT_INDEMNITE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_INDEMNITE, indemnite);
            String mensuel = getSession().getApplication().getLabel("RAPPORT_MENSUEL", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_MENSUEL, mensuel);
            String piece = getSession().getApplication().getLabel("RAPPORT_PIECE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_PIECE, piece);
            String commission = getSession().getApplication().getLabel("RAPPORT_COMMISSION", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_COMMISSION, commission);
            String heure = getSession().getApplication().getLabel("RAPPORT_HEURE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_HEURE, heure);
            String domicile = getSession().getApplication().getLabel("RAPPORT_DOMICILE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_DOMICILE, domicile);
            String gratification = getSession().getApplication().getLabel("RAPPORT_GRATIFICATION",
                    controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_GRATIFICATION, gratification);
            super.setParametres(CERapport_Param.L_LIBELLE_1, controle.getEleLibelleAutre1());
            super.setParametres(CERapport_Param.L_LIBELLE_2, controle.getEleLibelleAutre2());
            String element = getSession().getApplication().getLabel("RAPPORT_ELEMENT", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_ELEMENT, element);
        } catch (Exception e) {
            this._addError(null, e.getMessage());
        }

        super.setParametres(CERapport_Param.P_NUM_RAPPORT, controle.getRapportNumero());
        super.setParametres(CERapport_Param.P_NOM_REVISEUR, controle.getControleurNom());
        super.setParametres(CERapport_Param.P_DATE_CONTROLE, controle.getDateEffective());
        super.setParametres(CERapport_Param.P_PERIODE,
                controle.getDateDebutControle() + " au " + controle.getDateFinControle());// TODO SCO :
                                                                                          // Traduire le "au"
        super.setParametres(CERapport_Param.P_DATE_PRECEDENT, controle.getDatePrecedentControle());
        super.setParametres(CERapport_Param.P_BRANCHE_ECO,
                CEUtils.getLibelleIso(getSession(), controle.getBrancheEco(), controle.getLangueTiers()));
        super.setParametres(CERapport_Param.P_FORME_JURI,
                CEUtils.getLibelleIso(getSession(), controle.getFormeJuri(), controle.getLangueTiers()));
        super.setParametres(CERapport_Param.P_SUCCURSALE, controle.getSuccLibelle());
        super.setParametres(CERapport_Param.P_INSCRI_RC, controle.getInscriLibelle());
        super.setParametres(CERapport_Param.P_NB_SALARIE, controle.getNombreSalariesFixes());
        super.setParametres(CERapport_Param.P_DATE_BOUCLE, controle.getDateBouclement());
        super.setParametres(CERapport_Param.P_NOM_FIDU, controle.getComptaTenuPar());
        super.setParametres(CERapport_Param.P_CONTACT_1, controle.getPersonneContact1());
        super.setParametres(CERapport_Param.P_CONTACT_2, controle.getPersonneContact2());
        super.setParametres(CERapport_Param.P_CONTACT_3, controle.getPersonneContact3());
        super.setParametres(CERapport_Param.P_AFF_LAA, controle.getAffiliationLaa());
        super.setParametres(CERapport_Param.P_AFF_LPP, controle.getAffiliationLpp());
        super.setParametres(CERapport_Param.P_COMPTA_COM, controle.getDocComptaComplet());
        super.setParametres(CERapport_Param.P_COMPTA_SON, controle.getDocComptaSondage());
        super.setParametres(CERapport_Param.P_BILAN_COM, controle.getDocBilanComplet());
        super.setParametres(CERapport_Param.P_BILAN_SON, controle.getDocBilanSondage());
        if (controle.getElePourboire().booleanValue()) {
            super.setParametres(CERapport_Param.P_POURBOIRE, "X");
        }
        if (controle.getEleNature().booleanValue()) {
            super.setParametres(CERapport_Param.P_NATURE, "X");
        }
        if (controle.getEleHonoraire().booleanValue()) {
            super.setParametres(CERapport_Param.P_HONORAIRE, "X");
        }
        if (controle.getEleIndemnite().booleanValue()) {
            super.setParametres(CERapport_Param.P_INDEMNITE, "X");
        }
        if (controle.getEleMensuel().booleanValue()) {
            super.setParametres(CERapport_Param.P_MENSUEL, "X");
        }
        if (controle.getElePiece().booleanValue()) {
            super.setParametres(CERapport_Param.P_PIECE, "X");
        }
        if (controle.getEleCommission().booleanValue()) {
            super.setParametres(CERapport_Param.P_COMMISSION, "X");
        }
        if (controle.getEleAutre1().booleanValue()) {
            super.setParametres(CERapport_Param.P_LIBELLE_1, "X");
        }
        if (controle.getEleAutre2().booleanValue()) {
            super.setParametres(CERapport_Param.P_LIBELLE_2, "X");
        }
        if (controle.getEleHeure().booleanValue()) {
            super.setParametres(CERapport_Param.P_HEURE, "X");
        }
        if (controle.getEleDomicile().booleanValue()) {
            super.setParametres(CERapport_Param.P_DOMICILE, "X");
        }
        if (controle.getEleGratification().booleanValue()) {
            super.setParametres(CERapport_Param.P_GRATIFICATION, "X");
        }
    }

    @Override
    public void afterPrintDocument() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", controle.getNumAffilie());

        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    CEApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
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

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() {
        super.setDocumentTitle(controle.getNumAffilie() + " -1- " + controle.getNomTiers());
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTemplateFile(CERapport_P1_Doc.TEMPLATE_FILENAME);
        if (getEnvoyerGed().booleanValue()) {
            setTailleLot(1);
        } else {
            setTailleLot(500);
        }
        // Initialise le document pour le catalogue de texte
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber(CERapport_P1_Doc.DOC_NO);

        // super.setTemplateFile(getTemplateFilename(getEntity()));
        // setTemplateFile(TEMPLATE_FILENAME);
        manager.setSession(getSession());
        manager.setForControleEmployeurId(idControle);
        manager.find();
        if (manager.size() > 0) {
            controle = (CEControleEmployeur) manager.getFirstEntity();
        }

        // initialiser les variables d'aide
        try {
            adressePrincipale = controle.getAdressePrincipale(getTransaction(), controle.getDatePrevue());
            // adresseDomicile = entity.getAdresseDomicile(getTransaction(),
            // getPassage().getDateFacturation());
            // adressePaiement = controle.getAdressePaiement(getTransaction(),
            // controle.getDatePrevue());

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de retrouver l'adress principale, " + "du domicile pour : " + "ID="
                            + controle.getNumAffilie(), FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), controle.getLangueTiers());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        _headerText(headerBean, dateImpression);
        _summaryText();

        caisseReportHelper.addHeaderParameters(this, headerBean);

    }

    public String getDateImpression() {
        return dateImpression;
    }

    /**
     * @return
     */
    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    /**
     * @return
     */
    public String getIdControle() {
        return idControle;
    }

    /**
     * @return
     */
    public String getIdPersRef() {
        return idPersRef;
    }

    /**
     * @return
     */
    public int getIndex() {
        return index;
    }

    public boolean isPreRapport() {
        return preRapport;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = false;
        }

        return retValue;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    /**
     * @param boolean1
     */
    public void setEnvoyerGed(Boolean boolean1) {
        envoyerGed = boolean1;
    }

    /**
     * @param string
     */
    public void setIdControle(String string) {
        idControle = string;
    }

    /**
     * @param string
     */
    public void setIdPersRef(String string) {
        idPersRef = string;
    }

    /**
     * @param i
     */
    public void setIndex(int i) {
        index = i;
    }

    public void setPreRapport(boolean preRapport) {
        this.preRapport = preRapport;
    }

}
