package globaz.hercule.itext.controleEmployeur;

import globaz.babel.api.ICTListeTextes;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.service.CEDocumentItextService;
import globaz.hercule.service.CETiersService;
import globaz.naos.util.AFUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author SCO
 * @since 24 nov. 2010
 */
public class CELettreLibre_Doc extends CEDSLettre {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DOCUMENT_COMPTE_RENDU = "DOCUMENT_COMPTE_RENDU";
    public static final String NUM_INFOROM = "0187CCE";

    private static final String TITRE_LETTRE_LIBRE = "LETTRE_LIBRE";

    private String idControle;
    private boolean nextUneSeuleFois = true;
    private String texteLibre;

    /**
     * Constructeur de CELettreLibre_Doc
     */
    public CELettreLibre_Doc() throws Exception {
        super(new BSession(CEApplication.DEFAULT_APPLICATION_HERCULE));
    }

    /**
     * Constructeur de CECompteRendu_Doc
     */
    public CELettreLibre_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, CEApplication.APPLICATION_HERCULE_ROOT, CELettreLibre_Doc.DOCUMENT_COMPTE_RENDU);
        super.setDocumentTitle(getSession().getLabel(CELettreLibre_Doc.TITRE_LETTRE_LIBRE));
        setParentWithCopy(parent);
    }

    /**
     * Constructeur de CELettreLibre_Doc
     */
    public CELettreLibre_Doc(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CELettreLibre_Doc.DOCUMENT_COMPTE_RENDU);
        super.setDocumentTitle(getSession().getLabel(CELettreLibre_Doc.TITRE_LETTRE_LIBRE));
    }

    /**
     * Constructeur de CELettreLibre_Doc
     */
    public CELettreLibre_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CELettreLibre_Doc.DOCUMENT_COMPTE_RENDU);
        super.setDocumentTitle(getSession().getLabel(CELettreLibre_Doc.TITRE_LETTRE_LIBRE));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setFileTitle(getSession().getLabel(CELettreLibre_Doc.TITRE_LETTRE_LIBRE));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {

        setDocumentTitle(getSession().getLabel(CELettreLibre_Doc.TITRE_LETTRE_LIBRE));
        getDocumentInfo().setDocumentTypeNumber(CELettreLibre_Doc.NUM_INFOROM);
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentDate(getDateEnvoi());

        // récupération du requerant en cours et du tiers correspondant
        TITiersViewBean tiers = CETiersService.retrieveTiersViewBean(getSession(), getIdTiers());

        // Récupération du controle
        CEControleEmployeur controle = CEControleEmployeurService.retrieveControle(getSession(), getIdControle());

        // récupération de la langue du tiers
        setLangueIsoRequerant(AFUtil.toLangueIso(tiers.getLangue()));

        // définit le titre (Madame, Monsieur) du requérant
        String formulePolitesse = tiers.getFormulePolitesse(null);

        // Chargement du catalogue
        this.loadCatalogue(CEDSLettre.CE_DOCUMENT_LETTRE_LIBRE);

        // Liste des arguments
        // {0} = Formule de pollitesse
        // {1} = date de debut d'affiliation
        // {2} = Annee courante
        // {3} = numéro d'affilié
        // {4} = date de debut de controle
        // {5} = date de fin de controle
        // {6} = date effective
        // {7} = Nom du reviseur
        // {11}= Numéro de rapport
        String[] listeArgument = { formulePolitesse, null, getAnnee(), getNumAffilie(), getDateDebutControle(),
                getDateFinControle(), getDateEffective(), _getNomReviseur(), " ", " ", " ", controle.getRapportNumero() };

        StringBuffer corps = new StringBuffer("");
        corps.append(CEDocumentItextService.formatMessage(getTexteLibre(), listeArgument));
        corps.append("\n\n");
        corps.append(CEDocumentItextService.getTexte(catalogue, 3, listeArgument));
        corps.append("\n\n");

        // Mise en place du texte
        this.setParametres("L_TITRE", CEDocumentItextService.getTexte(catalogue, 1, listeArgument));
        this.setParametres("L_POLITESSE", CEDocumentItextService.getTexte(catalogue, 2, listeArgument));
        this.setParametres("L_CORPS", corps.toString());

        // Annexe
        try {
            ICTListeTextes listTexte = catalogue.getTextes(4);
            this.setParametres("L_ANNEXE",
                    CEDocumentItextService.formatMessage(listTexte.getTexte(1).toString(), listeArgument));
        } catch (Exception e) {
            this.setParametres("L_ANNEXE", " ");

        }

        // Récupération du document ITEXT
        setTemplateFile(CEDSLettre.MODEL_NAME_LETTRE_LIBRE);

        // Numero affilié passé au docinfo
        fillDocInfo();

        // Mise en place du header et du footer
        setHeaderAndFooter(tiers, _getDateImpression(tiers));
        nextUneSeuleFois = false;
    }

    public String getIdControle() {
        return idControle;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getTexteLibre() {
        return texteLibre;
    }

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

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    public void setTexteLibre(String texteLibre) {
        this.texteLibre = texteLibre;
    }
}
