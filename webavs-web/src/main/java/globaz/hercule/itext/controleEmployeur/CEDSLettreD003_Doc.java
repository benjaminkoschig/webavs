package globaz.hercule.itext.controleEmployeur;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hercule.application.CEApplication;
import globaz.hercule.service.CEDocumentItextService;
import globaz.hercule.service.CETiersService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.naos.util.AFUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author SCO
 * @since 9 ao?t 2010
 */
public class CEDSLettreD003_Doc extends CEDSLettre {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DOCUMENT_DS_RAPPEL = "DOCUMENT_DS_RAPPEL";
    public static final String NUM_INFOROM = "0240CCE";

    private static final String TITLE_DOCUMENT_DS_RAPPEL = "TITLE_DOCUMENT_DS_RAPPEL";

    /**
     * Constructeur de CERapport_P1_Doc
     */
    public CEDSLettreD003_Doc() throws Exception {
        super(new BSession(CEApplication.DEFAULT_APPLICATION_HERCULE));
    }

    /**
     * Constructeur de CERapport_P1_Doc
     */
    public CEDSLettreD003_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, CEApplication.APPLICATION_HERCULE_ROOT, CEDSLettreD003_Doc.DOCUMENT_DS_RAPPEL);
        super.setDocumentTitle(getSession().getLabel(CEDSLettreD003_Doc.TITLE_DOCUMENT_DS_RAPPEL));
        setParentWithCopy(parent);
    }

    /**
     * Constructeur de CERapport_P1_Doc
     */
    public CEDSLettreD003_Doc(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CEDSLettreD003_Doc.DOCUMENT_DS_RAPPEL);
        super.setDocumentTitle(getSession().getLabel(CEDSLettreD003_Doc.TITLE_DOCUMENT_DS_RAPPEL));
    }

    /**
     * Constructeur de CERapport_P1_Doc
     */
    public CEDSLettreD003_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CEDSLettreD003_Doc.DOCUMENT_DS_RAPPEL);
        super.setDocumentTitle(getSession().getLabel(CEDSLettreD003_Doc.TITLE_DOCUMENT_DS_RAPPEL));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setDocumentTitle(getSession().getLabel(CEDSLettreD003_Doc.TITLE_DOCUMENT_DS_RAPPEL));
        setFileTitle(getSession().getLabel(CEDSLettreD003_Doc.TITLE_DOCUMENT_DS_RAPPEL));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber(CEDSLettreD003_Doc.NUM_INFOROM);
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentDate(JACalendar.todayJJsMMsAAAA());

        for (int i = 0; i < docCourant.size(); i++) {
            LEParamEnvoiDataSource.paramEnvoi p = docCourant.getParamEnvoi(i);
            addPropriete(p.getCsType(), p.getValeur());
        }
        // r?cup?ration du requerant en cours et du tiers correspondant
        TITiersViewBean tiers = CETiersService.retrieveTiersViewBean(getSession(), getIdTiers());

        // r?cup?ration de la langue du tiers
        setLangueIsoRequerant(AFUtil.toLangueIso(tiers.getLangue()));

        // d?finit le titre (Madame, Monsieur) du requ?rant
        String formulePolitesse = tiers.getFormulePolitesse(null);

        // On cherche la borne inf?rieur sur lequel le document se porte
        String anneeBorneInferieur = CEDocumentItextService.calculAnneDepart(getAnnee(), getDateDebutAffiliation());

        // Chargement du catalogue
        this.loadCatalogue(CEDSLettre.CE_DOCUMENT_D003);

        // Liste des arguments
        // {0} = Formule de pollitesse
        // {1} = date de debut d'affiliation
        // {2} = Annee courante du document
        String[] listeArgument = { formulePolitesse, anneeBorneInferieur, getAnnee() };

        // Mise en place du texte
        this.setParametres("P_CORPS1", CEDocumentItextService.getTexte(catalogue, 1, listeArgument));
        this.setParametres("P_CORPS2", CEDocumentItextService.getTexte(catalogue, 2, listeArgument));

        // R?cup?ration du document ITEXT
        setTemplateFile(CEDSLettre.MODEL_NAME_D003);

        // Numero affili? pass? au docinfo
        fillDocInfo();

        // BZ 7413 : set la date d'impression
        if (!JadeStringUtil.isEmpty(dateImpression)) {
            setHeaderAndFooter(tiers, JACalendar.format(dateImpression, tiers.getLangueIso()));
        } else {
            setHeaderAndFooter(tiers, JACalendar.format(JACalendar.todayJJsMMsAAAA(), tiers.getLangueIso()));
        }
    }
}
