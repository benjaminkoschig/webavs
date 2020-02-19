package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;
import globaz.osiris.db.utils.CAReferenceBVR;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

/**
 * <H1>Description</H1>
 * <p>
 * .
 * </p>
 * 
 * @author Alexandre Cuva, 18-aug-2004
 */
public class CO03ARetraitOpposition extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String CONF_TEMPLATE_NAME = "CO_03A_CONF_RETR_OPP_AF";

    private static final String LETTRE_TEMPLATE_NAME = "CO_03A_LETTRE_RETR_OPP_AF";
    public static final String NUM_REF_INFOROM_LETTRE = "0029GCO";
    private static final long serialVersionUID = -2992544795851919726L;

    private static final int STATE_CONF = 2;
    private static final int STATE_IDLE = 0;
    private static final int STATE_LETTRE = 1;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private CAReferenceBVR bvr = null;
    private int state = CO03ARetraitOpposition.STATE_IDLE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO03ARetraitOpposition1.
     * 
     * @throws Exception
     */
    public CO03ARetraitOpposition() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO03ARetraitOpposition(BSession parent) throws FWIException {
        super(parent);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws FWIException
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        // Gestion du titre, doit être placé ici et non dans le
        // createDataSource() pour que l'étape apparaisse dans l'objet du mail.
        setDocumentTitle(getSession().getLabel("AQUILA_RETRAIT_OPPOSITION"));
        setNumeroReferenceInforom(CO03ARetraitOpposition.NUM_REF_INFOROM_LETTRE);
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            if (state == CO03ARetraitOpposition.STATE_CONF) {
                createDataSourceConf();
            } else {
                createDataSourceLettre();
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * Lettre de confirmation de retrait
     * 
     * @throws Exception
     */
    private void createDataSourceConf() throws Exception {
        // Gestion du modèle
        setTemplateFile(CO03ARetraitOpposition.CONF_TEMPLATE_NAME);

        // destinataire est l'affilie
        destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
        _setLangueFromTiers(destinataireDocument);

        // -- corps du document
        // ----------------------------------------------------------------------------
        // rechercher tous les paragraphes du corps du document
        StringBuilder body = new StringBuilder();

        getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

        // formater le corps, les conventions de remplacement pour les paragraphes du corps sont:
        // {0} = numéro de "Réception du retrait d'opposition saisi" poursuite
        // {1} = demandeur poursuite
        // {2} = date notification
        // {3} = ville de l'office de poursuite
        // {4} = date de la section
        String localiteOP = "";
        IntTiers op = getTiersService().getOfficePoursuite(getSession(), destinataireDocument,
                curContentieux.getCompteAnnexe().getIdExterneRole());

        if (op != null) {
            TIAdresseDataSource adresse = this.getAdresseDataSourcePrincipal(op);

            if (adresse != null) {
                localiteOP = adresse.localiteNom;
            }
        }

        // retrouver l'historique de notification CDP avec opp.
        COHistorique historiqueCDP = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION);
        COEtapeInfo etapeInfo = historiqueCDP.loadEtapeInfo(COEtapeInfoConfig.CS_DATE_NOTIFICATION_CDP);

        this.setParametres(
                COParameter.T10,
                formatMessage(body, new Object[] { curContentieux.getNumPoursuite(), this._getProperty("nom.caisse."),
                        formatDate(etapeInfo != null ? etapeInfo.getValeur() : curContentieux.getDateExecution()),
                        localiteOP, formatDate(curContentieux.getSection().getDateSection()) }));

        this.setParametres(ICaisseReportHelper.PARAM_HEADER_LIBELLES, getLibelle());
        this.setParametres(ICaisseReportHelper.PARAM_HEADER_DONNEES, getDonnees());

        getBvr().setSession(getSession());
        this.setParametres(COParameter.ADRESSECAISSE, getBvr().getAdresse());
        // no de compte
        body.setLength(0);
        body.append(getCatalogueTextesUtil().texte(getParent(), 1, 1));
        this.setParametres(COParameter.T1, formatMessage(body, new Object[] { curContentieux.getNumPoursuite() }));

        this.setParametres(COParameter.T2, getCatalogueTextesUtil().texte(getParent(), 1, 2));
        this.setParametres(COParameter.T12, getCatalogueTextesUtil().texte(getParent(), 3, 1));
        this.setParametres(COParameter.T13, getCatalogueTextesUtil().texte(getParent(), 3, 2));
    }

    /**
     * Lettre de demande de retrait
     * 
     * @throws Exception
     */
    private void createDataSourceLettre() throws Exception {
        // Gestion du modèle et du titre
        setTemplateFile(CO03ARetraitOpposition.LETTRE_TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_LETTRE_RETRAIT_OPPOSITION"));

        // destinataire est l'affilie
        destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
        _setLangueFromTiers(destinataireDocument);

        // Gestion de l'en-tête/pied de page/signature
        this._handleHeaders(curContentieux, true, false, true, getAdressePrincipale(destinataireDocument));

        // -- titre du doc
        // ------------------------------------------------------------------------------
        // rechercher tous les paragraphes du corps du document
        StringBuilder body = new StringBuilder();

        getCatalogueTextesUtil().dumpNiveau(getParent(), 4, body, "\n");

        // formater le corps, les conventions de remplacement pour les paragraphes du corps sont:
        // {0} = numéro de poursuite
        // {1} = date de la section
        this.setParametres(
                COParameter.T1,
                formatMessage(body, new Object[] { curContentieux.getNumPoursuite(),
                        formatDate(curContentieux.getSection().getDateSection()) }));

        // -- corps du doc
        // ------------------------------------------------------------------------------
        // rechercher tous les paragraphes du corps du document
        body.setLength(0);
        getCatalogueTextesUtil().dumpNiveau(getParent(), 5, body, "\n\n");

        // formater le corps, les conventions de remplacement pour les paragraphes du corps sont:
        // {0} = numéro de poursuite
        // {1} = formule de politesse (ex: Madame, Monsieur)
        // {2} = date notification
        // {3} = ville de l'office de poursuite
        // {4} = nombre de jours de délai
        // {5} = montant à payer
        // {6} = date de la section
        String localiteOP = "";
        IntTiers op = getTiersService().getOfficePoursuite(getSession(), destinataireDocument,
                curContentieux.getCompteAnnexe().getIdExterneRole());

        if (op != null) {
            TIAdresseDataSource adresse = this.getAdresseDataSourcePrincipal(op);

            if (adresse != null) {
                localiteOP = adresse.localiteNom;
            }
        }

        // retrouver l'historique de notification CDP avec opp.
        COHistorique historiqueCDP = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION);
        COEtapeInfo etapeInfo = historiqueCDP.loadEtapeInfo(COEtapeInfoConfig.CS_DATE_NOTIFICATION_CDP);

        this.setParametres(
                COParameter.T3,
                formatMessage(body, new Object[] { curContentieux.getNumPoursuite(),
                        getFormulePolitesse(destinataireDocument),
                        formatDate(etapeInfo != null ? etapeInfo.getValeur() : curContentieux.getDateExecution()),
                        localiteOP, getTransition().getDuree(), formatMontant(curContentieux.getSection().getSolde()),
                        formatDate(curContentieux.getSection().getDateSection()) }));

        // -- annexes
        // ---------------------------------------------------------------------------
        body.setLength(0);
        getCatalogueTextesUtil().dumpNiveau(getParent(), 6, body, "\n");

        /*
         * formater le copies, les conventions de remplacements sont: {0} = nombre de jours de délais
         */
        this.setParametres(COParameter.T2, formatMessage(body, new Object[] { getTransition().getDuree() }));
    }

    /**
     * Renvoie la référence BVR.
     * 
     * @return la référence BVR.
     */
    public CAReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new CAReferenceBVR();
        }
        return bvr;
    }

    /**
     * @return
     */
    private String getDonnees() {
        StringBuffer donnees = new StringBuffer("");
        // destinataire est l'affilie
        IntTiers affilie = curContentieux.getCompteAnnexe().getTiers();
        donnees.append(curContentieux.getSection().getIdExterne());
        donnees.append("\n");
        donnees.append(curContentieux.getSection().getCompteAnnexe().getIdExterneRole());
        donnees.append("\n");
        donnees.append(affilie.getNom());
        donnees.append(" ");
        donnees.append(affilie.getComplementNom()[0]);
        donnees.append(" ");
        donnees.append(affilie.getComplementNom()[1]);
        donnees.append("\n");
        donnees.append(affilie.getLieu());
        return donnees.toString();
    }

    /**
     * @return
     */
    private String getLibelle() {
        StringBuffer libelle = new StringBuffer("");
        libelle.append(getSession().getLabel("IMPRIMER_JOURNAL_NUM_SECTION"));
        libelle.append("\n");
        libelle.append(getSession().getLabel("IMPRIMER_JOURNAL_NUM_AFFILIE"));
        libelle.append("\n");
        libelle.append(getSession().getLabel("IMPRIMER_JOURNAL_AFFILIE"));
        return libelle.toString();
    }

    /**
     * @return
     * @throws FWIException
     */
    @Override
    public boolean next() throws FWIException {
        switch (state) {
            case STATE_IDLE:

                // on commenc ou a deja traité un contentieux, on regarde s'il reste
                // des contentieux a traiter
                if (super.next()) {
                    state = CO03ARetraitOpposition.STATE_LETTRE;

                    // on va créer la lettre
                    return true;
                } else {
                    // il n'y a plus de documents à créer
                    return false;
                }

            case STATE_LETTRE:

                // on vient de créer la lettre, on va créer la confirmation
                state = CO03ARetraitOpposition.STATE_CONF;

                return true;

            default:

                // on regarder si on encore des contentieux à traiter.
                state = CO03ARetraitOpposition.STATE_IDLE;

                return next();
        }
    }
}
