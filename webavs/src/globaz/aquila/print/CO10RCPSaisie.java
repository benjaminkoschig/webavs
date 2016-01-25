package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.process.interetmanuel.CAProcessInteretMoratoireManuel;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
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
public class CO10RCPSaisie extends CODocumentRequisition {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0031GCO";
    private static final long serialVersionUID = -6849158323191360306L;
    private static final String TEMPLATE_NAME = "CO_REQUISITION";
    private String dateDecisionMainLevee = "";
    private String dateJugementMainLevee = "";
    private String decisionMainLevee = "";

    private String jugementMainLevee = "";

    private String observation = "";
    private FWCurrency montantCreanceNette = new FWCurrency(0);

    private LinkedList<Map<String, String>> listDataSourceCreances = new LinkedList<Map<String, String>>();
    private List<Map<String, String>> listCreancesApresRP = new LinkedList<Map<String, String>>();

    /**
     * Crée une nouvelle instance de la classe CO10RCPSaisie.
     * 
     * @throws Exception DOCUMENT ME!
     */
    public CO10RCPSaisie() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent La session parente
     * @throws FWIException En cas de problème d'initialisaion
     */
    public CO10RCPSaisie(BSession parent) throws FWIException {
        super(parent, parent.getLabel("AQUILA_RCP"));
    }

