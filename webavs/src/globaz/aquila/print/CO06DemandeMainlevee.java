package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <H1>Description</H1>
 * <p>
 * .
 * </p>
 * 
 * @author Pascal Lovy, 25-nov-2004
 */
public class CO06DemandeMainlevee extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String DEMANDE_TEMPLATE_NAME = "CO_06_DEMANDE_MAINLEVEE_JU";
    public static final String NUM_REF_INFOROM_SITUATION = "0028GCO";
    public static final String NUMERO_REFERENCE_INFOROM = "0027GCO";
    private static final long serialVersionUID = 8334020787169517681L;
    private static final String SITUATION_TEMPLATE_NAME = "CO_07_SITUATION_COMPTE_JU";

    private static final int STATE_DEMANDE = 1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_SITUATION = 2;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateSituationCompte = null;
    private int state = CO06DemandeMainlevee.STATE_IDLE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO06DemandeMainlevee.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CO06DemandeMainlevee() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO06DemandeMainlevee(BSession parent) throws FWIException {
        super(parent);
    }

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        // Gestion du modèle et du titre, doit être placé ici et non dans le
        // createDataSourceDemande() pour que l'étape apparaisse dans l'objet du
        // mail.
        setTemplateFile(CO06DemandeMainlevee.DEMANDE_TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_MAIN_LEVEE"));
        setNumeroReferenceInforom(CO06DemandeMainlevee.NUMERO_REFERENCE_INFOROM);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            if (state == CO06DemandeMainlevee.STATE_DEMANDE) {
                createDataSourceDemande();
            } else {
                createDataSourceSituation();
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    private void createDataSourceDemande() throws Exception {
        // destinataire est le tribunal
        destinataireDocument = getTiersService().getTribunal(getSession(), curContentieux.getCompteAnnexe().getTiers(),
                curContentieux.getCompteAnnexe().getIdExterneRole());

        if (destinataireDocument == null) {
            this.log(getSession().getLabel("AQUILA_ERR_TRIBUNAL_INTROUVABLE"), FWMessage.AVERTISSEMENT);
        } else {
            _setLangueFromTiers(destinataireDocument);

            // Attention l'attribut cantonOfficePoursuite contient ici le canton du tribunal (ELP 2.0)
            TIAdresseDataSource adresseTribunal = this.getAdresseDataSourcePrincipal(destinataireDocument, getLangue());
            cantonOfficePoursuite = adresseTribunal.canton_court;

        }

        // Gestion de l'en-tête/pied de page/signature
        this._handleHeaders(destinataireDocument, curContentieux, true, false, true);

        // -- titre du doc
        // ------------------------------------------------------------------------------
        // rechercher toutes les lignes du titre du document
        StringBuffer body = new StringBuffer();

        getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

        /*
         * formater le titre, les conventions de remplacement pour les lignes du titre sont: {0} = retour chariot (\n)
         * {1} = numéro de poursuite {2} = débiteur {3} = période
         */
        this.setParametres(
                COParameter.T1,
                formatMessage(
                        body,
                        new Object[] { "\n", curContentieux.getNumPoursuite(),
                                this.getAdresseInLine(curContentieux.getCompteAnnexe().getTiers()),
                                curContentieux.getSection().getDescription(getLangue()), giveLibelleInfoRom246() }));

        // -- corps du doc
        // ------------------------------------------------------------------------------
        // rechercher tous les paragraphes du corps du document
        body.setLength(0);
        getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

        /*
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont:
         */
        this.setParametres(COParameter.T5, formatMessage(body, new Object[] {}));

        // -- annexes
        // ---------------------------------------------------------------------------
        this.setParametres(COParameter.T6, getCatalogueTextesUtil().texte(getParent(), 3, 1) + "\n" + getListeAnnexes());
    }

    /**
     * @throws Exception
     */
    private void createDataSourceSituation() throws Exception {
        // Gestion du modèle et du titre
        setTemplateFile(CO06DemandeMainlevee.SITUATION_TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_SITUATION_COMPTE"));
        super.getDocumentInfo().setDocumentTypeNumber(CO06DemandeMainlevee.NUM_REF_INFOROM_SITUATION);

        // destinataire est l'affilie
        destinataireDocument = curContentieux.getCompteAnnexe().getTiers();

        // Gestion de l'en-tête/pied de page/signature - pas d'adresse sur ce
        // document
        this._handleHeaders(null, curContentieux, true, false, true);

        // -- titre du doc
        // ------------------------------------------------------------------------------
        // rechercher toutes les lignes du titre du document
        StringBuffer body = new StringBuffer();

        getCatalogueTextesUtil().dumpNiveau(getParent(), 4, body, "\n");

        /*
         * formater le titre, les conventions de remplacement pour les lignes du titre sont: {0} = date situation compte
         * {1} = numéro de poursuite
         */
        this.setParametres(
                COParameter.T1,
                formatMessage(
                        body,
                        new Object[] { formatDate(curContentieux.getDateExecution()), curContentieux.getNumPoursuite() }));

        // -- sous-titre du doc
        // ------------------------------------------------------------------------------
        // rechercher toutes les lignes du sous-titre du document
        body.setLength(0);
        getCatalogueTextesUtil().dumpNiveau(getParent(), 5, body, "\n");

        /*
         * formater le sous-titre, les conventions de remplacement pour les lignes du sous-titre sont: {0} = nom de
         * l'affilie {1} = localite de l'affilie {2} = numéro section
         */
        TIAdresseDataSource adresse = getAdresseDataSourcePrincipalEnvoiOP(destinataireDocument);
        this.setParametres(
                COParameter.T2,
                formatMessage(body, new Object[] { destinataireDocument.getNom(),
                        adresse.localiteNpa + " " + adresse.localiteNom, curContentieux.getNumSection() }));

        // Montants
        this.setParametres(COParameter.L_MONTANT_CREANCE, getCatalogueTextesUtil().texte(getParent(), 8, 1));

        // if
        // (!JadeStringUtil.isBlankOrZero(CORequisitionPoursuiteUtil.getMontantCreance(getSession(),
        // curContentieux))) {
        // setParametres(COParameter.L_INTERET, formatMessage(new
        // StringBuffer(texte(9, 95)), new Object[] {
        // formatDate(CORequisitionPoursuiteUtil.getDateDebutInteretsTardifs(curContentieux)),
        // texte(4, 2),
        // formatMontant(CORequisitionPoursuiteUtil.getMontantCreance(getSession(),
        // curContentieux)) }));
        // }

        String[] infoSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(),
                curContentieux.getIdSection());

        String sectionDesc = new String(curContentieux.getSection().getDescription(getLangue())
                + " ("
                + curContentieux.getSection().getIdExterne()
                + ")"
                + " "
                + formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 90)), new Object[] {
                        formatDate(infoSection[2]), "", giveLibelleInfoRom246() }));

        this.setParametres(COParameter.L_SECTION_DESCR, sectionDesc);

        this.setParametres(COParameter.P_BASE, formatMontant(infoSection[0]));
        this.setParametres(COParameter.P_DEVISE, getCatalogueTextesUtil().texte(getParent(), 7, 8));
        // -- lignes détail
        // -----------------------------------------------------------------------
        // on veut que la situation de compte aille jusqu'à une date donnée
        List dataSource = createSituationCompteDS(infoSection[2], COParameter.F1, COParameter.F2, COParameter.F3,
                getCatalogueTextesUtil().texte(getParent(), 7, 8), ICOEtape.CS_DEMANDE_DE_MAINLEVEE_ENVOYEE,
                infoSection[1]);

        // -- lignes pour nouveaux frais
        FWCurrency totalNouvellesTaxes = addTaxesToDS(dataSource, COParameter.F1, COParameter.F2, COParameter.F3,
                getCatalogueTextesUtil().texte(getParent(), 7, 8));

        // -- versements
        if (!dataSource.isEmpty()) {
            this.setDataSource(dataSource);
        }

        if (totalNouvellesTaxes != null) {
            totalNouvellesTaxes.add(curContentieux.getSolde());
            this.setParametres(COParameter.M8, formatMontant(totalNouvellesTaxes.toString()));
        } else {
            this.setParametres(COParameter.M8, formatMontant(curContentieux.getSolde()));
        }

        // -- corps du doc
        // ------------------------------------------------------------------------------
        // rechercher tous les paragraphes du corps du document
        body.setLength(0);
        getCatalogueTextesUtil().dumpNiveau(getParent(), 6, body, "\n\n");

        /*
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = date debut
         * interets moratoires {1} = taux interets moratoires {2} = capital interets moratoires {3} = frais poursuite
         */
        BigDecimal frais = new BigDecimal(curContentieux.getSection().getFrais());
        frais = frais.add(new BigDecimal(curContentieux.getSection().getAmende()));

        // Attention l'attribut cantonOfficePoursuite contient ici le canton du tribunal (ELP 2.0)
        if (CORequisitionPoursuiteUtil.isNouveauRegimeSelonDateRP(getSession(), curContentieux)
                && !CORequisitionPoursuiteUtil.isOfficeDontWantToUseNewRegime(getSession(), cantonOfficePoursuite)) {

            this.setParametres(
                    COParameter.T11,
                    formatMessage(
                            body,
                            new Object[] {
                                    formatDate(CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(),
                                            curContentieux)),
                                    formatMontant(CORequisitionPoursuiteUtil.getMontantCreanceSoumis(getTransaction(),
                                            curContentieux, CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(
                                                    getSession(), curContentieux))), formatMontant(frais.toString()),
                                    getCatalogueTextesUtil().texte(getParent(), 7, 8) }));

        } else {

            this.setParametres(
                    COParameter.T11,
                    formatMessage(
                            body,
                            new Object[] {
                                    formatDate(CORequisitionPoursuiteUtil.getDateDebutInteretsTardifs(getSession(),
                                            getTransaction(), curContentieux)),
                                    formatMontant(CORequisitionPoursuiteUtil.getMontantCreanceSoumis(getTransaction(),
                                            curContentieux)), formatMontant(frais.toString()),
                                    getCatalogueTextesUtil().texte(getParent(), 7, 8) }));
        }

        // Rajoute la date à la signature
        this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_DATA,
                getImporter().getParametre().get(ICaisseReportHelper.PARAM_SIGNATURE_DATA) + " "
                        + formatDate(getDateExecution()));
    }

    /**
     * getter pour l'attribut date situation compte.
     * 
     * @return la valeur courante de l'attribut date situation compte
     */
    public String getDateSituationCompte() {
        return dateSituationCompte;
    }

    @Override
    public String getDescriptionExtraitCompte(CAExtraitCompte extraitCompte) {

        StringBuffer descriptionExtraitCompte = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(extraitCompte.getLibelleExtraitCompte())) {
            descriptionExtraitCompte.append(getSession().getCodeLibelle(extraitCompte.getLibelleExtraitCompte()));
        } else {
            if (extraitCompte.getIdTypeOperation().equalsIgnoreCase(APIOperation.CAECRITURE)
                    || extraitCompte.getIdTypeOperation().equalsIgnoreCase(APIOperation.CAAUXILIAIRE)) {
                descriptionExtraitCompte.append(extraitCompte.getRubrique().getDescription(getLangue()));
            } else {
                descriptionExtraitCompte.append(getTypeOperationDescriptionFromCache(
                        extraitCompte.getIdTypeOperation(), getLangue()));
            }
        }

        descriptionExtraitCompte.append(" ");
        descriptionExtraitCompte.append(formatMessage(
                new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 90)),
                new Object[] { formatDate(extraitCompte.getDate()),
                        getSession().getCodeLibelle(extraitCompte.getProvenancePmt()), "" }));

        return descriptionExtraitCompte.toString();
    }

    /**
     * @return DOCUMENT ME!
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public boolean next() throws FWIException {
        switch (state) {
            case STATE_IDLE:

                // on commence ou a deja traité un contentieux, on regarde s'il
                // reste des contentieux a traiter
                if (super.next()) {
                    state = CO06DemandeMainlevee.STATE_DEMANDE;

                    // on va créer la demande
                    return true;
                } else {
                    // il n'y a plus de documents à créer
                    return false;
                }

            case STATE_DEMANDE:

                // on vient de créer la demande, on va créer la confirmation
                state = CO06DemandeMainlevee.STATE_SITUATION;

                return true;

            default:

                // on regarder si on encore des contentieux à traiter.
                state = CO06DemandeMainlevee.STATE_IDLE;

                return next();
        }
    }

    /**
     * setter pour l'attribut date situation compte.
     * 
     * @param dateSituationCompte
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSituationCompte(String dateSituationCompte) {
        this.dateSituationCompte = dateSituationCompte;
    }
}
