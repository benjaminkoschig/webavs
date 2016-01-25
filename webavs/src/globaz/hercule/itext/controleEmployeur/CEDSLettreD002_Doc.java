package globaz.hercule.itext.controleEmployeur;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.ICEControleEmployeur;
import globaz.hercule.service.CEDocumentItextService;
import globaz.hercule.service.CETiersService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.naos.util.AFUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author SCO
 * @since 28 juil. 2010
 */

// Exemple : CIImpressionRappelBta_Doc
public class CEDSLettreD002_Doc extends CEDSLettre {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DOCUMENT_DS = "DOCUMENT_DS";
    private static final String NUM_INFOROM_D002 = "0238CCE";

    private static final String NUM_INFOROM_D002_BARCODE = "238";
    private static final String NUM_INFOROM_D005 = "0239CCE";

    private static final String NUM_INFOROM_D005_BARCODE = "239";
    private static final String TITLE_DOCUMENT_DS = "TITLE_DOCUMENT_DS";

    /**
     * Constructeur de CEDSLettreD002_Doc
     */
    public CEDSLettreD002_Doc() throws Exception {
        super(new BSession(CEApplication.DEFAULT_APPLICATION_HERCULE));
    }

    /**
     * Constructeur de CEDSLettreD002_Doc
     */
    public CEDSLettreD002_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, CEApplication.APPLICATION_HERCULE_ROOT, CEDSLettreD002_Doc.DOCUMENT_DS);
        super.setDocumentTitle(getSession().getLabel(CEDSLettreD002_Doc.TITLE_DOCUMENT_DS));
        setParentWithCopy(parent);
    }

    /**
     * Constructeur de CEDSLettreD002_Doc
     */
    public CEDSLettreD002_Doc(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CEDSLettreD002_Doc.DOCUMENT_DS);
        super.setDocumentTitle(getSession().getLabel(CEDSLettreD002_Doc.TITLE_DOCUMENT_DS));
    }

    /**
     * Constructeur de CEDSLettreD002_Doc
     */
    public CEDSLettreD002_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CEDSLettreD002_Doc.DOCUMENT_DS);
        super.setDocumentTitle(getSession().getLabel(CEDSLettreD002_Doc.TITLE_DOCUMENT_DS));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setDocumentTitle(getSession().getLabel(CEDSLettreD002_Doc.TITLE_DOCUMENT_DS));
        setFileTitle(getSession().getLabel(CEDSLettreD002_Doc.TITLE_DOCUMENT_DS));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber(CEDSLettreD002_Doc.NUM_INFOROM_D002);
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentDate(JACalendar.todayJJsMMsAAAA());

        for (int i = 0; i < docCourant.size(); i++) {
            LEParamEnvoiDataSource.paramEnvoi p = docCourant.getParamEnvoi(i);
            addPropriete(p.getCsType(), p.getValeur());
        }
        // récupération du requerant en cours et du tiers correspondant
        TITiersViewBean tiers = CETiersService.retrieveTiersViewBean(getSession(), getIdTiers());

        // récupération de la langue du tiers
        setLangueIsoRequerant(AFUtil.toLangueIso(tiers.getLangue()));

        // définit le titre (Madame, Monsieur) du requérant
        String formulePolitesse = tiers.getFormulePolitesse(null);

        // On cherche la borne inférieur sur lequel le document se porte
        String anneeBorneInferieur = CEDocumentItextService.calculAnneDepart(getAnnee(), getDateDebutAffiliation());

        // Création du document

        // Liste des arguments
        // {0} = Formule de pollitesse
        // {1} = date de debut d'affiliation
        // {2} = Annee courante
        String[] listeArgument = { formulePolitesse, anneeBorneInferieur, getAnnee(), "", "", "", "", "", "", "",
                tiers.getNomPrenom() };

        // Choix du template suivant la masse salariale
        if (!ICEControleEmployeur.CATEGORIE_MASSE_0.equals(getCategorieMasse())) {

            // Chargement du catalogue
            this.loadCatalogue(CEDSLettre.CE_DOCUMENT_D005);

            this.setParametres("P_CORPS1", CEDocumentItextService.getTexte(catalogue, 1, listeArgument));
            this.setParametres("P_CORPS2", CEDocumentItextService.getTexte(catalogue, 2, listeArgument));

            // Récupération du document ITEXT
            setTemplateFile(CEDSLettre.MODEL_NAME_D005);

            getDocumentInfo().setDocumentTypeNumber(CEDSLettreD002_Doc.NUM_INFOROM_D005);
            getDocumentInfo().setBarcode(
                    CEDSLettreD002_Doc.NUM_INFOROM_D005_BARCODE + getAnnee()
                            + CEUtils.unFormatNumeroAffilie(getSession(), getNumAffilie()));

        } else {

            // Chargement du catalogue
            this.loadCatalogue(CEDSLettre.CE_DOCUMENT_D002);

            this.setParametres("P_CONCERNE", CEDocumentItextService.getTexte(catalogue, 1, listeArgument));
            this.setParametres("P_CAS1", CEDocumentItextService.getTexte(catalogue, 2, listeArgument));
            this.setParametres("P_CAS2", CEDocumentItextService.getTexte(catalogue, 3, listeArgument));
            this.setParametres("P_CORPS", CEDocumentItextService.getTexte(catalogue, 4, listeArgument));

            // Récupération du document ITEXT
            setTemplateFile(CEDSLettre.MODEL_NAME_D002);

            getDocumentInfo().setDocumentTypeNumber(CEDSLettreD002_Doc.NUM_INFOROM_D002);
            getDocumentInfo().setBarcode(
                    CEDSLettreD002_Doc.NUM_INFOROM_D002_BARCODE + getAnnee()
                            + CEUtils.unFormatNumeroAffilie(getSession(), getNumAffilie()));
        }

        // Numero affilié passé au docinfo
        fillDocInfo();

        // BZ 7413 : set la date d'impression
        if (!JadeStringUtil.isEmpty(dateImpression)) {
            setHeaderAndFooter(tiers, JACalendar.format(dateImpression, tiers.getLangueIso()));
        } else {
            setHeaderAndFooter(tiers, JACalendar.format(JACalendar.todayJJsMMsAAAA(), tiers.getLangueIso()));
        }

    }
}
