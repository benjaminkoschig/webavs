package globaz.hercule.itext.controleEmployeur;

import globaz.babel.api.ICTListeTextes;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.service.CEDocumentItextService;
import globaz.hercule.service.CETiersService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.util.AFUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author SCO
 * @since 14 oct. 2010
 */
public class CELettreLibreCatalogue_Doc extends CEDSLettre {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DOCUMENT_LETTRE_LIBRE_CATALOGUE = "DOCUMENT_LETTRE_LIBRE_CATALOGUE";
    public static final String NUM_INFOROM = "0254CCE";

    private static final String TITRE_IMPRESSION_LETTRE_LIBRE_CATALOGUE = "TITRE_IMPRESSION_LETTRE_LIBRE_CATALOGUE";

    private String idControle;
    private String idDocument = "";
    private String idDocumentDefaut = "";
    private boolean nextUneSeuleFois = true;

    /**
     * Constructeur de CELettreLibreCatalogue_Doc
     */
    public CELettreLibreCatalogue_Doc() throws Exception {
        super(new BSession(CEApplication.DEFAULT_APPLICATION_HERCULE));
    }

    /**
     * Constructeur de CELettreLibreCatalogue_Doc
     */
    public CELettreLibreCatalogue_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, CEApplication.APPLICATION_HERCULE_ROOT,
                CELettreLibreCatalogue_Doc.DOCUMENT_LETTRE_LIBRE_CATALOGUE);
        super.setDocumentTitle(getSession()
                .getLabel(CELettreLibreCatalogue_Doc.TITRE_IMPRESSION_LETTRE_LIBRE_CATALOGUE));
        setParentWithCopy(parent);
    }

    /**
     * Constructeur de CELettreLibreCatalogue_Doc
     */
    public CELettreLibreCatalogue_Doc(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT,
                CELettreLibreCatalogue_Doc.DOCUMENT_LETTRE_LIBRE_CATALOGUE);
        super.setDocumentTitle(getSession()
                .getLabel(CELettreLibreCatalogue_Doc.TITRE_IMPRESSION_LETTRE_LIBRE_CATALOGUE));
    }

    /**
     * Constructeur de CELettreLibreCatalogue_Doc
     */
    public CELettreLibreCatalogue_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT,
                CELettreLibreCatalogue_Doc.DOCUMENT_LETTRE_LIBRE_CATALOGUE);
        super.setDocumentTitle(getSession()
                .getLabel(CELettreLibreCatalogue_Doc.TITRE_IMPRESSION_LETTRE_LIBRE_CATALOGUE));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setDocumentTitle(getSession().getLabel(CELettreLibreCatalogue_Doc.TITRE_IMPRESSION_LETTRE_LIBRE_CATALOGUE));
        setFileTitle(getSession().getLabel(CELettreLibreCatalogue_Doc.TITRE_IMPRESSION_LETTRE_LIBRE_CATALOGUE));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {

        getDocumentInfo().setDocumentTypeNumber(CELettreLibreCatalogue_Doc.NUM_INFOROM);
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
        this.loadCatalogue(CEDSLettre.CE_DOCUMENT_LETTRE_LIBRE_CATALOGUE, getIdDocument(), getIdDocumentDefaut());

        // Liste des arguments
        // {0} = Formule de pollitesse
        // {1} = date de debut d'affiliation
        // {2} = Annee courante
        // {3} = numéro d'affilié
        // {4} = date de debut de controle
        // {5} = date de fin de controle
        // {6} = date effective
        // {7} = visa reviseur
        // {11}= Numéro de rapport
        String[] listeArgument = { formulePolitesse, null, getAnnee(), getNumAffilie(), getDateDebutControle(),
                getDateFinControle(), formatDateEffective(), _getNomReviseur(), " ", " ", " ",
                controle.getRapportNumero() };

        // Mise en place du texte
        this.setParametres("P_CORPS1", CEDocumentItextService.getTexte(catalogue, 1, listeArgument));
        this.setParametres("P_CORPS2", CEDocumentItextService.getTexte(catalogue, 2, listeArgument));

        // Annexe
        try {
            ICTListeTextes listTexte = catalogue.getTextes(3);
            this.setParametres("L_ANNEXE",
                    CEDocumentItextService.formatMessage(listTexte.getTexte(1).toString(), listeArgument));
        } catch (Exception e) {
            this.setParametres("L_ANNEXE", " ");

        }

        // Récupération du document ITEXT
        setTemplateFile(CEDSLettre.MODEL_NAME_LETTRE_LIBRE_CATALOGUE);

        // Numero affilié passé au docinfo
        fillDocInfo();

        // Mise en place du header et du footer
        setHeaderAndFooter(tiers, _getDateImpression(tiers));
        nextUneSeuleFois = false;
    }

    public String formatDateEffective() {
        String dateEffectiveFormat = "";
        if (!JadeStringUtil.isEmpty(getDateEffective())) {
            dateEffectiveFormat = JACalendar.format(getDateEffective(), getLangueIsoRequerant());
        }

        return dateEffectiveFormat;
    }

    public String getIdControle() {
        return idControle;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdDocumentDefaut() {
        return idDocumentDefaut;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#next()
     */
    @Override
    public boolean next() throws FWIException {
        return nextUneSeuleFois;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdDocumentDefaut(String idDocumentDefaut) {
        this.idDocumentDefaut = idDocumentDefaut;
    }
}