    /**
     * @throws FWIException DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setTemplateFile(CO10RCPSaisie.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RCP"));
        setNumeroReferenceInforom(CO10RCPSaisie.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * 
     * @param dateNotificationCDP
     * @throws Exception
     */
    private void setParametersEnCommun(String dateNotificationCDP) throws Exception {
        // -- zone en-tête du document
        // --------------------------------------------------------------------
        this.setParametres(COParameter.T1, getCatalogueTextesUtil().texte(getParent(), 1, 1));

        /*
         * formater l'en-tete, les conventions de remplacement pour les lignes de l'en-tete sont: {0} = no poursuite
         * {1} = date notification
         */
        COHistorique historiqueADB = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                ICOEtape.CS_ACTE_DE_DEFAUT_DE_BIEN_SAISI);
        if (historiqueADB != null) {
            COEtapeInfo etapeInfoADB = historiqueADB.loadEtapeInfo(COEtapeInfoConfig.CS_NUMERO_ADB);
            this.setParametres(
                    COParameter.T2,
                    formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 2, 2)),
                            new Object[] { etapeInfoADB != null ? etapeInfoADB.getValeur() : "" }));
        } else {
            this.setParametres(
                    COParameter.T2,
                    formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 2, 1)), new Object[] {
                            curContentieux.getNumPoursuite(), dateNotificationCDP }));
        }

        this.setParametres(COParameter.T3, getCatalogueTextesUtil().texte(getParent(), 3, 1));

        this.setParametres(COParameter.T5, getCatalogueTextesUtil().texte(getParent(), 3, 4));
        this.setParametres(COParameter.ADRESSE,
                getAdressePrincipaleEnvoiOP(curContentieux.getCompteAnnexe().getTiers()));
        this.setParametres(
                COParameter.T6,
                formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 3, 5)), new Object[] {
                        curContentieux.getCompteAnnexe().getRole().getDescription(getLangue()),
                        curContentieux.getCompteAnnexe().getIdExterneRole() }));
        this.setParametres(
                COParameter.T7,
                formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 3, 6)),
                        new Object[] { this._getProperty(CODocumentManager.JASP_PROP_BODY_NOM_ADRESSE_CAISSE) }));
        this.setParametres(
                COParameter.T8,
                formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 3, 7)),
                        new Object[] { getNumeroCCP() }));

        this.setParametres(COParameter.HEADER1, getCatalogueTextesUtil().texte(getParent(), 4, 1));

        this.setParametres(COParameter.P_DEVISE, getCatalogueTextesUtil().texte(getParent(), 4, 2));
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public List<CAInteretManuelVisualComponent> getDetailInterets() throws Exception {
        CAProcessInteretMoratoireManuel process = new CAProcessInteretMoratoireManuel();
        process.setSession(getSession());
        process.setDateFin(getDateExecution());
        process.setIdSection(curContentieux.getIdSection());
        process.setSimulationMode(false);
        process.executeProcess();

        return process.getVisualComponents();
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
     * Ajoute ou supprime les créances dans le listDatasourceCreances. Réordonne les points.
     * 
     * @param dataSource
     * @return
     * @throws Exception
     */
    private void verificationLignesCreances() throws Exception {
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
            if (montantInterets.isZero() == false) {
                interets.put(
                        COParameter.F1,
                        numeroteur
                                + ") "
                                + formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 97)),
                                        new Object[] { formatDate(CORequisitionPoursuiteUtil.getDateExecutionRP(
                                                getSession(), curContentieux)) }));
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
    public void createDataSource() throws Exception {
        try {
            // ////////////////////////////////
            // LES POINTS EN COMMUN ----------
            initOfficePoursuite(getParent(), 3, 2, 3, 3);

            this._handleHeaders(null, curContentieux, true, false, true);

            String dateNotificationCDP = formatDate(getDateNotificationCDP());

            String[] infoSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(),
                    curContentieux.getIdSection());

            setParametersEnCommun(dateNotificationCDP);
            // --------------------------------
            // /////////////////////////////////

            if (CORequisitionPoursuiteUtil.isNouveauRegimeSelonDateRP(getSession(), curContentieux) == true
                    && CORequisitionPoursuiteUtil.isOfficeDontWantToUseNewRegime(getSession(), cantonOfficePoursuite) == false) {
                nouveauRegime(infoSection, dateNotificationCDP);
            } else {
                ancienRegime(infoSection, dateNotificationCDP);
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    private void nouveauRegime(String[] infoSection, String dateNotificationCDP) throws Exception {
        this.setParametres(COParameter.L_SECTION_DESCR, "1) " + curContentieux.getSection().getDescription(getLangue())
                + " (" + curContentieux.getSection().getIdExterne() + ")");

        // Récupération des lignes de la partie CREANCE
        listDataSourceCreances = createSituationCompteDSNouveauRegimePourRCPetRV(infoSection[2],
                CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(), curContentieux), COParameter.F1,
                COParameter.F2, COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 4, 2),
                ICOEtape.CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE, infoSection[1]);

        // Déduction des paiements/compensations sur le montant de base de la créance
        montantCreanceNette = new FWCurrency(infoSection[0]);
        montantCreanceNette.add(getMontantTotalPaiement());

        if (!listDataSourceCreances.isEmpty()) {
            // Repartir le montant de base negatif sur les autres creances positives (sauf interets tardifs)
            repartitionMontantCreanceNegatif();

            // les créances survenu après la date d'exécution de la RP
            listCreancesApresRP = createSituationCompteDSApresDateRP(
                    CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(), curContentieux), true,
                    COParameter.F1, COParameter.F2, COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 4, 2),
                    ICOEtape.CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE, infoSection[1]);

            // Ajout ou suppression des créances dans la liste DataSource
            verificationLignesCreances();

            List<Map<String, String>> liste = new ArrayList<Map<String, String>>();
            // Nous mettons les lignes des creances dans la datasource
            if (!listDataSourceCreances.isEmpty()) {
                // Information nouveaux frais
                liste.addAll(listDataSourceCreances.subList(0, listDataSourceCreances.size()));

                addTaxesToDS(listDataSourceCreances, COParameter.F1, COParameter.F2, COParameter.F3,
                        getCatalogueTextesUtil().texte(getParent(), 4, 2));
                this.setDataSource(liste);
            }
        }

        this.setParametres(COParameter.P_BASE, formatMontant(montantCreanceNette.toString()));

        String montantCreance = CORequisitionPoursuiteUtil.getMontantCreanceSoumis(getTransaction(), curContentieux,
                CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(), curContentieux));

        if (!JadeStringUtil.isBlankOrZero(montantCreance)) {
            this.setParametres(
                    COParameter.L_INTERET,
                    formatMessage(
                            new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 95)),
                            new Object[] {
                                    formatDate(CORequisitionPoursuiteUtil.getDateExecutionRPPlus1Day(getSession(),
                                            curContentieux)), getCatalogueTextesUtil().texte(getParent(), 4, 2),
                                    formatMontant(montantCreance) }));
        }

        // Affichage des lignes dans Titre de la creances et autres
        initFooter(dateNotificationCDP);
    }

    /**
     * Ancienne façon de faire pour la Requisition de continuer la poursuite
     * 
     * @param infoSection
     * @param dateInteret
     * @param dateNotificationCDP
     * @throws Exception
     */
    private void ancienRegime(String[] infoSection, String dateNotificationCDP) throws Exception {
        String montantCreance = CORequisitionPoursuiteUtil.getMontantCreanceSoumis(getTransaction(), curContentieux);

        String dateInteret = CORequisitionPoursuiteUtil.getDateDebutInteretsTardifs(getSession(), getTransaction(),
                curContentieux);

        this.setParametres(COParameter.L_SECTION_DESCR, curContentieux.getSection().getDescription(getLangue()) + " ("
                + curContentieux.getSection().getIdExterne() + ")");

        if (!JadeStringUtil.isBlankOrZero(montantCreance)) {
            this.setParametres(
                    COParameter.L_INTERET,
                    formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 95)), new Object[] {
                            formatDate(dateInteret), getCatalogueTextesUtil().texte(getParent(), 4, 2),
                            formatMontant(montantCreance) }));
        }

        this.setParametres(COParameter.P_BASE, formatMontant(infoSection[0]));

        // Les créances du bloc Creances
        List<Map<String, String>> dataSource = createSituationCompteDS(infoSection[2], COParameter.F1, COParameter.F2,
                COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 4, 2),
                ICOEtape.CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE, infoSection[1]);

        if (!dataSource.isEmpty()) {
            // Information nouveaux frais
            addTaxesToDS(dataSource, COParameter.F1, COParameter.F2, COParameter.F3,
                    getCatalogueTextesUtil().texte(getParent(), 4, 2));
            this.setDataSource(dataSource);
        }

        // Les informations pour le bloc Titre de la créances
        // Les informations pour le bas de page
        initFooter(dateNotificationCDP);
    }

    public String getDateDecisionMainLevee() {
        return dateDecisionMainLevee;
    }

    public String getDateJugementMainLevee() {
        return dateJugementMainLevee;
    }

    /**
     * @return la date du commandement de payer avec opposition s'il y a sinon sans opposition.
     * @throws Exception
     */
    private String getDateNotificationCDP() throws Exception {
        // retrouver l'historique de la saisie du CDP
        COHistorique historiqueCDP = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION);
        COHistorique historiqueCDPAvec = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION);
        if (historiqueCDPAvec != null) {
            if ((historiqueCDP == null)
                    || (historiqueCDP.getSpy().getTimeStamp() < historiqueCDPAvec.getSpy().getTimeStamp())) {
                historiqueCDP = historiqueCDPAvec;
            }
        }

        if (historiqueCDP == null) {
            this.log(getSession().getLabel("AQUILA_IMPOSSIBLE_DE_TROUVER_HISTORIQUE_DU_CDP"));
        }

        // la date de notification du CDP
        COEtapeInfo etapeInfo = historiqueCDP.loadEtapeInfo(COEtapeInfoConfig.CS_DATE_NOTIFICATION_CDP);
        if (etapeInfo == null) {
            this.log(getSession().getLabel("AQUILA_IMPOSSIBLE_DE_TROUVER_DATE_NOTIFICATION_CDP"));
        }
        return etapeInfo != null ? etapeInfo.getValeur() : "";
    }

    public String getDecisionMainLevee() {
        return decisionMainLevee;
    }

    public String getJugementMainLevee() {
        return jugementMainLevee;
    }

    public String getObservation() {
        return observation;
    }

    /**
     * 
     * @return
     */
    private String initAnnexes() {
        String valueAnnexe = "";

        // ANNEXE JUGEMENT DE MAINLEVEE
        try {
            COHistorique historiqueMainlevee;

            // Va rechercher si une étape jugement de mainlevée a été saisie
            historiqueMainlevee = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                    ICOEtape.CS_JUGEMENT_DE_MAINLEVEE_SAISI);

            if (historiqueMainlevee != null) {
                // Si il y a une taxe de mainlevée nous insérons le montant dans l'annexe
                if (!JadeStringUtil.isBlankOrZero(getTaxesMainlevee())) {
                    valueAnnexe = formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 91)),
                            new Object[] { getTaxesMainlevee(), getCatalogueTextesUtil().texte(getParent(), 4, 2) });
                } else {
                    // Autrement nous affichons uniquement son texte
                    valueAnnexe = formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 9, 91)),
                            new Object[] { "", "" });
                }
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
            abort();
        }

        // INFOROM 447 - Ajout de la plausibilité d'un annexe du retrait d'opposition pour la RCP
        // ANNEXE RETRAIT OPPOSITION
        try {
            COHistorique historiqueRetraitOpp;

            // Va rechercher si une étape reception ROPP a été saisie
            historiqueRetraitOpp = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                    ICOEtape.CS_RECEPTION_RETRAIT_D_OPPOSITION_SAISIE);

            if (historiqueRetraitOpp != null) {
                // Si le txtAnnexe est rempli avec la mainlevée et qu il y a un retrait d opposition saisi, c'est qu il
                // y a un probleme car il ne peut y avoir qu un des deux saisie en même temps
                if (!JadeStringUtil.isBlankOrZero(valueAnnexe)) {
                    throw new Exception("it can't be a withdrawal of opposition AND judgment release in the same time");
                }
                valueAnnexe = getCatalogueTextesUtil().texte(getParent(), 9, 92);
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
            abort();
        }

        return valueAnnexe;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer initObservation() throws Exception {
        StringBuffer observation = new StringBuffer(getObservation());

        if (!JadeStringUtil.isEmpty(getJugementMainLevee()) && !JadeStringUtil.isEmpty(getDateJugementMainLevee())) {
            observation.append(getSession().getApplication().getLabel(getJugementMainLevee(), getLangue()) + " ");
            observation.append(getDateJugementMainLevee() + "\n");
        }

        if (!JadeStringUtil.isEmpty(getDecisionMainLevee()) && !JadeStringUtil.isEmpty(getDateDecisionMainLevee())) {
            observation.append(getSession().getApplication().getLabel(getDecisionMainLevee(), getLangue()) + " ");
            observation.append(getDateDecisionMainLevee() + "\n");
        }
        observation.append(getCatalogueTextesUtil().texte(getParent(), 1, 1));

        return observation;
    }

    /**
     * Initialise le corps du bas de page et le pied de page
     * 
     * @param dateNotificationCDP la date de la notification du CDP
     * @throws Exception l'exception levée
     */
    private void initFooter(String dateNotificationCDP) throws Exception {
        StringBuffer body = new StringBuffer();
        body.setLength(0);
        getCatalogueTextesUtil().dumpNiveau(getParent(), 6, body, "\n");

        // Informations sur l'étape de sommation/Décision provenant de l'historique.
        COHistorique historique = initHistorique();
        String etapeLibelle = "";
        String dateExecution = "";
        if (historique != null) {
            etapeLibelle = historique.getEtape().getLibEtapeLibelle();
            dateExecution = formatDate(historique.getDateExecution());
        }

        // Texte sur les observations affichée sur le document comprenant les écritures des jugement de mainlevée
        StringBuffer observation = initObservation();

        // INFOROM 427 - Ajout de la plausibilité d'un annexe du retrait d'opposition pour la RCP
        // Nous faisons un test si le texte des annexes doit être affiché
        // Si il y a eu une reception de retrait d'opposition saisi ou un jugement de mainlevée saisi
        // nous affichons les annexes, autrement nous masquons le texte des annexes dans le document.
        String valueAnnexe = initAnnexes();

        StringBuilder titreAnnexe = new StringBuilder();
        if (!JadeStringUtil.isBlankOrZero(valueAnnexe)) {
            titreAnnexe.append("\n").append(getCatalogueTextesUtil().texte(getParent(), 7, 1));
        } else {
            titreAnnexe.append("");
        }

        /*
         * formater l'en-tete, les conventions de remplacement pour les lignes de l'en-tete sont : <br/> {0} = retour
         * chariot <br/> {1} = numéro section <br/> {2} = numéro compte annexe <br/> {3} = date section <br/> {4} = date
         * notification CDP <br/> {5} = observation <br/> {6} = annexe <br/> {7} = etape de sommation/décision <br/> {8}
         * = date d'éxcution de la sommtion/décision <br/> {9} = Description de la section
         */
        this.setParametres(
                COParameter.T12,
                formatMessage(body.append(titreAnnexe),
                        new Object[] { "\n", curContentieux.getSection().getIdExterne(),
                                curContentieux.getSection().getCompteAnnexe().getIdExterneRole(),
                                formatDate(curContentieux.getSection().getDateSection()), dateNotificationCDP,
                                observation.toString(), valueAnnexe, etapeLibelle, dateExecution,
                                curContentieux.getSection().getDescription(getLangue()), giveLibelleInfoRom246() }));

        // Rajoute la date à la signature
        this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_DATA,
                getImporter().getParametre().get(ICaisseReportHelper.PARAM_SIGNATURE_DATA) + " "
                        + formatDate(getDateExecution()));
    }

    private COHistorique initHistorique() {
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

    public void setDateDecisionMainLevee(String dateDecisionMainLevee) {
        this.dateDecisionMainLevee = dateDecisionMainLevee;
    }

    public void setDateJugementMainLevee(String dateJugementMainLevee) {
        this.dateJugementMainLevee = dateJugementMainLevee;
    }

    public void setDecisionMainLevee(String decisionMainLevee) {
        this.decisionMainLevee = decisionMainLevee;
    }

    public void setJugementMainLevee(String jugementMainLevee) {
        this.jugementMainLevee = jugementMainLevee;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

}
