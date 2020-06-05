package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COEtapeInfoConfigManager;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * <p>
 * .
 * </p>
 *
 * @author Alexandre Cuva, 18-aug-2004
 */
public class CO14RequisitionDeVente extends CODocumentRequisition {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0036GCO";
    private static final long serialVersionUID = 6441489594568606636L;
    private static final String TEMPLATE_NAME = "CO_REQUISITION";

    private String observation = "";
    private FWCurrency montantCreanceNette = new FWCurrency(0);

    private LinkedList<Map<String, String>> listDataSourceCreances = new LinkedList<Map<String, String>>();
    private List<Map<String, String>> listCreancesApresRP = new LinkedList<Map<String, String>>();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO14RequisitionDeVente.
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CO14RequisitionDeVente() throws Exception {
    }

    /**
     * Initialise le document.
     *
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO14RequisitionDeVente(BSession parent) throws FWIException {
        super(parent, parent.getLabel("AQUILA_RDV"));
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setTemplateFile(CO14RequisitionDeVente.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RDV"));
        setNumeroReferenceInforom(CO14RequisitionDeVente.NUMERO_REFERENCE_INFOROM);
    }

    @Override
    public String getJasperTemplate() {
        return null;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    private String setParametersEnCommun() throws Exception {
        // -- zone en-tête du document
        // --------------------------------------------------------------------
        this.setParametres(COParameter.T1, getCatalogueTextesUtil().texte(getParent(), 1, 1));

        StringBuilder body = new StringBuilder();

        getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n");

        /*
         * formater l'en-tete, les conventions de remplacement pour les lignes de l'en-tete sont: {0} = no série {1}
         * = no poursuite {2} = date réception
         */
        String noSerie = "";
        String dateReception = "";
        String typeSaisie = "";
        COEtapeInfoConfigManager etapeInfoConfigManager = new COEtapeInfoConfigManager();

        etapeInfoConfigManager.setForIdEtape(getTransition().getEtapeSuivante().getIdEtape());
        etapeInfoConfigManager.setSession(getSession());
        etapeInfoConfigManager.find();

        for (int id = 0; id < etapeInfoConfigManager.size(); ++id) {
            COEtapeInfoConfig etapeInfoConfig = (COEtapeInfoConfig) etapeInfoConfigManager.get(id);

            if (COEtapeInfoConfig.CS_DATE_RECEPTION_RDV.equals(etapeInfoConfig.getCsLibelle())) {
                dateReception = (String) getIdEtapeInfoConfigToValeur().get(etapeInfoConfig.getIdEtapeInfoConfig());
            } else if (COEtapeInfoConfig.CS_NO_SERIE_RDV.equals(etapeInfoConfig.getCsLibelle())) {
                noSerie = (String) getIdEtapeInfoConfigToValeur().get(etapeInfoConfig.getIdEtapeInfoConfig());
            } else if (COEtapeInfoConfig.CS_TYPE_SAISIE.equals(etapeInfoConfig.getCsLibelle())) {
                // Retrouve le type de saisie
                typeSaisie = (String) getIdEtapeInfoConfigToValeur().get(etapeInfoConfig.getIdEtapeInfoConfig());
            }

        }

        this.setParametres(
                COParameter.T2,
                formatMessage(body,
                        new Object[]{noSerie, curContentieux.getNumPoursuite(), formatDate(dateReception)}));
        this.setParametres(COParameter.T3, getCatalogueTextesUtil().texte(getParent(), 3, 1));

