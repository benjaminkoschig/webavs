package globaz.hercule.itext.controleEmployeur;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.hercule.application.CEApplication;
import globaz.hercule.service.CEDocumentItextService;
import globaz.hercule.service.CETiersService;
import globaz.naos.util.AFUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author SCO
 * @since 23 nov. 2010
 */
public class CEDeleguerTiers_Doc extends CEDSLettre {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DOCUMENT_DELEGUER_TIERS = "DOCUMENT_DELEGUER_TIERS";
    public static final String NUM_INFOROM = "0255CCE";

    private static final String TITRE_IMPRESSION_DELEGUER_TIERS = "TITRE_IMPRESSION_DELEGUER_TIERS";

    private boolean nextUneSeuleFois = true;
    private String nomDeleguation;

    /**
     * Constructeur de CEDeleguerTiers_Doc
     */
    public CEDeleguerTiers_Doc() throws Exception {
        super(new BSession(CEApplication.DEFAULT_APPLICATION_HERCULE));
    }

    /**
     * Constructeur de CEDeleguerTiers_Doc
     */
    public CEDeleguerTiers_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, CEApplication.APPLICATION_HERCULE_ROOT, CEDeleguerTiers_Doc.DOCUMENT_DELEGUER_TIERS);
        super.setDocumentTitle(getSession().getLabel(CEDeleguerTiers_Doc.TITRE_IMPRESSION_DELEGUER_TIERS));
        setParentWithCopy(parent);
    }

    /**
     * Constructeur de CEDeleguerTiers_Doc
     */
    public CEDeleguerTiers_Doc(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CEDeleguerTiers_Doc.DOCUMENT_DELEGUER_TIERS);
        super.setDocumentTitle(getSession().getLabel(CEDeleguerTiers_Doc.TITRE_IMPRESSION_DELEGUER_TIERS));
    }

    /**
     * Constructeur de CEDeleguerTiers_Doc
     */
    public CEDeleguerTiers_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CEDeleguerTiers_Doc.DOCUMENT_DELEGUER_TIERS);
        super.setDocumentTitle(getSession().getLabel(CEDeleguerTiers_Doc.TITRE_IMPRESSION_DELEGUER_TIERS));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setDocumentTitle(getSession().getLabel(CEDeleguerTiers_Doc.TITRE_IMPRESSION_DELEGUER_TIERS));
        setFileTitle(getSession().getLabel(CEDeleguerTiers_Doc.TITRE_IMPRESSION_DELEGUER_TIERS));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber(CEDeleguerTiers_Doc.NUM_INFOROM);
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentDate(getDateEnvoi());

        // récupération du requerant en cours et du tiers correspondant
        TITiersViewBean tiers = CETiersService.retrieveTiersViewBean(getSession(), getIdTiers());

        // récupération de la langue du tiers
        setLangueIsoRequerant(AFUtil.toLangueIso(tiers.getLangue()));

        // définit le titre (Madame, Monsieur) du requérant
        String formulePolitesse = tiers.getFormulePolitesse(null);

        // Chargement du catalogue
        this.loadCatalogue(CEDSLettre.CE_DOCUMENT_DELEGUER_TIERS);

        // Liste des arguments
        // {0} = Formule de politesse
        // {1} = date de debut d'affiliation
        // {2} = Annee courante
        // {3} = numéro d'affilié
        // {4} = date de debut de controle
        // {5} = date de fin de controle
        // {6} = date effective
        // {7} = ....
        // {8} = nom du delegué
        String[] listeArgument = { formulePolitesse, null, getAnnee(), getNumAffilie(), getDateDebutControle(),
                getDateFinControle(), getDateEffective(), null, getNomDeleguation() };

        // Mise en place du texte
        this.setParametres("P_CONCERNE", CEDocumentItextService.getTexte(catalogue, 1, listeArgument));
        this.setParametres("P_CORPS1", CEDocumentItextService.getTexte(catalogue, 2, listeArgument));
        this.setParametres("P_DELEGUER", CEDocumentItextService.getTexte(catalogue, 3, listeArgument));
        this.setParametres("P_CORPS2", CEDocumentItextService.getTexte(catalogue, 4, listeArgument));

        // Récupération du document ITEXT
        setTemplateFile(CEDSLettre.MODEL_NAME_DELEGUER_TIERS);

        // Numero affilié passé au docinfo
        fillDocInfo();

        // Mise en place du header et du footer
        setHeaderAndFooter(tiers, _getDateImpression(tiers));
        nextUneSeuleFois = false;
    }

    public String getNomDeleguation() {
        return nomDeleguation;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#next()
     */
    @Override
    public boolean next() throws FWIException {
        return nextUneSeuleFois;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setNomDeleguation(String nomDeleguation) {
        this.nomDeleguation = nomDeleguation;
    }

}