        this.setParametres(COParameter.T5, getCatalogueTextesUtil().texte(getParent(), 3, 4));
        this.setParametres(COParameter.ADRESSE,
                getAdressePrincipaleEnvoiOP(curContentieux.getCompteAnnexe().getTiers()));
        this.setParametres(
                COParameter.T6,
                formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 3, 5)), new Object[] {
                        curContentieux.getCompteAnnexe().getRole().getDescription(getLangue()),
                        curContentieux.getCompteAnnexe().getIdExterneRole()}));
        this.setParametres(
                COParameter.T7,
                formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 3, 6)),
                        new Object[]{this._getProperty(CODocumentManager.JASP_PROP_BODY_NOM_ADRESSE_CAISSE)}));
        this.setParametres(
                COParameter.T8,
                formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 3, 7)),
                        new Object[]{getNumeroCCP()}));

        this.setParametres(COParameter.HEADER1, getCatalogueTextesUtil().texte(getParent(), 4, 1));

        this.setParametres(COParameter.P_DEVISE, getCatalogueTextesUtil().texte(getParent(), 4, 2));

        return typeSaisie;
    }

    /**
     *
     * @param typeSaisie
     * @param montantCreance
     * @throws Exception
     */
    private void ancienRegime(String[] infoSection, String typeSaisie) throws Exception {

        String dateInteret = CORequisitionPoursuiteUtil.getDateDebutInteretsTardifs(getSession(), getTransaction(),
                curContentieux);

        this.setParametres(
                COParameter.L_MONTANT,
                formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 9, 95)),
                        new Object[]{formatDate(dateInteret)}));

        String montantCreance = CORequisitionPoursuiteUtil.getMontantCreanceSoumis(getTransaction(), curContentieux);

        String[] datePeriodes = JadePropertiesService.getInstance().getProperty("aquila.tauxInteret.pandemie.periodes").split(":");
        if (datePeriodes.length == 2) {
            String dateDebut = datePeriodes[0];
            String dateFin = datePeriodes[1];
            String dateExecutionRP = CORequisitionPoursuiteUtil.getDateExecutionRP(getSession(), curContentieux);
            if (JadeDateUtil.isDateBefore(dateExecutionRP, dateDebut) || JadeDateUtil.isDateAfter(dateExecutionRP, dateFin)) {
                if (!JadeStringUtil.isBlankOrZero(montantCreance)) {
                    this.setParametres(
                            COParameter.L_INTERET,
                            formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 9, 95)), new Object[]{
                                    formatDate(dateInteret), getCatalogueTextesUtil().texte(getParent(), 4, 2),
                                    formatMontant(montantCreance)}));
                }
            } else {
                if (!JadeStringUtil.isBlankOrZero(montantCreance)) {
                    this.setParametres(
                            COParameter.L_INTERET,
                            formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 10, 1)), new Object[]{
                                    formatDate(dateInteret), getCatalogueTextesUtil().texte(getParent(), 4, 2),
                                    formatMontant(montantCreance)}));
                }

            }
        } else {
            if (!JadeStringUtil.isBlankOrZero(montantCreance)) {
                this.setParametres(
                        COParameter.L_INTERET,
                        formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 9, 95)), new Object[]{
                                formatDate(dateInteret), getCatalogueTextesUtil().texte(getParent(), 4, 2),
                                formatMontant(montantCreance)}));
            }
        }

        this.setParametres(COParameter.L_SECTION_DESCR, curContentieux.getSection().getDescription(getLangue()) + " ("
                + curContentieux.getSection().getIdExterne() + ")");

        this.setParametres(COParameter.P_MONTANT, formatMontant(montantCreance));
        this.setParametres(COParameter.P_BASE, formatMontant(infoSection[0]));

        List dataSource = createSituationCompteDS(infoSection[2], COParameter.F1, COParameter.F2, COParameter.F3,
                getCatalogueTextesUtil().texte(getParent(), 4, 2), ICOEtape.CS_REQUISITION_DE_VENTE_SAISIE,
                infoSection[1]);

        // lignes pour nouveaux frais
        addTaxesToDS(dataSource, COParameter.F1, COParameter.F2, COParameter.F3,
                getCatalogueTextesUtil().texte(getParent(), 4, 2));

        if (!dataSource.isEmpty()) {
            this.setDataSource(dataSource);
        }

        initFooter(typeSaisie);
    }

    /**
     * Supression de l'emplacement de la liste
     */
    private void removeFromListDSCreances(Integer index) {
        // Pour enlever de la liste, nous settons l'emplacement de l'objet a null et nous faisons un remove(null)
        // afin que le linkedList efface toutes les cases ayant une valeur a null
        listDataSourceCreances.set(index, null);
        listDataSourceCreances.remove(null);
    }

    public Integer getIndexDSCreancesSearchNatureRubrique(String valueToSearch) {
        Integer index = null;

        for (int i = 0; i < listDataSourceCreances.size(); i++) {
            Map<String, String> object = listDataSourceCreances.get(i);

            String valueOfObject = object.get(CARubrique.FIELD_NATURERUBRIQUE);

            if (valueOfObject.equalsIgnoreCase(valueToSearch)) {
                index = i;
            }
        }

        return index;
    }

    /**
     * Repartit le montant de base sur les créances positives
     *
     * @throws Exception
     */
    private void repartitionMontantCreanceNegatif() throws Exception {
        // Si le montant de base est negatif, nous allons essayer de déduire les creances et d'arriver a un
        // montant de base a zero
        FWCurrency montantAJauger = new FWCurrency(montantCreanceNette.toString());
        if (montantCreanceNette.isNegative()) {

            // Supression de la liste des creances pour ne pas déduire les interets avec le montant de base si négatif
            Integer indexInterets = getIndexDSCreancesSearchNatureRubrique(APIRubrique.INTERETS_MORATOIRES);
            Map<String, String> interets = null;
            if (indexInterets != null) {
                interets = listDataSourceCreances.get(indexInterets);
                removeFromListDSCreances(indexInterets);
            }

            montantAJauger.abs();

            for (Map<String, String> value : listDataSourceCreances) {
                // Si le montant de base est à zéro
                if (montantCreanceNette.isZero() == true) {
                    break;
                }

                FWCurrency montantLigne = new FWCurrency(value.get(COParameter.F2));

                if (montantAJauger.compareTo(new FWCurrency(value.get(COParameter.F2))) != 1) {
                    montantCreanceNette.add(montantAJauger);
                    montantLigne.sub(montantAJauger);
                    montantAJauger.sub(montantAJauger);
                    value.put(COParameter.F2, montantLigne.toString());
                } else {
                    montantAJauger.sub(montantLigne);
                    montantCreanceNette.add(montantLigne);
                    montantLigne = new FWCurrency(0);
                    value.put(COParameter.F2, montantLigne.toString());
                }
            }

            // Nous remettons les interets tardifs dans la liste
            if (interets != null) {
                listDataSourceCreances.addLast(interets);
            }
        }
    }

    /**
     *
     * @param typeSaisie
     * @param montantCreance
     * @throws Exception
     */
    private void nouveauRegime(String[] infoSection, String typeSaisie) throws Exception {
        this.setParametres(COParameter.L_SECTION_DESCR, "1) " + curContentieux.getSection().getDescription(getLangue())
                + " (" + curContentieux.getSection().getIdExterne() + ")");

        // Récupération des lignes de la partie CREANCE
        listDataSourceCreances = createSituationCompteDSNouveauRegimePourRCPetRV(infoSection[2],
                CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(), curContentieux), COParameter.F1,
                COParameter.F2, COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 4, 2),
                ICOEtape.CS_REQUISITION_DE_VENTE_SAISIE, infoSection[1]);

        // Déduction des paiements/compensations sur le montant de base de la créance
        montantCreanceNette = new FWCurrency(infoSection[0]);
        montantCreanceNette.add(getMontantTotalPaiement());

        if (!listDataSourceCreances.isEmpty()) {
            // Repartir le montant de base negatif sur les autres creances positives
            repartitionMontantCreanceNegatif();

            // les créances survenu après la date d'exécution de la RP
            listCreancesApresRP = createSituationCompteDSApresDateRP(
                    CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(), curContentieux), true,
                    COParameter.F1, COParameter.F2, COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 4, 2),
                    ICOEtape.CS_REQUISITION_DE_VENTE_SAISIE, infoSection[1]);

            // Ajout ou suppression des créances dans la liste DataSource
            verificationLignesCreances();

            // lignes pour nouveaux frais
            addTaxesToDS(listDataSourceCreances, COParameter.F1, COParameter.F2, COParameter.F3,
                    getCatalogueTextesUtil().texte(getParent(), 4, 2));

            List<Map<String, String>> liste = new ArrayList<Map<String, String>>();
            // Nous mettons les lignes des creances dans la datasource
            if (!listDataSourceCreances.isEmpty()) {
                // Information nouveaux frais
                liste.addAll(listDataSourceCreances.subList(0, listDataSourceCreances.size()));

                this.setDataSource(listDataSourceCreances);
            }
        }

        this.setParametres(COParameter.P_BASE, formatMontant(montantCreanceNette.toString()));

        String montantCreance = CORequisitionPoursuiteUtil.getMontantCreanceSoumis(getTransaction(), curContentieux,
                CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(), curContentieux));


        if (!JadeStringUtil.isBlankOrZero(montantCreance)) {
            String[] datePeriodes = JadePropertiesService.getInstance().getProperty("aquila.tauxInteret.pandemie.periodes").split(":");
            if (datePeriodes.length == 2) {
                String dateDebut = datePeriodes[0];
                String dateFin = datePeriodes[1];
                String dateExecutionRP = CORequisitionPoursuiteUtil.getDateExecutionRP(getSession(), curContentieux);
                if (JadeDateUtil.isDateBefore(dateExecutionRP, dateDebut) || JadeDateUtil.isDateAfter(dateExecutionRP, dateFin)) {
                    this.setParametres(
                            COParameter.L_INTERET,
                            formatMessage(
                                    new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 9, 95)),
                                    new Object[]{
                                            formatDate(CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(),
                                                    curContentieux)), getCatalogueTextesUtil().texte(getParent(), 4, 2),
                                            formatMontant(montantCreance)}));
                } else {
                    this.setParametres(
                            COParameter.L_INTERET,
                            formatMessage(
                                    new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 10, 1)),
                                    new Object[]{
                                            formatDate(CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(),
                                                    curContentieux)), getCatalogueTextesUtil().texte(getParent(), 4, 2),
                                            formatMontant(montantCreance)}));
                }
            } else {
                this.setParametres(
                        COParameter.L_INTERET,
                        formatMessage(
                                new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 9, 95)),
                                new Object[]{
                                        formatDate(CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(),
                                                curContentieux)), getCatalogueTextesUtil().texte(getParent(), 4, 2),
                                        formatMontant(montantCreance)}));
            }
        }
        initFooter(typeSaisie);
    }

    /**
     * Ajoute ou supprime les créances dans le listDatasourceCreances. Réordonne les points.
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    private void verificationLignesCreances () throws Exception {
        int numeroteur = 2;

        // Suppression des intérêts dans la liste pour l'ordonner après
        Integer indexInterets = getIndexDSCreancesSearchNatureRubrique(APIRubrique.INTERETS_MORATOIRES);
        Map<String, String> interets = null;
        if (indexInterets != null) {
            interets = listDataSourceCreances.get(indexInterets);
            removeFromListDSCreances(indexInterets);
        }

        // Suppression de la sommation dans la liste pour l'ordonner après
        Integer indexSommation = getIndexDSCreancesSearchNatureRubrique(APIRubrique.TAXE_SOMMATION);
        Map<String, String> taxeSommation = null;
        if (indexSommation != null) {
            taxeSommation = listDataSourceCreances.get(indexSommation);
            FWCurrency montantTaxeSom = new FWCurrency(taxeSommation.get(COParameter.F2));
            // Si la taxe de sommation a été déduite avec le montant de base et reste a Zero, on ne
            // l'affiche pas
            if (montantTaxeSom.isZero() == false) {
                taxeSommation.put(COParameter.F1, numeroteur + ") " + taxeSommation.get(COParameter.F1));
                numeroteur++;
            } else {
                taxeSommation = null;
            }
            removeFromListDSCreances(indexSommation);
        }

        // Liste toutes les créances (SANS sommation et intérêts)
        for (Map<String, String> value : listDataSourceCreances) {
            FWCurrency montantLigne = new FWCurrency(value.get(COParameter.F2));
            if (montantLigne.isZero() == false) {
                value.put(COParameter.F1, numeroteur + ") " + value.get(COParameter.F1));
                numeroteur++;
            }
        }

        // La taxe de sommation doit toujours être en 2eme lignes si y'en a une.
        if (taxeSommation != null && taxeSommation.isEmpty() == false) {
            listDataSourceCreances.addFirst(taxeSommation);
        }

        // Les intérêts doivent toujours être a la fin des lignes de créances normales
        if (interets != null && interets.size() > 0) {
            FWCurrency montantInterets = new FWCurrency(interets.get(COParameter.F2));
            // Si la taxe de sommation a été déduite avec le montant de base et reste a Zero, on ne
            // l'affiche pas
            if (montantInterets.isZero() == false) {
                interets.put(
                        COParameter.F1,
                        numeroteur
                                + ") "
                                + formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 9, 97)),
                                new Object[]{formatDate(CORequisitionPoursuiteUtil.getDateExecutionRP(
                                        getSession(), curContentieux))}));
                listDataSourceCreances.addLast(interets);
                numeroteur++;
            }
        }

        // Ajout des lignes des créances après la date du RP
        for (Map<String, String> value : listCreancesApresRP) {
            FWCurrency montantLigne = new FWCurrency(value.get(COParameter.F2));
            if (montantLigne.isZero() == false) {
                value.put(COParameter.F1, numeroteur + ") " + value.get(COParameter.F1));
                listDataSourceCreances.add(value);
                numeroteur++;
            }
        }
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource () throws Exception {
        try {
            // ////////////////////////////////
            // LES POINTS EN COMMUN ----------
            initOfficePoursuite(getParent(), 3, 2, 3, 3);

            this._handleHeaders(null, curContentieux, true, false, true);

            String typeSaisie = setParametersEnCommun();

            String[] infoSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(),
                    curContentieux.getIdSection());
            // --------------------------------
            // /////////////////////////////////

            if (CORequisitionPoursuiteUtil.isNouveauRegimeSelonDateRP(getSession(), curContentieux) == true
                    && CORequisitionPoursuiteUtil.isOfficeDontWantToUseNewRegime(getSession(), cantonOfficePoursuite) == false) {
                nouveauRegime(infoSection, typeSaisie);
            } else {
                ancienRegime(infoSection, typeSaisie);
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * @return the observation
     */
    public String getObservation () {
        return observation;
    }

    /**
     * pied de page
     *
     * @param dateReceptionCDP
     */
    private void initFooter (String typeSaisie){
        // -- pied de page
        // --------------------------------------------------------------------------
        StringBuilder body = new StringBuilder();
        body.setLength(0);
        getCatalogueTextesUtil().dumpNiveau(getParent(), 5, body, "\n");

        COHistorique historique = initHistorique();

        String annexeMainlevee = "";
        try {
            COHistorique historiqueMainlevee;
            historiqueMainlevee = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                    ICOEtape.CS_DEMANDE_DE_MAINLEVEE_ENVOYEE);
            if (historiqueMainlevee != null) {
                annexeMainlevee = formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 9, 91)),
                        new Object[]{getTaxesMainlevee(), getCatalogueTextesUtil().texte(getParent(), 4, 2)});
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
            abort();
        }

        StringBuffer observation = new StringBuffer(getObservation());
        observation.append(getCatalogueTextesUtil().texte(getParent(), 1, 1));

        /*
         * formater l'en-tete, les conventions de remplacement pour les lignes de l'en-tete sont : <br/> {0} = retour
         * chariot <br/> {1} = numéro de poursuite <br/> {2} = numéro section <br/> {3} = numéro compte annexe <br/> {4}
         * = date section <br/> {5} = description de la section <br/> {6} = observation <br/> {7} = type de saisie <br/>
         * {8} = annexe mainlevée <br/> {9} = etape de sommation/décision <br/> {10} = date d'éxcution de la
         * sommtion/décision <br/>
         */
        this.setParametres(
                COParameter.T12,
                formatMessage(body,
                        new Object[]{"\n", curContentieux.getNumPoursuite(),
                                curContentieux.getSection().getIdExterne(),
                                curContentieux.getSection().getCompteAnnexe().getIdExterneRole(),
                                formatDate(curContentieux.getSection().getDateSection()),
                                curContentieux.getSection().getDescription(_getLangue()), observation,
                                getSession().getCodeLibelle(typeSaisie), annexeMainlevee,
                                (historique == null ? "" : historique.getEtape().getLibEtapeLibelle()),
                                (historique == null ? "" : formatDate(historique.getDateExecution())),
                                giveLibelleInfoRom246()}));

        // Rajoute la date à la signature
        this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_DATA,
                getImporter().getParametre().get(ICaisseReportHelper.PARAM_SIGNATURE_DATA) + " "
                        + formatDate(getDateExecution()));
    }

    /**
     * @return
     * @throws Exception
     */
    private COHistorique initHistorique () {
        // Contrôler si ADB déjà effectuer, si oui on récupères les infos de
        // l'ADB (n° d'ADB)
        COHistorique historique = null;

        // Charge l'historique de la décision
        try {
            historique = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux, ICOEtape.CS_DECISION);
            // S'il n'y a pas de décision, on va chercher l'historique de la
            // sommation
            if (historique == null) {
                historique = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                        ICOEtape.CS_SOMMATION_ENVOYEE);
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }

        return historique;
    }

    /**
     * @param observation
     *            the observation to set
     */
    public void setObservation (String observation){
        this.observation = observation;
    }
}
